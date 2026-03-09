package com.example.dbook.book.service;

import com.example.dbook.book.dto.RecommendedBookDto;
import com.example.dbook.book.entity.Book;
import com.example.dbook.book.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookApiService bookApiService;

    @Transactional
    public void updateMonthlyBooks(String searchDt, int age, String gender) {
        try {
            bookRepository.updateAllMonthlyBookToFalse();

            List<RecommendedBookDto> recommendedBookDtoList =
                    bookApiService.getRecommendedBook(searchDt, age, gender, 100);

            for (RecommendedBookDto dto : recommendedBookDtoList) {
                bookRepository.findByIsbn(dto.getIsbn13())
                        .ifPresentOrElse(
                                existingBook -> {
                                    existingBook.updateInfo(dto.getBookname(), dto.getAuthors(), dto.getBookImageURL());
                                    existingBook.setMonthly(true);
                                },
                                () -> {
                                    Book newBook = Book.builder()
                                            .title(dto.getBookname())
                                            .author(dto.getAuthors())
                                            .imgeUrl(dto.getBookImageURL())
                                            .isbn(dto.getIsbn13())
                                            .price(15000)
                                            .isMonthly(true)
                                            .build();
                                    bookRepository.save(newBook);
                                }
                        );
            }
        } catch (Exception e) {
            System.err.println("도서 업데이트 중 에러 발생: " + e.getMessage());
        }
    }
}