package com.example.dbook.order.repository;

import com.example.dbook.member.entity.Member;
import com.example.dbook.order.entity.OrderBook;
import com.example.dbook.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {

    List<OrderBook> findAllByOrder(Orders order);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO order_book (order_id, book_id, order_price)
            VALUES (:orderId, :bookId, :orderPrice)
            ON CONFLICT (order_id, book_id) DO NOTHING
            """, nativeQuery = true)
    void safeInsertOrderBook(@Param("orderId") Long orderId, @Param("bookId") Long bookId, @Param("orderPrice") int orderPrice);
}
