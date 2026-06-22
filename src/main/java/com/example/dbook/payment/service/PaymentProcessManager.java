package com.example.dbook.payment.service;

import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import com.example.dbook.order.entity.PlanType;
import com.example.dbook.payment.dto.BillingKeyRequest;
import com.example.dbook.payment.dto.BillingKeyResponse;
import com.example.dbook.payment.dto.TossInitialPaymentResponse;
import com.example.dbook.payment.entity.BillingKey;
import com.example.dbook.payment.infrastructure.TossPaymentsClient;
import com.example.dbook.payment.repository.BillingKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PaymentProcessManager {

    private final TossPaymentsClient tossPaymentsClient;
    private final BillingKeyRepository billingKeyRepository;
    private final MemberRepository memberRepository;

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    // 토스서버에서 발급 받은 키 DB에 저장
    public BillingKeyResponse requestAndSaveBillingKey(String authkey, String customerKey, String email){

        String encodedSecretKey = encodedSecretKeyToBase64(secretKey);

        BillingKeyRequest request = BillingKeyRequest.builder()
                .authKey(authkey)
                .customerKey(customerKey)
                .build();

        BillingKeyResponse response = tossPaymentsClient.requestBillingKeyFromToss(request, encodedSecretKey);

        //DB에 저장
        saveBillingKey(response, email);

        return response;

    }

    private String encodedSecretKeyToBase64(String key){
        String encodedSecretKey = key + ":";
        return Base64.getEncoder().encodeToString(encodedSecretKey.getBytes());
    }

    private void saveBillingKey(BillingKeyResponse response, String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        BillingKey billingKeyEntity = BillingKey.builder()
                .billingKey(response.getBillingKey())
                .customerKey(response.getCustomerKey())
                .member(member)
                .build();

        billingKeyRepository.save(billingKeyEntity);

        member.updateSubscriptionStatus("Y");
    }

    public TossInitialPaymentResponse executeInitialSubscriptionPayment(String billingKey, String tossOrderId, PlanType planType, Member member) {

        String encodedSecretKey = encodedSecretKeyToBase64(secretKey);

        return tossPaymentsClient.executeInitialSubscriptionPayment(
                billingKey,
                tossOrderId,
                planType,
                member,
                encodedSecretKey
        );
    }
}
