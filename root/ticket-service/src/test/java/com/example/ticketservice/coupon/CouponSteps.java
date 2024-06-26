package com.example.ticketservice.coupon;

import com.example.ticketservice.coupon.dto.CouponResponseDto;
import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.List;

public class CouponSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<CouponResponseDto> getCenterCoupons(Long memberId, Long centerId) throws Exception {
        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .headers("id", memberId, "role", "USER")
                .when()
                .get("/api/v1/coupons/{centerId}", centerId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, CouponResponseDto.class));
    }

    public static Long issueSingleCoupon(Long memberId, Long couponId) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .headers("id", memberId, "role", "USER")
                .when()
                .post("/api/v1/coupons/issue/{couponId}", couponId)
                .then().log().all()
                .statusCode(201)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), Long.class);
    }

    public static void issueAllCoupons(Long memberId, List<CouponResponseDto> coupons) {
        List<Long> couponIds = coupons.stream()
                        .map(CouponResponseDto::getCouponId)
                        .toList();

        RestAssured
            .given().log().all()
            .contentType(ContentType.JSON.withCharset("UTF-8"))
            .headers("id", memberId, "role", "USER")
            .queryParam("couponIds", couponIds)
            .when()
            .post("/api/v1/coupons/issue/all")
            .then().log().all()
            .statusCode(201)
            .extract().asString();
    }

    public static Long getMyCouponCount(Long memberId) throws Exception {
        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .when()
                .get("/api/v1/coupons/client/my/count/{memberId}", memberId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), Long.class);
    }

    public static List<MemberCouponResponseDto> getAvailableMemberCoupons(Long memberId) throws Exception {
        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .headers("id", memberId, "role", "USER")
                .when()
                .get("/api/v1/coupons/members")
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, MemberCouponResponseDto.class));

    }
}
