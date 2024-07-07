package com.example.ticketservice.pay.batch;

import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.PaymentRefund;
import com.example.ticketservice.pay.entity.member.TransactionError;
import com.example.ticketservice.pay.entity.member.enums.*;
import com.example.ticketservice.pay.event.PaymentCompletedEvent;
import com.example.ticketservice.pay.event.PaymentRefundedEvent;
import com.example.ticketservice.pay.repository.PaymentRefundRepository;
import com.example.ticketservice.pay.repository.TransactionErrorRepository;
import com.example.ticketservice.pay.repository.payment.PaymentRepository;
import com.example.ticketservice.pay.toss.TossPaymentClient;
import com.example.ticketservice.pay.toss.TossTransactionsResponseDto;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class TossTransactionReconciliationBatchConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final TossPaymentClient tossPaymentClient;

    private final PaymentRepository paymentRepository;

    private final PaymentRefundRepository paymentRefundRepository;

    private final TransactionErrorRepository transactionErrorRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Bean
    public Job tossTransactionReconciliationJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("tossTransactionReconciliationJob", jobRepository)
                .start(tossTransactionReconciliationStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step tossTransactionReconciliationStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("tossTransactionReconciliationStep", jobRepository)
                .<TossTransactionsResponseDto, Payment>chunk(100, transactionManager)
                .reader(tossTransactionReader())
                .processor(tossTransactionItemProcessor())
                .writer(paymentItemWriter())
                .build();
    }

    @Bean
    public ItemReader<TossTransactionsResponseDto> tossTransactionReader() {
        return new ItemReader<TossTransactionsResponseDto>() {
            private List<TossTransactionsResponseDto> transactions;
            private int currentIndex;

            @Override
            public TossTransactionsResponseDto read() {
                if (transactions == null) {
                    LocalDateTime startDate = LocalDate.now().minusDays(1).atStartOfDay();
                    LocalDateTime endDate = LocalDate.now().atStartOfDay().minusNanos(1);

                    transactions = tossPaymentClient.executeTransaction(startDate, endDate, "");
                    currentIndex = 0;
                }

                if (currentIndex >= transactions.size()) {
                    return null;
                }

                return transactions.get(currentIndex++);
            }
        };
    }

    @Bean
    public ItemProcessor<TossTransactionsResponseDto, Payment> tossTransactionItemProcessor() {
        return transaction -> {
            if (transaction.getStatus().equals(PSPConfirmationStatusEnum.CANCELED.name()) ||
                transaction.getStatus().equals(PSPConfirmationStatusEnum.PARTIAL_CANCELED.name())) {
                Optional<PaymentRefund> refundOptional = paymentRefundRepository.findByPspPaymentKeyAndIsRefundDoneTrue(transaction.getPaymentKey());
                if (refundOptional.isPresent()) {
                    PaymentRefund refund = refundOptional.get();

                    // 금액 검증
                    if (transaction.getAmount() != refund.getAmount().intValue()) {
                        TransactionError transactionError = TransactionError.mismatchOf(transaction, refund);
                        transactionErrorRepository.save(transactionError);
                    }
                    // 상태 검증
                    if (transaction.getStatus().equals(PSPConfirmationStatusEnum.DONE.name())
                        && refund.getStatus() != PaymentStatusEnum.SUCCESS.getValue()) {
                        refund.refund(LocalDateTime.parse(transaction.getTransactionAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME), "");
                        eventPublisher.publishEvent(new PaymentRefundedEvent(refund));
                    }
                } else {
                    // 누락되었거나, status가 안 맞거나. 다시 status 조건 빼고 다시 조회해옴.
                    List<PaymentRefund> refunds = paymentRefundRepository.findAllByPspPaymentKeyOrderByCreatedAtDesc(transaction.getPaymentKey());
                    if (!refunds.isEmpty()) {
                        PaymentRefund refund = refunds.get(0);

                        // 금액 검증
                        if (transaction.getAmount() != refund.getAmount().intValue()) {
                            TransactionError transactionError = TransactionError.mismatchOf(transaction, refund);
                            transactionErrorRepository.save(transactionError);
                        }
                        // 상태 검증
                        if (transaction.getStatus().equals(PSPConfirmationStatusEnum.DONE.name())
                                && refund.getStatus() != PaymentStatusEnum.SUCCESS.getValue()) {
                            refund.refund(LocalDateTime.parse(transaction.getTransactionAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME), "");
                            eventPublisher.publishEvent(new PaymentRefundedEvent(refund));
                        }
                    } else {
                        TransactionError transactionError = TransactionError.refundNotFoundOf(transaction);
                        transactionErrorRepository.save(transactionError);
                    }
                }
            }
            Optional<Payment> paymentOptional = paymentRepository.findByPspPaymentKey(transaction.getPaymentKey());
            if (paymentOptional.isPresent()) {
                Payment payment = paymentOptional.get();

                // 금액 검증
                if (transaction.getAmount() != payment.getAmount().intValue()) {
                    TransactionError transactionError = TransactionError.mismatchOf(transaction, payment);
                    transactionErrorRepository.save(transactionError);
                }

                // 상태 검증
                if (transaction.getStatus().equals(PSPConfirmationStatusEnum.DONE.name())
                    && payment.getStatus() != PaymentStatusEnum.SUCCESS.getValue()) {
                    payment.confirm();
                    eventPublisher.publishEvent(new PaymentCompletedEvent(payment));
                } else if (!transaction.getStatus().equals(PSPConfirmationStatusEnum.DONE.name())
                            && payment.getStatus() == PaymentStatusEnum.SUCCESS.getValue()) {
                    TransactionError transactionError = TransactionError.statusMismatchOf(transaction, payment);
                    transactionErrorRepository.save(transactionError);
                }
            } else {
                // 누락
                TransactionError transactionError = TransactionError.paymentNotFoundOf(transaction);
                transactionErrorRepository.save(transactionError);
            }

            return null;
        };
    }

    @Bean
    public ItemWriter<Payment> paymentItemWriter() {
        return new JpaItemWriterBuilder<Payment>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
