package com.example.ticketservice.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "ApiExceptionEntity", description = "예외 응답 객체")
public class ApiExceptionEntity {
    @Schema(description = "에러 코드", example = "T001")
    private String code;

    @Schema(description = "에러 메시지", example = "존재하지 않는 이용권입니다.")
    private String message;

    @Override
    public String toString() {
        return "ApiExceptionEntity [errorCode=" + code + ", errorMessage=" + message + "]";
    }

}
