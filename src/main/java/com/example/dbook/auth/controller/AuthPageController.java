package com.example.dbook.auth.controller;

import com.example.dbook.auth.dto.SignUpRequestDto;
import com.example.dbook.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthPageController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(SignUpRequestDto dto, Model model) {
        try {
            memberService.signup(dto);
            return "redirect:/auth/login";
        }catch (RuntimeException e){
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/auth/signup");
            return "common/alert";
        }
    }
}
