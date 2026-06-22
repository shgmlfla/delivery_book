package com.example.dbook.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingKeyResponse {
    private String billingKey;
    private String customerKey;
    private String authenticatedAt;
    private String method; // CARD 카드
    private CardInfo card;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardInfo {
        private String issuerCode;
        private String number;
        private String cardType;
    }

}
