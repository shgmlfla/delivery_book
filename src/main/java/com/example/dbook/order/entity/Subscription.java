package com.example.dbook.order.entity;

import com.example.dbook.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String planName;

    private Integer price;

    private LocalDate nextChargeDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    public enum SubscriptionStatus {
        ACTIVE,    // 구독 중
        PAUSED,    // 일시 정지
        CANCELLED, // 해지됨
        EXPIRED    // 만료됨
    }

    @Builder
    public Subscription(Member member, String planName, Integer price, LocalDate nextChargeDate, SubscriptionStatus status) {
        this.member = member;
        this.planName = planName;
        this.price = price;
        this.nextChargeDate = nextChargeDate;
        this.status = status;
    }
}
