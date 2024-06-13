package com.example.companyservice.fixture;

import com.example.companyservice.company.dto.request.CompanyCreateRequestDto;
import com.example.companyservice.company.entity.Company;
import com.example.companyservice.company.entity.CreateStatusEnum;
import com.example.companyservice.company.entity.PaymentTypeEnum;
import com.example.companyservice.company.entity.Role;

import java.time.LocalDate;

public class CompanyFixture {

    public static final double LATITUDE = 37.56100278;
    public static final double LONGITUDE = 126.9996417;

    public static Company defaultSocialCompany() {
        Company company = Company.builder()
                .email("company@company.com")
                .provider("kakao")
                .role(Role.COMPANY)
                .isDelete(false)
                .createStatus(CreateStatusEnum.WAIT.getTypeValue())
                .build();

        return company;
    }

    public static CompanyCreateRequestDto defaultCompanyUpdateRequest() {
        return CompanyCreateRequestDto.builder()
                .isOption1(true)
                .isOption2(true)
                .isDutyFree(true)
                .companyName("companyName")
                .representativeName("representativeName")
                .phoneNumber("phoneNumber")
                .businessNumber("businessNumber")
                .businessAddress("businessAddress")
                .openingDate(LocalDate.parse("2021-01-01"))
                .onlineReportNumber("onlineReportNumber")
                .accountBank("accountBank")
                .accountNumber("accountNumber")
                .entryPermission("정률제")
                .isLooppass(true)
                .isRefundable(true)
                .paymentType(PaymentTypeEnum.POINT.getName())
                .price(10000)
                .vat(1000)
                .totalPrice(11000)
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .build();
    }
}
