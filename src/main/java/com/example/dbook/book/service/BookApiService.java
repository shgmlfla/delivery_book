package com.example.dbook.book.service;

import com.example.dbook.book.dto.*;
import com.example.dbook.main.service.BookApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookApiService {

    private final BookApiClient bookApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<HotTrendBookDto> getHotTrendBooks(String searchDt) {
        String json = bookApiClient.getHotTrend(searchDt);

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode docs = root
                    .path("response")
                    .path("results")
                    .get(0)
                    .path("result")
                    .path("docs");

            List<HotTrendBookDto> list = new ArrayList<>();
            for (JsonNode node : docs){
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, HotTrendBookDto.class));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("HotTrend JSON 파싱 실패: " + e.getMessage());
        }
    }

    public List<NewBookDto> getNewBook(String libCode) {
        String json = bookApiClient.getNewBook(libCode);

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

