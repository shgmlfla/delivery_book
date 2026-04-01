package com.example.dbook.book.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private int price;

    @Column(name = "image_url")
    private String imgeUrl;

    @Column
    private String isbn;

    private boolean isMonthly; //정기 구독 도서

    public void updateInfo(String title, String author, String imgeUrl){
        this.title = title;
        this.author = author;
        this.imgeUrl = imgeUrl;
    }

    public void setMonthly(boolean status){
        this.isMonthly = status;
    }
}
