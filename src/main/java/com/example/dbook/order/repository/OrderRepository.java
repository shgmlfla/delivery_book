package com.example.dbook.order.repository;

import com.example.dbook.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

   @Query("SELECT o FROM Orders o " +
            "WHERE o.member.id = :memberId " +
            "AND o.orderStatus = :status " +
            "AND o.orderType = :orderType " +
            "ORDER BY o.orderDate DESC " +
            "LIMIT 1 ")
    Optional<Orders> findLatestSubscription(@Param("memberId") Long memberId,
                                            @Param("status") Orders.OrderStatus status,
                                            @Param("orderType") Orders.OrderType orderType);


   Optional<Orders> findByTossOrderId(String tossOrderId);
}
