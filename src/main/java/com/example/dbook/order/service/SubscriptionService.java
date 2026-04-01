package com.example.dbook.order.service;

import com.example.dbook.member.entity.Member;
import com.example.dbook.order.dto.SubscriptionDto;
import com.example.dbook.order.entity.PlanType;
import com.example.dbook.order.entity.Subscription;
import com.example.dbook.order.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public SubscriptionDto getMySubscription(Long memberId){
        return subscriptionRepository.findByMemberId(memberId)
                .map(SubscriptionDto::from)
                .orElse(null);
    }

    @Transactional
    public void createOrUpadateSubscription(Member member, PlanType planType){
        Subscription subscription = subscriptionRepository.findByMemberId(member.getId())
                .map(existing -> {
                    existing.setPlanName(planType);
                    existing.setPrice(planType.getPrice());
                    existing.setNextChargeDate(LocalDate.now().plusMonths(1));
                    existing.setStatus(Subscription.SubscriptionStatus.ACTIVE);
                    return existing;
                })
                .orElseGet(() -> {
                    return Subscription.createSubscription(member, planType);
                });

        subscriptionRepository.save(subscription);

    }

}
