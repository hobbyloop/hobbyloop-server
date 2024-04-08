package com.example.ticketservice.lecturereservation;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.service.AmazonS3Service;
import com.example.ticketservice.ticket.utils.AdminTicketSteps;
import com.example.ticketservice.ticket.utils.TicketSteps;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

public class LectureReservationAcceptanceTest extends AcceptanceTest {
    @MockBean
    private CompanyServiceClient companyServiceClient;

    @MockBean
    private AmazonS3Service amazonS3Service;

    long ticketId;

    long userTicketId;
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        given(companyServiceClient.getCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(amazonS3Service.upload(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");

        ticketId = AdminTicketSteps.createTicket(1L, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketId);

        userTicketId = TicketSteps.purchaseTicket(ticketId);
    }
}
