package com.example.dbook.order.service;

import com.example.dbook.order.dto.DeliveryResponseDto;
import com.example.dbook.order.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryResponseDto getDeliveryStauts(Long subscriptionId){
        return deliveryRepository.findBySubscriptionId(subscriptionId)
                .map(DeliveryResponseDto::from)
                .orElse(null);
    }
}
