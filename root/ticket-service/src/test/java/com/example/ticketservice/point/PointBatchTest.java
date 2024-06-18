package com.example.ticketservice.point;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.repository.PointHistoryRepository;
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
public class PointBatchTest extends AcceptanceTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("expiredPointJob")
    private Job expiredPointJob;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    Long memberId = 1L;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        PointSteps.earnPointWhenJoining(memberId);
    }

    @Test
    public void expiredPointJobSuccess() throws Exception {
        Long memberPoints = PointSteps.getMyTotalPoints(memberId);
        assertThat(memberPoints).isEqualTo(3000L);

        PointHistory expiredHistory = pointHistoryRepository.findByMemberId(memberId).get(0);
        ReflectionTestUtils.setField(expiredHistory, "expirationDateTime", LocalDateTime.now().minusDays(1));
        pointHistoryRepository.save(expiredHistory);

        // when
        jobLauncherTestUtils.setJob(expiredPointJob);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        List<PointHistory> pointHistories = pointHistoryRepository.findByMemberId(memberId);
        assertThat(pointHistories.size()).isEqualTo(2);
        memberPoints = PointSteps.getMyTotalPoints(memberId);
        assertThat(memberPoints).isEqualTo(0L);
        expiredHistory = pointHistoryRepository.findById(expiredHistory.getId()).orElseThrow();
        assertThat(expiredHistory.getIsProcessedByBatch()).isTrue();
        assertThat(expiredHistory.getUpdatedBy()).isEqualTo("batch");
    }
}
