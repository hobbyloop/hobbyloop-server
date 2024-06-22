package com.example.ticketservice.common.kafka;

import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final TicketRepository ticketRepository;

    @KafkaListener(topics = "update-address-info")
    @Transactional
    public void updateProfileImg(String kafkaMessage) {
        log.info("Kafka Message ->" + kafkaMessage);

        // 역직렬화
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        Object centerIdObj = map.get("centerId");
        if (centerIdObj instanceof Integer centerIdInt) {
            Long centerId = Long.valueOf(centerIdInt);
            String centerName = (String) map.get("centerName");
            String logoImageUrl = (String) map.get("logoImageUrl");
            String address = (String) map.get("address");
            Double latitude = (Double) map.get("latitude");
            Double longitude = (Double) map.get("longitude");
            ticketRepository.updateCenterAddressInfo(centerId, centerName, logoImageUrl, address, latitude, longitude);
        }
    }
}
