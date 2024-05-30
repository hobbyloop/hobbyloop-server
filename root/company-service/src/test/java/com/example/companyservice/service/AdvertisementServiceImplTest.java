package com.example.companyservice.service;

import com.example.companyservice.company.dto.request.AdvertisementRequestDto;
import com.example.companyservice.company.dto.request.CenterCreateRequestDto;
import com.example.companyservice.company.dto.request.CompanyCreateRequestDto;
import com.example.companyservice.company.dto.response.AdvertisementResponseDto;
import com.example.companyservice.company.entity.*;
import com.example.companyservice.company.service.AdvertisementService;
import com.example.companyservice.company.repository.CenterRepository;
import com.example.companyservice.company.repository.company.CompanyRepository;
import com.example.companyservice.company.repository.advertisement.AdvertisementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"eureka.client.enabled=false"})
@Transactional
public class AdvertisementServiceImplTest {

    @Autowired
    AdvertisementService advertisementService;

    @Autowired
    AdvertisementRepository advertisementRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CenterRepository centerRepository;

    @Test
    @DisplayName("배너 광고 조회")
    public void getAdvertisementList_in3Km() {
        // Given
        Company company = Company.of(new CompanyCreateRequestDto(), 1L);
        companyRepository.save(company);

        for (int i = 1; i <= 20; i++) {
            CenterCreateRequestDto centerCreateRequestDto;
            centerCreateRequestDto = CenterCreateRequestDto.builder()
                    .centerName("center" + i)
                    .build();

            Center center = Center.of(centerCreateRequestDto, company, "", "");
            centerRepository.save(center);

            AdvertisementRequestDto advertisementRequestDto = AdvertisementRequestDto.builder()
                    .content("content")
                    .keyword("keyword")
                    .adType(AdvertisementTypeEnum.BANNER.getName())
                    .paymentType(PaymentTypeEnum.PRE.getName())
                    .adStart(LocalDate.now().minusDays(10))
                    .adEnd(LocalDate.now().plusDays(10))
                    .adRank(20 - i + 1)
                    .build();
            Advertisement advertisement = Advertisement.of(advertisementRequestDto, center, null, null);
            advertisement.updateIsOpen(true);
            advertisementRepository.save(advertisement);
        }

        // When
        List<AdvertisementResponseDto> advertisementList = advertisementService.getAdvertisementList();

        // Then
        assertThat(advertisementList.size()).isEqualTo(20);
        assertThat(advertisementList.get(0).getAdRank()).isEqualTo(1);
        assertThat(advertisementList.get(0).getCenterName()).isEqualTo("center20");
        assertThat(advertisementList.get(19).getAdRank()).isEqualTo(20);
        assertThat(advertisementList.get(19).getCenterName()).isEqualTo("center1");
    }
}