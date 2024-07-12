package com.example.ticketservice.common.kafka;

import com.example.ticketservice.ticket.client.dto.request.CenterOriginalAndUpdateInfoDto;
import com.example.ticketservice.ticket.client.dto.response.BlindReviewRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public void send(String topic, CenterOriginalAndUpdateInfoDto requestDto) {
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from User microservice: " + requestDto);
    }

    public void send(String topic, BlindReviewRequestDto requestDto) {
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from User microservice: " + requestDto);
    }
}
