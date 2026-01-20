package com.example.dbook.auth.controller;

import com.example.dbook.auth.dto.LoginRequestDto;
import com.example.dbook.auth.dto.SignupRequestDto;
import com.example.dbook.auth.dto.SignupResponseDto;
import com.example.dbook.auth.dto.TokenDto;
import com.example.dbook.common.dto.ApiResponse;
import com.example.dbook.config.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.dbook.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginRequestDto request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        String token = jwtTokenProvider.createToken(authentication);
        return new TokenDto(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto request) {
        System.out.println("@@컨트롤러 호출=");
        SignupResponseDto responseDto = memberService.signup(request);
        System.out.println("@@request="+request.getEmail());
        return ResponseEntity.ok(ApiResponse.ok("회원가입 성공", responseDto));
    }


}
