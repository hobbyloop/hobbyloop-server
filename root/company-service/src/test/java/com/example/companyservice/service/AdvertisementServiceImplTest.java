package com.example.companyservice.service;

import com.example.companyservice.dto.request.AdvertisementRequestDto;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.request.LocationRequestDto;
import com.example.companyservice.dto.response.AdvertisementResponseDto;
import com.example.companyservice.entity.*;
import com.example.companyservice.repository.CenterRepository;
import com.example.companyservice.repository.CompanyRepository;
import com.example.companyservice.repository.advertisement.AdvertisementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"eureka.client.enabled=false"})
@Transactional
class AdvertisementServiceImplTest {

    @Autowired
    AdvertisementService advertisementService;

    @Autowired
    AdvertisementRepository advertisementRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CenterRepository centerRepository;

    @Test
    @DisplayName("반경 3km 이내 조회")
    public void getAdvertisementList_in3Km() {
        // Given
        Company company = Company.from("sseioul@naver.com");
        companyRepository.save(company);

        for (int i = 1; i <= 100; i++) {
            CenterCreateRequestDto centerCreateRequestDto;
            if (i % 5 == 0) {
                centerCreateRequestDto = CenterCreateRequestDto.builder()
                        .centerName("center" + i)
                        .latitude(37.288412)
                        .longitude(127.051658)
                        .build();
            } else {
                centerCreateRequestDto = CenterCreateRequestDto.builder()
                        .centerName("center" + i)
                        .latitude(37.497952)
                        .longitude(127.027619)
                        .build();
            }

            Center center = Center.of(centerCreateRequestDto, company, "", "");
            centerRepository.save(center);

            AdvertisementRequestDto advertisementRequestDto = AdvertisementRequestDto.builder()
                    .content("content")
                    .keyword("keyword")
                    .adType(AdvertisementTypeEnum.BANNER.getName())
                    .paymentType(PaymentTypeEnum.PRE.getName())
                    .adStart(LocalDate.now().minusDays(10))
                    .adEnd(LocalDate.now().plusDays(10))
                    .build();
            Advertisement advertisement = Advertisement.of(advertisementRequestDto, center);
            advertisement.updateIsOpen(true);
            advertisementRepository.save(advertisement);
        }

        // When
        LocationRequestDto requestDto = new LocationRequestDto(37.2904308, 127.045804);
        List<AdvertisementResponseDto> advertisementList = advertisementService.getAdvertisementList(requestDto);

        // Then
        assertThat(advertisementList.size()).isEqualTo(20);
        advertisementList.forEach(ad -> {
            System.out.println(ad.getCenterId());
        });
    }
}