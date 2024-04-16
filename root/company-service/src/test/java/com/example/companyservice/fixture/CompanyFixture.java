package com.example.companyservice.fixture;

import com.example.companyservice.dto.request.CompanyUpdateRequestDto;
import com.example.companyservice.entity.Company;
import com.example.companyservice.entity.CreateStatusEnum;
import com.example.companyservice.entity.PaymentTypeEnum;
import com.example.companyservice.entity.Role;

import java.time.LocalDate;

public class CompanyFixture {

    public static final double LATITUDE = 37.56100278;
    public static final double LONGITUDE = 126.9996417;

    public static Company defaultSocialCompany() {
        Company company = Company.builder()
                .email("company@company.com")
                .password("password")
                .provider("kakao")
                .providerId("123456")
                .isDelete(false)
                .createStatus(CreateStatusEnum.WAIT.getTypeValue())
                .build();

        company.addRole(Role.COMPANY);

        return company;
    }

    public static CompanyUpdateRequestDto defaultCompanyUpdateRequest() {
        return CompanyUpdateRequestDto.builder()
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
                .isReservationService(true)
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
