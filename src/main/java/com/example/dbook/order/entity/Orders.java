package com.example.dbook.order.entity;

import com.example.dbook.member.entity.Member;
import com.example.dbook.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //토스페이먼츠 고유 orderId
    @Column(unique = true)
    private String tossOrderId;

    @Column
    private LocalDateTime orderDate;

    @Column
    private Integer total_price;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public enum OrderType{
        SUBSCRIPTION,
        EXPIRED,
        PURCHASE
    }

    public enum OrderStatus {
        READY,
        PAYMENT_COMPLETED,
        PAYMENT_FAILED,
        CANCELLED
    }

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

}
