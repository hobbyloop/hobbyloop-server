package com.example.ticketservice.pay.repository.purchasehistory;

import com.example.ticketservice.pay.dto.request.PurchaseHistoryInOneWeekResponseDto;
import org.springframework.data.repository.query.Param;

public interface PurchaseHistoryRepositoryCustom {

    PurchaseHistoryInOneWeekResponseDto getHotTicketIdInOneWeek(@Param(value = "centerId") long centerId);
}
