package com.example.dbook.order.dto;

import com.example.dbook.order.entity.Delivery;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryResponseDto {
    private String statusMessage;
    private int progress;

    public static DeliveryResponseDto from(Delivery delivery) {
        if (delivery == null) return null;

        return DeliveryResponseDto.builder()
                .statusMessage(delivery.getStatus().name())
                .progress(delivery.getProgress())
                .build();
    }
}