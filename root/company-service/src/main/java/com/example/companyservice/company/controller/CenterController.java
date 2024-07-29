package com.example.companyservice.company.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.company.dto.response.BookmarkCenterResponseDto;
import com.example.companyservice.company.dto.response.CenterInfoDetailResponseDto;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.company.dto.response.HotCenterTicketResponseDto;
import com.example.companyservice.company.dto.response.RecommendedCenterResponseDto;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/centers")
@Tag(name = "앱 시설 조회 API")
public class CenterController {

    private final CenterService centerService;

    @GetMapping("/info/detail/{centerId}/{advertisementId}")
    @Operation(summary = "시설 상세 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CenterInfoDetailResponseDto.class)))
    public ResponseEntity<BaseResponseDto<CenterInfoDetailResponseDto>> getCenterInfoDetail(HttpServletRequest request,
                                                                                            @Parameter(description = "시설 아이디", required = true)
                                                                                            @PathVariable(value = "centerId") long centerId,
                                                                                            @Parameter(description = "광고 아이디", required = true)
                                                                                            @PathVariable(value = "advertisementId") long advertisementId) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterInfoDetail(centerId, memberId, advertisementId)));
    }

    @GetMapping("/bookmark/{bookmarkId}/{sortId}")
    @Operation(summary = "북마크한 시설 리스트 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BookmarkCenterResponseDto.class)))
    public ResponseEntity<BaseResponseDto<List<BookmarkCenterResponseDto>>> getBookmarkCenterList(HttpServletRequest request,
                                                                                                  @Parameter(description = "북마크 아이디", required = true)
                                                                                                  @PathVariable(value = "bookmarkId") long bookmarkId,
                                                                                                  @Parameter(description = "정렬 기준", required = true)
                                                                                                  @PathVariable(value = "sortId") int sortId) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getBookmarkCenterList(memberId, bookmarkId, sortId)));
    }

    @GetMapping("/hot-tickets/{allow-location}/{latitude}/{longitude}")
    @Operation(summary = "이번주 HOT 이용권 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = HotCenterTicketResponseDto.class)))
    public ResponseEntity<BaseResponseDto<List<HotCenterTicketResponseDto>>> getHotCenterTicketList(HttpServletRequest request,
                                                                                                    @Parameter(description = "위치 공유 여부", required = true)
                                                                                                    @PathVariable(value = "allow-location") int allowLocation,
                                                                                                    @Parameter(description = "위도", required = true)
                                                                                                    @PathVariable(value = "latitude") double latitude,
                                                                                                    @Parameter(description = "경도", required = true)
                                                                                                    @PathVariable(value = "longitude") double longitude) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getHotCenterTicketList(memberId, allowLocation, latitude, longitude)));
    }

    @GetMapping("/recommend/{allow-location}/{latitude}/{longitude}")
    @Operation(summary = "하비루프 추천 시설 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = RecommendedCenterResponseDto.class)))
    public ResponseEntity<BaseResponseDto<List<RecommendedCenterResponseDto>>> getRecommendedCenterList(HttpServletRequest request,
                                                                                                        @Parameter(description = "위치 공유 여부", required = true)
                                                                                                        @PathVariable(value = "allow-location") int allowLocation,
                                                                                                        @Parameter(description = "위도", required = true)
                                                                                                        @PathVariable(value = "latitude") double latitude,
                                                                                                        @Parameter(description = "경도", required = true)
                                                                                                        @PathVariable(value = "longitude") double longitude) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getRecommendedCenterList(memberId, allowLocation, latitude, longitude)));
    }
}
