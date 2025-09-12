package com.example.dbook.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }
}
