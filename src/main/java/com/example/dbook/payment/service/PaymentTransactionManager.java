package com.example.dbook.payment.service;

import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import com.example.dbook.order.entity.Orders;
import com.example.dbook.order.entity.PlanType;
import com.example.dbook.order.repository.OrderRepository;
import com.example.dbook.payment.dto.TossInitialPaymentResponse;
import com.example.dbook.payment.entity.Payment;
import com.example.dbook.payment.repository.PaymentRepository;
import com.example.dbook.subscription.service.SubscriptionBookService;
import com.example.dbook.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentTransactionManager {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;
    private final SubscriptionBookService subscriptionBookService;

    // 결제 상태 READY
    @Transactional
    public Orders prepareOrder(String email, PlanType planType) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

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

        return orderRepository.save(orders);
    }

    // 결제 완료
    @Transactional
    public void processPaymentSuccess(Long orderId, Member member, TossInitialPaymentResponse approvalResponse, PlanType planType) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("주문 정보가 존재하지 않습니다."));

        Payment payment = Payment.builder()
                .tossPaymentKey(approvalResponse.getPaymentKey())
                .tossOrderId(orders.getTossOrderId())
                .amount(Long.valueOf(planType.getPrice()))
                .paymentMethod("CARD")
                .approvedAt(LocalDateTime.now())
                .order(orders)
                .member(member)
                .build();
        paymentRepository.save(payment);

        orders.setOrderStatus(Orders.OrderStatus.PAYMENT_COMPLETED);

        subscriptionBookService.selectedSubsriptionBooks(orders, member.getId());
        subscriptionService.createOrUpadateSubscription(member.getId(), planType);
    }
}