package com.example.dbook.book.service;

import com.example.dbook.book.dto.*;
import com.example.dbook.main.service.BookApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookApiService {

    private final BookApiClient bookApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${library.default-code}")
    private String defaultLibCode;

    @CircuitBreaker(name = "bookApiCircuitBreaker", fallbackMethod = "getHotTrendBooksFallback")
    public List<HotTrendBookDto> getHotTrendBooks(String searchDt) throws Exception{

        String json = bookApiClient.getHotTrend(searchDt);
        if (json == null || json.isBlank()){
            log.warn("정보나루 API 응답이 비었습니다. searchDt: {}", searchDt);
            return Collections.emptyList();
        }

        JsonNode root = objectMapper.readTree(json);

        JsonNode resultsNode = root
                .path("response")
                .path("results");

        List<HotTrendBookDto> list = new ArrayList<>();

        if (resultsNode.isArray()) {
            for (JsonNode results : resultsNode) {
                JsonNode docs = results.path("result").path("docs");

                if (docs.isArray()) {
                    for (JsonNode node : docs) {
                        JsonNode docNode = node.path("doc");
                        list.add(objectMapper.treeToValue(docNode, HotTrendBookDto.class));
                    }
                }
            }
        }
        List<HotTrendBookDto> distinctList = list.stream()
                .filter(distinctByKey(HotTrendBookDto::getIsbn13))
                .collect(Collectors.toList());

        return distinctList;
    }

    public List<HotTrendBookDto> getHotTrendBooksFallback(String searchDt, Throwable e){
        log.error("인기 급상승 도서 조회 실패 - searchDt: {}", searchDt, e);
        return Collections.emptyList();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @CircuitBreaker(name = "bookApiCircuitBreaker", fallbackMethod = "getNewBookFallback")
    public List<NewBookDto> getNewBook(String newBookSearchDt) throws Exception{

        String json = bookApiClient.getNewBook(defaultLibCode, newBookSearchDt);
        if (json == null || json.isBlank()){
            log.warn("정보나루 API 응답이 비었습니다. searchDt: {}", newBookSearchDt);
            return Collections.emptyList();
        }
        JsonNode root = objectMapper.readTree(json);

        JsonNode docs = root
                .path("response")
                .path("docs");

        List<NewBookDto> list = new ArrayList<>();
        if (docs.isArray()) {
            for (JsonNode node : docs) {
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, NewBookDto.class));
            }
        }
        return list;
    }

    public List<NewBookDto> getNewBookFallback(String newBookSearchDt, Throwable e){
        log.error("신규 도서 조회 실패 - searchDt: {}", newBookSearchDt, e);
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "bookApiCircuitBreaker", fallbackMethod = "getBestSellerFallback")
    public List<BestSellerBookDto> getBestSeller(String searchDt) throws Exception{

        String json = bookApiClient.getBestSeller(searchDt);

        if (json == null || json.isBlank()){
            log.warn("정보나루 API 응답이 비었습니다. searchDt: {}", searchDt);
            return Collections.emptyList();
        }
        JsonNode root = objectMapper.readTree(json);

        JsonNode docs = root
                .path("response")
                .path("docs");

        List<BestSellerBookDto> list = new ArrayList<>();
        if (docs.isArray()) {
            for (JsonNode node : docs) {
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, BestSellerBookDto.class));
            }
        }
        return list;
    }

    public List<BestSellerBookDto> getBestSellerFallback(String searchDt, Throwable e){
        log.error("베스트셀러 도서 조회 실패 - searchDt: {}", searchDt, e);
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "bookApiCircuitBreaker", fallbackMethod = "getSearchBookFallback")
    public List<SearchBookDto> getSearchBook(String title, String author) throws Exception{

        String json = bookApiClient.getSearchBook(title, author);
        if (json == null || json.isBlank()){
            log.warn("정보나루 API 응답이 비었습니다. title: {}, author : {}", title, author);
            return Collections.emptyList();
        }
        JsonNode root = objectMapper.readTree(json);

        JsonNode docs = root
                .path("response")
                .path("docs");

        List<SearchBookDto> list = new ArrayList<>();

        if(docs.isArray()) {
            for (JsonNode node : docs) {
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, SearchBookDto.class));
            }
        }
        return list;
    }

    public List<SearchBookDto> getSearchBookFallback(String title, String author, Throwable e){
        log.error("도서 조회 실패 - title: {}, author: {}", title, author, e);
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "bookApiCircuitBreaker", fallbackMethod = "searchByIsbnFallback")
    public List<SearchBookDto> searchByIsbn(String isbn) throws Exception{

        String json = bookApiClient.getSearchByIsbn(isbn);
        if(json == null || json.isBlank()){
            log.warn("정보나루 API 응답이 비어있습니다. ISBN: {}", isbn);
            return Collections.emptyList();
        }

        JsonNode root = objectMapper.readTree(json);

        JsonNode docs = root
                .path("response")
                .path("docs");

        List<SearchBookDto> list = new ArrayList<>();

        if(docs.isArray()) {
            for (JsonNode node : docs) {
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, SearchBookDto.class));
            }
        }
        return list;
    }

    public List<SearchBookDto> searchByIsbnFallback(String isbn, Throwable e){
        log.error("isbn으로 도서 조회 실패 - isbn: {}", isbn, e);
        return Collections.emptyList();
    }

    //랜덤
    @CircuitBreaker(name = "bookApiCircuitBreaker", fallbackMethod = "getRecommendedBookFallback")
    public List<RecommendedBookDto> getRecommendedBook(String searchDt,  int age, String gender, int pageSize)
        throws Exception{

        String json = bookApiClient.getRecommendedBook(searchDt, age, gender, pageSize);
        if(json == null || json.isBlank()){
            log.warn("정보나루 API 응답이 비어있습니다. searchDt: {}, age: {}, gender: {}, pageSize: {}", searchDt, age, gender, pageSize);
            return Collections.emptyList();
        }

        JsonNode root = objectMapper.readTree(json);

        JsonNode docs = root
                .path("response")
                .path("docs");

        List<RecommendedBookDto> list = new ArrayList<>();
        if(docs.isArray()) {
            for (JsonNode node : docs) {
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, RecommendedBookDto.class));
            }
        }
        return list;
    }

    public List<RecommendedBookDto> getRecommendedBookFallback(String searchDt,  int age, String gender, int pageSize, Throwable e){
        log.error("랜덤 도서 조회 실패 - searchDt: {}, age: {}, gender: {}, pageSize: {}", searchDt, age, gender, pageSize, e);
        return Collections.emptyList();
    }
}

