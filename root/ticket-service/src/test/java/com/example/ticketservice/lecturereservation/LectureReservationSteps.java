package com.example.ticketservice.lecturereservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;

public class LectureReservationSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Long reserveLecture(long userTicketId) throws Exception {
        long lectureScheduleId = 1L; // TODO: Replace with actual lectureScheduleId

        String responseBody = RestAssured
                .given().log().all()
                .when()
                .headers("id", 1L, "role", "USER")   // TODO: Replace with actual member ID
                .post("/api/v1/lecture-reservation/{lectureScheduleId}/{userTicketId}", lectureScheduleId, userTicketId)
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        return objectMapper.readTree(responseBody).get("data").asLong();
    }
}
