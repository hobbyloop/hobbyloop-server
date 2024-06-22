package com.example.ticketservice.ticket.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "시설 회원 등록 요청 바디")
public class CenterMembershipJoinRequestDto {
    @Schema(description = "회원이 구매한 이용권 아이디. 아 이거 centerId로 검증 추가해야겠네", example = "1")
    private Long ticketId;

    @Schema(description = "회원 본명", example = "이채림")
    private String memberName;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "회원 성별", example = "남 or 여")
    private String gender;

    @Schema(description = "회원 생일", example = "yyyy-MM-dd")
    private String birthday;

    @Schema(description = "회원 이메일", example = "hobbyloop@gmail.com")
    private String email;
}
