package com.example.ticketservice.common.aop;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.security.RoleAuthorization;
import com.example.ticketservice.common.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class RoleAuthorizationAop {
    @Pointcut("@annotation(com.example.ticketservice.common.security.RoleAuthorization)")
    public void roleAuthorizationPointcut() {}

    @Around("roleAuthorizationPointcut()")
    public Object roleAuthorizationAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RoleAuthorization roleAuthorization = signature.getMethod().getAnnotation(RoleAuthorization.class);
        String[] requiredRoles = roleAuthorization.roles();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String userRole = Utils.parseRole(request);

        if (Arrays.asList(requiredRoles).contains(userRole)) {
            return joinPoint.proceed();
        } else {
            throw new ApiException(ExceptionEnum.ACCESS_NOW_ALLOW_EXCEPTION);
        }
    }
}
