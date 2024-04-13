package com.example.companyservice.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.dto.response.*;
import com.example.companyservice.service.CenterService;
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

    @GetMapping("/info/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterInfoResponseDto>> getCenterInfo(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterInfo(centerId)));
    }

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

    @GetMapping("/hot-tickets/{latitude}/{longitude}")
    public ResponseEntity<BaseResponseDto<List<HotCenterTicketResponseDto>>> getHotCenterTicketList(HttpServletRequest request,
                                                                                                    @PathVariable(value = "latitude") double latitude,
                                                                                                    @PathVariable(value = "longitude") double longitude) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getHotCenterTicketList(memberId, latitude, longitude)));
    }

    @GetMapping("/related-centers/{latitude}/{longitude}")
    public ResponseEntity<BaseResponseDto<List<RecommendedCenterResponseDto>>> getRecommendedCenterList(HttpServletRequest request,
                                                                                                        @PathVariable(value = "latitude") double latitude,
                                                                                                        @PathVariable(value = "longitude") double longitude) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getRecommendedCenterList(memberId, latitude, longitude)));
    }
}
