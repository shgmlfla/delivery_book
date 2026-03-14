package com.example.dbook.order.service;

import com.example.dbook.order.dto.SubscriptionDto;
import com.example.dbook.order.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionDto getMySubscription(Long memberId){
        return subscriptionRepository.findByMemberId(memberId)
                .map(SubscriptionDto::from)
                .orElse(null);
    }


}
