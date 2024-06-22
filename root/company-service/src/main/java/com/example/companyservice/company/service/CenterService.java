package com.example.companyservice.company.service;

import com.example.companyservice.company.dto.request.BusinessRequestDto;
import com.example.companyservice.company.dto.request.CenterCreateRequestDto;
import com.example.companyservice.company.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.company.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CenterService {

    CenterCreateResponseDto createCenter(long companyId, CenterCreateRequestDto requestDto, MultipartFile logoImage, List<MultipartFile> centerImageList);

    CenterResponseListDto getCenterList(long companyId);

    CenterHomeResponseDto getCenterHome(long centerId);

    CenterBusinessResponseDto getCenterBusiness(long centerId);

    Long updateCenter(long centerId, CenterUpdateRequestDto requestDto, MultipartFile logoImage, List<MultipartFile> centerImageList);

    void updateQuickButton(long centerId, List<Integer> requestDto);

    Long updateBusinessInfo(long centerId, BusinessRequestDto requestDto);

    CenterInfoResponseDto getCenterInfo(long centerId);

    CenterInfoDetailResponseDto getCenterInfoDetail(long centerId, long memberId);

    List<BookmarkCenterResponseDto> getBookmarkCenterList(long memberId, long bookmarkId, int sortId);

    OriginalCenterResponseDto getOriginalCenterInfo(long centerId);

    OriginalBusinessResponseDto getOriginalBusinessInfo(long centerId);

    List<HotCenterTicketResponseDto> getHotCenterTicketList(long memberId, int allowLocation, double latitude, double longitude);

    List<RecommendedCenterResponseDto> getRecommendedCenterList(long memberId, int allowLocation, double latitude, double longitude);

    IsBookmarkResponseDto getIsBookmark(long centerId, long memberId);

    Long getCompanyIdOfCenter(Long centerId);
}
