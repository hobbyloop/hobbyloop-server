package com.example.ticketservice.dto.response;

import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.HourResponseDto;
import com.example.ticketservice.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponseDto {

    private Long ticketId;

    private String ticketName;

    private String ticketImageUrl;

    private String address;

    private String centerName;

    private String operatingHour;

    private String breakHour;

    private LocalDate expirationStartDate;

    private LocalDate expirationEndDate;

    private int totalCount;

    private int issueCount;

    private boolean isUpload;

    public static TicketResponseDto from(Ticket ticket, CenterInfoResponseDto centerInfo) {
        return TicketResponseDto.builder()
                .ticketId(ticket.getId())
                .ticketName(ticket.getName())
                .ticketImageUrl(ticket.getTicketImageUrl())
                .address(centerInfo.getAddress())
                .centerName(centerInfo.getCenterName())
                .operatingHour(generateOperatingHour(centerInfo.getOperatingHourList()))
                .breakHour(generateBreakHour(centerInfo.getBreakHourList()))
                .expirationStartDate(ticket.getExpirationStartDate())
                .expirationEndDate(ticket.getExpirationEndDate())
                .totalCount(ticket.getTotalCount())
                .issueCount(ticket.getIssueCount())
                .isUpload(ticket.isUpload())
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

    private static String generateBreakHour(List<HourResponseDto> breakHourList) {
        if (breakHourList == null || breakHourList.isEmpty()) {
            return "휴무 없음";
        }

        StringBuilder sb = new StringBuilder();

        for (HourResponseDto hour : breakHourList) {
            sb.append(hour.getDay()).append(" ");
        }

        sb.append("휴무");

        return sb.toString();
    }
}
