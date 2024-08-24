package com.example.companyservice.common.dto;

import com.example.companyservice.company.entity.DayEnum;
import com.example.companyservice.lecture.entity.LectureBreakHour;
import com.example.companyservice.lecture.entity.LectureOperatingHour;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HourStringFactory {
    public static String generateOperatingHour(List<LectureOperatingHour> operatingHourList) {
        StringBuilder sb = new StringBuilder();
        String prevDays = DayEnum.findByValue(operatingHourList.get(0).getDay()).name();
        LocalTime prevOpenAt = operatingHourList.get(0).getOpenAt();
        LocalTime prevCloseAt = operatingHourList.get(0).getCloseAt();

        for (int i = 1; i < operatingHourList.size(); i++) {
            String days = DayEnum.findByValue(operatingHourList.get(i).getDay()).name();
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

    public static String generateBreakHour(List<LectureBreakHour> breakHourList) {
        Set<String> operatingDays = breakHourList.stream()
                .map(hour -> {
                    return DayEnum.findByValue(hour.getDay()).name();
                })
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
