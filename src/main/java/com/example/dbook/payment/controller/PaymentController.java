package com.example.dbook.payment.controller;

import com.example.dbook.common.dto.ApiResponse;
import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import com.example.dbook.payment.service.PaymentService;
import com.example.dbook.subscription.dto.SubscriptionRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    private final MemberRepository memberRepository;
    private final PaymentService paymentService;

    @Value("${toss.payment.client-key}")
    private String CLIENT_KEY;

    @GetMapping("/billing")
    public String getBillingPage(@AuthenticationPrincipal UserDetails userDetails, Model model){
        Member member = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        model.addAttribute("clientKey", CLIENT_KEY);
        model.addAttribute("customerKey", "customer_" + member.getId());
        model.addAttribute("member", member);

        return "payment/billing";
    }

    @PostMapping("/issue-billing-key")
    @ResponseBody
    public ResponseEntity<ApiResponse<String>> issueBillingKey(@RequestBody SubscriptionRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        try {
            String email = userDetails.getUsername();

            paymentService.completeSubscriptionProcess(
                    request.getAuthKey(),
                    request.getCustomerKey(),
                    email,
                    request.getPlanType()
            );

            return ResponseEntity.ok(ApiResponse.ok("정기 구독 및 결제 완료", null));


        } catch (IllegalArgumentException e) {
            log.error("구독 처리 오류 발생: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            log.error("구독 처리 중 서버 시스템 오류 발생", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "결제 중 오류 발생"));
        }

    }

    @GetMapping("/success")
    public String successPage() {
        return "payment/success";
    }

    @GetMapping("/fail")
    public String failPage() {
        return "payment/fail";
    }


}
