package com.example.ticketservice.point;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.point.dto.PointHistoryListResponseDto;
import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.repository.PointHistoryRepository;
import com.example.ticketservice.point.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBatchTest
public class PointsBatchTest extends AcceptanceTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("expiredPointJob")
    private Job expiredPointJob;

    @Autowired
    @Qualifier("expiringSoonPointJob")
    private Job expiringSoonPointJob;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    Long memberId = 1L;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        PointSteps.earnPointWhenJoining(memberId);
        // TODO: 추후 결제로 적립 포인트 추가해서 더 테스트하기
    }

    @Test
    public void expiredPointJobSuccess() throws Exception {
        // given
        Long memberPoints = PointSteps.getMyTotalPoints(memberId);
        assertThat(memberPoints).isEqualTo(3000L);

        Point expiringSoonPoint = pointRepository.findAll().get(0);
        ReflectionTestUtils.setField(expiringSoonPoint, "expirationDateTime", LocalDateTime.now().minusDays(1));
        pointRepository.save(expiringSoonPoint);

        // when
        jobLauncherTestUtils.setJob(expiredPointJob);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        List<PointHistory> pointHistories = pointHistoryRepository.findByMemberId(memberId);
        assertThat(pointHistories.size()).isEqualTo(2);
        memberPoints = PointSteps.getMyTotalPoints(memberId);
        assertThat(memberPoints).isEqualTo(0L);
    }

    @Test
    public void expiringSoonPointJobSuccess() throws Exception {
        // given
        Long memberPoints = PointSteps.getMyTotalPoints(memberId);
        assertThat(memberPoints).isEqualTo(3000L);

        Point expiringSoonPoint = pointRepository.findAll().get(0);
        ReflectionTestUtils.setField(expiringSoonPoint, "expirationDateTime", LocalDateTime.now().plusDays(29));
        pointRepository.save(expiringSoonPoint);

        // when
        jobLauncherTestUtils.setJob(expiringSoonPointJob);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        PointHistoryListResponseDto response = PointSteps.getExpiringSoonPointHistory(memberId);
        assertThat(response.getTotalPoints()).isEqualTo(3000L);
        assertThat(response.getPointHistories().size()).isEqualTo(1);
    }
}
