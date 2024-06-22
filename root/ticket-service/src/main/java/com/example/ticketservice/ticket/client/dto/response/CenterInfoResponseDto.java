package com.example.ticketservice.ticket.client.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "시설 상세 정보")
public class CenterInfoResponseDto {

    @Schema(description ="시설 이름", example = "필라피티 스튜디오")
    public String centerName;

    @Schema(description = "시설 주소", example = "서울시 동작구 동작로 12-1")
    public String address;

    @Schema(description = "공지", example = "6월 21일 센터 사정으로 휴무입니다...")
    private String announcement;

    @Schema(description = "소개", example = "최고의 강사만 고집하는 어쩌구.. ")
    private String introduce;

    @Schema(description = "연락처")
    private String contact;

    @Schema(description = "카카오톡 링크")
    private String kakaoLink;

    @Schema(description = "루프패스 여부", example = "true")
    private boolean looppass;

    @Schema(description = "중도환불가능업체 여부", example = "true")
    private boolean refundable;

    @Schema(description = "운영시간 정보")
    private List<HourResponseDto> operatingHourList;

    @Schema(description = "휴무시간 정보")
    private List<HourResponseDto> breakHourList;

    @Schema(description = "위도")
    private double latitude;

    @Schema(description = "경도")
    private double longitude;
}
