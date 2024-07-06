package com.example.ticketservice.common.kafka;

import com.example.ticketservice.point.repository.PointRepository;
import com.example.ticketservice.point.repository.PointsRepository;
import com.example.ticketservice.ticket.client.dto.request.CenterOriginalAndUpdateInfoDto;
import com.example.ticketservice.ticket.repository.centermembership.CenterMembershipRepository;
import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final TicketRepository ticketRepository;
    private final CenterMembershipRepository centerMembershipRepository;
    private final PointsRepository pointsRepository;
    private final PointRepository pointRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "update-address-info")
    @Transactional
    public void updateAddressInfo(String kafkaMessage) throws JsonProcessingException{
        log.info("Kafka Message ->" + kafkaMessage);

        // 역직렬화
        CenterOriginalAndUpdateInfoDto requestDto = objectMapper.readValue(kafkaMessage, CenterOriginalAndUpdateInfoDto.class);
        try {
            log.info(requestDto.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional
    @KafkaListener(topics = "update-centermembership-info")
    public void updateCenterMembershipInfo(String kafkaMessage) {
        try {
            log.info("Kafka Message ->" + kafkaMessage);

            // 역직렬화
            Map<Object, Object> map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});

            Object memberIdObj = map.get("memberId");
            if (memberIdObj instanceof Integer memberIdInt) {
                Long memberId = Long.valueOf(memberIdInt);
                String name = (String) map.get("name");
                String phoneNumber = (String) map.get("phoneNumber");
                LocalDate birthday = LocalDate.parse((String) map.get("birthday"), DateTimeFormatter.ISO_LOCAL_DATE);

                centerMembershipRepository.updateMemberInfo(memberId, name, phoneNumber, birthday);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @KafkaListener(topics = "delete-member-points")
    @Transactional
    public void deleteMemberPoints(String kafkaMessage) {
        try {
            log.info("Kafka Message ->" + kafkaMessage);

            Map<Object, Object> map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});

            Object memberIdObj = map.get("memberId");
            if (memberIdObj instanceof Integer memberIdInt) {
                Long memberId = Long.valueOf(memberIdInt);
                pointRepository.deleteAllByMemberId(memberId);
                pointsRepository.deleteAllByMemberId(memberId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
