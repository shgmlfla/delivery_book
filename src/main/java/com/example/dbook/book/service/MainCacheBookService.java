package com.example.dbook.book.service;

import com.example.dbook.book.dto.BestSellerBookDto;
import com.example.dbook.book.dto.HotTrendBookDto;
import com.example.dbook.book.dto.NewBookDto;
import com.example.dbook.book.entity.CacheBookType;
import com.example.dbook.book.entity.MainCacheBook;
import com.example.dbook.book.repository.MainCacheBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainCacheBookService {

    private final MainCacheBookRepository mainCacheBookRepository;
    private final BookApiService bookApiService;
    private final MainCacheTransactionalService mainCacheTransactionalService;

    public void refreshMainApiCache() {
        String hotTrendsSearchDt = LocalDateTime.now().minusDays(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String bestSellerSearchDt = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String newBookSearchDt = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));

        try {
            List<HotTrendBookDto> hotTrends = bookApiService.getHotTrendBooks(hotTrendsSearchDt);
            if (hotTrends != null && !hotTrends.isEmpty()) {
                List<MainCacheBook> entities = hotTrends.stream()
                        .map(dto -> dto.toEntity(CacheBookType.HOT_TREND))
                        .toList();

                mainCacheTransactionalService.saveCacheBooks(CacheBookType.HOT_TREND, entities);
                log.info("인기 급상승 데이터 갱신 완료");
            }
        } catch (Exception e) {
            log.error("인기 급상승 캐시 갱신 실패", e);
        }

        try {
            List<BestSellerBookDto> bestSellers = bookApiService.getBestSeller(bestSellerSearchDt);
            if (bestSellers != null && !bestSellers.isEmpty()) {
                List<MainCacheBook> entities = bestSellers.stream()
                        .map(dto -> dto.toEntity(CacheBookType.BEST_SELLER))
                        .toList();

                mainCacheTransactionalService.saveCacheBooks(CacheBookType.BEST_SELLER, entities);
                log.info("베스트 셀러 도서 데이터 갱신 완료");
            }
        } catch (Exception e) {
            log.error("베스트 셀러 캐시 갱신 실패", e);
        }

        try {
        List<NewBookDto> newBooks = bookApiService.getNewBook(newBookSearchDt);
        if (newBooks != null && !newBooks.isEmpty()) {
            List<MainCacheBook> entities = newBooks.stream()
                    .map(dto -> dto.toEntity(CacheBookType.NEW_BOOK))
                    .toList();

            mainCacheTransactionalService.saveCacheBooks(CacheBookType.NEW_BOOK, entities);
            log.info("신규 도서 데이터 갱신 완료");
        }
        } catch (Exception e) {
            log.error("신규 도서 캐시 갱신 실패", e);
        }
    }

}
