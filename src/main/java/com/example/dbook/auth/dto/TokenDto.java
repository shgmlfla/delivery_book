package com.example.dbook.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {

    private String grantType;     // Bearer
    private String accessToken;   // JWT 문자열
    private long expiresIn;       // 만료 시간 (ms)
}
