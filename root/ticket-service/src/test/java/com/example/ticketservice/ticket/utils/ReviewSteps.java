package com.example.ticketservice.ticket.utils;

import com.example.ticketservice.ticket.dto.request.ReviewRequestDto;
import com.example.ticketservice.ticket.dto.response.ReviewByCenterResponseDto;
import com.example.ticketservice.ticket.dto.response.ReviewResponseDto;
import com.example.ticketservice.ticket.dto.response.TicketReviewListByCenterResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReviewSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Long createReview(Long ticketId, ReviewRequestDto request) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
            .given().log().all()
            .contentType(ContentType.MULTIPART.withCharset("UTF-8"))
            .header("id", 1L, "role", "USER")   // TODO: Replace with actual member ID
            .multiPart(new MultiPartSpecBuilder(objectMapper.writeValueAsString(request)).controlName("requestDto").mimeType("application/json").charset(StandardCharsets.UTF_8).build())
            .multiPart("reviewImageList", generateMockImageFile(), "image/jpeg")
            .when()
            .post("/api/v1/reviews/{ticketId}", ticketId)
            .then().log().all()
            .statusCode(201)
            .extract().asString();

        return objectMapper.readTree(responseBody).get("data").asLong();
    }

    public static void createComment(Long reviewId) {
        RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .header("id", 1L, "role", "USER")   // TODO: Replace with actual member ID
            .body("{\"content\": \"test content\"}")
            .when()
            .post("/api/v1/comments/{reviewId}", reviewId)
            .then().log().all()
            .statusCode(201);
    }

    public static List<ReviewResponseDto> getReviewList(Long ticketId, int pageNo, int sortId) throws JsonProcessingException {
        String responseBody = RestAssured
            .given().log().all()
            .header("id", 1L)   // TODO: Replace with actual member ID
            .when()
            .header("id", 1L, "role", "USER")
            .get("/api/v1/reviews/{ticketId}/{pageNo}/{sortId}", ticketId, pageNo, sortId)
            .then().log().all()
            .statusCode(200)
            .extract().asString();

        JsonNode data = objectMapper.readTree(responseBody).get("data");
        List<ReviewResponseDto> reviewList = new ArrayList<>();
        for (JsonNode review : data) {
            reviewList.add(objectMapper.convertValue(review, ReviewResponseDto.class));
        }

        return reviewList;
    }

    public static TicketReviewListByCenterResponseDto getReviewListByCenter(long centerId, int pageNo, int sortId) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .header("id", 1L, "role", "USER")
                .when()
                .get("/api/v1/centers/{centerId}/reviews/{pageNo}/{sortId}", centerId, pageNo, sortId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode data = objectMapper.readTree(responseBody).get("data");

        int reviewCount = data.get("reviewCount").asInt();
        float score = (float) data.get("score").asDouble();

        List<ReviewByCenterResponseDto> reviewList = objectMapper.convertValue(
                data.get("reviewList"),
                new TypeReference<List<ReviewByCenterResponseDto>>() {}
        );

        return new TicketReviewListByCenterResponseDto(reviewCount, score, reviewList);
    }

    private static File generateMockImageFile() throws IOException {
        File tempFile = File.createTempFile("test", ".jpg");
        tempFile.deleteOnExit(); // 프로그램 종료 시 파일 삭제

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write("test file content".getBytes());
        }

        return tempFile;
    }

}
