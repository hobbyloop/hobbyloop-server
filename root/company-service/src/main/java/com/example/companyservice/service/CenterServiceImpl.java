package com.example.companyservice.service;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.response.CenterCreateResponseDto;
import com.example.companyservice.dto.response.CenterResponseDto;
import com.example.companyservice.dto.response.CenterResponseListDto;
import com.example.companyservice.dto.response.HourResponseDto;
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

import java.util.ArrayList;
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
    public CenterCreateResponseDto createCenter(long companyId, CenterCreateRequestDto requestDto, MultipartFile logo, List<MultipartFile> centerImageList) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));
        Center center = Center.of(requestDto, company);
        Center saveCenter = centerRepository.save(center);

        List<HourResponseDto> operatingHourResponseDtoList = saveOperatingHour(requestDto, saveCenter);
        List<HourResponseDto> breakHourResponseDtoList = saveBreakHour(requestDto, saveCenter);

        return CenterCreateResponseDto.of(saveCenter, operatingHourResponseDtoList, breakHourResponseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public CenterResponseListDto getCenterList(long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));
        List<Center> centerList = centerRepository.findAllByCompanyId(companyId);
        List<CenterResponseDto> responseDtoList = centerList.stream().map(CenterResponseDto::from).toList();
        return CenterResponseListDto.of(company.getStartAt(), company.getEndAt(), responseDtoList);
    }

    private List<HourResponseDto> saveBreakHour(CenterCreateRequestDto requestDto, Center center) {
        List<HourResponseDto> breakHourDtoList = new ArrayList<>();
        requestDto.getBreakHourList().forEach(b -> {
            CenterBreakHour centerBreakHour = CenterBreakHour.of(b, center);
            centerBreakHourRepository.save(centerBreakHour);
            breakHourDtoList.add(HourResponseDto.of(
                    centerBreakHour.getDay(),
                    centerBreakHour.getOpenAt(),
                    centerBreakHour.getCloseAt())
            );
        });
        return breakHourDtoList;
    }

    private List<HourResponseDto> saveOperatingHour(CenterCreateRequestDto requestDto, Center center) {
        List<HourResponseDto> operatingHourList = new ArrayList<>();
        requestDto.getOperatingHourList().forEach(o -> {
            CenterOpeningHour centerOpeningHour = CenterOpeningHour.of(o, center);
            centerOpeningHourRepository.save(centerOpeningHour);
            operatingHourList.add(HourResponseDto.of(
                    centerOpeningHour.getDay(),
                    centerOpeningHour.getOpenAt(),
                    centerOpeningHour.getCloseAt())
            );
        });
        return operatingHourList;
    }
}
