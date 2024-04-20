package com.example.companyservice.common.util;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    /**
     * HttpServletRequest로부터 header에서 Id를 조회합니다. 없을 경우, 권한이 없음으로 간주합니다.
     * @param request
     * @return
     */
    public static long parseAuthorizedId(HttpServletRequest request) {
        try {
            return Long.parseLong(request.getHeader("id"));
        } catch (NumberFormatException ex) {
            throw new ApiException(ExceptionEnum.ACCESS_NOW_ALLOW_EXCEPTION);
        }
    }
}
