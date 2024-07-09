package com.example.ticketservice.pay.service;

import com.example.ticketservice.pay.dto.response.TransactionErrorResponseDto;
import com.example.ticketservice.pay.entity.member.TransactionError;
import com.example.ticketservice.pay.entity.member.enums.TransactionErrorStatusEnum;
import com.example.ticketservice.pay.repository.TransactionErrorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionErrorServiceImpl {
    private final TransactionErrorRepository transactionErrorRepository;

    @Transactional(readOnly = true)
    public List<TransactionErrorResponseDto> getOpendTransactionErrors(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<TransactionError> transactionErrors = transactionErrorRepository.findAllByStatusAndCreatedAtBetweenOrderByCreatedAtAsc(TransactionErrorStatusEnum.OPEN.getValue(), startDateTime, endDateTime);

        return transactionErrors.stream()
                .map(TransactionErrorResponseDto::from)
                .collect(Collectors.toList());
    }
}
