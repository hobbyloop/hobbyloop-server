package com.example.companyservice.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Schema(description = "변경되지 않은 기존의 값들도 세팅해서 보내주세요. (프로필 사진 제외)")
public class MemberUpdateRequestDto {
    @Parameter(required = true)
    private String name;

    @Parameter(required = true)
    private String nickname;

    @Parameter(description = "포맷: yyyy-MM-dd", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;
}
