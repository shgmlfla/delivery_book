package com.example.dbook.book.dto;

import lombok.Data;

@Data
public class BestSellerBookDto extends  BookBaseDto{

    private Integer ranking;
    private Integer loan_count;
}
