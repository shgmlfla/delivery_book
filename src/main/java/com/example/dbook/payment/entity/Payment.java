package com.example.dbook.payment.entity;

import com.example.dbook.member.entity.Member;
import com.example.dbook.order.entity.Orders;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tossPaymentKey;
    private String tossOrderId;
    private String orderName;
    private Long amount;
    private LocalDateTime approvedAt;

    private String paymentMethod; //결제 방법(Card, 가상계좌)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
