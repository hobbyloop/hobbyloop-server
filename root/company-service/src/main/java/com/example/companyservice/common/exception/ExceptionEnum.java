package com.example.companyservice.common.exception;

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
    NOT_SUPPORT_PROVIDER_TYPE(HttpStatus.BAD_REQUEST, "E006", "지원하지 않는 소셜로그인입니다."),
    ACCESS_NOW_ALLOW_EXCEPTION(HttpStatus.FORBIDDEN, "E007", "접근 권한이 없습니다."),
    LOGIN_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E008", "로그인 실패"),
    COMPANY_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "C001", "존재하지 않는 업체입니다."),
    CENTER_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "CT002", "존재하지 않는 시설입니다."),
    BOOKMARK_ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, "B001", "이미 북마크된 시설입니다."),
    BOOKMARK_ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, "B002", "이미 북마크 해제된 시설입니다."),
    RANK_ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, "A001", "이미 등록된 광고게재순위입니다."),
    NOT_HAS_TICKET_EXCEPTION(HttpStatus.NOT_FOUND, "T001", "이용권을 등록한 시설만 광고 등록이 가능합니다."),
    BANNER_NULL_POINTER_EXCEPTION(HttpStatus.NOT_FOUND, "B001", "배너 광고 등록시 배너 이미지를 첨부해주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}