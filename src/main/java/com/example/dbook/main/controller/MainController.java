package com.example.dbook.main.controller;

import com.example.dbook.book.dto.BestSellerBookDto;
import com.example.dbook.book.dto.SearchBookDto;
import com.example.dbook.book.dto.HotTrendBookDto;
import com.example.dbook.book.dto.NewBookDto;
import com.example.dbook.book.entity.CacheBookType;
import com.example.dbook.book.entity.MainCacheBook;
import com.example.dbook.book.repository.MainCacheBookRepository;
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

    private final MainCacheBookRepository mainCacheBookRepository;
    private final BookApiService bookApiService;

    @GetMapping("/")
    public String main(Model model) throws Exception{
        List<MainCacheBook> hotTrendBookList = mainCacheBookRepository.findAllByCacheBookType(CacheBookType.HOT_TREND);
        List<MainCacheBook> bestSellerBookList = mainCacheBookRepository.findAllByCacheBookType(CacheBookType.BEST_SELLER);
        List<MainCacheBook> newBookList = mainCacheBookRepository.findAllByCacheBookType(CacheBookType.NEW_BOOK);

        model.addAttribute("hotTrendBooks", hotTrendBookList);
        model.addAttribute("bestSellerBooks", bestSellerBookList);
        model.addAttribute("newBooks", newBookList);

        return "main/mainPage";
    }

    @GetMapping("/searchResults")
    public String searchResults(@RequestParam(value="title", required = false) String title,
                         @RequestParam(value="author", required = false) String author,
                         Model model) throws Exception{

        List<SearchBookDto> searchBookResults = bookApiService.getSearchBook(title, author);

        model.addAttribute("searchBookResults", searchBookResults);
        model.addAttribute("title", title);
        model.addAttribute("author", author);

        return "main/searchResults";
    }



}
