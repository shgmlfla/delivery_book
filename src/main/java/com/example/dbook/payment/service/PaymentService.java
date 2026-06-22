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
@Transactional(readOnly=true)
@Slf4j
public class PaymentService {

    private final PaymentProcessManager paymentProcessManager;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;
    private final BillingKeyRepository billingKeyRepository;

    @Transactional
    public void completeSubscriptionProcess(String authKey, String customerKey, String email, PlanType planType){

        // 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        //ORDERS 테이블 READY 상태
        String uniqueSuffix = java.util.UUID.randomUUID().toString().substring(0, 8);
        String tossOrderId = "SUB_" + member.getId() + "_" + System.currentTimeMillis() + "_" + uniqueSuffix;

        Orders orders = Orders.builder()
                .member(member)
                .tossOrderId(tossOrderId)
                .orderDate(LocalDateTime.now())
                .total_price(planType.getPrice())
                .orderStatus(Orders.OrderStatus.READY)
                .orderType(Orders.OrderType.SUBSCRIPTION)
                .build();
        orderRepository.save(orders);

        //빌링키 조회
        Optional<BillingKey> existingKey = billingKeyRepository.findByMemberId(member.getId());
        String billingKey;

        if (existingKey.isPresent()) {
            log.info("기존 빌링키 회원 ID: {}", member.getId());
            billingKey = existingKey.get().getBillingKey();
        } else {
            log.info("새로운 빌링키 발급 - 회원 ID: {}", member.getId());
            BillingKeyResponse billingKeyResponse = paymentProcessManager.requestAndSaveBillingKey(authKey, customerKey, email);
            billingKey = billingKeyResponse.getBillingKey();
        }

        //첫 결제
        TossInitialPaymentResponse approvalResponse = paymentProcessManager.executeInitialSubscriptionPayment(billingKey, tossOrderId, planType, member);
        
        //결제 내역(payment) 저장
        Payment payment = Payment.builder()
                .tossPaymentKey(null)
                .tossOrderId(tossOrderId)
                .amount(Long.valueOf(planType.getPrice()))
                .paymentMethod("CARD")
                .approvedAt(LocalDateTime.now())
                .order(orders)
                .member(member)
                .build();
        paymentRepository.save(payment);

        // 주문상태 READY -> 완료
        orders.setOrderStatus(Orders.OrderStatus.PAYMENT_COMPLETED);

        subscriptionService.createOrUpadateSubscription(member.getId(), planType);

        log.info("구독 결제 시스템 완료 회원 ID: {}, 가입 플랜: {}", member.getId(), planType);
    }
}
