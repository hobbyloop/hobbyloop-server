package com.example.companyservice.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.service.BookmarkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookmarkController {

    public final BookmarkService bookmarkService;

    @PostMapping("/bookmark/{centerId}")
    public ResponseEntity<BaseResponseDto<Long>> createBookmark(HttpServletRequest request,
                                                                @PathVariable long centerId) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(bookmarkService.createBookmark(memberId, centerId)));
    }

    @DeleteMapping("/bookmark/{centerId}")
    public ResponseEntity<BaseResponseDto<Void>> deleteBookmark(HttpServletRequest request,
                                                                @PathVariable long centerId) {
        long memberId = Utils.parseAuthorizedId(request);
        bookmarkService.deleteBookmark(memberId, centerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new BaseResponseDto<>());
    }
}
