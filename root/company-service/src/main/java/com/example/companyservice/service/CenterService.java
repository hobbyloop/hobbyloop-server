package com.example.companyservice.service;

import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.dto.response.CenterCompanyResponseDto;
import com.example.companyservice.dto.response.CenterCreateResponseDto;
import com.example.companyservice.dto.response.CenterHomeResponseDto;
import com.example.companyservice.dto.response.CenterResponseListDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CenterService {

    CenterCreateResponseDto createCenter(long companyId, CenterCreateRequestDto requestDto);

    CenterResponseListDto getCenterList(long companyId);

    CenterHomeResponseDto getCenterHome(long centerId);

    CenterCompanyResponseDto getCenterCompany(long centerId);

    Long updateCenter(long centerId, CenterUpdateRequestDto requestDto);

    void updateQuickButton(long centerId, List<Integer> requestDto);
}
