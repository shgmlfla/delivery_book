package com.example.dbook.subscription.dto;

import com.example.dbook.order.entity.PlanType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionRequest {

    private String authKey;
    private String customerKey;
    private PlanType planType;
}
