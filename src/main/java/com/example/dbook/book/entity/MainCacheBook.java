package com.example.dbook.book.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "main_cache_book")
public class MainCacheBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;
    private String title;
    private String author;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CacheBookType cacheBookType;

    private LocalDateTime cachedAt;

    @Builder
    public MainCacheBook(String isbn, String title, String author, String imageUrl, CacheBookType cacheBookType){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.cacheBookType = cacheBookType;
        this.cachedAt = LocalDateTime.now();
    }
}
