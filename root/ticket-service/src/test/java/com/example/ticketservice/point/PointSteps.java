package com.example.ticketservice.point;

import com.example.ticketservice.point.dto.PointEarnedResponseDto;
import com.example.ticketservice.point.dto.PointHistoryListResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;

public class PointSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static PointEarnedResponseDto earnPointWhenJoining(Long memberId) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .post("/api/v1/points/client/join/{memberId}", memberId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, PointEarnedResponseDto.class);
    }

    public static PointHistoryListResponseDto getPointHistory(Long memberId) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .headers("id", memberId, "role", "USER")
                .get("/api/v1/points/histories")
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        TypeReference<PointHistoryListResponseDto> typeRef = new TypeReference<PointHistoryListResponseDto>() {};
        return objectMapper.readValue(dataNode.traverse(), typeRef);
    }

    public static Long getMyTotalPoints(Long memberId) throws Exception {
        String responseBody = RestAssured
                .given().log().all()
                .when()
                .get("/api/v1/points/client/my/{memberId}", memberId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, Long.class);
    }
}
