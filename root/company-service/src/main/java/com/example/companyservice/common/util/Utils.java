package com.example.companyservice.common.util;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    /**
     * HttpServletRequest로부터 header에서 companyId를 조회합니다. 없을 경우, 권한이 없음으로 간주합니다.
     * @param request
     * @return
     */
    public static long parseAuthorizedCompanyId(HttpServletRequest request) {
        try {
            return Long.parseLong(request.getHeader("companyId"));
        } catch (NumberFormatException ex) {
            throw new ApiException(ExceptionEnum.ACCESS_NOW_ALLOW_EXCEPTION);
        }
    }

    /**
     * HttpServletRequest로부터 header에서 UserId를 조회합니다. 없을 경우, 권한이 없음으로 간주합니다.
     * @param request
     * @return
     */
    public static long parseAuthorizedMemberId(HttpServletRequest request) {
        try {
            return Long.parseLong(request.getHeader("memberId"));
        } catch (NumberFormatException ex) {
            throw new ApiException(ExceptionEnum.ACCESS_NOW_ALLOW_EXCEPTION);
        }
    }
}
