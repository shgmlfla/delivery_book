package com.example.dbook.book.controller;

import com.example.dbook.book.dto.SearchBookDto;
import com.example.dbook.book.service.BookApiService;
import com.example.dbook.book.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/book")
@AllArgsConstructor
public class BooViewController {

    private final BookApiService bookApiService;
    private final BookService bookService;

    @GetMapping("/details")
    public String bookDetails(@RequestParam("isbn") String isbn, Model model) {

        List<SearchBookDto> result = bookApiService.searchByIsbn(isbn);
        SearchBookDto bookDetails = result.isEmpty() ? null : result.get(0);

        model.addAttribute("bookDetails", bookDetails);
        return "book/bookDetails";
    }

}
