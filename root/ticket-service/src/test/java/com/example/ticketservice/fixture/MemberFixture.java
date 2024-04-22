package com.example.ticketservice.fixture;

import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;

import java.time.LocalDate;

public class MemberFixture {
    public static MemberInfoResponseDto defaultMemberInfoResponse() {
        return new MemberInfoResponseDto("홍길동", "010-1234-5678", LocalDate.of(2000, 1, 1), "여");
    }


}
