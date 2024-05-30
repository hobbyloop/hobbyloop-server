package com.example.ticketservice.ticket.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterDistanceInfoResponseDto {

    private Long centerId;

    private boolean isRefundable;

    private boolean isBookmark;

    private String logoImageUrl;

    private String centerName;

    private String address;

    private boolean isSatisfied;
}
