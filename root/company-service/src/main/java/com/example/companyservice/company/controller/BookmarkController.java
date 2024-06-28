package com.example.companyservice.company.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.company.service.BookmarkService;
import com.example.companyservice.common.dto.BaseResponseDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "북마크 API")
public class BookmarkController {

    public final BookmarkService bookmarkService;

    @PostMapping("/bookmark/{centerId}")
    @Operation(summary = "북마크 저장")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class)))
    public ResponseEntity<BaseResponseDto<Long>> createBookmark(HttpServletRequest request,
                                                                @Parameter(description = "시설 아이디", required = true)
                                                                @PathVariable(value = "centerId") long centerId) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(bookmarkService.createBookmark(memberId, centerId)));
    }

    @DeleteMapping("/bookmark/{centerId}")
    @Operation(summary = "북마크 취소")
    @ApiResponse(responseCode = "204", description = "성공", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<BaseResponseDto<Void>> deleteBookmark(HttpServletRequest request,
                                                                @Parameter(description = "시설 아이디", required = true)
                                                                @PathVariable(value = "centerId") long centerId) {
        long memberId = Utils.parseAuthorizedId(request);
        bookmarkService.deleteBookmark(memberId, centerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new BaseResponseDto<>());
    }
}
