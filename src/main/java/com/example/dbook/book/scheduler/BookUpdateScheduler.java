package com.example.dbook.book.scheduler;

import com.example.dbook.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookUpdateScheduler implements CommandLineRunner {

    private final BookService bookService;

    @Override
    public void run(String... args) throws Exception {
        updateMonthlyBooks();
    }

    @Scheduled(cron = "0 0 4 1 * ?")
    public void schedulerUpdateBooks() {
        log.info("도서 업데이트 스케줄러 실행");
        updateMonthlyBooks();
    }

    public void updateMonthlyBooks(){
        try{
            String searchDt = LocalDate.now().toString();
            int ageCode = -1; //나이 무관
            String genderCode = "2"; // 성별 무관

            bookService.updateMonthlyBooks(searchDt, ageCode, genderCode);

            log.info("스케줄러 작동 완료");
        }catch(Exception e){
            log.error("도서 업데이트 스케줄러 에러 발생", e);
        }
    }
}
