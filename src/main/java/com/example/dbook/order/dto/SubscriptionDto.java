package com.example.dbook.order.dto;

import com.example.dbook.order.entity.Subscription;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@Builder
public class SubscriptionDto {

    private Long id;
    private String planName;
    private Integer price;
    private LocalDate nextChargeDate;
    private String status;

    public static SubscriptionDto from(Subscription entity){
        return SubscriptionDto.builder()
                .id(entity.getId())
                .planName(entity.getPlanName().getDescription())
                .price(entity.getPrice())
                .nextChargeDate(entity.getNextChargeDate())
                .status(entity.getStatus().name())
                .build();
    }
}
