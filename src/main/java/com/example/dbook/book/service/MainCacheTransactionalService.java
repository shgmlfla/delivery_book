package com.example.dbook.book.service;

import com.example.dbook.book.entity.CacheBookType;
import com.example.dbook.book.entity.MainCacheBook;
import com.example.dbook.book.repository.MainCacheBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MainCacheTransactionalService {

    private final MainCacheBookRepository mainCacheBookRepository;

    @Transactional
    public void saveCacheBooks(CacheBookType type, List<MainCacheBook> entities) {
        mainCacheBookRepository.deleteByCacheBookType(type);
        mainCacheBookRepository.saveAll(entities);
    }

}
