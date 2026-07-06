package com.example.dbook.member.controller;

import com.example.dbook.book.entity.Book;
import com.example.dbook.config.security.CustomUserDetails;
import com.example.dbook.member.dto.MyPageResponseDto;
import com.example.dbook.member.service.MyPageService;
import com.example.dbook.order.entity.Orders;
import com.example.dbook.order.repository.OrderRepository;
import com.example.dbook.subscription.dto.MyPageBookDto;
import com.example.dbook.subscription.dto.SubscriptionDto;
import com.example.dbook.subscription.respository.SubscriptionQueryRepository;
import com.example.dbook.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final SubscriptionService subscriptionService;
    private final OrderRepository orderRepository;
    private final SubscriptionQueryRepository subscriptionQueryRepository;

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        if(userDetails == null){
            model.addAttribute("msg", "로그인이 필요합니다.");
            model.addAttribute("url", "/auth/login");
            return "common/alert";
        }

        Long memberId = userDetails.getId();

        MyPageResponseDto myPageData = myPageService.getMyPageInfo(memberId);
        model.addAttribute("myPageData", myPageData);

        Orders latestOrder = orderRepository.findLatestSubscription(
                memberId,
                Orders.OrderStatus.PAYMENT_COMPLETED,
                Orders.OrderType.SUBSCRIPTION
        ).orElse(null);

        List<MyPageBookDto> subscriptionBooks = List.of();

        if (latestOrder != null) {
            subscriptionBooks = subscriptionQueryRepository.findSelectedBooksByOrderId(latestOrder.getId());
        }

        model.addAttribute("subscriptionBooks", subscriptionBooks);

        //구독권
        SubscriptionDto sub = subscriptionService.getMySubscription(memberId);
        model.addAttribute("sub", sub);

        return "member/mypage";
    }

}
