package com.example.dbook.auth.controller;
import com.example.dbook.auth.service.AuthService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.dbook.auth.dto.LoginRequestDto;
import com.example.dbook.auth.dto.TokenDto;
import com.example.dbook.config.security.JwtTokenProvider;
import com.example.dbook.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Tag(name = "Auth", description = "인증 및 인가 관리(로그인/회원가입)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final AuthService authService;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Operation(summary = "로그인", description = "JWT 적용한 로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto request, HttpServletResponse response) {
        try {
            String accessToken = authService.authenticateAndGenerateToken(request);

            boolean isSecure = "prod".equals(activeProfile);

            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .path("/")
                    .httpOnly(true)
                    .maxAge(3600)
                    .sameSite("Lax")
                    .secure(false)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok().body(Map.of("message", "로그인이 완료되었습니다."));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 틀렸습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러: " + e.getMessage());
        }
    }

}
