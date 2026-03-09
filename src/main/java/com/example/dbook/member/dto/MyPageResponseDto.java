package com.example.dbook.member.dto;

import com.example.dbook.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyPageResponseDto {

    private String email;
    private String nickname;
    private String address;
    private String isSubscriber;
}