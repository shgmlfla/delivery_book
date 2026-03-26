package com.example.dbook.main.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class BookApiClient {

    private final RestClient restClient;

    @Value("${book.api.key}")
    private String authkey;

    private static final String BASE_URL = "http://data4library.kr/api/";

    public BookApiClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(BASE_URL).build();
    }

    //요즘 뜨는 책 (대출 급상승)
    public String getHotTrend(String searchDt){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "hotTrend")
                .queryParam("authKey", authkey)
                .queryParam("searchDt", searchDt)
                .queryParam("format", "json")
                .toUriString();

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    //신작 도서
    public String getNewBook(String defaultLibCode){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "newArrivalBook")
                .queryParam("authKey", authkey)
                .queryParam("libCode", defaultLibCode)
                .queryParam("format", "json")
                .toUriString();

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    //베스트 셀러 (BestSellerBook)
    public String getBestSeller(String searchDt){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "loanItemSrch")
                .queryParam("authKey", authkey)
                .queryParam("searchDt", searchDt)
                .queryParam("format", "json")
                .toUriString();

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    // 도서 검색
    public String getSearchBook(String title, String author){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "srchBooks")
                .queryParam("authKey", authkey)
                .queryParam("title", title)
                .queryParam("author", author)
                .queryParam("format", "json")
                .build(false)
                .toUriString();

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    //도서 상세 페이지
    public String getSearchByIsbn(String isbn){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "srchBooks")
                .queryParam("authKey", authkey)
                .queryParam("isbn13", isbn)
                .queryParam("format", "json")
                .toUriString();

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    //랜덤 도서 (정개 배송 도서)
    public String getRecommendedBook(String searchDt, int age,  String gender, int pageSize){

        int maxPage = 5000 / pageSize;  //도서 최대 건 수 5000
        int randomPage = (int) (Math.random() * maxPage) + 1;

        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "loanItemSrch")
                .queryParam("authKey", authkey)
                .queryParam("searchDt", searchDt)
                .queryParam("age", age)
                .queryParam("gender", gender)
                .queryParam("pageNo", randomPage)
                .queryParam("pageSize", pageSize)
                .queryParam("format", "json")
                .toUriString();

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    //오늘의 랜덤 픽

    //신작 도서

    //우리 동네 인기

}
