package com.example.ticketservice.pay;

import com.example.ticketservice.pay.dto.request.CheckoutRequestDto;
import com.example.ticketservice.pay.dto.request.PaymentConfirmRequestDto;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.dto.response.CheckoutResponseDto;
import com.example.ticketservice.pay.dto.response.PaymentConfirmResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class PaymentSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static CheckoutPrepareResponseDto prepareCheckout(Long memberId, Long ticketId) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .headers("id", memberId, "role", "USER")
                .post("/api/v1/payments/checkout/prepare/{ticketId}", ticketId)
                .then().log().all()
                .statusCode(201)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, CheckoutPrepareResponseDto.class);
    }

    public static CheckoutResponseDto checkout(Long memberId, CheckoutRequestDto request) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .when()
                .headers("id", memberId, "role", "USER")
                .body(request)
                .post("/api/v1/payments/checkout")
                .then().log().all()
                .statusCode(201)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, CheckoutResponseDto.class);
    }

    public static PaymentConfirmResponseDto confirm(Long memberId, PaymentConfirmRequestDto request) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .when()
                .headers("id", memberId, "role", "USER")
                .body(request)
                .post("/api/v1/payments/confirm")
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, PaymentConfirmResponseDto.class);
    }
}
