package com.example.companyservice.common.kafka;

import com.example.companyservice.company.client.dto.request.BlindReviewRequestDto;
import com.example.companyservice.company.client.dto.request.CenterOriginalAndUpdateInfoDto;
import com.example.companyservice.company.entity.Advertisement;
import com.example.companyservice.member.dto.MemberDeletedDto;
import com.example.companyservice.member.dto.MemberUpdatedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void send(String topic, MemberUpdatedDto requestDto) {
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from User microservice: " + requestDto);
    }

    public void send(String topic, MemberDeletedDto requestDto) {
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from User microservice: " + requestDto);
    }

    public void send(String topic, List<Long> advertisementIdList) {
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(advertisementIdList);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from User microservice: " + advertisementIdList);
    }

    public void send(String topic, Long advertisementId) {
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(advertisementId);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from User microservice: " + advertisementId);
    }
}