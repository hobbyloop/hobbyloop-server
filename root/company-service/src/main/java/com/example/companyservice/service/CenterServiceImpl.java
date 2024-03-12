package com.example.companyservice.service;

import com.example.companyservice.client.TicketServiceClient;
import com.example.companyservice.client.dto.TicketResponseDto;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.response.*;
import com.example.companyservice.entity.*;
import com.example.companyservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService {

    private final CompanyRepository companyRepository;

    private final CenterRepository centerRepository;

    private final CenterOperatingHourRepository centerOperatingHourRepository;

    private final CenterBreakHourRepository centerBreakHourRepository;

    private final QuickButtonRepository quickButtonRepository;

    private final TicketServiceClient ticketServiceClient;

    @Override
    @Transactional
    public CenterCreateResponseDto createCenter(long companyId, CenterCreateRequestDto requestDto) {
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

    @Override
    @Transactional(readOnly = true)
    public CenterHomeResponseDto getCenterHome(long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        CenterCreateResponseDto centerResponseDto = createCenterResponseDto(centerId, center);
        List<Integer> quickButtonList = getQuickButtonList(centerId);
        List<TicketResponseDto> ticketResponseDtoList = ticketServiceClient.getTicketList(centerId).getData();
        return new CenterHomeResponseDto(centerResponseDto, quickButtonList, ticketResponseDtoList);
    }

    private List<Integer> getQuickButtonList(long centerId) {
        List<QuickButton> quickButtonList = quickButtonRepository.findAllByCenterId(centerId);
        return quickButtonList
                .stream()
                .map(QuickButton::getButtonId)
                .toList();
    }

    private CenterCreateResponseDto createCenterResponseDto(long centerId, Center center) {
        List<CenterOperatingHour> centerOpeningHourList = centerOperatingHourRepository.findAllByCenterId(centerId);
        List<CenterBreakHour> centerBreakHourList = centerBreakHourRepository.findAllByCenterId(centerId);

        List<HourResponseDto> operationHourDtoList = centerOpeningHourList
                .stream()
                .map(o -> HourResponseDto.of(o.getDay(), o.getOpenAt(), o.getCloseAt()))
                .toList();
        List<HourResponseDto> breakHourDtoList = centerBreakHourList
                .stream()
                .map(b -> HourResponseDto.of(b.getDay(), b.getOpenAt(), b.getCloseAt()))
                .toList();

        return CenterCreateResponseDto.of(center, operationHourDtoList, breakHourDtoList);
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
        List<HourResponseDto> operatingHourDtoList = new ArrayList<>();
        requestDto.getOperatingHourList().forEach(o -> {
            CenterOperatingHour centerOpeningHour = CenterOperatingHour.of(o, center);
            centerOperatingHourRepository.save(centerOpeningHour);
            operatingHourDtoList.add(HourResponseDto.of(
                    centerOpeningHour.getDay(),
                    centerOpeningHour.getOpenAt(),
                    centerOpeningHour.getCloseAt())
            );
        });
        return operatingHourDtoList;
    }
}
