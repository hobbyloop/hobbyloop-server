package com.example.companyservice.acceptance.steps;

import com.example.companyservice.member.dto.MemberDetailResponseDto;
import com.example.companyservice.member.dto.request.CreateMemberRequestDto;
import com.example.companyservice.member.dto.request.MemberUpdateRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MemberSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void joinMember(CreateMemberRequestDto request) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        RestAssured
            .given().log().all()
            .contentType(ContentType.JSON.withCharset("UTF-8"))
            .body(request)
            .when()
            .post("/api/v1/members/join")
            .then().log().all()
            .statusCode(200)
            .extract().asString();
    }

    public static void updateMember(long memberId, MemberUpdateRequestDto request) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        RestAssured
            .given().log().all()
            .contentType(ContentType.MULTIPART.withCharset("UTF-8"))
            .multiPart(new MultiPartSpecBuilder(request).controlName("requestDto").mimeType("application/json").charset(StandardCharsets.UTF_8).build())
            .multiPart("profileImage", generateMockImageFile(), "image/jpeg")
            .when()
            .header("id", memberId)   // TODO: Replace with actual member ID
            .patch("/api/v1/members")
            .then().log().all()
            .statusCode(200)
            .extract().body();
    }

    public static MemberDetailResponseDto getMemberDetail(long memberId) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .header("id", memberId)
                .get("/api/v1/members")
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, MemberDetailResponseDto.class);
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
