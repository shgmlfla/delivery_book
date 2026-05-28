package com.example.dbook.book.scheduler;

import com.example.dbook.book.service.MainCacheBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainCacheBookScheduler {

    private final MainCacheBookService mainCacheBookService;

    //@Scheduled(cron = "0 0 4 * * *")
    @Scheduled(cron = "0 53 23 * * *")
    public void refreshMainCache(){
        log.info("메인 화면 도서 캐시 갱신 스케줄러 시작");
        mainCacheBookService.refreshMainApiCache();
        log.info("메인 화면 도서 캐시 갱신 스케줄러 완료");
    }
}
