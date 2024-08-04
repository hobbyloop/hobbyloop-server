package com.example.ticketservice.ticket.client.dto.response;

import com.example.ticketservice.ticket.entity.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class TicketClientForLectureResponseDto {
    private Long ticketId;
    private String name;
    private String ticketImageUrl;
    private String address;
    private int totalCount;
    private int issueCount;
    private LocalDate expirationStartDate;
    private LocalDate expirationEndDate;
    @Schema(description = "운영시간", example = "월,화,수,목,금,토,일 / 09:00~18:00")
    private String operatingHour;

    @Schema(description = "휴무시간", example = "휴무 없음")
    private String breakHour;

    public static TicketClientForLectureResponseDto from(Ticket ticket, List<HourResponseDto> operatingHourList, List<HourResponseDto> breakHourList) {
        return TicketClientForLectureResponseDto.builder()
                .ticketId(ticket.getId())
                .name(ticket.getName())
                .ticketImageUrl(ticket.getTicketImageUrl())
                .address(ticket.getAddress())
                .totalCount(ticket.getTotalCount())
                .issueCount(ticket.getIssueCount())
                .expirationStartDate(ticket.getExpirationStartDate())
                .expirationEndDate(ticket.getExpirationEndDate())
                .operatingHour(generateOperatingHour(operatingHourList))
                .breakHour(generateBreakHour(breakHourList))
                .build();
    }

    private static String generateOperatingHour(List<HourResponseDto> operatingHourList) {
        StringBuilder sb = new StringBuilder();
        String prevDays = operatingHourList.get(0).getDay();
        LocalTime prevOpenAt = operatingHourList.get(0).getOpenAt();
        LocalTime prevCloseAt = operatingHourList.get(0).getCloseAt();

        for (int i = 1; i < operatingHourList.size(); i++) {
            String days = operatingHourList.get(i).getDay();
            LocalTime openAt = operatingHourList.get(i).getOpenAt();
            LocalTime closeAt = operatingHourList.get(i).getCloseAt();

            if (!openAt.equals(prevOpenAt) || !closeAt.equals(prevCloseAt)) {
                sb.append(prevDays).append(" / ")
                        .append(prevOpenAt.format(DateTimeFormatter.ofPattern("HH:mm"))).append("~")
                        .append(prevCloseAt.format(DateTimeFormatter.ofPattern("HH:mm"))).append(" / ");
                prevDays = days;
                prevOpenAt = openAt;
                prevCloseAt = closeAt;
            } else {
                prevDays += "," + days;
            }
        }

        sb.append(prevDays).append(" / ")
                .append(prevOpenAt.format(DateTimeFormatter.ofPattern("HH:mm"))).append("~")
                .append(prevCloseAt.format(DateTimeFormatter.ofPattern("HH:mm")));

        return sb.toString();
    }

    private static String generateBreakHour(List<HourResponseDto> operatingHourList) {
        Set<String> operatingDays = operatingHourList.stream()
                .map(HourResponseDto::getDay)
                .collect(Collectors.toSet());

        List<String> allDays = Arrays.asList("월", "화", "수", "목", "금", "토", "일");
        List<String> breakDays = allDays.stream()
                .filter(day -> !operatingDays.contains(day))
                .collect(Collectors.toList());

        if (breakDays.isEmpty()) {
            return "휴무 없음";
        }

        StringBuilder sb = new StringBuilder();

        for (String day : breakDays) {
            sb.append(day).append(" ");
        }

        sb.append("휴무");

        return sb.toString();
    }
}
