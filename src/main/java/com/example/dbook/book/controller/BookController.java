package com.example.dbook.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    @GetMapping("/bookDetails")
    public String bookDetails() {
        return "book/bookDetails";  // templates/book/bookDetails.html
    }
}
