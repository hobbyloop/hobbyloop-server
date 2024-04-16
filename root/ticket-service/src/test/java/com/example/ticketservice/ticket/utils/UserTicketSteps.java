package com.example.ticketservice.ticket.utils;

import com.example.ticketservice.dto.response.AvailableUserTicketResponseDto;
import com.example.ticketservice.dto.response.AvailableUserTicketsWithCenterInfo;
import com.example.ticketservice.dto.response.RecentPurchaseUserTicketListResponseDto;
import com.example.ticketservice.dto.response.SimpleCenterInfo;
import com.fasterxml.jackson.core.JsonParser;
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

public class UserTicketSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Long purchaseTicket(Long ticketId) throws Exception {

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .header("id", 1L)   // TODO: Replace with actual member ID
                .post("/api/v1/user-tickets/{ticketId}/purchase", ticketId)
                .then().log().all()
                .statusCode(201)
                .extract().asString();

        return objectMapper.readTree(responseBody).get("data").asLong();
    }

    public static Map<String, AvailableUserTicketsWithCenterInfo> getMyAvailableUserTicketList() throws Exception {

            String responseBody = RestAssured
                    .given().log().all()
                    .when()
                    .header("id", 1L)   // TODO: Replace with actual member ID
                    .get("/api/v1/user-tickets/available")
                    .then().log().all()
                    .statusCode(200)
                    .extract().asString();

            JsonNode responseJson = objectMapper.readTree(responseBody);
            JsonNode dataNode = responseJson.get("data");

            SimpleModule module = new SimpleModule();
            module.addDeserializer(YearMonth.class, new JsonDeserializer<YearMonth>() {
                @Override
                public YearMonth deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                    String yearMonthString = parser.getValueAsString();
                    return YearMonth.parse(yearMonthString);
                }
            });

            objectMapper.registerModule(module);
            objectMapper.registerModule(new JavaTimeModule());

            TypeReference<Map<String, AvailableUserTicketsWithCenterInfo>> typeRef = new TypeReference<Map<String, AvailableUserTicketsWithCenterInfo>>() {};
            return objectMapper.readValue(dataNode.traverse(), typeRef);
    }

    public static Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>> getMyRecentPurchaseUserTicketList() throws Exception {

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .header("id", 1L)   // TODO: Replace with actual member ID
                .get("/api/v1/user-tickets/recent-purchase")
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode dataNode = responseJson.get("data");

        SimpleModule module = new SimpleModule();
        module.addDeserializer(YearMonth.class, new JsonDeserializer<YearMonth>() {
            @Override
            public YearMonth deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String yearMonthString = parser.getValueAsString();
                return YearMonth.parse(yearMonthString);
            }
        });

        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());

        TypeReference<Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>>> typeRef = new TypeReference<Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>>>() {};
        return objectMapper.readValue(dataNode.traverse(), typeRef);

    }
}
