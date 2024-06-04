package com.example.ticketservice.ticket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsingHistoryByMonthDto {
    private String yearMonth;
    private List<UsingHistoryDto> usingHistories;
}
