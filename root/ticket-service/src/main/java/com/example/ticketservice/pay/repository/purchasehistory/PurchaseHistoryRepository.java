package com.example.ticketservice.pay.repository.purchasehistory;

import com.example.ticketservice.pay.entity.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long>, PurchaseHistoryRepositoryCustom {
}
