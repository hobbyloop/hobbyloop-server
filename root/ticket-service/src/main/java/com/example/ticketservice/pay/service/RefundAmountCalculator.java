package com.example.ticketservice.pay.service;

import com.example.ticketservice.ticket.entity.Ticket;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RefundAmountCalculator {

    public static Long calculate(Ticket ticket,
                                 Long amount,
                                 LocalDate refundDate) {
        int refundRegulation = ticket.getRefundRegulation();
        LocalDate startDate = ticket.getExpirationStartDate();
        LocalDate endDate = ticket.getExpirationEndDate();

        Long refundAmount = 0L;

        switch (refundRegulation) {
            case 1:
                long daysBefore = ChronoUnit.DAYS.between(refundDate, startDate);

                if (daysBefore >= 10) {
                    refundAmount = amount;
                } else {
                    refundAmount = amount - (amount * (10 - daysBefore) * 10 / 100);
                }
                break;
            case 2:
                long daysBeforeStart = ChronoUnit.DAYS.between(refundDate, startDate);

                if (daysBeforeStart >= 7) {
                    refundAmount = amount;
                } else if (daysBeforeStart >= 5) {
                    refundAmount = amount * 90 / 100;
                } else if (daysBeforeStart >= 3) {
                    refundAmount = amount * 80 / 100;
                } else if (daysBeforeStart >= 2) {
                    refundAmount = amount * 70 / 100;
                } else if (daysBeforeStart >= 1) {
                    refundAmount = amount * 60 / 100;
                } else {
                    long usedDays = ChronoUnit.DAYS.between(startDate, refundDate);
                    long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
                    refundAmount = amount * 60 / 100 - (amount * 60 / 100 / totalDays * usedDays);
                }
                break;
            case 3:
                if (refundDate.isBefore(startDate)) {
                    refundAmount = amount;
                }
                break;
            case 5:
                refundAmount = amount;
                break;
            default:
                throw new IllegalStateException("유효하지 않은 환불 규정입니다.");
        }
        return refundAmount;
    }
}
