package com.example.ticketservice.common.exception;

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
    TICKET_CANNOT_CANCEL_UPLOAD(HttpStatus.BAD_REQUEST, "T003", "이미 구매한 사용자가 존재해 이용권 업로드를 취소할 수 없습니다."),
    REVIEW_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "R001", "존재하지 않는 리뷰입니다."),
    USER_TICKET_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "UT001", "존재하지 않는 이용권입니다."),
    NO_REMAINING_USER_TICKET_EXCEPTION(HttpStatus.BAD_REQUEST, "UT002", "이용권 잔여 횟수가 없습니다."),
    EXPIRED_USER_TICKET_EXCEPTION(HttpStatus.BAD_REQUEST, "UT003", "만료된 이용권입니다."),
    UNAPPROVED_USER_TICKET_EXCEPTION(HttpStatus.BAD_REQUEST, "UT004", "승인되지 않은 이용권입니다."),
    CENTER_MEMBERSHIP_ALREADY_JOINED_EXCEPTION(HttpStatus.BAD_REQUEST, "CM001", "이미 등록된 회원입니다."),
    CENTER_MEMBERSHIP_NOT_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, "CM002", "존재하지 않는 회원입니다."),
    INVALID_POINT_SCOPE_EXCEPTION(HttpStatus.BAD_REQUEST, "P001", "포인트 사용 범위가 잘못 설정되었습니다."),
    COUPON_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "CP001", "존재하지 않는 쿠폰입니다."),
    COUPON_OUT_OF_DATE_EXCEPTION(HttpStatus.BAD_REQUEST, "CP002", "쿠폰 사용 기한 범위를 벗어났습니다."),
    COUPON_ALREADY_ISSUED_EXCEPTION(HttpStatus.BAD_REQUEST, "CP003", "이미 발급된 쿠폰입니다."),
    COUPON_SOLD_OUT_EXCEPTION(HttpStatus.BAD_REQUEST, "CP004", "쿠폰을 더이상 발급받을 수 없습니다."),
    NOT_ALLOW_LOCATION_Exception(HttpStatus.BAD_REQUEST, "L001", "사용자가 위치 정보를 허용하지 않았습니다."),
    CHECKOUT_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "CH001", "존재하지 않는 체크아웃입니다."),
    PAYMENT_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "P001", "존재하지 않는 결제입니다."),
    PAYMENT_AMOUNT_MISMATCH_EXCEPTION(HttpStatus.BAD_REQUEST, "P002", "결제 금액이 일치하지 않습니다."),
    UNAUTHORIZED_PAYMENT_REQUEST_EXCEPTION(HttpStatus.UNAUTHORIZED, "P003", "승인되지 않은 결제 요청입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}