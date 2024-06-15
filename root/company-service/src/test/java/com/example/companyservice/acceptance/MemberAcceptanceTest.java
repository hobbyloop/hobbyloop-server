package com.example.companyservice.acceptance;

import com.example.companyservice.acceptance.steps.MemberSteps;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.common.service.AmazonS3Service;
import com.example.companyservice.company.client.TicketServiceClient;
import com.example.companyservice.fixture.MemberFixture;
import com.example.companyservice.member.client.dto.PointEarnedResponseDto;
import com.example.companyservice.member.dto.MemberDetailResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

public class MemberAcceptanceTest extends AcceptanceTest {

    @MockBean
    private AmazonS3Service amazonS3Service;

    @MockBean
    private TicketServiceClient ticketServiceClient;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // mock for join member
        given(amazonS3Service.saveS3Img(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");
        given(ticketServiceClient.earnPointsWhenJoining(anyLong())).willReturn(
                new BaseResponseDto<>(new PointEarnedResponseDto(3000L, 3000L, LocalDateTime.now()))
        ); // TODO: Fixture로 대체
    }

    @Test
    public void joinMemberSuccess() throws Exception {
        // given & when
        long response = MemberSteps.joinMember(MemberFixture.defaultCreateMemberRequest());

        // then
        assertThat(response).isNotNull();
    }

    @Test
    public void updateMemberSuccess() throws Exception {
        // given
        long memberId = MemberSteps.joinMember(MemberFixture.defaultCreateMemberRequest());

        // when
        MemberSteps.updateMember(memberId, MemberFixture.defaultMemberUpdateRequest());
        MemberDetailResponseDto response = MemberSteps.getMemberDetail(memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(MemberFixture.UPDATED_NAME);
    }
}
