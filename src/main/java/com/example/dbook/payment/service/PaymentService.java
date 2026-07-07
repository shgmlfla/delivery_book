package com.example.dbook.payment.service;

import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import com.example.dbook.order.entity.Orders;
import com.example.dbook.order.entity.PlanType;
import com.example.dbook.order.repository.OrderRepository;
import com.example.dbook.payment.dto.TossInitialPaymentResponse;
import com.example.dbook.payment.dto.TossSubscriptionPaymentRequest;
import com.example.dbook.payment.entity.BillingKey;
import com.example.dbook.payment.repository.BillingKeyRepository;
import com.example.dbook.subscription.service.SubscriptionBookService;
import com.example.dbook.subscription.service.SubscriptionService;
import com.example.dbook.payment.dto.BillingKeyResponse;
import com.example.dbook.payment.entity.Payment;
import com.example.dbook.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentProcessManager paymentProcessManager;
    private final BillingKeyRepository billingKeyRepository;
    private final PaymentTransactionManager paymentTransactionManager;

    public void completeSubscriptionProcess(String authKey, String customerKey, String email, PlanType planType){

        Orders orders = paymentTransactionManager.prepareOrder(email, planType);
        Member member = orders.getMember();

        String billingKey = billingKeyRepository.findByMemberId(member.getId())
                .map(BillingKey::getBillingKey)
                .orElseGet(() -> {
                    log.info("새로운 빌링키 발급 - 회원 ID: {}", member.getId());
                    return paymentProcessManager.requestAndSaveBillingKey(authKey, customerKey, email).getBillingKey();
                });

        TossInitialPaymentResponse approvalResponse = paymentProcessManager.executeInitialSubscriptionPayment(
                billingKey, orders.getTossOrderId(), planType, member
        );

        try {
            paymentTransactionManager.processPaymentSuccess(orders.getId(), member, approvalResponse, planType);
        } catch (Exception e) {
            log.error("결제는 성공했으나 DB처리 중 오류 발생", e);
            throw new RuntimeException("결제 완료 후 시스템 처리 중 에러 발생", e);
        }

        log.info("구독 결제 시스템 완료 회원 ID: {}, 가입 플랜: {}", member.getId(), planType);
    }
}
