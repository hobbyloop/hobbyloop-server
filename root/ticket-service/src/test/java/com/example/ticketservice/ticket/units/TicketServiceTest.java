package com.example.ticketservice.ticket.units;

import com.example.ticketservice.DatabaseCleanup;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.ticket.repository.ticket.UserTicketRepository;
import com.example.ticketservice.ticket.service.AmazonS3Service;
import com.example.ticketservice.ticket.service.TicketService;
import com.example.ticketservice.ticket.service.UserTicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TicketServiceTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private TicketService ticketService;

    @MockBean
    private CompanyServiceClient companyServiceClient;

    @MockBean
    private AmazonS3Service amazonS3Service;

    long ticketId;

    @BeforeEach
    public void setUp() throws Exception {
        databaseCleanup.execute();

        given(companyServiceClient.getCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(amazonS3Service.upload(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");

        given(companyServiceClient.getOriginalCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalCenterResponseDto()));
        given(companyServiceClient.getOriginalBusinessInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalBusinessResponseDto()));

        ticketId = ticketService.createTicket(1L, TicketFixture.defaultTicketCreateRequest(), generateMockImageFile()).getTicketId();
        ticketService.uploadTicket(ticketId);
    }


    private MultipartFile generateMockImageFile() throws IOException {
        File tempFile = File.createTempFile("test", ".jpg");
        tempFile.deleteOnExit();

        String content = "test file content";
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(content.getBytes());
        }

        Path path = tempFile.toPath();
        String name = tempFile.getName();
        String originalFileName = tempFile.getName();
        String contentType = "image/jpeg";
        byte[] contentBytes = Files.readAllBytes(path);

        return new MockMultipartFile(name, originalFileName, contentType, contentBytes);
    }
}
