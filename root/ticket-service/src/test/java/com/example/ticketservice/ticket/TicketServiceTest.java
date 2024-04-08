package com.example.ticketservice.ticket;

import com.example.ticketservice.DatabaseCleanup;
import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.response.TicketDetailResponseDto;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.repository.ticket.UserTicketRepository;
import com.example.ticketservice.service.AmazonS3Service;
import com.example.ticketservice.service.TicketService;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * given: 티켓이 존재하는 상태에서
     * when: 티켓을 동시에 10명이 구매한다.
     * then: 티켓이 정상적으로 구매되고 발급된다.
     */
    @Test
    public void concurrencyPurchaseTicketSuccess() throws Exception {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for(int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    // when
                    ticketService.purchaseTicket(1L, ticketId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // then
        TicketDetailResponseDto ticket = ticketService.getTicketDetail(ticketId);
        assertThat(ticket.getIssueCount()).isEqualTo(10);

    }

    /**
     * given: 티켓이 존재하는 상태에서(총 수량은 15개)
     * when: 티켓을 동시에 16명이 구매한다
     * then: 티켓은 15개가 발급되고, 한 명은 구매에 실패한다.
     */
    @Test
    public void concurrencyPurchaseTicketWithOneFailureDueToExceedingTotalCount() throws Exception {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(TicketFixture.TOTAL_COUNT + 1);
        AtomicInteger failCount = new AtomicInteger(0);

        for(int i = 0; i < TicketFixture.TOTAL_COUNT + 1; i++) {
            executorService.execute(() -> {
                try {
                    // when
                    ticketService.purchaseTicket(1L, ticketId);
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // then
        TicketDetailResponseDto ticket = ticketService.getTicketDetail(ticketId);
        assertThat(ticket.getIssueCount()).isEqualTo(TicketFixture.TOTAL_COUNT);
        assertThat(failCount.get()).isEqualTo(1);
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
