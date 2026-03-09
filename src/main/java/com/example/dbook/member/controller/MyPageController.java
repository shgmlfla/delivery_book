package com.example.dbook.member.controller;

import com.example.dbook.config.security.CustomUserDetails;
import com.example.dbook.member.dto.MyPageResponseDto;
import com.example.dbook.member.service.MyPageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    private MyPageService myPageService;

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long memberId = userDetails.getId();

        MyPageResponseDto myPageData = myPageService.getMyPageInfo(memberId);

        return "mypage/mypage";
    }
}
