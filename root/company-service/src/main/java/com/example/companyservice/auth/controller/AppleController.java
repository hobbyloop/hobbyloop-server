package com.example.companyservice.auth.controller;//package com.example.companyservice.common.controller;
//
//import com.example.companyservice.common.service.AppleService;
//import com.example.companyservice.common.dto.BaseResponseDto;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1")
//public class AppleController {
//    private final AppleService appleService;
//    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    @GetMapping("/oauth2/login/apple")
//    public void loginRequest(HttpServletRequest request, HttpServletResponse response,
//                             @RequestParam(value = "redirect_uri", required = false) String redirectUri,
//                             @RequestParam(value = "state") String state) throws IOException {
//        redirectStrategy.sendRedirect(request, response, appleService.getAppleLoginUrl(redirectUri, state));
//    }
//
//    @PostMapping("/api/callback/apple")
//    public ResponseEntity<BaseResponseDto<Void>> callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String state = request.getParameter("state");
//        appleService.login(request, response, request.getParameter("code"), state);
//        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>());
//    }
//}