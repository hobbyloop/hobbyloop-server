package com.example.ticketservice.ticket;

import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.response.AdminReviewTicketResponseDto;
import com.example.ticketservice.dto.response.AdminTicketResponseDto;
import com.example.ticketservice.dto.response.TicketCreateResponseDto;
import com.example.ticketservice.dto.response.TicketResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AdminTicketSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static TicketCreateResponseDto createTicket(Long centerId, TicketCreateRequestDto request) throws IOException {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.MULTIPART.withCharset("UTF-8"))
                .multiPart(new MultiPartSpecBuilder(request).controlName("requestDto").mimeType("application/json").charset(StandardCharsets.UTF_8).build())
                .multiPart("ticketImage", generateMockImageFile(), "image/jpeg")
                .when()
                .post("/api/v1/admin/tickets/management/{centerId}", centerId)
                .then().log().all()
                .statusCode(201)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, TicketCreateResponseDto.class);
    }

    public static List<TicketResponseDto> getTicketManagementList(long centerId, long ticketId) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .get("/api/v1/admin/tickets/management/{centerId}/{ticketId}", centerId, ticketId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, TicketResponseDto.class));
    }

    public static List<AdminTicketResponseDto> getTicketWithReviewList(long centerId, long ticketId) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .get("/api/v1/admin/tickets/review/{centerId}/{ticketId}", centerId, ticketId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, AdminTicketResponseDto.class));
    }

    public static AdminReviewTicketResponseDto getTicketWithReview(long ticketId) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .get("/api/v1/admin/tickets/review/{ticketId}", ticketId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, AdminReviewTicketResponseDto.class);
    }

    private static File generateMockImageFile() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("ticketImage", "test.jpg", "image/jpeg", "test file content".getBytes());
        File file = new File("test.jpg");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(mockFile.getBytes());
        fos.close();
        return file;
    }
}
