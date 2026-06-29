package com.example.dbook.subscription.service;

import com.example.dbook.book.entity.Book;
import com.example.dbook.book.repository.BookRepository;
import com.example.dbook.order.entity.OrderBook;
import com.example.dbook.order.entity.Orders;
import com.example.dbook.order.repository.OrderBookRepository;
import com.example.dbook.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionBookService {

    private static final int SUBSCRIPTION_BOOK_COUNT = 4;
    private final BookRepository bookRepository;
    private final OrderBookRepository orderBookRepository;


    @Transactional
    public List<Book> selectedSubsriptionBooks(Orders order, Long memberId){
        List<OrderBook> existing = orderBookRepository.findAllByOrder(order);
        if (!existing.isEmpty()) {
            return existing.stream().map(OrderBook::getBook).toList();
        }

        List<Book> selected = bookRepository.findRandomMonthlyBooksExcludingMember(memberId);
        if (selected.size() < SUBSCRIPTION_BOOK_COUNT) {
            throw new IllegalStateException(
                    "선정 가능한 월간 도서가 부족합니다. (필요: " + SUBSCRIPTION_BOOK_COUNT + ", 가능: " + selected.size() + ")"
            );
        }

        List<OrderBook> orderBooks = selected.stream()
                .map(book -> OrderBook.builder()
                        .order(order)
                        .book(book)
                        .build())
                .toList();
        orderBookRepository.saveAll(orderBooks);
        return selected;

    }


}
