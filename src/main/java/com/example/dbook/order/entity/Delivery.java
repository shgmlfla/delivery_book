package com.example.dbook.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public enum DeliveryStatus {
        READY, SHIPPING, COMPLETED
    }

    private int progress;

    public void updateStatus(DeliveryStatus status){
        this.status = status;
        switch (status){
            case READY -> this.progress = 0;
            case SHIPPING -> this.progress = 50;
            case COMPLETED -> this.progress = 100;
        }
    }
}