package com.example.dbook.order.repository;

import com.example.dbook.member.entity.Member;
import com.example.dbook.order.entity.OrderBook;
import com.example.dbook.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {

    List<OrderBook> findAllByOrder(Orders order);
}
