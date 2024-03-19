package com.example.companyservice.service;

import com.example.companyservice.client.TicketServiceClient;
import com.example.companyservice.client.dto.response.TicketResponseDto;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.dto.request.BusinessRequestDto;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.dto.request.HourRequestDto;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService {

    private final CompanyRepository companyRepository;

    private final CenterRepository centerRepository;

    private final CenterOperatingHourRepository centerOperatingHourRepository;

    private final CenterBreakHourRepository centerBreakHourRepository;

    private final QuickButtonRepository quickButtonRepository;

    private final TicketServiceClient ticketServiceClient;

    private final BookmarkRepository bookmarkRepository;

    private final AmazonS3Service amazonS3Service;

    @Override
    @Transactional
    public CenterCreateResponseDto createCenter(long companyId,
                                                CenterCreateRequestDto requestDto,
                                                MultipartFile logoImage,
                                                List<MultipartFile> centerImageList) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));

        String logoImageKey = saveS3Img(logoImage);
        String logoImageUrl = amazonS3Service.getFileUrl(logoImageKey);

        Center center = Center.of(requestDto, company, logoImageKey, logoImageUrl);
        Center saveCenter = centerRepository.save(center);

        List<HourResponseDto> operatingHourResponseDtoList = saveOperatingHour(requestDto.getOperatingHourList(), saveCenter);
        List<HourResponseDto> breakHourResponseDtoList = saveBreakHour(requestDto.getBreakHourList(), saveCenter);

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

    @Override
    @Transactional(readOnly = true)
    public CenterBusinessResponseDto getCenterBusiness(long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        CenterCreateResponseDto centerResponseDto = createCenterResponseDto(centerId, center);
        List<String> centerImageUrlList = new ArrayList<>();
        BusinessResponseDto businessResponseDto = BusinessResponseDto.from(center);
        return CenterBusinessResponseDto.of(centerResponseDto, center.getLogoImageUrl(), centerImageUrlList, businessResponseDto);
    }

    @Override
    @Transactional
    public Long updateCenter(long centerId, CenterUpdateRequestDto requestDto) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        center.centerUpdate(requestDto);

        centerOperatingHourRepository.deleteAllByCenterId(centerId);
        centerBreakHourRepository.deleteAllByCenterId(centerId);

        saveOperatingHour(requestDto.getOperatingHourList(), center);
        saveBreakHour(requestDto.getBreakHourList(), center);
        return center.getId();
    }

    @Override
    @Transactional
    public void updateQuickButton(long centerId, List<Integer> requestDto) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        quickButtonRepository.deleteAllByCenterId(centerId);
        List<QuickButton> quickButtonList = requestDto.stream().map(b -> QuickButton.of(b, center)).toList();
        quickButtonRepository.saveAll(quickButtonList);
    }

    @Override
    @Transactional
    public Long updateBusinessInfo(long centerId, BusinessRequestDto requestDto) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        center.businessInfoUpdate(requestDto);
        return center.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public CenterInfoResponseDto getCenterInfo(long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        List<CenterOperatingHour> centerOperatingHourList = centerOperatingHourRepository.findAllByCenterId(centerId);
        List<HourResponseDto> operatingHourResponseDtoList = centerOperatingHourList
                .stream()
                .map(o -> HourResponseDto.of(o.getDay(), o.getOpenAt(), o.getCloseAt()))
                .toList();
        List<CenterBreakHour> centerBreakHourList = centerBreakHourRepository.findAllByCenterId(centerId);
        List<HourResponseDto> breakHourResponseDtoList = centerBreakHourList
                .stream()
                .map(b -> HourResponseDto.of(b.getDay(), b.getOpenAt(), b.getCloseAt()))
                .toList();
        return CenterInfoResponseDto.of(center, operatingHourResponseDtoList, breakHourResponseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public CenterInfoDetailResponseDto getCenterInfoDetail(long centerId, long memberId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        Optional<Bookmark> isBookmark = bookmarkRepository.findByCenterIdAndMemberId(centerId, memberId);
        Integer reviewCount = ticketServiceClient.getReviewCountByCenterId(centerId).getData();
        List<CenterOperatingHour> operatingHourList = centerOperatingHourRepository.findAllByCenterId(centerId);
        List<HourResponseDto> hourResponseDtoList = operatingHourList
                .stream()
                .map(o -> HourResponseDto.of(o.getDay(), o.getOpenAt(), o.getCloseAt()))
                .toList();
        return CenterInfoDetailResponseDto.of(center, isBookmark.isPresent(), hourResponseDtoList, reviewCount);
    }

    private String saveS3Img(MultipartFile profileImg) {
        try {
            return amazonS3Service.upload(profileImg, "CenterImage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private List<HourResponseDto> saveBreakHour(List<HourRequestDto> requestDto, Center center) {
        List<HourResponseDto> breakHourDtoList = new ArrayList<>();
        requestDto.forEach(b -> {
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

    private List<HourResponseDto> saveOperatingHour(List<HourRequestDto> requestDto, Center center) {
        List<HourResponseDto> operatingHourDtoList = new ArrayList<>();
        requestDto.forEach(o -> {
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
