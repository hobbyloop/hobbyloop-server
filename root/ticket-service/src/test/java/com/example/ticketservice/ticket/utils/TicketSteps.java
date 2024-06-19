package com.example.ticketservice.ticket.utils;

import com.example.ticketservice.ticket.dto.response.TicketByCenterResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;

import java.util.List;

public class TicketSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<TicketByCenterResponseDto> getTicketListByCenter(long centerId) throws JsonProcessingException {

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .headers("id", 1L, "role", "USER")
                .get("/api/v1/tickets/centers/{centerId}", centerId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, TicketByCenterResponseDto.class));
    }

}
