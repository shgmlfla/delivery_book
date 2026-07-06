package com.example.dbook.subscription.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MyPageBookDto {
    private final Long bookId;
    private final String title;
    private final String author;

    @QueryProjection
    public MyPageBookDto(Long bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }
}
