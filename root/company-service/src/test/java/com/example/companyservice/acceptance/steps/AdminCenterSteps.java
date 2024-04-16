package com.example.companyservice.acceptance.steps;

import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.response.CenterCreateResponseDto;
import com.example.companyservice.dto.response.CenterResponseListDto;
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
import java.util.List;

public class AdminCenterSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static CenterCreateResponseDto createCenter(long companyId, CenterCreateRequestDto requestDto) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.MULTIPART.withCharset("UTF-8"))
                .header("id", companyId)
                .multiPart(new MultiPartSpecBuilder(requestDto).controlName("requestDto").mimeType("application/json").charset(StandardCharsets.UTF_8).build())
                .multiPart("logoImage", generateMockImageFile())
                .multiPart("centerImageList", generateMockImageFile())
                .when()
                .post("/api/v1/admin/centers")
                .then().log().all()
                .statusCode(201)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, CenterCreateResponseDto.class);

    }

    public static List<CenterResponseListDto> getCenterList(long companyId) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .contentType("application/json")
                .header("id", companyId)
                .when()
                .get("/api/v1/admin/centers")
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.convertValue(dataNode, objectMapper.getTypeFactory().constructCollectionType(List.class, CenterResponseListDto.class));
    }

    private static File generateMockImageFile() throws IOException {
        File tempFile = File.createTempFile("test", ".jpg");
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write("test file content".getBytes());
        }

        return tempFile;
    }
}
