package com.example.companyservice.company.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "업체 등록 요청 바디")
public class CompanyCreateRequestDto {


    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotBlank
    @Schema(description = "이메일", required = true)
    private String email;

    @NotBlank
    @Schema(description = "로그인 인증 업체", example = "Kakao, Naver, Google", required = true)
    private String provider;

    @NotBlank
    @Schema(description = "인증 업체 로그인 요청 ID", required = true)
    private String subject;

    @NotBlank
    @Schema(description = "인증 업체 로그인 요청 토큰", required = true)
    private String oauth2AccessToken;

    @NotNull
    @JsonProperty
    @Schema(description = "선택사항1", required = true)
    private boolean isOption1;

    @NotNull
    @JsonProperty
    @Schema(description = "선택사항2", required = true)
    private boolean isOption2;

    @NotNull
    @JsonProperty
    @Schema(description = "면세사업자 여부", required = true)
    private boolean isDutyFree;

    @NotBlank
    @Schema(description = "개인 인증 ci", required = true)
    private String ci;

    @NotBlank
    @Schema(description = "개인 인증 di", required = true)
    private String di;

    @NotBlank
    @Schema(description = "업체명", required = true)
    private String companyName;

    @NotBlank
    @Schema(description = "대표자 이름", required = true)
    private String representativeName;

    @NotBlank
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
    @Schema(description = "핸드폰 번호", example = "010-0000-0000", required = true)
    private String phoneNumber;

    @NotBlank
    @Schema(description = "사업자번호", required = true)
    private String businessNumber;

    @NotBlank
    @Schema(description = "사업장 주소", required = true)
    private String businessAddress;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "오픈 일자", required = true)
    private LocalDate openingDate;

    @Schema(description = "통신판매번호", required = true)
    private String onlineReportNumber;

    @NotBlank
    @Schema(description = "은행 이름", example = "농협", required = true)
    private String accountBank;

    @NotBlank
    @Schema(description = "계좌번호", required = true)
    private String accountNumber;

    @NotBlank
    @Schema(description = "입점권한", example = "일반/원데이&소수클래스/프리미엄/예약서비스", required = true)
    private String entryPermission;

    @JsonProperty
    private boolean isLooppass;

    @JsonProperty
    @Schema(description = "환불가능여부", required = true)
    private boolean isRefundable;

    @NotBlank
    @Schema(description = "결제 정산 방식", example = "정률제/정액제", required = true)
    private String paymentType;

    @NotNull
    @Schema(description = "결제 금액", required = true)
    private int price;

    @NotNull
    @Schema(description = "부가세", required = true)
    private int vat;

    @NotNull
    @Schema(description = "총 금액", required = true)
    private int totalPrice;

    @NotNull
    @Schema(description = "위도", example = "00.000000", required = true)
    private double latitude;

    @NotNull
    @Schema(description = "경도", example = "00.000000", required = true)
    private double longitude;
}
