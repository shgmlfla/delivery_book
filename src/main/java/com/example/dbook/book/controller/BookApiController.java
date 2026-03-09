package com.example.dbook.book.controller;

import com.example.dbook.book.dto.HotTrendBookDto;
import com.example.dbook.book.service.BookApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookApiController {

    private final BookApiService bookApiService;

    @GetMapping("/hot_trend")
    public List<HotTrendBookDto> getHotTrendBooks(@RequestParam("searchDt") String searchDt){
        return bookApiService.getHotTrendBooks(searchDt);
    }

}
