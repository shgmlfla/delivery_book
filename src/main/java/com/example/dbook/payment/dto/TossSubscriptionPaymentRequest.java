package com.example.dbook.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossSubscriptionPaymentRequest {
    private String customerKey;
    private int amount;
    private String orderId;
    private String orderName;
    private String customerEmail;
    private String customerName;
}
