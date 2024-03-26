package com.example.ticketservice.dto.response;

import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReviewResponseDto {

    private String centerName;

    private String address;

    private String ticketName;

    private float score;

    private List<ReviewCommentResponseDto> reviewResponseDtoList;

    public static AdminReviewResponseDto of(CenterInfoResponseDto responseDto,
                                            Ticket ticket,
                                            float score,
                                            List<ReviewCommentResponseDto> reviewResponseDtoList) {
        return AdminReviewResponseDto.builder()
                .centerName(responseDto.getCenterName())
                .address(responseDto.getAddress())
                .ticketName(ticket.getName())
                .score(score)
                .reviewResponseDtoList(reviewResponseDtoList)
                .build();
    }
}
