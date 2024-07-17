package com.example.ticketservice.pay.batch;

import com.example.ticketservice.pay.batch.dailytransactionreport.CheckoutStepHandler;
import com.example.ticketservice.pay.batch.dailytransactionreport.PaymentStepHandler;
import com.example.ticketservice.pay.batch.dailytransactionreport.RefundStepHandler;
import com.example.ticketservice.pay.batch.dailytransactionreport.dto.CheckoutStepMetrics;
import com.example.ticketservice.pay.batch.dailytransactionreport.dto.PaymentStepMetrics;
import com.example.ticketservice.pay.batch.dailytransactionreport.dto.RefundStepMetrics;
import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.pay.entity.member.DailyTransactionReport;
import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.PaymentRefund;
import com.example.ticketservice.pay.repository.DailyTransactionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;


@Configuration
@RequiredArgsConstructor
public class DailyTransactionReportBatchConfig {
    private final DailyTransactionReportRepository dailyTransactionReportRepository;
    private final CheckoutStepHandler checkoutStepHandler;
    private final PaymentStepHandler paymentStepHandler;
    private final RefundStepHandler refundStepHandler;

    @Bean
    public Job dailyTransactionReportJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("dailyTransactionReportJob", jobRepository)
                .start(checkoutStep(jobRepository, transactionManager))
                .next(paymentStep(jobRepository, transactionManager))
                .next(refundStep(jobRepository, transactionManager))
                .next(saveDailyTransactionReportStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step checkoutStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("checkoutStep", jobRepository)
                .<Checkout, CheckoutStepMetrics>chunk(100, transactionManager)
                .reader(checkoutStepHandler.checkoutReader())
                .processor(checkoutStepHandler.checkoutProcessor())
                .writer(checkoutStepHandler.checkoutWriter())
                .build();
    }

    @Bean
    public Step paymentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("paymentStep", jobRepository)
                .<Payment, PaymentStepMetrics>chunk(100, transactionManager)
                .reader(paymentStepHandler.paymentReader())
                .processor(paymentStepHandler.paymentProcessor())
                .writer(paymentStepHandler.paymentWriter())
                .build();
    }

    @Bean
    public Step refundStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("refundStep", jobRepository)
                .<PaymentRefund, RefundStepMetrics>chunk(100, transactionManager)
                .reader(refundStepHandler.refundReader())
                .processor(refundStepHandler.refundProcessor())
                .writer(refundStepHandler.refundWriter())
                .build();
    }

    @Bean
    public Step saveDailyTransactionReportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("saveDailyTransactionReportStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    ExecutionContext stepContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

                    DailyTransactionReport report = (DailyTransactionReport) stepContext.get("dailyTransactionReport");
                    report.setDate(LocalDate.now().minusDays(1));

                    dailyTransactionReportRepository.save(report);

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }
}
