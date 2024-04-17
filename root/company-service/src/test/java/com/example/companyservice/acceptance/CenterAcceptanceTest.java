package com.example.companyservice.acceptance;

import com.example.companyservice.acceptance.steps.AdminCenterSteps;
import com.example.companyservice.acceptance.steps.CompanySteps;
import com.example.companyservice.client.TicketServiceClient;
import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.dto.response.CenterCreateResponseDto;
import com.example.companyservice.fixture.CenterFixture;
import com.example.companyservice.fixture.CompanyFixture;
import com.example.companyservice.repository.CompanyRepository;
import com.example.companyservice.service.AmazonS3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CenterAcceptanceTest extends AcceptanceTest {
    @MockBean
    private AmazonS3Service amazonS3Service;

    @MockBean
    private TicketServiceClient ticketServiceClient;

    @Autowired
    private CompanyRepository companyRepository;

    Long companyId = 1L;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        companyRepository.save(CompanyFixture.defaultSocialCompany());
        given(ticketServiceClient.createCompanyRatePlan(any())).willReturn(new BaseResponseDto<>(1L));
        CompanySteps.updateCompanyInfo(companyId, CompanyFixture.defaultCompanyUpdateRequest());
    }
    
    @Test
    public void createCenterSuccess() throws Exception {
        // given
        mockForCreateCenter();

        // when
        CenterCreateResponseDto response = AdminCenterSteps.createCenter(companyId, CenterFixture.defaultCenterCreateRequest());

        // then
        assertThat(response).isNotNull();
    }

    private void mockForCreateCenter() throws IOException {
        given(amazonS3Service.upload(any(), any())).willReturn("test");
        given(amazonS3Service.getFileUrl(any())).willReturn("test");
    }
}
