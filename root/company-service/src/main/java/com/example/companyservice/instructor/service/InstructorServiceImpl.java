package com.example.companyservice.instructor.service;

import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.common.service.RedisService;
import com.example.companyservice.common.util.JwtUtils;
import com.example.companyservice.instructor.dto.request.CreateInstructorRequestDto;
import com.example.companyservice.instructor.entity.Instructor;
import com.example.companyservice.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    private final JwtUtils jwtUtils;

    private final RedisService redisService;

    @Override
    @Transactional
    public TokenResponseDto createInstructor(CreateInstructorRequestDto requestDto) {
        Instructor instructor = Instructor.from(requestDto);
        instructorRepository.save(instructor);
        String accessToken = jwtUtils.createToken(instructor.getId(), instructor.getRole());
        String refreshToken = jwtUtils.createRefreshToken(instructor.getId(), instructor.getRole());
        redisService.setValues(refreshToken, instructor.getSubject());
        return TokenResponseDto.of(accessToken, refreshToken);
    }
}
