package com.example.companyservice.acceptance.steps;

import com.example.companyservice.company.dto.request.CompanyCreateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;

public class CompanySteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Long updateCompanyInfo(long companyId, CompanyCreateRequestDto requestDto) throws Exception {
        String responseBody = RestAssured
                .given().log().all()
                .contentType("application/json")
                .header("id", companyId)
                .body(requestDto)
                .when()
                .patch("/api/v1/admin/companies")
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        return objectMapper.readTree(responseBody).get("data").asLong();
    }

    public static Boolean checkTaxFree(long companyId) throws Exception {
        String responseBody = RestAssured
                .given().log().all()
                .contentType("application/json")
                .header("id", companyId)
                .when()
                .get("/api/v1/admin/companies/check/tax-free")
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        return objectMapper.readTree(responseBody).get("data").asBoolean();
    }
}
