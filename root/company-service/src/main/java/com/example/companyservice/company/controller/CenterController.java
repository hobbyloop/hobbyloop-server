package com.example.companyservice.company.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.company.dto.response.BookmarkCenterResponseDto;
import com.example.companyservice.company.dto.response.CenterInfoDetailResponseDto;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.company.dto.response.HotCenterTicketResponseDto;
import com.example.companyservice.company.dto.response.RecommendedCenterResponseDto;
import com.example.companyservice.company.service.CenterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/centers")
public class CenterController {

    private final CenterService centerService;

    @GetMapping("/info/detail/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterInfoDetailResponseDto>> getCenterInfoDetail(HttpServletRequest request,
                                                                                            @PathVariable(value = "centerId") long centerId) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterInfoDetail(centerId, memberId)));
    }

    @GetMapping("/bookmark/{bookmarkId}/{sortId}")
    public ResponseEntity<BaseResponseDto<List<BookmarkCenterResponseDto>>> getBookmarkCenterList(HttpServletRequest request,
                                                                                                  @PathVariable(value = "bookmarkId") long bookmarkId,
                                                                                                  @PathVariable(value = "sortId") int sortId) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getBookmarkCenterList(memberId, bookmarkId, sortId)));
    }

    @GetMapping("/hot-tickets/{allow-location}/{latitude}/{longitude}")
    public ResponseEntity<BaseResponseDto<List<HotCenterTicketResponseDto>>> getHotCenterTicketList(HttpServletRequest request,
                                                                                                    @PathVariable(value = "allow-location") int allowLocation,
                                                                                                    @PathVariable(value = "latitude") double latitude,
                                                                                                    @PathVariable(value = "longitude") double longitude) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getHotCenterTicketList(memberId, allowLocation, latitude, longitude)));
    }

    @GetMapping("/recommend/{allow-location}/{latitude}/{longitude}")
    public ResponseEntity<BaseResponseDto<List<RecommendedCenterResponseDto>>> getRecommendedCenterList(HttpServletRequest request,
                                                                                                        @PathVariable(value = "allow-location") int allowLocation,
                                                                                                        @PathVariable(value = "latitude") double latitude,
                                                                                                        @PathVariable(value = "longitude") double longitude) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getRecommendedCenterList(memberId, allowLocation, latitude, longitude)));
    }
}
