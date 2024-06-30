package com.example.companyservice.batch;

import com.example.companyservice.acceptance.AcceptanceTest;
import com.example.companyservice.acceptance.steps.MemberSteps;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.common.service.AmazonS3Service;
import com.example.companyservice.company.client.TicketServiceClient;
import com.example.companyservice.fixture.MemberFixture;
import com.example.companyservice.member.client.dto.PointEarnedResponseDto;
import com.example.companyservice.member.entity.Member;
import com.example.companyservice.member.entity.MemberArchive;
import com.example.companyservice.member.repository.MemberArchiveRepository;
import com.example.companyservice.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBatchTest
public class MemberBatchTest extends AcceptanceTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("withdrawnMemberJob")
    private Job withdrawnMemberJob;

    @MockBean
    private AmazonS3Service amazonS3Service;

    @MockBean
    private TicketServiceClient ticketServiceClient;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberArchiveRepository memberArchiveRepository;

    Long memberId;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // mock for join member
        given(amazonS3Service.saveS3Img(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");
        given(ticketServiceClient.earnPointsWhenJoining(anyLong())).willReturn(
                new BaseResponseDto<>(new PointEarnedResponseDto(3000L, 3000L, LocalDateTime.now()))
        );

        MemberSteps.joinMember(MemberFixture.defaultCreateMemberRequest());
        memberId = memberRepository.findAll().get(0).getId();
    }

    @Test
    public void withdrawnMemberJobSuccess() throws Exception {
        // given
        MemberSteps.deleteMember(memberId);

        Member withdrawnMember = memberRepository.findById(memberId).orElseThrow();

        ReflectionTestUtils.setField(withdrawnMember, "deletedAt", LocalDateTime.now().minusDays(91));
        memberRepository.save(withdrawnMember);

        // when
        jobLauncherTestUtils.setJob(withdrawnMemberJob);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        List<MemberArchive> archives = memberArchiveRepository.findAll();
        assertThat(archives.size()).isEqualTo(1);
    }
}
