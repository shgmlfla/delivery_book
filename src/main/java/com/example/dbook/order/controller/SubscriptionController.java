package com.example.dbook.order.controller;

import com.example.dbook.config.security.CustomUserDetails;
import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import com.example.dbook.order.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/plans")
    public String showPlans(){
        return "order/subscriptionPlan";
    }

    @PostMapping("/cancel/{id}")
    public String cancelSubscription(@PathVariable("id") Long subscriptionId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                                     RedirectAttributes rttr){
        try{
            subscriptionService.cancelSubscription(subscriptionId, userDetails.getId());
            rttr.addFlashAttribute("message", "구독이 정상적으로 해지되었습니다.");
            return "redirect:/mypage/mypage";
        } catch (Exception e) {
            log.error("구독 해지 실패 에러: ", e);
            rttr.addFlashAttribute("message", "시스템 오류로 해지에 실패했습니다.");
        }
        return "redirect:/member/mypage";

    }

}
