package com.example.ticketservice.centermembership;

import com.example.ticketservice.dto.response.CenterMemberResponseDto;
import com.example.ticketservice.dto.response.UnapprovedUserTicketListResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;

import java.util.List;

public class CenterMembershipSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static List<UnapprovedUserTicketListResponseDto> getUnapprovedUserTicketList(long centerId) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .get("/api/v1/admin/center-membership/unapproved/{centerId}", centerId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, UnapprovedUserTicketListResponseDto.class));
    }

    public static void approveUserTicket(long userTicketId) {
        RestAssured
                .given().log().all()
                .when()
                .patch("/api/v1/admin/center-membership/{userTicketId}/approve", userTicketId)
                .then().log().all()
                .statusCode(200);
    }

    public static List<CenterMemberResponseDto> getCenterMemberList(long centerId) throws JsonProcessingException {
        String responseBody = RestAssured
                .given().log().all()
                .when()
                .get("/api/v1/admin/center-membership/unapproved/{centerId}", centerId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), new TypeReference<List<CenterMemberResponseDto>>() {});
    }
}
