package com.example.dbook.order.entity;

import com.example.dbook.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
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

    @Column
    private LocalDateTime orderDate;

    @Column
    private Integer total_price;

    @Column
    private String status; //PAYMENT_COMPLETED, CANCELLED

    public enum OrderType{
        SUBSCRIPTION,
        EXPIRED,
        PURCHASE
    }

    @Enumerated(EnumType.STRING)
    private OrderType orderType;


}
