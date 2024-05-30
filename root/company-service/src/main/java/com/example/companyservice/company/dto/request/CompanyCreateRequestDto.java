package com.example.companyservice.company.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class CompanyCreateRequestDto {


    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotBlank
    private String email;

    @NotBlank
    private String provider;

    @NotBlank
    private String subject;

    @NotBlank
    private String oauth2AccessToken;

    @NotBlank
    @JsonProperty
    private boolean isOption1;

    @NotBlank
    @JsonProperty
    private boolean isOption2;

    @NotBlank
    @JsonProperty
    private boolean isDutyFree;

    @NotBlank
    private String ci;

    @NotBlank
    private String di;

    @NotBlank
    private String companyName;

    @NotBlank
    private String representativeName;

    @NotBlank
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
    private String phoneNumber;

    @NotBlank
    private String businessNumber;

    @NotBlank
    private String businessAddress;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate openingDate;

    private String onlineReportNumber;

    @NotBlank
    private String accountBank;

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String entryPermission;

    @JsonProperty
    private boolean isLooppass;

    @JsonProperty
    private boolean isRefundable;

    @NotBlank
    private String paymentType;

    @NotBlank
    private int price;

    @NotBlank
    private int vat;

    @NotBlank
    private int totalPrice;

    @NotBlank
    private double latitude;

    @NotBlank
    private double longitude;
}
