package com.example.ticketservice.ticket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class SimpleCenterInfo {
    private long centerId;
    private String centerName;
    private boolean isRefundable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleCenterInfo that = (SimpleCenterInfo) o;
        return isRefundable == that.isRefundable && Objects.equals(centerName, that.centerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centerName, isRefundable);
    }
}
