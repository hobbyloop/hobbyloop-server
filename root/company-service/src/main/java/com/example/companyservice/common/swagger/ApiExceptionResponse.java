package com.example.companyservice.common.swagger;

import com.example.companyservice.common.exception.ExceptionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiExceptionResponse {
    ExceptionEnum[] value();
}
