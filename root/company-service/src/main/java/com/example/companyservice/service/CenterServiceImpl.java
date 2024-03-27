package com.example.companyservice.service;

import com.example.companyservice.client.TicketServiceClient;
import com.example.companyservice.client.dto.response.BookmarkScoreTicketResponseDto;
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
import com.example.companyservice.repository.bookmark.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private final CenterImageRepository centerImageRepository;

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
        saveCenterImage(saveCenter, centerImageList);

        List<HourResponseDto> operatingHourResponseDtoList = saveOperatingHour(requestDto.getOperatingHourList(), saveCenter);
        List<HourResponseDto> breakHourResponseDtoList = saveBreakHour(requestDto.getBreakHourList(), saveCenter);

        return CenterCreateResponseDto.of(saveCenter, logoImageUrl, operatingHourResponseDtoList, breakHourResponseDtoList);
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
        List<Integer> quickButtonList = quickButtonRepository.findAllButtonIdByCenterId(centerId);
        List<TicketResponseDto> ticketResponseDtoList = ticketServiceClient.getTicketList(centerId).getData();
        return new CenterHomeResponseDto(centerResponseDto, quickButtonList, ticketResponseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public CenterBusinessResponseDto getCenterBusiness(long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        CenterCreateResponseDto centerResponseDto = createCenterResponseDto(centerId, center);
        List<String> centerImageUrlList = centerImageRepository.findAllCenterImage(centerId);
        BusinessResponseDto businessResponseDto = BusinessResponseDto.from(center);
        return CenterBusinessResponseDto.of(centerResponseDto, centerImageUrlList, businessResponseDto);
    }

    @Override
    @Transactional
    public Long updateCenter(long centerId,
                             CenterUpdateRequestDto requestDto,
                             MultipartFile logoImage,
                             List<MultipartFile> centerImageList) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));

        amazonS3Service.delete(center.getLogoImageKey());
        String logoImageKey = saveS3Img(logoImage);
        String logoImageUrl = amazonS3Service.getFileUrl(logoImageKey);
        center.centerUpdate(requestDto, logoImageKey, logoImageUrl);

        deleteAllCenterImage(centerId);
        saveCenterImage(center, centerImageList);

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
        List<HourResponseDto> operatingHourResponseDtoList = getOperationHourDtoList(centerOperatingHourList);
        List<CenterBreakHour> centerBreakHourList = centerBreakHourRepository.findAllByCenterId(centerId);
        List<HourResponseDto> breakHourResponseDtoList = getBreakHourDtoList(centerBreakHourList);
        return CenterInfoResponseDto.of(center, operatingHourResponseDtoList, breakHourResponseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public CenterInfoDetailResponseDto getCenterInfoDetail(long centerId, long memberId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        List<String> centerImageUrlList = centerImageRepository.findAllCenterImage(centerId);
        Optional<Bookmark> isBookmark = bookmarkRepository.findByCenterIdAndMemberId(centerId, memberId);
        Integer reviewCount = ticketServiceClient.getReviewCountByCenterId(centerId).getData();
        List<CenterOperatingHour> operatingHourList = centerOperatingHourRepository.findAllByCenterId(centerId);
        List<HourResponseDto> hourResponseDtoList = getOperationHourDtoList(operatingHourList);
        return CenterInfoDetailResponseDto.of(center, centerImageUrlList, isBookmark.isPresent(), hourResponseDtoList, reviewCount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookmarkCenterResponseDto> getBookmarkCenterList(long memberId, long bookmarkId, int sortId) {
        List<Bookmark> bookmarkList = bookmarkRepository.getBookmarkList(memberId, bookmarkId, sortId);
        List<Long> centerIdList = bookmarkList.stream().map(b -> b.getCenter().getId()).toList();
        Map<Long, BookmarkScoreTicketResponseDto> bookmarkTicketResponseDtoMap = ticketServiceClient.getBookmarkTicketList(centerIdList).getData();
        return getBookmarkCenterResponseDtoList(bookmarkList, bookmarkTicketResponseDtoMap);
    }

    @Override
    @Transactional(readOnly = true)
    public OriginalCenterResponseDto getOriginalCenterInfo(long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        List<String> centerImageUrlList = centerImageRepository.findAllCenterImage(centerId);
        List<CenterOperatingHour> centerOperatingHourList = centerOperatingHourRepository.findAllByCenterId(centerId);
        List<HourResponseDto> operationHourDtoList = getOperationHourDtoList(centerOperatingHourList);
        List<CenterBreakHour> centerBreakHourList = centerBreakHourRepository.findAllByCenterId(centerId);
        List<HourResponseDto> breakHourDtoList = getBreakHourDtoList(centerBreakHourList);
        return OriginalCenterResponseDto.of(center, centerImageUrlList, operationHourDtoList, breakHourDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public OriginalBusinessResponseDto getOriginalBusinessInfo(long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        return OriginalBusinessResponseDto.from(center);
    }

    private static List<BookmarkCenterResponseDto> getBookmarkCenterResponseDtoList(List<Bookmark> bookmarkList, Map<Long, BookmarkScoreTicketResponseDto> bookmarkTicketResponseDtoMap) {
        List<BookmarkCenterResponseDto> result = new ArrayList<>();
        bookmarkList.forEach((b) -> {
            Center center = b.getCenter();
            BookmarkCenterResponseDto bookmarkCenterResponseDto =
                    BookmarkCenterResponseDto.of(
                            b,
                            center,
                            bookmarkTicketResponseDtoMap.get(center.getId()).getScore(),
                            bookmarkTicketResponseDtoMap.getOrDefault(center.getId(), null)
                                    .getBookmarkTicketResponseDtoList()
                    );
            result.add(bookmarkCenterResponseDto);
        });
        return result;
    }

    private void saveCenterImage(Center center, List<MultipartFile> centerImageList) {
        centerImageList.forEach(i -> {
            String centerImageKey = saveS3Img(i);
            String centerImageUrl = amazonS3Service.getFileUrl(centerImageKey);
            CenterImage centerImage = CenterImage.of(centerImageKey, centerImageUrl, center);
            centerImageRepository.save(centerImage);
        });
    }

    private String saveS3Img(MultipartFile profileImg) {
        try {
            return amazonS3Service.upload(profileImg, "CenterImage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteAllCenterImage(long centerId) {
        List<CenterImage> OldCenterImageList = centerImageRepository.findAllByCenterId(centerId);
        OldCenterImageList.forEach(i -> {
            amazonS3Service.delete(i.getCenterImageKey());
            centerImageRepository.delete(i);
        });
    }

    private CenterCreateResponseDto createCenterResponseDto(long centerId, Center center) {
        List<CenterOperatingHour> centerOpeningHourList = centerOperatingHourRepository.findAllByCenterId(centerId);
        List<CenterBreakHour> centerBreakHourList = centerBreakHourRepository.findAllByCenterId(centerId);

        List<HourResponseDto> operationHourDtoList = getOperationHourDtoList(centerOpeningHourList);
        List<HourResponseDto> breakHourDtoList = getBreakHourDtoList(centerBreakHourList);

        return CenterCreateResponseDto.of(center, center.getLogoImageUrl(), operationHourDtoList, breakHourDtoList);
    }

    private List<HourResponseDto> saveBreakHour(List<HourRequestDto> requestDto, Center center) {
        List<CenterBreakHour> centerBreakHourList = requestDto.stream().map(b -> CenterBreakHour.of(b, center)).toList();
        centerBreakHourRepository.saveAll(centerBreakHourList);
        return getBreakHourDtoList(centerBreakHourList);
    }

    private List<HourResponseDto> saveOperatingHour(List<HourRequestDto> requestDto, Center center) {
        List<CenterOperatingHour> centerOperatingHourList = requestDto.stream().map(o -> CenterOperatingHour.of(o, center)).toList();
        centerOperatingHourRepository.saveAll(centerOperatingHourList);
        return getOperationHourDtoList(centerOperatingHourList);
    }

    private static List<HourResponseDto> getOperationHourDtoList(List<CenterOperatingHour> centerOpeningHourList) {
        return centerOpeningHourList
                .stream()
                .map(o -> HourResponseDto.of(o.getDay(), o.getOpenAt(), o.getCloseAt()))
                .toList();
    }

    private static List<HourResponseDto> getBreakHourDtoList(List<CenterBreakHour> centerBreakHourList) {
        return centerBreakHourList
                .stream()
                .map(b -> HourResponseDto.of(b.getDay(), b.getOpenAt(), b.getCloseAt()))
                .toList();
    }
}
