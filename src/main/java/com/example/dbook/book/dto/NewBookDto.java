package com.example.dbook.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data
public class NewBookDto {

    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String set_isbn13;
    private String bookImageURL;
    private String addition_symbol;
    private String vol;
    private String class_no;
    private String class_nm;
    private String reg_date;

}

