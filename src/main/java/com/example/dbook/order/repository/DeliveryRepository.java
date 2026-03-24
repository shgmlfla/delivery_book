package com.example.dbook.order.repository;

import com.example.dbook.order.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findBySubscriptionId(Long subscriptionId);
}
