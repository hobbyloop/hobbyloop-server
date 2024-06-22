package com.example.ticketservice.ticket.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @Schema(description = "댓글 내용", example = "괜찮아보이네요.")
    private String content;
}
