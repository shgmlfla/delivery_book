package com.example.dbook.bookApiTest;

import com.example.dbook.book.service.BookApiClient;
import com.example.dbook.book.service.BookApiService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CircuitBreakerTest {

    @Autowired
    private BookApiService bookApiService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockitoBean
    private BookApiClient bookApiClient;

    @Test
    @DisplayName("API 호출 시 연속 실패할 경우 서킷 OPEN 상태, 시간 도서 API는 정상 호출")
    void bookApiCircuitBreakerTest() throws Exception {

        // 서킷 초기화
        CircuitBreaker hotTrendCircuit = circuitBreakerRegistry.circuitBreaker("hotTrendCircuitBreaker");
        CircuitBreaker newBookCircuit = circuitBreakerRegistry.circuitBreaker("newBookCircuitBreaker");
        hotTrendCircuit.reset();
        newBookCircuit.reset();

        given(bookApiClient.getHotTrend(anyString()))
                .willThrow(new RuntimeException("인기 도서 엔드포인트 오류 발생"));

        given(bookApiClient.getNewBook(anyString(), anyString()))
                .willThrow(new RuntimeException("신간 도서 엔드포인트 오류 발생"));

        String testDt = "2026-06-05";

        for (int i = 0; i < 5; i++) {
            try {
                bookApiService.getHotTrendBooks(testDt);
            } catch (Exception e) {

            }
        }

        assertThat(hotTrendCircuit.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        assertThat(newBookCircuit.getState()).isEqualTo(CircuitBreaker.State.CLOSED);

        //6번째 호출 시 api 확인
        bookApiService.getHotTrendBooks(testDt);
        bookApiService.getNewBook(testDt);

        Mockito.verify(bookApiClient, Mockito.times(1)).getNewBook(anyString(), anyString());
        Mockito.verify(bookApiClient, Mockito.atMost(5)).getHotTrend(anyString());
    }
}
