package com.example.dbook.book.repository;

import com.example.dbook.book.entity.CacheBookType;
import com.example.dbook.book.entity.MainCacheBook;
import org.hibernate.Cache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainCacheBookRepository extends JpaRepository<MainCacheBook, Long> {
    void deleteByCacheBookType(CacheBookType cacheBookType);
    List<MainCacheBook> findAllByCacheBookType(CacheBookType cacheBookType);
}
