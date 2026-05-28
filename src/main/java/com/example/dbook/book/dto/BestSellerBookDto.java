package com.example.dbook.book.dto;

import com.example.dbook.book.entity.CacheBookType;
import com.example.dbook.book.entity.MainCacheBook;
import lombok.Data;

@Data
public class BestSellerBookDto extends  BookBaseDto{

    private Integer ranking;
    private Integer loan_count;

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
