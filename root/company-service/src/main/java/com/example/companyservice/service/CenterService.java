package com.example.companyservice.service;

import com.example.companyservice.dto.request.CenterCreateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CenterService {

    Long createCenter(long companyId, CenterCreateRequestDto requestDto, MultipartFile logo, List<MultipartFile> centerImageList);
}
