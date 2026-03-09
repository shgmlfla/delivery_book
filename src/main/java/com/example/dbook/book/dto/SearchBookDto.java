package com.example.dbook.book.dto;

import lombok.Data;

@Data
public class SearchBookDto {

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
    private String loan_count;

}
