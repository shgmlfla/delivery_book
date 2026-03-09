package com.example.dbook.member.entity;

import com.example.dbook.book.entity.Book;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "member_book")
public class MemberBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberBookStatus status;

    public enum MemberBookStatus{
        READING,     //대여중 (정기 배송)
        COMPLETED,  //완독
        PURCHASED;  //구매한 도서
    }


}
