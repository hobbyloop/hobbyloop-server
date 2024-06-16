package com.example.ticketservice.coupon;

import com.example.ticketservice.coupon.dto.CouponCreateRequestDto;
import com.example.ticketservice.coupon.dto.CouponResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.List;

public class AdminCouponSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Long createCoupon(Long adminId, CouponCreateRequestDto request) throws Exception {
        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("id", adminId) // TODO: 어떻게 하지... 음...
                .body(request)
                .when()
                .post("/api/v1/admin/coupons")
                .then().log().all()
                .statusCode(201)
                .extract().asString();

        return objectMapper.readTree(responseBody).get("data").asLong();
    }

}
