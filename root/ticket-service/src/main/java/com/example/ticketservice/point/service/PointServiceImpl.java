package com.example.ticketservice.point.service;

import com.example.ticketservice.point.dto.PointEarnedResponseDto;
import com.example.ticketservice.point.dto.PointHistoryByMonthResponseDto;
import com.example.ticketservice.point.dto.PointHistoryListResponseDto;
import com.example.ticketservice.point.dto.PointHistoryResponseDto;
import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
import com.example.ticketservice.point.policy.PlatformPointPolicy;
import com.example.ticketservice.point.policy.SignupPointPolicy;
import com.example.ticketservice.point.repository.PointHistoryRepository;
import com.example.ticketservice.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Override
    @Transactional
    public PointEarnedResponseDto earnPointWhenJoining(Long memberId) {
        PlatformPointPolicy signupPolicy = new SignupPointPolicy();

        Point point = Point.builder()
                .memberId(memberId)
                .balance(0L)
                .usableScope(PointUsableScopeEnum.GENERAL.getValue())
                .build();

        PointHistory pointHistory = signupPolicy.earnOrUse(point);

        pointRepository.save(point);
        pointHistoryRepository.save(pointHistory);

        return new PointEarnedResponseDto(pointHistory.getAmount(), point.getBalance(), pointHistory.getExpirationDateTime());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMemberTotalPoints(Long memberId) {
        List<Point> pointsList = pointRepository.findByMemberId(memberId);

        return pointsList.stream().mapToLong(Point::getBalance).sum();
    }

    @Override
    @Transactional(readOnly = true)
    public PointHistoryListResponseDto getPointHistory(Long memberId) {
        List<PointHistory> pointHistoryList = pointHistoryRepository.findByMemberId(memberId);

        Map<String, List<PointHistoryResponseDto>> pointHistoriesByMonth = new HashMap<>();
        for (PointHistory pointHistory : pointHistoryList) {

            String yearMonth = pointHistory.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM"));

            if (!pointHistoriesByMonth.containsKey(yearMonth)) {
                pointHistoriesByMonth.put(yearMonth, new ArrayList<>());
            }

            pointHistoriesByMonth.get(yearMonth).add(PointHistoryResponseDto.from(pointHistory));
        }

        List<PointHistoryByMonthResponseDto> pointHistoryByMonthList = new ArrayList<>();

        for (Map.Entry<String, List<PointHistoryResponseDto>> entry : pointHistoriesByMonth.entrySet()) {
            String yearMonth = entry.getKey();
            List<PointHistoryResponseDto> pointHistories = entry.getValue();

            pointHistories.sort(Comparator.comparing(PointHistoryResponseDto::getCreatedAt).reversed());

            PointHistoryByMonthResponseDto pointHistoryByMonth = new PointHistoryByMonthResponseDto(yearMonth, pointHistories);
            pointHistoryByMonthList.add(pointHistoryByMonth);
        }

        pointHistoryByMonthList.sort(Comparator.comparing((PointHistoryByMonthResponseDto history) -> YearMonth.parse(history.getYearMonth(), DateTimeFormatter.ofPattern("yyyy/MM"))).reversed());

        Long totalPoints = getMemberTotalPoints(memberId);

        return new PointHistoryListResponseDto(totalPoints, pointHistoryByMonthList);
    }

    @Override
    @Transactional
    public PointEarnedResponseDto earnPointGeneral(Long memberId, PlatformPointPolicy pointPolicy) {
        Point point = pointRepository.findByMemberIdAndUsableScopeIs(memberId, PointUsableScopeEnum.GENERAL.getValue())
                .orElseThrow();

        PointHistory pointHistory = pointPolicy.earnOrUse(point);
        pointHistoryRepository.save(pointHistory);

        return new PointEarnedResponseDto(pointHistory.getAmount(), point.getBalance(), pointHistory.getExpirationDateTime());
    }

    @Override
    @Transactional
    public PointEarnedResponseDto earnPointSpecific(Long memberId, Long companyId, PlatformPointPolicy pointPolicy) {
        Point point = pointRepository.findByMemberIdAndCompanyId(memberId, companyId)
                .orElseGet(() -> Point.builder()
                        .memberId(memberId)
                        .companyId(companyId)
                        .balance(0L)
                        .usableScope(PointUsableScopeEnum.SPECIFIC.getValue())
                        .build());

        PointHistory pointHistory = pointPolicy.earnOrUse(point);
        pointHistoryRepository.save(pointHistory);

        return new PointEarnedResponseDto(pointHistory.getAmount(), point.getBalance(), pointHistory.getExpirationDateTime());
    }
}
