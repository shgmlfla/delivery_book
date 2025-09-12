package com.example.dbook.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Member;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {
    private String email;
    private String password;
/*
    public Member toEntity(String password){
        return Member.builder()
                .email(email)
                .password(password)
                .role(Role.USER)
                .type(Type.FORM)
                .build();
    }
*/
}
