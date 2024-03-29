package com.example.companyservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CompanyUpdateRequestDto {

    @JsonProperty
    private boolean isOption1;

    @JsonProperty
    private boolean isOption2;

    @JsonProperty
    private boolean isDutyFree;

    private String companyName;

    private String representativeName;

    private String phoneNumber;

    private String businessNumber;

    private String businessAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate openingDate;

    private String onlineReportNumber;

    private String accountBank;

    private String accountNumber;

    private String entryPermission;

    @JsonProperty
    private boolean isReservationService;

    @JsonProperty
    private boolean isLooppass;

    private String paymentType;

    private int price;

    private int vat;

    private int totalPrice;
}
