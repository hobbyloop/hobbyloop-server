package com.example.companyservice.service;

import com.example.companyservice.dto.request.BusinessRequestDto;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.dto.response.*;
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

    List<BookmarkCenterResponseDto> getBookmarkCenterList(long memberId);
}
