package com.example.ticketservice.ticket.dto.response;

import com.example.ticketservice.ticket.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.ticket.client.dto.response.HourResponseDto;
import com.example.ticketservice.ticket.entity.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "관리자 내 정보 이용권 목록 응답 바디")
public class AdminMyTicketResponseDto {
    @Schema(description = "이용권 아이디", example = "1")
    private Long ticketId;

    @Schema(description = "시설 이름", example = "필라피티 스튜디오")
    private String centerName;

    @Schema(description = "시설 주소", example = "서울시 동작구 동작로 12-1")
    private String address;

    @Schema(description = "이용권 이름", example = "6:1 필라테스 15회")
    private String ticketName;

    @Schema(description = "이용권 이미지 URL")
    private String ticketImageUrl;

    @Schema(description = "운영시간", example = "월,화,수,목,금,토,일 / 09:00~18:00")
    private String operatingHour;

    @Schema(description = "휴무시간", example = "휴무 없음")
    private String breakHour;

    @Schema(description = "이용권 신청 마감기한 - 시작일", example = "2024-06-20")
    private LocalDate expirationStartDate;

    @Schema(description = "이용권 신청 마감기한 - 종료일", example = "2024-06-20")
    private LocalDate expirationEndDate;

    @Schema(description = "이용권 총 수량(최대 판매가능 수량)", example = "100")
    private int totalCount;

    @Schema(description = "이용권 발급 수량(사용자가 구매한 횟수)", example = "14")
    private int issueCount;

    public static AdminMyTicketResponseDto from(Ticket ticket, CenterInfoResponseDto centerInfo) {
        return AdminMyTicketResponseDto.builder()
                .ticketId(ticket.getId())
                .centerName(centerInfo.getCenterName())
                .address(centerInfo.getAddress())
                .ticketName(ticket.getName())
                .ticketImageUrl(ticket.getTicketImageUrl())
                .operatingHour(generateOperatingHour(centerInfo.getOperatingHourList()))
                .breakHour(generateBreakHour(centerInfo.getOperatingHourList()))
                .expirationStartDate(ticket.getExpirationStartDate())
                .expirationEndDate(ticket.getExpirationEndDate())
                .totalCount(ticket.getTotalCount())
                .issueCount(ticket.getIssueCount())
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
