package com.example.dbook.payment.scheduler;

import com.example.dbook.member.entity.Member;
import com.example.dbook.order.entity.Orders;
import com.example.dbook.payment.dto.TossInitialPaymentResponse;
import com.example.dbook.payment.dto.TossSubscriptionPaymentRequest;
import com.example.dbook.payment.entity.BillingKey;
import com.example.dbook.payment.repository.BillingKeyRepository;
import com.example.dbook.payment.infrastructure.TossPaymentsClient;
import com.example.dbook.subscription.respository.SubscriptionRepository;
import com.example.dbook.payment.service.PaymentTransactionManager;
import com.example.dbook.subscription.entity.Subscription.SubscriptionStatus;
import com.example.dbook.subscription.entity.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BillingScheduler {
    private final SubscriptionRepository subscriptionRepository;
    private final BillingKeyRepository billingKeyRepository;
    private final PaymentTransactionManager paymentTransactionManager;
    private final TossPaymentsClient tossPaymentsClient;

    @Scheduled(cron = "0 0 4 * * *")
    public void runRegularPaymentBatch() {
        log.info("정기 결제 스케줄러 - 자동 정기 결제 프로세스를 시작합니다.");

        List<Subscription> targetSubscriptions = subscriptionRepository.findTargetSubscriptions(LocalDate.now(), SubscriptionStatus.ACTIVE);
        if (targetSubscriptions.isEmpty()) {
            log.info("정기 결제 스케줄러 - 오늘 결제 대상자가 없습니다.");
            return;
        }

        log.info("정기 결제 스케줄링 - 총 {}건의 결제 대상자를 발견했습니다.", targetSubscriptions.size());

        for (Subscription sub : targetSubscriptions) {
            Member member = sub.getMember();
            try {
                log.info("정기 결제 - 회원 ID: {}, 빌링키 확인", member.getId());

                BillingKey billingKeyEntity = billingKeyRepository.findByMemberId(member.getId())
                        .orElseThrow(() -> new IllegalStateException("회원의 빌링키가 없습니다. 회원 ID: " + member.getId()));

                Orders orders = paymentTransactionManager.prepareOrder(member.getEmail(), sub.getPlanName());

                TossSubscriptionPaymentRequest request = TossSubscriptionPaymentRequest.builder()
                        .customerKey("customer_" + member.getId())
                        .amount(sub.getPrice())
                        .orderId(orders.getTossOrderId())
                        .orderName(sub.getPlanName() + " 정기 구독권")
                        .customerEmail(member.getEmail())
                        .build();

                TossInitialPaymentResponse approvalResponse = tossPaymentsClient.executeRegularPayment(billingKeyEntity.getBillingKey(), request);
                log.info("주문 ID: {} -> 토스 API 결제 승인 완료", orders.getTossOrderId());

                paymentTransactionManager.processPaymentSuccess(orders.getId(), member, approvalResponse, sub.getPlanName());

                log.info("구독 갱신 성공 - 회원 ID: {} 구독 차기 결제일 연장 완료", member.getId());

            } catch (Exception e) {
                log.error("정기 결제 실패 - 회원 ID: {} 처리 중 에러 발생: {}", member.getId(), e.getMessage());
            }
        }

        log.info("[정기 결제 배치 스케줄러] 자동 정기 결제 프로세스가 성공적으로 완료되었습니다.");
    }
}