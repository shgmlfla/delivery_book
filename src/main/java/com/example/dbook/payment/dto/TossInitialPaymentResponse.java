package com.example.dbook.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossInitialPaymentResponse {

    private String paymentKey;
    private String orderId;
    private Long totalAmount;
    private String method;
    private String approvedAt;
    private String status;

    private CardInfo card;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardInfo {
        private String issuerCode;
        private String number;
        private String cardType;
    }
}