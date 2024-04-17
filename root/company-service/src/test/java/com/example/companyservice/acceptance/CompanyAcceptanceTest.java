package com.example.companyservice.acceptance;

import com.example.companyservice.acceptance.steps.CompanySteps;
import com.example.companyservice.client.TicketServiceClient;
import com.example.companyservice.fixture.CompanyFixture;
import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompanyAcceptanceTest extends AcceptanceTest {

    @MockBean
    private TicketServiceClient ticketServiceClient;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        companyRepository.save(CompanyFixture.defaultSocialCompany());
    }

    @Test
    public void updateCompanyInfoSuccess() throws Exception {
        // given
        long companyId = 1L;

        // when
        mockForUpdateCompanyInfo();
        Long response = CompanySteps.updateCompanyInfo(companyId, CompanyFixture.defaultCompanyUpdateRequest());

        // then
        assertThat(response).isEqualTo(1L);
    }

    @Test
    public void checkTaxFreeSuccess() throws Exception {
        // given
        long companyId = 1L;
        mockForUpdateCompanyInfo();
        CompanySteps.updateCompanyInfo(companyId, CompanyFixture.defaultCompanyUpdateRequest());

        // when
        Boolean response = CompanySteps.checkTaxFree(companyId);

        // then
        assertThat(response).isTrue();
    }

    private void mockForUpdateCompanyInfo() {
        given(ticketServiceClient.createCompanyRatePlan(any())).willReturn(new BaseResponseDto<>(1L));
    }
}
