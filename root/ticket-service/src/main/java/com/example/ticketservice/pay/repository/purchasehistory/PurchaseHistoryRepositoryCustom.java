package com.example.ticketservice.pay.repository.purchasehistory;

import com.example.ticketservice.pay.dto.request.PurchaseHistoryInOneWeekResponseDto;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PurchaseHistoryRepositoryCustom {

    Optional<PurchaseHistoryInOneWeekResponseDto> getHotTicketIdInOneWeek(@Param(value = "centerId") long centerId);
}
