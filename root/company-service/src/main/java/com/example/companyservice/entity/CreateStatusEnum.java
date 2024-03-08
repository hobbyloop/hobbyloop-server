package com.example.companyservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CreateStatusEnum {

    NOT(1, "신청안함"),
    WAIT(2, "대기"),
    SUCCESS(3, "완료"),
    FAIL(4, "실패");

    private final int typeValue;

    private final String name;
}
