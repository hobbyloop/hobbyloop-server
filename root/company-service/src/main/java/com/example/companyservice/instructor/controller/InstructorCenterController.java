package com.example.companyservice.instructor.controller;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.instructor.dto.request.CreateInstructorCenterRequestDto;
import com.example.companyservice.instructor.dto.response.CreateInstructorCenterResponseDto;
import com.example.companyservice.instructor.dto.response.InstructorCenterResponseDto;
import com.example.companyservice.instructor.service.InstructorCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InstructorCenterController {

    private final InstructorCenterService instructorCenterService;

    @PostMapping("/instructor-center/{centerId}")
    public ResponseEntity<BaseResponseDto<CreateInstructorCenterResponseDto>> createInstructorCenter(@PathVariable(name = "centerId") long centerId,
                                                                                                     @RequestBody CreateInstructorCenterRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(instructorCenterService.createInstructorCenter(centerId, requestDto)));
    }

    @GetMapping("/instructor-center/{centerId}/{sort}")
    public ResponseEntity<BaseResponseDto<List<InstructorCenterResponseDto>>> getInstructorCenter(@PathVariable(name = "centerId") long centerId,
                                                                                                  @PathVariable(name = "sort") int sort) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(instructorCenterService.getInstructorCenter(centerId, sort)));
    }

    @PatchMapping("/instructor-center/{instructorCenterId}/auth/{auth}")
    public ResponseEntity<BaseResponseDto<Void>> updateInstructorCenterAuth(@PathVariable(name = "instructorCenterId") long instructorCenterId,
                                                                            @PathVariable(name = "auth") int auth) {
        instructorCenterService.updateInstructorCenterAuth(instructorCenterId, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new BaseResponseDto<>());
    }
}
