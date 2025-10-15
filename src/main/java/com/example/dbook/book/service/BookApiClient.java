package com.example.dbook.book.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class BookApiClient {

    private final RestClient restClient;

    public BookApiClient(RestClient restClient) {
        this.restClient = restClient;
    }
}
