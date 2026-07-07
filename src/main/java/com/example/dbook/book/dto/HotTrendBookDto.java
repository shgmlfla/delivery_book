package com.example.dbook.book.dto;


import com.example.dbook.book.entity.CacheBookType;
import com.example.dbook.book.entity.MainCacheBook;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HotTrendBookDto extends  BookBaseDto {

    private String difference;
    private String baseWeekRank;
    private String pastWeekRank;

    public MainCacheBook toEntity(CacheBookType type){
        return MainCacheBook.builder()
                .isbn(this.getIsbn13())
                .title(this.getBookname())
                .author(this.getAuthors())
                .imageUrl(this.getBookImageURL())
                .cacheBookType(type)
                .build();
    }
}
