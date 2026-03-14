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

    @Query("SELECT ob FROM OrderBook ob WHERE ob.order.member = :member")
    List<OrderBook> findSelectedBooksByMember(@Param("member") Member member);  //정기 구독 도서 4권

    @Query("SELECT DISTINCT ob.book.isbn FROM OrderBook ob WHERE ob.order.member.id = :memberId")
    List<String> findIsbnByMemberId(@Param("memberId") Long memberId);

    List<OrderBook> findAllByOrder(Orders order);
}
