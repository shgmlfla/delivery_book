package com.example.dbook.subscription.respository;

import com.example.dbook.book.entity.QBook;
import com.example.dbook.order.entity.QOrderBook;
import com.example.dbook.subscription.dto.MyPageBookDto;
import com.example.dbook.subscription.dto.QMyPageBookDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<MyPageBookDto> findSelectedBooksByOrderId(Long orderId) {
        QOrderBook orderBook = QOrderBook.orderBook;
        QBook book = QBook.book;

        return queryFactory
                .select(new QMyPageBookDto(
                        book.id,
                        book.title,
                        book.author
                ))
                .from(orderBook)
                .join(orderBook.book, book)
                .where(orderBook.order.id.eq(orderId))
                .fetch();
    }
}
