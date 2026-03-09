package com.example.dbook.member.service;

import com.example.dbook.book.entity.Book;
import com.example.dbook.book.repository.BookRepository;
import com.example.dbook.member.dto.MyPageResponseDto;
import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import com.example.dbook.order.entity.OrderBook;
import com.example.dbook.order.entity.Orders;
import com.example.dbook.order.repository.OrderBookRepository;
import com.example.dbook.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final OrderBookRepository orderBookRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    public MyPageResponseDto getMyPageInfo(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        return MyPageResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .isSubscriber(member.getIs_subscriber())
                .build();


    }

    @Transactional
    public List<Book> getMySubscriptionBooks(Long memberId){
        //최신 구독권을 가진 고객
        Optional<Orders> currentSubscription = orderRepository.findLatestSubscription(memberId, "PAYMENT_COMPLETED", Orders.OrderType.SUBSCRIPTION);

        if (currentSubscription.isEmpty()){
            return Collections.emptyList();
        }

        Orders subOrder = currentSubscription.get();
        
        //4권 선정 여부 확인
        List<OrderBook> existingBooks = orderBookRepository.findAllByOrder(subOrder);

        if(!existingBooks.isEmpty()){
            return existingBooks.stream().map(OrderBook::getBook).toList();
        }
        return selectedSubscriptionBooks(subOrder, memberId);
    }

    private List<Book> selectedSubscriptionBooks(Orders subOrder, Long memberId){
        //도서 100권
        List<Book> monthlyBooks = bookRepository.findAllByIsMonthlyTrue();

        // 구매 및 구독한 도서 isbn
        List<String> pastIsbn = orderBookRepository.findIsbnByMemberId(memberId);

        List<Book> newMonthlyBooks = monthlyBooks.stream()
                .filter(book -> !pastIsbn.contains(book.getIsbn()))
                .collect(Collectors.toList());

        // 4권 선별
        Collections.shuffle(newMonthlyBooks);
        List<Book> selectedFourBooks = newMonthlyBooks.stream().limit(4).toList();

        for (Book book:selectedFourBooks){
            OrderBook orderBook = OrderBook.builder()
                    .order(subOrder)
                    .book(book)
                    .build();

            orderBookRepository.save(orderBook);
        }

        return selectedFourBooks;
    }
}
