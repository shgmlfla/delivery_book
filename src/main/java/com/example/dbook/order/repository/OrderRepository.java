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
            "AND o.status = :status " +
            "AND o.orderType = :orderType " +
            "ORDER BY o.orderDate DESC ")
    Optional<Orders> findLatestSubscription(@Param("memberId") Long memberId,
                                            @Param("status") String status,
                                            @Param("orderType") Orders.OrderType orderType);


   Optional<Orders> findByTossOrderId(String tossOrderId);
}
