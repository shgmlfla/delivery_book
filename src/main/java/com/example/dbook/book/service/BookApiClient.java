package com.example.dbook.main.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

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
    public String getNewBook(String libCode){
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "newArrivalBook")
                .queryParam("authKey", authkey)
                .queryParam("libCode", libCode)
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

    //이달의 키워드

    //베스트 샐로 TOP10

    //오늘의 랜덤 픽

    //신작 도서

    //우리 동네 인기

}
