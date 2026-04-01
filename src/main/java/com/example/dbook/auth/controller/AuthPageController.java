package com.example.dbook.auth.controller;

import com.example.dbook.auth.dto.SignupRequestDto;
import com.example.dbook.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthPageController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(SignupRequestDto dto) {
        authService.signup(dto);
        return "redirect:/auth/login";
    }
}
