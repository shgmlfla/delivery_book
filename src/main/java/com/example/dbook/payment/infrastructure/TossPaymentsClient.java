package com.example.dbook.payment.infrastructure;

import com.example.dbook.member.entity.Member;
import com.example.dbook.order.entity.PlanType;
import com.example.dbook.payment.dto.BillingKeyRequest;
import com.example.dbook.payment.dto.BillingKeyResponse;
import com.example.dbook.payment.dto.TossInitialPaymentResponse;
import com.example.dbook.payment.dto.TossSubscriptionPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
@RequiredArgsConstructor
public class TossPaymentsClient {

    private final WebClient tossWebClient;

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    private String getEncodedAuth() {
        String auth = secretKey + ":";
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    //billing 키 발급
    public BillingKeyResponse requestBillingKeyFromToss(BillingKeyRequest billingKeyRequest){
        return tossWebClient.post()
                .uri("/v1/billing/authorizations/issue")
                .header("Authorization", getEncodedAuth())
                .bodyValue(billingKeyRequest)
                .retrieve()
                .bodyToMono(BillingKeyResponse.class)
                .block();
    }

    // 토스 서버에서 API 호출
    private String createHeader(String billingKey, TossSubscriptionPaymentRequest request){
        return tossWebClient.post()
                .uri("/v1/billing/" + billingKey)
                .header("Authorization", getEncodedAuth())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    //첫 달 결게
    public TossInitialPaymentResponse executeInitialSubscriptionPayment(String billingKey, String tossOrderId, PlanType planType, Member member) {

        TossSubscriptionPaymentRequest requestBody = TossSubscriptionPaymentRequest.builder()
                .customerKey("customer_" + member.getId())
                .orderId(tossOrderId)
                .amount(planType.getPrice())
                .orderName(planType.name() + " 정기 구독 첫 달 결제")
                .customerEmail(member.getEmail())
                .customerName(member.getUsername())
                .build();

        return tossWebClient.post()
                .uri("/v1/billing/" + billingKey)
                .header("Authorization", getEncodedAuth())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TossInitialPaymentResponse.class)
                .block();
    }

    //정기 결제
    public TossInitialPaymentResponse executeRegularPayment(String billingKey, TossSubscriptionPaymentRequest request){
        return tossWebClient.post()
                .uri("/v1/billing/" + billingKey)
                .header("Authorization", getEncodedAuth())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TossInitialPaymentResponse.class)
                .block();
    }

}
