package com.example.dbook.order.dto;

import com.example.dbook.order.entity.Subscription;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SubscriptionDto {
    private String planName;
    private Integer price;
    private LocalDate nextChargeDate;
    private String status;

    public static SubscriptionDto from(Subscription entity){
        return SubscriptionDto.builder()
                .planName(entity.getPlanName())
                .price(entity.getPrice())
                .nextChargeDate(entity.getNextChargeDate())
                .status(entity.getStatus().name())
                .build();
    }
}
