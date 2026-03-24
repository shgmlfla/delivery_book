package com.example.dbook.member.controller;

import com.example.dbook.book.entity.Book;
import com.example.dbook.config.security.CustomUserDetails;
import com.example.dbook.member.dto.MyPageResponseDto;
import com.example.dbook.member.service.MyPageService;
import com.example.dbook.order.dto.SubscriptionDto;
import com.example.dbook.order.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        if(userDetails == null){
            return "redirect:/login";
        }

        Long memberId = userDetails.getId();

        MyPageResponseDto myPageData = myPageService.getMyPageInfo(memberId);
        model.addAttribute("myPageData", myPageData);

        List<Book> subscriptionBooks = myPageService.getMySubscriptionBooks(memberId);
        model.addAttribute("subscriptionBooks", subscriptionBooks);

        //구독권
        SubscriptionDto sub = subscriptionService.getMySubscription(memberId);
        model.addAttribute("sub", sub);

        return "member/mypage";
    }

}
