package com.example.dbook.member.controller;

import com.example.dbook.auth.dto.SignupRequestDto;
import com.example.dbook.common.dto.ApiResponse;
import com.example.dbook.config.security.CustomUserDetails;
import com.example.dbook.member.dto.MemberEditDto;
import com.example.dbook.member.entity.Member;
import com.example.dbook.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody @Valid SignupRequestDto dto){
        memberService.signup(dto);
        return ResponseEntity.ok(ApiResponse.ok("회원가입 성공", null));
    }

    @GetMapping("/edit")
    public String editPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){

        Member member = userDetails.getUser();

        MemberEditDto dto = MemberEditDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .build();

        model.addAttribute("memberUpdateDto", dto);

        return "member/memberEdit";
    }

    @PostMapping("/update")
        public String editMemberInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @ModelAttribute MemberEditDto memberEditDto, RedirectAttributes redirectAttributes){

        memberService.editMemberInfo(userDetails.getId(), memberEditDto);

        redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");

        return "redirect:/mypage/mypage";
    }
}
