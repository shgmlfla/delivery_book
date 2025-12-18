package com.example.dbook.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookBaseDto {
    private String no;
    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String addition_symbol;
    private String vol;
    private String class_no;
    private String class_nm;
    private String bookImageURL;
    private String bookDtlUrl;
}
