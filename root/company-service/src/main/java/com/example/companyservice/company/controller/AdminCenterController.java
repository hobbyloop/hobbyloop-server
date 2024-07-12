package com.example.companyservice.company.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.company.dto.request.BusinessRequestDto;
import com.example.companyservice.company.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.company.dto.response.*;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.company.dto.request.CenterCreateRequestDto;
import com.example.companyservice.company.service.CenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/centers")
@Tag(name = "관리자용 시설 API")
public class AdminCenterController {

    private final CenterService centerService;

    @PostMapping()
    @Operation(summary = "시설 생성")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = CenterCreateResponseDto.class)))
    public ResponseEntity<BaseResponseDto<CenterCreateResponseDto>> createCenter(HttpServletRequest request,
                                                                                 @RequestPart(value = "requestDto") CenterCreateRequestDto requestDto,
                                                                                 @Parameter(description = "로고 이미지", required = true)
                                                                                 @RequestPart(value = "logoImage") MultipartFile logoImage,
                                                                                 @Parameter(description = "시설 소개 이미지 리스트", required = true)
                                                                                 @RequestPart(value = "centerImageList") List<MultipartFile> centerImageList) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(centerService.createCenter(companyId, requestDto, logoImage, centerImageList)));
    }

    @GetMapping()
    @Operation(summary = "시설 프로필 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CenterResponseListDto.class)))
    public ResponseEntity<BaseResponseDto<CenterResponseListDto>> getCenterList(HttpServletRequest request) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterList(companyId)));
    }

    @GetMapping("/home/{centerId}")
    @Operation(summary = "시설 관리자 페이지 홈 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CenterHomeResponseDto.class)))
    public ResponseEntity<BaseResponseDto<CenterHomeResponseDto>> getCenterHome(@Parameter(description = "시설 아이디", required = true)
                                                                                @PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterHome(centerId)));
    }

    @GetMapping("/tickets/{centerId}")
    @Operation(summary = "시설 관리자 페이지 시설 상세 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CenterBusinessResponseDto.class)))
    public ResponseEntity<BaseResponseDto<CenterBusinessResponseDto>> getCenterCompany(@Parameter(description = "시설 아이디", required = true)
                                                                                       @PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterBusiness(centerId)));
    }

    @PostMapping("/quick-button/{centerId}")
    @Operation(summary = "즐겨찾기 버튼 수정")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<BaseResponseDto<Void>> updateQuickButton(@Parameter(description = "시설 아이디", required = true)
                                                                   @PathVariable(value = "centerId") long centerId,
                                                                   @RequestBody List<Integer> requestDto) {
        centerService.updateQuickButton(centerId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>());
    }

    @GetMapping("/original/{centerId}")
    @Operation(summary = "수정 전 시설 정보 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = OriginalCenterResponseDto.class)))
    public ResponseEntity<BaseResponseDto<OriginalCenterResponseDto>> getOriginalCenterInfo(@Parameter(description = "시설 아이디", required = true)
                                                                                            @PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getOriginalCenterInfo(centerId)));
    }

    @GetMapping("/original/business/{centerId}")
    @Operation(summary = "수정 전 사업자 정보 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = OriginalBusinessResponseDto.class)))
    public ResponseEntity<BaseResponseDto<OriginalBusinessResponseDto>> getOriginalBusinessInfo(@Parameter(description = "시설 아이디", required = true)
                                                                                                @PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getOriginalBusinessInfo(centerId)));
    }

    @PatchMapping("/{centerId}")
    @Operation(summary = "시설 정보 업데이트")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class)))
    public ResponseEntity<BaseResponseDto<Long>> updateCenter(@Parameter(description = "시설 아이디", required = true)
                                                              @PathVariable(value = "centerId") long centerId,
                                                              @RequestPart(value = "requestDto") CenterUpdateRequestDto requestDto,
                                                              @Parameter(description = "로고 이미지", required = true)
                                                              @RequestPart(value = "logoImage") MultipartFile logoImage,
                                                              @Parameter(description = "시설 소개 이미지 리스트", required = true)
                                                              @RequestPart(value = "centerImageList") List<MultipartFile> centerImageList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.updateCenter(centerId, requestDto, logoImage, centerImageList)));
    }

    @PatchMapping("/business/{centerId}")
    @Operation(summary = "사업자 정보 업데이트")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class)))
    public ResponseEntity<BaseResponseDto<Long>> updateBusinessInfo(@Parameter(description = "시설 아이디", required = true)
                                                                    @PathVariable(value = "centerId") long centerId,
                                                                    @RequestBody BusinessRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.updateBusinessInfo(centerId, requestDto)));
    }

    @PostMapping("/blind/review/{centerId}/{reviewId}")
    @Operation(summary = "리뷰 블라인드 요청")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<BaseResponseDto<String>> blindReview(@Parameter(description = "시설 아이디", required = true)
                                                               @PathVariable(value = "centerId") long centerId,
                                                               @Parameter(description = "리뷰 아이디", required = true)
                                                               @PathVariable(value = "reviewId") long reviewId,
                                                               @RequestBody String reason) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(centerService.blindReview(centerId, reviewId, reason)));
    }
}
