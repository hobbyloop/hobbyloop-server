package com.example.ticketservice.pay.toss;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TossTransactionException {
    INVALID_DATE(400, "날짜 데이터가 잘못 되었습니다."),
    INVALID_TRANSACTION_KEY(400, "잘못된 페이징 키 입니다."),
    UNAUTHORIZED_KEY(401, "인증되지 않은 시크릿 키 혹은 클라이언트 키 입니다."),
    INCORRECT_BASIC_AUTH_FORMAT(403, "잘못된 요청입니다. ':' 를 포함해 인코딩해주세요."),
    UNKNOWN(500, "알 수 없는 에러입니다.");;

    private final int statusCode;
    private final String description;

    public Boolean isRetryableError() {
        return this.statusCode == 500;
    }

    public static TossTransactionException get(String errorCode) {
        return Arrays.stream(TossTransactionException.values())
                .filter(error -> error.name().equals(errorCode))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
