package com.library.backend.controller;

import com.library.backend.dto.ApiResponse;
import com.library.backend.entity.Book;
import com.library.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResponse<Book>> addBook(@RequestBody Book book) {
        Book savedBook = bookService.addBook(book);
        ApiResponse<Book> response = new ApiResponse<>("SUCCESS", "Book added successfully", savedBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Book>>> getAll() {
        List<Book> books = bookService.getAllBooks();
        ApiResponse<List<Book>> response = new ApiResponse<>("SUCCESS", "Books retrieved", books);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Book>>> getAvailable() {
        List<Book> books = bookService.getAvailableBooks();
        ApiResponse<List<Book>> response = new ApiResponse<>("SUCCESS", "Available books retrieved", books);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Book>>> search(@RequestParam String keyword) {
        List<Book> books = bookService.searchBooks(keyword);
        ApiResponse<List<Book>> response = new ApiResponse<>("SUCCESS", "Search results", books);
        return ResponseEntity.ok(response);
    }
}