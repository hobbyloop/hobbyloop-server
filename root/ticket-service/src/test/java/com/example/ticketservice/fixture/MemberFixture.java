package com.example.ticketservice.fixture;

import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;

import java.time.LocalDate;

public class MemberFixture {
    public static MemberInfoResponseDto defaultMemberInfoResponse() {
        return new MemberInfoResponseDto("홍길동", "010-1234-5678", LocalDate.of(2000, 1, 1), "남");
    }

    public static MemberInfoResponseDto secondMemberInfoResponse() {
        return new MemberInfoResponseDto("고길동", "010-9876-5432", LocalDate.of(1999, 12, 31), "여");
    }
}
