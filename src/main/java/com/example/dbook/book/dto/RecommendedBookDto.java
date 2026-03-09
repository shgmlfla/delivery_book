package com.example.dbook.book.dto;

import lombok.Data;

@Data
public class RecommendedBookDto extends  BookBaseDto{
    private Integer ranking;
    private Integer loan_count;
}
