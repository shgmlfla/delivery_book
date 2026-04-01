package com.example.dbook.book.service;

import com.example.dbook.book.dto.*;
import com.example.dbook.main.service.BookApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookApiService {

    private final BookApiClient bookApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${library.default-code}")
    private String defaultLibCode;

    public List<HotTrendBookDto> getHotTrendBooks(String searchDt) {
        String json = bookApiClient.getHotTrend(searchDt);

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode resultsNode = root
                    .path("response")
                    .path("results");

            List<HotTrendBookDto> list = new ArrayList<>();
            for (JsonNode results : resultsNode) {
                JsonNode docs = results.path("result").path("docs");

                for (JsonNode node : docs) {
                    JsonNode docNode = node.path("doc");
                    list.add(objectMapper.treeToValue(docNode, HotTrendBookDto.class));
                }
            }

            List<HotTrendBookDto> distinctList = list.stream()
                    .filter(distinctByKey(HotTrendBookDto::getIsbn13))
                    .collect(Collectors.toList());

            return distinctList;

        } catch (Exception e) {
            throw new RuntimeException("HotTrend JSON 파싱 실패: " + e.getMessage());
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    public List<NewBookDto> getNewBook(String newBookSearchDt) {

        String json = bookApiClient.getNewBook(defaultLibCode, newBookSearchDt);

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode docs = root
                    .path("response")
                    .path("docs");

            List<NewBookDto> list = new ArrayList<>();
            for (JsonNode node : docs){
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, NewBookDto.class));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("BestSellerBook JSON 파싱 실패: " + e.getMessage());
        }
    }

    public List<BestSellerBookDto> getBestSeller(String searchDt) {
        String json = bookApiClient.getBestSeller(searchDt);

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode docs = root
                    .path("response")
                    .path("docs");

            List<BestSellerBookDto> list = new ArrayList<>();
            for (JsonNode node : docs){
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, BestSellerBookDto.class));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("BestSellerBook JSON 파싱 실패: " + e.getMessage());
        }
    }

    public List<SearchBookDto> getSearchBook(String title, String author) {
        String json = bookApiClient.getSearchBook(title, author);

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode docs = root
                    .path("response")
                    .path("docs");

            List<SearchBookDto> list = new ArrayList<>();
            for (JsonNode node : docs){
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, SearchBookDto.class));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("BookSearch JSON 파싱 실패: " + e.getMessage());
        }
    }

    public List<SearchBookDto> getSearchByIsbn(String isbn) {
        String json = bookApiClient.getSearchByIsbn(isbn);

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode docs = root
                    .path("response")
                    .path("docs");

            List<SearchBookDto> list = new ArrayList<>();
            for (JsonNode node : docs){
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, SearchBookDto.class));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("BookSearch JSON 파싱 실패: " + e.getMessage());
        }
    }

    public List<SearchBookDto> searchByIsbn(String isbn) {
        String json = bookApiClient.getSearchByIsbn(isbn);

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode docs = root
                    .path("response")
                    .path("docs");

            List<SearchBookDto> list = new ArrayList<>();
            for (JsonNode node : docs){
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, SearchBookDto.class));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("BookSearch JSON 파싱 실패: " + e.getMessage());
        }
    }

    //랜덤
    public List<RecommendedBookDto> getRecommendedBook(String searchDt,  int age, String gender, int pageSize) {
        String json = bookApiClient.getRecommendedBook(searchDt, age, gender, pageSize);

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode docs = root
                    .path("response")
                    .path("docs");

            List<RecommendedBookDto> list = new ArrayList<>();
            for (JsonNode node : docs){
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, RecommendedBookDto.class));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("RecommendedBook JSON 파싱 실패: " + e.getMessage());
        }
    }
}

