package com.example.ticketservice.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;

public class TicketSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Long purchaseTicket(Long ticketId) throws Exception {

        String responseBody = RestAssured
            .given().log().all()
            .when()
            .header("id", 1L)   // TODO: Replace with actual member ID
            .post("/api/v1/tickets/{ticketId}/purchase", ticketId)
            .then().log().all()
            .statusCode(201)
            .extract().asString();

        return objectMapper.readTree(responseBody).get("data").asLong();
    }
}
