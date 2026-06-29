package com.example.dbook.book.repository;

import com.example.dbook.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    @Modifying
    @Query("UPDATE Book b SET b.isMonthly = false")
    void updateAllMonthlyBookToFalse();

    @Query(value = """
            SELECT b.*
            FROM book b
            WHERE b.is_monthly = true
              AND NOT EXISTS (
                  SELECT 1
                  FROM order_book ob
                  JOIN orders o ON ob.order_id = o.id
                  JOIN book bk ON ob.book_id = bk.id
                  WHERE o.member_id = :memberId
                    AND bk.isbn = b.isbn
              )
            ORDER BY RANDOM()
            LIMIT 4
            """, nativeQuery = true)
    List<Book> findRandomMonthlyBooksExcludingMember(@Param("memberId") Long memberId);

}
