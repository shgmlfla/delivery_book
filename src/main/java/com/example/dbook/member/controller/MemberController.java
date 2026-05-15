package com.example.dbook.member.controller;

import com.example.dbook.auth.dto.SignUpRequestDto;
import com.example.dbook.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(SignUpRequestDto dto) {
        memberService.signup(dto);
        return "redirect:/auth/login";
    }

}
