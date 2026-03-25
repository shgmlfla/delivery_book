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

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    private final BookApiService bookApiService;

    @GetMapping("/")
    public String main(Model model) {
        String searchDt = "2025-12-05";
        String libCode = "141553";

        List<HotTrendBookDto> hotTrendBookList = bookApiService.getHotTrendBooks(searchDt);
        List<NewBookDto> newBookList = bookApiService.getNewBook(libCode);
        List<BestSellerBookDto> bestSellerBookList = bookApiService.getBestSeller(searchDt);

        model.addAttribute("hotTrendBooks", hotTrendBookList);
        model.addAttribute("newBooks", newBookList);
        model.addAttribute("bestSellerBooks", bestSellerBookList);

        return "main/mainPage";
    }

    @GetMapping("/test")
    public String mainTest(Model model) {
        String searchDt = "2025-12-05";
        String libCode = "141553";

        List<HotTrendBookDto> hotTrendBookList = bookApiService.getHotTrendBooks(searchDt);
        List<NewBookDto> newBookList = bookApiService.getNewBook(libCode);
        List<BestSellerBookDto> bestSellerBookList = bookApiService.getBestSeller(searchDt);

        model.addAttribute("hotTrendBooks", hotTrendBookList);
        model.addAttribute("newBooks", newBookList);
        model.addAttribute("bestSellerBooks", bestSellerBookList);

        return "main/mainPageTest";
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
