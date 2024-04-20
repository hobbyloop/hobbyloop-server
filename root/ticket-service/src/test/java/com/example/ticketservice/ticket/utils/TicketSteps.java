package com.example.ticketservice.ticket.utils;

import com.example.ticketservice.dto.response.RecentPurchaseUserTicketListResponseDto;
import com.example.ticketservice.dto.response.TicketByCenterResponseDto;
import com.example.ticketservice.dto.response.TicketResponseDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class TicketSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<TicketByCenterResponseDto> getTicketListByCenter(long centerId) throws JsonProcessingException {

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .get("/api/v1/tickets/centers/{centerId}", centerId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        return objectMapper.readValue(dataNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, TicketByCenterResponseDto.class));
    }

}
