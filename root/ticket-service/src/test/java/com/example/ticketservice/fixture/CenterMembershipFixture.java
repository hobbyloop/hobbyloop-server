package com.example.ticketservice.fixture;

import com.example.ticketservice.dto.request.CenterMembershipJoinRequestDto;

public class CenterMembershipFixture {

    public static CenterMembershipJoinRequestDto defaultCenterMembershipJoinRequest(Long ticketId) {
        return new CenterMembershipJoinRequestDto(
                ticketId,
                "홍길동",
                "010-1234-5678",
                "남",
                "2000-01-01",
                "honggildong@gmail.com"
        );

    }
}
