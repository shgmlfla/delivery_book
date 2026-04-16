package com.example.dbook.order.service;

import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import com.example.dbook.order.dto.SubscriptionDto;
import com.example.dbook.order.entity.Orders;
import com.example.dbook.order.entity.PlanType;
import com.example.dbook.order.entity.Subscription;
import com.example.dbook.order.entity.SubscriptionLog;
import com.example.dbook.order.repository.OrderRepository;
import com.example.dbook.order.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SubscriptionDto getMySubscription(Long memberId){
        return subscriptionRepository.findFirstByMemberIdOrderByStartDateDesc(memberId)
                .map(SubscriptionDto::from)
                .orElse(null);
    }

    @Transactional
    public void createOrUpadateSubscription(Long memberId, PlanType planType){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Subscription subscription = subscriptionRepository
                .findFirstByMemberIdOrderByStartDateDesc(member.getId())
                .map(existing -> {
                    existing.setPlanName(planType);
                    existing.setPrice(planType.getPrice());
                    if (planType == PlanType.YEARLY) {
                        existing.setNextChargeDate(LocalDate.now().plusYears(1));
                    } else {
                        existing.setNextChargeDate(LocalDate.now().plusMonths(1));
                    }
                    existing.setStatus(Subscription.SubscriptionStatus.ACTIVE);
                    return existing;
                })
                .orElseGet(() -> {
                    return Subscription.createSubscription(member, planType);
                });

        subscriptionRepository.save(subscription);

    }

    @Transactional
    public void cancelSubscription(Long subscriptionId, Long memberId){
        Subscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구독 정보입니다."));

        if (!sub.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("본인의 구독만 해지 가능합니다.");
        }

        if (sub.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("이미 해지되었거나 활성 상태가 아닙니다.");
        }
        sub.cancel();
    }

}
