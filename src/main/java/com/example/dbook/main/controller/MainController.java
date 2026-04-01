package com.example.dbook.main.controller;

import com.example.dbook.book.dto.BestSellerBookDto;
import com.example.dbook.book.dto.SearchBookDto;
import com.example.dbook.book.dto.HotTrendBookDto;
import com.example.dbook.book.dto.NewBookDto;
import com.example.dbook.book.service.BookApiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    private final BookApiService bookApiService;

    @GetMapping("/")
    public String main(Model model) {
        String hotTrendsSearchDt = LocalDateTime.now().minusDays(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String startDt = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String newBookSearchDt = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));

        List<HotTrendBookDto> hotTrendBookList = bookApiService.getHotTrendBooks(hotTrendsSearchDt);
        List<NewBookDto> newBookList = bookApiService.getNewBook(newBookSearchDt);
        List<BestSellerBookDto> bestSellerBookList = bookApiService.getBestSeller(startDt);

        model.addAttribute("hotTrendBooks", hotTrendBookList);
        model.addAttribute("newBooks", newBookList);
        model.addAttribute("bestSellerBooks", bestSellerBookList);

        return "main/mainPage";
    }

    @GetMapping("/searchResults")
    public String searchResults(@RequestParam(value="title", required = false) String title,
                         @RequestParam(value="author", required = false) String author,
                         Model model) {

        List<SearchBookDto> searchBookResults = bookApiService.getSearchBook(title, author);

        model.addAttribute("searchBookResults", searchBookResults);
        model.addAttribute("title", title);
        model.addAttribute("author", author);

        return "main/searchResults";
    }



}
