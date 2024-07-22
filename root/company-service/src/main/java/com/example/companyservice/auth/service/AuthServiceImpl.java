package com.example.companyservice.auth.service;

import com.example.companyservice.admin.entity.Admin;
import com.example.companyservice.admin.repository.AdminRepository;
import com.example.companyservice.auth.dto.request.AdminLoginRequestDto;
import com.example.companyservice.auth.dto.request.MemberLoginRequestDto;
import com.example.companyservice.auth.dto.response.MemberLoginResponseDto;
import com.example.companyservice.auth.entity.Role;
import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.service.RedisService;
import com.example.companyservice.common.util.JwtUtils;
import com.example.companyservice.company.entity.Company;
import com.example.companyservice.company.repository.company.CompanyRepository;
import com.example.companyservice.instructor.domain.Instructor;
import com.example.companyservice.instructor.infrastructure.persistence.InstructorRepository;
import com.example.companyservice.member.entity.Member;
import com.example.companyservice.member.repository.MemberRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    private final InstructorRepository instructorRepository;

    private final CompanyRepository companyRepository;

    private final AdminRepository adminRepository;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final RedisService redisService;

    @Override
    @Transactional(readOnly = true)
    public MemberLoginResponseDto memberLogin(MemberLoginRequestDto requestDto) {
        String oAuthAccessToken = requestDto.getAccessToken();
        String provider = requestDto.getProvider();
        String reqURL = getReqUrl(provider);
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if ("Kakao".equals(provider)) {
                conn.setRequestMethod("POST");
            } else {
                conn.setRequestMethod("GET");
            }

            // 요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + oAuthAccessToken);

            int responseCode = conn.getResponseCode();
            log.info("responseCode : {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : {}", result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            HashMap<String, String> userInfo = getUserInfo(provider, element);

            String email = userInfo.get("email");
            String subject = userInfo.get("subject");
            Optional<Member> memberOptional = memberRepository.findByProviderAndSubjectAndIsDeletedFalse(provider, subject);
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
                String accessToken = jwtUtils.createToken(member.getId(), member.getRole());
                String refreshToken = jwtUtils.createRefreshToken(member.getId(), member.getRole());
                redisService.setValues(refreshToken, subject);
                return MemberLoginResponseDto.of(accessToken, refreshToken, null, null, null, null);
            } else {
                return MemberLoginResponseDto.of(null, null, email, provider, subject, oAuthAccessToken);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String getReqUrl(String provider) {
        if ("Kakao".equals(provider)) {
            return "https://kapi.kakao.com/v2/user/me";
        } else if ("Naver".equals(provider)) {
            return "https://openapi.naver.com/v1/nid/me";
        } else {
            return "https://www.googleapis.com/oauth2/v2/userinfo";
        }
    }

    private HashMap<String, String> getUserInfo(String provider, JsonElement element) {
        HashMap<String, String> userInfo = new HashMap<>();
        if ("Kakao".equals(provider)) {
            String subject = element.getAsJsonObject().get("id").getAsString();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("email", email);
            userInfo.put("subject", subject);
        } else if ("Naver".equals(provider)) {
            JsonObject naverAccount = element.getAsJsonObject().get("response").getAsJsonObject();

            String subject = naverAccount.getAsJsonObject().get("id").getAsString();
            String email = naverAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("email", email);
            userInfo.put("subject", subject);
        } else {
            String subject = element.getAsJsonObject().get("id").getAsString();
            String email = element.getAsJsonObject().get("email").getAsString();

            userInfo.put("email", email);
            userInfo.put("subject", subject);
        }
        return userInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto adminLogin(AdminLoginRequestDto requestDto) {
        Admin admin = adminRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NOT_EXIST_EMAIL_EXCEPTION));

        if (!passwordEncoder.matches(requestDto.getPassword(), admin.getPassword())) {
            throw new ApiException(ExceptionEnum.PASSWORD_NOT_MATCH_EXCEPTION);
        }

        String accessToken = jwtUtils.createToken(admin.getId(), admin.getRole());
        String refreshToken = jwtUtils.createRefreshToken(admin.getId(), admin.getRole());
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public TokenResponseDto refreshAccessToken(HttpServletRequest request) {
        String accessToken = jwtUtils.resolveToken(request, "Authorization");
        String refreshToken = jwtUtils.resolveToken(request, "RefreshAuthorization");

        Long refreshTokenPk = Long.parseLong(jwtUtils.getUserPk(refreshToken));
        String role;
        try {
            role = jwtUtils.getRole(refreshToken);
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.ACCESS_NOW_ALLOW_EXCEPTION);
        }
        String subject = getSubject(refreshTokenPk, role);
        tokenValidation(accessToken, refreshToken, subject);

        String newAccessToken = jwtUtils.createToken(refreshTokenPk, Role.findByName(role));
        String newRefreshToken = jwtUtils.createRefreshToken(refreshTokenPk, Role.findByName(role));

        redisService.setValues(newRefreshToken, subject);

        return TokenResponseDto.of(newAccessToken, newRefreshToken);
    }

    private String getSubject(Long id, String role) {
        if ("USER".equals(role)) {
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));
            return member.getSubject();
        } else if ("INSTRUCTOR".equals(role)) {
            return null;
        } else {
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.COMPANY_NOT_EXIST_EXCEPTION));
            return company.getSubject();
        }
    }

    private void tokenValidation(String accessToken, String refreshToken, String subject) {
        String refreshTokenRedis = redisService.getValues(subject);

        // 리프레쉬 토큰과 액세스 토큰 null 체크
        if (accessToken == null || refreshToken == null) {
            log.error("accessToken or refreshToken null");
            throw new ApiException(ExceptionEnum.ACCESS_NOW_ALLOW_EXCEPTION);
        }

        // 리프레쉬 토큰 유효성 검사 - 만료시 에러
        if (!jwtUtils.validateToken(refreshToken)) {
            log.error("refreshToken not valid");
            throw new ApiException(ExceptionEnum.ACCESS_NOW_ALLOW_EXCEPTION);
        }


        // 헤더 리프레쉬 토큰과 레디스 리프레쉬 토큰 동등성 비교
        if (!refreshToken.equals(refreshTokenRedis)) {
            log.error("accessToken and refreshToken not equals");
            throw new ApiException(ExceptionEnum.ACCESS_NOW_ALLOW_EXCEPTION);
        }

        // 액세스 토큰 유효성 검사 - 통과했을 때 해킹으로 간주
        if (jwtUtils.validateToken(accessToken)) {
            log.error("accessToken is valid");
            throw new ApiException(ExceptionEnum.ACCESS_NOW_ALLOW_EXCEPTION);
        }
    }
}
