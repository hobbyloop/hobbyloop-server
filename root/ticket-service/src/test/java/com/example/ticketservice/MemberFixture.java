package com.example.ticketservice;

import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;

public class MemberFixture {
    public static MemberInfoResponseDto defaultMemberInfoResponse() {
        return new MemberInfoResponseDto("홍길동", "010-1234-5678");
    }


}
