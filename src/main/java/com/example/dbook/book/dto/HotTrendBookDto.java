package com.example.dbook.book.dto;


import lombok.Data;

@Data
public class HotTrendBookDto extends  BookBaseDto {

    private String difference;
    private String baseWeekRank;
    private String pastWeekRank;
}
