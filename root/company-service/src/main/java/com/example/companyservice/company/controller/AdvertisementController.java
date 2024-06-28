package com.example.companyservice.company.controller;

import com.example.companyservice.company.dto.request.AdvertisementRequestDto;
import com.example.companyservice.company.service.AdvertisementService;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.company.dto.response.AdvertisementResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "광고 API")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping("/advertisement/{centerId}")
    @Operation(summary = "광고 등록")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class)))
    public ResponseEntity<BaseResponseDto<Long>> createAdvertisement(@Parameter(description = "시설 아이디", required = true)
                                                                     @PathVariable(value = "centerId") long centerId,
                                                                     @RequestPart(value = "requestDto") @Valid AdvertisementRequestDto requestDto,
                                                                     @Parameter(description = "광고 배너 이미지", required = true)
                                                                     @RequestPart(value = "bannerImage") MultipartFile bannerImage) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(advertisementService.createAdvertisement(centerId, requestDto, bannerImage)));
    }

    @GetMapping("/advertisement")
    @Operation(summary = "배너 광고 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AdvertisementResponseDto.class)))
    public ResponseEntity<BaseResponseDto<List<AdvertisementResponseDto>>> getAdvertisementList() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(advertisementService.getAdvertisementList()));
    }

    @GetMapping("/advertisement/rank")
    @Operation(summary = "사용중인 광고 게재 순위 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Integer.class)))
    public ResponseEntity<BaseResponseDto<List<Integer>>> getUsedRank() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(advertisementService.getUsedRank()));
    }
}
