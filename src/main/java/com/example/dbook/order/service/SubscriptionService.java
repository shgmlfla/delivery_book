package com.example.dbook.order.service;

import com.example.dbook.member.entity.Member;
import com.example.dbook.order.dto.SubscriptionDto;
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
                .map(subscription -> SubscriptionDto.builder()
                        .planName(subscription.getPlanName())
                        .price(subscription.getPrice())
                        .nextChargeDate(subscription.getNextChargeDate())
                        .status(subscription.getStatus().name())
                        .build())
                .orElse(null);
    }

    public void createOrUpadateSubscription(Member member, String planName, Integer price){
        Subscription subscription = subscriptionRepository.findByMemberId(member.getId())
                .map(existing -> {
                    existing.setNextChargeDate(LocalDate.now().plusMonths(1));
                    existing.setStatus(Subscription.SubscriptionStatus.ACTIVE);
                    return existing;
                })
                .orElseGet(() -> {
                    return Subscription.builder()
                            .member(member)
                            .planName(planName)
                            .price(price)
                            .nextChargeDate(LocalDate.now().plusMonths(1))
                            .status(Subscription.SubscriptionStatus.ACTIVE)
                            .build();
                });

        subscriptionRepository.save(subscription);

    }

}
