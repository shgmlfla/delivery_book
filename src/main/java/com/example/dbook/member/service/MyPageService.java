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
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {

    private final MemberRepository memberRepository;
    private final OrderBookRepository orderBookRepository;
    private final OrderRepository orderRepository;

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
    public List<Book> getMySubscriptionBooks(Long memberId) {
        return orderRepository
                .findLatestSubscription(memberId, Orders.OrderStatus.PAYMENT_COMPLETED, Orders.OrderType.SUBSCRIPTION)
                .map(order -> orderBookRepository.findAllByOrder(order).stream()
                        .map(OrderBook::getBook)
                        .toList())
                .orElse(Collections.emptyList());
    }

}
