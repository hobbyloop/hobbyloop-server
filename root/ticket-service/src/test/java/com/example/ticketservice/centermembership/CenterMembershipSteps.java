package com.example.ticketservice.centermembership;

import com.example.ticketservice.ticket.dto.request.CenterMembershipJoinRequestDto;
import com.example.ticketservice.ticket.dto.response.centermembership.CenterMemberResponseDto;
import com.example.ticketservice.ticket.dto.response.centermembership.CenterMembershipDetailResponseDto;
import com.example.ticketservice.ticket.dto.response.centermembership.CenterMembershipJoinedResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.UnapprovedUserTicketListResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.List;

public class CenterMembershipSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static CenterMembershipJoinedResponseDto joinCenterMembershipByAdmin(
            long centerId, CenterMembershipJoinRequestDto request) throws Exception {
        String responseBody = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .body(request)
                .when()
                .headers("id", 1L, "role", "COMPANY")
                .post("/api/v1/admin/center-membership/{centerId}/{memberId}", centerId, 1L)
                .then().log().all()
                .statusCode(201)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.treeToValue(dataNode, CenterMembershipJoinedResponseDto.class);
    }


    public static List<UnapprovedUserTicketListResponseDto> getUnapprovedUserTicketList(long centerId) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .headers("id", 1L, "role", "COMPANY")
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
                .headers("id", 1L, "role", "COMPANY")
                .patch("/api/v1/admin/center-membership/{userTicketId}/approve", userTicketId)
                .then().log().all()
                .statusCode(200);
    }

    public static List<CenterMemberResponseDto> getCenterMemberList(long centerId, int pageNo, int sortId) throws JsonProcessingException {
        String responseBody = RestAssured
                .given().log().all()
                .when()
                .headers("id", 1L, "role", "COMPANY")
                .get("/api/v1/admin/center-membership/{centerId}/{pageNo}/{sortId}", centerId, pageNo, sortId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), new TypeReference<List<CenterMemberResponseDto>>() {});
    }

    public static CenterMembershipDetailResponseDto getCenterMembershipDetail(long centerMembershipId) throws Exception {

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .headers("id", 1L, "role", "COMPANY")
                .get("/api/v1/admin/center-membership/{centerMembershipId}", centerMembershipId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.readValue(dataNode.toString(), new TypeReference<CenterMembershipDetailResponseDto>() {});
    }
}
