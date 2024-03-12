package com.example.companyservice.service;

import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.response.CenterCreateResponseDto;
import com.example.companyservice.dto.response.CenterHomeResponseDto;
import com.example.companyservice.dto.response.CenterResponseListDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CenterService {

    CenterCreateResponseDto createCenter(long companyId, CenterCreateRequestDto requestDto);

    CenterResponseListDto getCenterList(long companyId);

    CenterHomeResponseDto getCenterHome(long centerId);
}
