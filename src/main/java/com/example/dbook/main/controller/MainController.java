package com.example.dbook.main.controller;

import com.example.dbook.book.dto.BestSellerBookDto;
import com.example.dbook.book.dto.HotTrendBookDto;
import com.example.dbook.book.dto.NewBookDto;
import com.example.dbook.book.service.BookApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private final BookApiService bookApiService;

    public MainController(BookApiService bookApiService) {
        this.bookApiService = bookApiService;
    }

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


}
