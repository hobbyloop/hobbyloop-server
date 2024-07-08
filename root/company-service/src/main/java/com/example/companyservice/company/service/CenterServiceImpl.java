package com.example.companyservice.company.service;

import com.example.companyservice.common.kafka.KafkaProducer;
import com.example.companyservice.common.service.AmazonS3Service;
import com.example.companyservice.company.client.TicketServiceClient;
import com.example.companyservice.company.client.dto.request.CenterOriginalAndUpdateInfoDto;
import com.example.companyservice.company.client.dto.response.*;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.company.dto.request.BusinessRequestDto;
import com.example.companyservice.company.dto.request.CenterCreateRequestDto;
import com.example.companyservice.company.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.company.dto.request.HourRequestDto;
import com.example.companyservice.company.dto.response.*;
import com.example.companyservice.company.entity.*;
import com.example.companyservice.company.repository.*;
import com.example.companyservice.company.repository.advertisement.AdvertisementRepository;
import com.example.companyservice.company.repository.bookmark.BookmarkRepository;
import com.example.companyservice.company.repository.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private final AdvertisementRepository advertisementRepository;

    private final LocationService locationService;

    private final KafkaProducer kafkaProducer;

    @Override
    @Transactional
    public CenterCreateResponseDto createCenter(long companyId,
                                                CenterCreateRequestDto requestDto,
                                                MultipartFile logoImage,
                                                List<MultipartFile> centerImageList) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));

        String logoImageKey = amazonS3Service.saveS3Img(logoImage, "CenterImage");
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
        return CenterResponseListDto.of(company.getStartAt(), company.getEndAt(), centerList);
    }

    @Override
    @Transactional(readOnly = true)
    public CenterHomeResponseDto getCenterHome(long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        CenterCreateResponseDto centerResponseDto = createCenterResponseDto(centerId, center);
        List<Integer> quickButtonList = quickButtonRepository.findAllButtonIdByCenterId(centerId);
        TicketClientBaseResponseDto ticketBaseResponseDto = ticketServiceClient.getTicketList(centerId).getData();
        return CenterHomeResponseDto.of(centerResponseDto, quickButtonList, ticketBaseResponseDto);
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

        String logoImageKey = amazonS3Service.saveS3Img(logoImage, "CenterImage");
        String logoImageUrl = amazonS3Service.getFileUrl(logoImageKey);

        List<CenterOperatingHour> operatingHourList = centerOperatingHourRepository.findAllByCenterId(centerId);
        List<CenterBreakHour> breakHourList = centerBreakHourRepository.findAllByCenterId(centerId);
        List<HourResponseDto> operationHourDtoList = getOperationHourDtoList(operatingHourList);
        List<HourResponseDto> breakHourDtoList = getBreakHourDtoList(breakHourList);

        CenterOriginalAndUpdateInfoDto centerOriginalAndUpdateInfoDto = CenterOriginalAndUpdateInfoDto.of(
                center,
                operationHourDtoList,
                breakHourDtoList,
                requestDto,
                logoImageKey,
                logoImageUrl
        );
        center.centerUpdate(requestDto, logoImageKey, logoImageUrl);

        centerImageRepository.updateAllIsDeletedTrue(centerId);
        List<String> oldCenterImageKeyList = centerImageRepository
                .findAllByCenterIdAndIsDeletedTrue(centerId).stream().map(CenterImage::getCenterImageKey).toList();
        List<String> newCenterImageList = saveCenterImage(center, centerImageList);

        centerOriginalAndUpdateInfoDto.setOldCenterImageKeyList(oldCenterImageKeyList);
        centerOriginalAndUpdateInfoDto.setNewCenterImageKeyList(newCenterImageList);

        centerOperatingHourRepository.deleteAllByCenterId(centerId);
        centerBreakHourRepository.deleteAllByCenterId(centerId);

        saveOperatingHour(requestDto.getOperatingHourList(), center);
        saveBreakHour(requestDto.getBreakHourList(), center);

        kafkaProducer.send("update-address-info", centerOriginalAndUpdateInfoDto);

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
        boolean isBookmark = bookmarkRepository.existsByCenterIdAndMemberId(centerId, memberId);
        TicketDetailClientResponseDto ticketInfo = ticketServiceClient.getTicketDetailInfo(centerId).getData();
        List<CenterOperatingHour> operatingHourList = centerOperatingHourRepository.findAllByCenterId(centerId);
        List<CenterBreakHour> breakHourList = centerBreakHourRepository.findAllByCenterId(centerId);
        List<HourResponseDto> hourResponseDtoList = getOperationHourDtoList(operatingHourList);
        List<HourResponseDto> breakHourDtoList = getBreakHourDtoList(breakHourList);
        return CenterInfoDetailResponseDto.of(center, centerImageUrlList, isBookmark, hourResponseDtoList, breakHourDtoList, ticketInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookmarkCenterResponseDto> getBookmarkCenterList(long memberId, long bookmarkId, int sortId) {
        List<Bookmark> bookmarkList = bookmarkRepository.getBookmarkList(memberId, bookmarkId, sortId);
        List<Long> centerIdList = bookmarkList.stream().map(b -> b.getCenter().getId()).toList();
        Map<Long, BookmarkScoreTicketResponseDto> bookmarkTicketResponseDtoMap = ticketServiceClient.getBookmarkTicketList(centerIdList).getData();
        List<BookmarkCenterResponseDto> result = new ArrayList<>();
        bookmarkList.forEach((b) -> {
            Center center = b.getCenter();
            BookmarkCenterResponseDto bookmarkCenterResponseDto =
                    BookmarkCenterResponseDto.of(b, center,
                            bookmarkTicketResponseDtoMap.get(center.getId()).getScore(),
                            bookmarkTicketResponseDtoMap.get(center.getId()).getReviewCount(),
                            bookmarkTicketResponseDtoMap.getOrDefault(center.getId(), null)
                                    .getBookmarkTicketResponseDtoList()
                    );
            result.add(bookmarkCenterResponseDto);
        });
        return result;
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

    @Override
    @Transactional(readOnly = true)
    public List<HotCenterTicketResponseDto> getHotCenterTicketList(long memberId, int allowLocation, double latitude, double longitude) {
        List<MainHomeCenterResponseDto> responseDtoList = new ArrayList<>();
        List<Long> centerIdList = new ArrayList<>();
        List<Advertisement> advertisementList = advertisementRepository.findAllCPCAdvertisement();
        toMainHomeCenterResponseDto(memberId, allowLocation, latitude, longitude, responseDtoList, advertisementList, centerIdList);
        Map<Long, TicketInfoClientResponseDto> ticketResponseDtoMap = ticketServiceClient.getHotTicketList(centerIdList).getData();
        return responseDtoList.stream().map(c -> HotCenterTicketResponseDto.of(c, ticketResponseDtoMap.get(c.getCenterId()))).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommendedCenterResponseDto> getRecommendedCenterList(long memberId, int allowLocation, double latitude, double longitude) {
        List<MainHomeCenterResponseDto> responseDtoList = new ArrayList<>();
        List<Advertisement> advertisementList = advertisementRepository.findAllCPCCPMAdvertisement();
        List<Long> centerIdList = new ArrayList<>();
        toMainHomeCenterResponseDto(memberId, allowLocation, latitude, longitude, responseDtoList, advertisementList, centerIdList);
        Map<Long, TicketInfoClientResponseDto> ticketResponseDtoMap = ticketServiceClient.getRecommendTicketList(centerIdList).getData();
        return responseDtoList.stream().map(c -> RecommendedCenterResponseDto.of(c, ticketResponseDtoMap.get(c.getCenterId()))).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public IsBookmarkResponseDto getIsBookmark(long centerId, long memberId) {
        boolean isBookmark = bookmarkRepository.existsByCenterIdAndMemberId(centerId, memberId);
        return IsBookmarkResponseDto.of(isBookmark);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCompanyIdOfCenter(Long centerId) {
        Center center = centerRepository.findById(centerId).orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));

        return center.getCompany().getId();
    }

    private void toMainHomeCenterResponseDto(long memberId, int allowLocation, double latitude, double longitude, List<MainHomeCenterResponseDto> responseDtoList, List<Advertisement> advertisementList, List<Long> centerIdList) {
        for (Advertisement advertisement : advertisementList) {
            if (centerIdList.size() >= 30) break;
            Center center = advertisement.getCenter();
            if (allowLocation == 1) {
                double distance = locationService.getDistance(latitude, longitude, center.getLatitude(), center.getLongitude());
                if (distance > 3) continue;
            }
            centerIdList.add(center.getId());
            boolean isBookmark = bookmarkRepository.existsByCenterIdAndMemberId(center.getId(), memberId);
            MainHomeCenterResponseDto responseDto = MainHomeCenterResponseDto.of(center, isBookmark);
            responseDtoList.add(responseDto);
        }
    }

    private List<String> saveCenterImage(Center center, List<MultipartFile> centerImageList) {
        List<String> newCenterImageKeyList = new ArrayList<>();
        centerImageList.forEach(i -> {
            String centerImageKey = amazonS3Service.saveS3Img(i, "CenterImage");
            String centerImageUrl = amazonS3Service.getFileUrl(centerImageKey);
            CenterImage centerImage = CenterImage.of(centerImageKey, centerImageUrl, center);
            centerImageRepository.save(centerImage);
            newCenterImageKeyList.add(centerImageKey);
        });
        return newCenterImageKeyList;
    }
//
//    private void deleteAllCenterImage(long centerId) {
//        List<CenterImage> oldCenterImageList = centerImageRepository.findAllByCenterId(centerId);
//        oldCenterImageList.forEach(i -> {
//            amazonS3Service.delete(i.getCenterImageKey());
//            centerImageRepository.delete(i);
//        });
//    }

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
