package com.example.companyservice.member.service;

import com.example.companyservice.common.util.JwtUtils;
import com.example.companyservice.member.dto.MemberLoginResponseDto;
import com.example.companyservice.member.dto.request.MemberLoginRequestDto;
import com.example.companyservice.member.entity.Member;
import com.example.companyservice.member.repository.MemberRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MemberLoginServiceImpl implements MemberLoginService {

    private final MemberRepository memberRepository;

    private final JwtUtils jwtUtils;

    @Override
    @Transactional(readOnly = true)
    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto) {
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
            Optional<Member> memberOptional = memberRepository.findByProviderAndSubject(provider, subject);
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
                String accessToken = jwtUtils.createToken(member.getId(), member.getRole());
                String refreshToken = jwtUtils.createRefreshToken(member.getId(), member.getRole());
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
}
