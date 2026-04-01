package com.example.dbook.order.entity;

import com.example.dbook.member.entity.Member;
import com.example.dbook.order.repository.SubscriptionRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private PlanType planName;

    private int price;

    private LocalDate nextChargeDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    public enum SubscriptionStatus {
        ACTIVE,    // 구독 중
        PAUSED,    // 일시 정지
        CANCELLED, // 해지됨
        EXPIRED    // 만료됨
    }

    public static Subscription createSubscription(Member member, PlanType planType) {
        return Subscription.builder()
                .member(member)
                .planName(planType)
                .price(planType.getPrice())
                .status(SubscriptionStatus.ACTIVE)
                .nextChargeDate(LocalDate.now().plusMonths(1))
                .build();
    }

}
