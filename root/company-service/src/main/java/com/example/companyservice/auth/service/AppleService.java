package com.example.companyservice.auth.service;//package com.example.companyservice.common.service;
//
//import com.example.companyservice.common.exception.ApiException;
//import com.example.companyservice.common.exception.ExceptionEnum;
//import com.example.companyservice.common.util.JwtUtils;
//import com.example.companyservice.company.entity.Company;
//import com.example.companyservice.company.repository.company.CompanyRepository;
//import com.example.companyservice.instructor.infrastructure.persistence.InstructorRepository;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.minidev.json.JSONObject;
//import net.minidev.json.parser.JSONParser;
//import net.minidev.json.parser.ParseException;
//import org.apache.commons.lang.StringUtils;
//import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
//import org.bouncycastle.openssl.PEMParser;
//import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.*;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.io.StringReader;
//import java.security.PrivateKey;
//import java.util.*;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class AppleService extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final CompanyRepository companyRepository;
//
//    private final InstructorRepository instructorRepository;
//
//    private final JwtUtils jwtUtils;
//
//    private final static String APPLE_AUTH_URL = "https://appleid.apple.com";
//
//    @Value("${apple.team-id}")
//    private String APPLE_TEAM_ID;
//
//    @Value("${apple.login-key}")
//    private String APPLE_LOGIN_KEY;
//
//    @Value("${apple.client-id}")
//    private String APPLE_CLIENT_ID;
//
//    @Value("${apple.redirect-url}")
//    private String APPLE_REDIRECT_URL;
//
//    @Value("${apple.key-path}")
//    private String APPLE_KEY_PATH;
//
//    public String getAppleLoginUrl(String redirectUri, String state) {
//        String loginUrl = APPLE_AUTH_URL + "/auth/authorize"
//                + "?client_id=" + APPLE_CLIENT_ID
//                + "&redirect_uri=" + APPLE_REDIRECT_URL
//                + "&response_type=code%20id_token&scope=name%20email&response_mode=form_post"
//                + "&state=" + state;
//
//        if (redirectUri != null && !redirectUri.isEmpty()) loginUrl = loginUrl + "&redirect=" + redirectUri;
//
//        return loginUrl;
//    }
//
//    public void login(HttpServletRequest request, HttpServletResponse response, String code, String state) {
//        try {
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObj = (JSONObject) jsonParser.parse(generateAuthToken(code));
//
//            // ID TOKEN을 통해 회원 고유 식별자 받기
//            SignedJWT signedJWT = SignedJWT.parse(String.valueOf(jsonObj.get("id_token")));
//            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), JSONObject.class);
//
//            if (StringUtils.isNotEmpty(state)) {
//                String email = String.valueOf(payload.get("email"));
//                String provider = "Apple";
//                String subject = String.valueOf(payload.get("sub"));
//                String oauth2AccessToken = String.valueOf(jsonObj.get("access_token"));
//
//                if ("company".equals(state)) {
//                    Optional<Company> optionalCompany = companyRepository.findByProviderAndSubject(provider, subject);
//                    if (optionalCompany.isPresent()) {
//                        Company company = optionalCompany.get();
//                        String accessToken = jwtUtils.createToken(company.getId());
//                        String refreshToken = jwtUtils.createRefreshToken(company.getId());
//                        sendToken(request, response, accessToken, refreshToken, null, null, null, null);
//                    } else {
//                        sendToken(request, response, null, null, email, provider, subject, oauth2AccessToken);
//                    }
//                } else if ("instructor".equals(state)) {
//
//                } else {
//
//                }
//            } else {
//                throw new ApiException(ExceptionEnum.LOGIN_FAIL_EXCEPTION);
//            }
//        } catch (ParseException | JsonProcessingException e) {
//            throw new RuntimeException("Failed to parse json data");
//        } catch (IOException | java.text.ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void sendToken(HttpServletRequest request,
//                           HttpServletResponse response,
//                           String accessToken,
//                           String refreshToken,
//                           String email,
//                           String provider,
//                           String subject,
//                           String oauth2AccessToken) throws IOException {
//
//        String url = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/" + provider + "/callback")
//                .queryParam("access-token", accessToken)
//                .queryParam("refresh-token", refreshToken)
//                .queryParam("email", email)
//                .queryParam("provider", provider)
//                .queryParam("subject", subject)
//                .queryParam("oauth2AccessToken", oauth2AccessToken)
//                .build()
//                .toUri()
//                .toString();
//
//        getRedirectStrategy().sendRedirect(request, response, url);
//    }
//
//    public String generateAuthToken(String code) throws IOException {
//        if (code == null) throw new IllegalArgumentException("Failed get authorization code");
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", APPLE_CLIENT_ID);
//        params.add("client_secret", createClientSecretKey());
//        params.add("code", code);
//        params.add("redirect_uri", APPLE_REDIRECT_URL);
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(
//                    APPLE_AUTH_URL + "/auth/token",
//                    HttpMethod.POST,
//                    httpEntity,
//                    String.class
//            );
//
//            return response.getBody();
//        } catch (HttpClientErrorException e) {
//            throw new IllegalArgumentException("Apple Auth Token Error");
//        }
//    }
//
//    public String createClientSecretKey() throws IOException {
//        // headerParams 적재
//        Map<String, Object> headerParamsMap = new HashMap<>();
//        headerParamsMap.put("kid", APPLE_LOGIN_KEY);
//        headerParamsMap.put("alg", "ES256");
//
//        // clientSecretKey 생성
//        return Jwts
//                .builder()
//                .setHeaderParams(headerParamsMap)
//                .setIssuer(APPLE_TEAM_ID)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30)) // 만료 시간 (30초)
//                .setAudience(APPLE_AUTH_URL)
//                .setSubject(APPLE_CLIENT_ID)
//                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
//                .compact();
//    }
//
//    private PrivateKey getPrivateKey() throws IOException {
//        ClassPathResource resource = new ClassPathResource(APPLE_KEY_PATH);
//        String privateKey = new String(resource.getInputStream().readAllBytes());
//
//        Reader pemReader = new StringReader(privateKey);
//        PEMParser pemParser = new PEMParser(pemReader);
//        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
//        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
//
//        return converter.getPrivateKey(object);
//    }
//}