package com.example.companyservice.service;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.entity.Center;
import com.example.companyservice.entity.CenterBreakHour;
import com.example.companyservice.entity.CenterOpeningHour;
import com.example.companyservice.entity.Company;
import com.example.companyservice.repository.CenterBreakHourRepository;
import com.example.companyservice.repository.CenterOpeningHourRepository;
import com.example.companyservice.repository.CenterRepository;
import com.example.companyservice.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService {

    private final CompanyRepository companyRepository;

    private final CenterRepository centerRepository;

    private final CenterOpeningHourRepository centerOpeningHourRepository;

    private final CenterBreakHourRepository centerBreakHourRepository;

    @Override
    @Transactional
    public Long createCenter(long companyId, CenterCreateRequestDto requestDto, MultipartFile logo, List<MultipartFile> centerImageList) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));
        Center center = Center.of(requestDto, company);
        Center saveCenter = centerRepository.save(center);

        saveOperatingHour(requestDto, saveCenter);
        saveBreakHour(requestDto, saveCenter);

        return saveCenter.getId();
    }

    private void saveBreakHour(CenterCreateRequestDto requestDto, Center center) {
        requestDto.getBreakHourList().forEach(b -> {
            CenterBreakHour centerBreakHour = CenterBreakHour.of(b, center);
            centerBreakHourRepository.save(centerBreakHour);
        });
    }

    private void saveOperatingHour(CenterCreateRequestDto requestDto, Center center) {
        requestDto.getOperatingHourList().forEach(o -> {
            CenterOpeningHour centerOpeningHour = CenterOpeningHour.of(o, center);
            centerOpeningHourRepository.save(centerOpeningHour);
        });
    }
}
