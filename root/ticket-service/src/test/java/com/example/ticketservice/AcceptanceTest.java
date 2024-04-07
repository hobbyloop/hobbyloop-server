package com.example.ticketservice;

import io.restassured.RestAssured;
import io.restassured.config.DecoderConfig;
import io.restassured.config.EncoderConfig;
import io.restassured.config.MultiPartConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(properties = "spring.mvc.encoding.charset=UTF-8")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();

        RestAssured.port = port;
        RestAssured.config = RestAssured.config()
                .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))
                .decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8"))
                .multiPartConfig(MultiPartConfig.multiPartConfig().defaultCharset("UTF-8"));

    }
}
