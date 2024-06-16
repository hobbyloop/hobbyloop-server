package com.example.ticketservice.coupon;

import com.example.ticketservice.coupon.dto.CouponResponseDto;
import com.example.ticketservice.ticket.dto.response.UnapprovedUserTicketListResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.List;

public class CouponSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<CouponResponseDto> getCenterCoupons(Long memberId, Long centerId) throws Exception {
        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .header("id", memberId)
                .when()
                .get("/api/v1/coupons/{centerId}", centerId)
                .then().log().all()
                .statusCode(201)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, CouponResponseDto.class));
    }
}
