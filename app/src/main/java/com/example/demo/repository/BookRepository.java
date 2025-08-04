package com.example.demo.repository;

import com.example.demo.model.Book;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends CrudRepository<Book, Long> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Book> findWithLockingById(Long bookId);

    @Transactional
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.reviews WHERE b.title LIKE %:name%")
    List<Book> findBooksByTitleContaining(@Param("name") String name);
}
