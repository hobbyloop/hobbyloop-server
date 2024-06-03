package com.example.ticketservice.ticket.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E001", "내부 문제로 다음번에 다시 시도해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E002", "내부 문제로 다음번에 다시 시도해주세요."),
    API_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "E003", "존재하지 않는 API 입니다."),
    API_METHOD_NOT_ALLOWED_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED, "E004", "지원하지 않는 Method 입니다."),
    API_PARAMETER_EXCEPTION(HttpStatus.BAD_REQUEST, "E005", "파라미터 타입과 값을 확인하세요."),
    ACCESS_NOW_ALLOW_EXCEPTION(HttpStatus.FORBIDDEN, "E006", "접근 권한이 없습니다."),
    TICKET_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "T001", "존재하지 않는 이용권입니다."),
    TICKET_NOT_UPLOAD_EXCEPTION(HttpStatus.BAD_REQUEST, "T002", "판매 중인 이용권이 아닙니다."),
    TICKET_SOLD_OUT_EXCEPTION(HttpStatus.BAD_REQUEST, "T002", "이용권이 모두 판매되었습니다."),
    REVIEW_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "R001", "존재하지 않는 리뷰입니다."),
    USER_TICKET_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "UT001", "존재하지 않는 이용권입니다."),
    NO_REMAINING_USER_TICKET_EXCEPTION(HttpStatus.BAD_REQUEST, "UT002", "이용권 잔여 횟수가 없습니다."),
    EXPIRED_USER_TICKET_EXCEPTION(HttpStatus.BAD_REQUEST, "UT003", "만료된 이용권입니다."),
    CENTER_MEMBERSHIP_ALREADY_JOINED_EXCEPTION(HttpStatus.BAD_REQUEST, "CM001", "이미 등록된 회원입니다."),
    CENTER_MEMBERSHIP_NOT_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, "CM002", "존재하지 않는 회원입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}