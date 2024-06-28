package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.client.dto.response.TicketClientBaseResponseDto;
import com.example.companyservice.company.client.dto.response.TicketClientResponseDto;
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
@Schema(title = "시설 관리자 페이지 홈 응답 바디")
public class CenterHomeResponseDto {

    @Schema(description = "시설 정보")
    private CenterCreateResponseDto centerInfo;

    @Schema(description = "즐겨찾기 버튼 리스트")
    private List<Integer> quickButtonIdList;

    @Schema(description = "회원수", example = "1")
    private int membershipCount;

    @Schema(description = "이용권 정보 리스트")
    private List<TicketClientResponseDto> ticketResponseDtoList;

    public static CenterHomeResponseDto of(CenterCreateResponseDto centerInfo,
                                           List<Integer> quickButtonIdList,
                                           TicketClientBaseResponseDto ticketBaseResponseDto) {
        return CenterHomeResponseDto.builder()
                .centerInfo(centerInfo)
                .quickButtonIdList(quickButtonIdList)
                .membershipCount(ticketBaseResponseDto.getMembershipCount())
                .ticketResponseDtoList(ticketBaseResponseDto.getTicketClientResponseDtoList())
                .build();
    }
}