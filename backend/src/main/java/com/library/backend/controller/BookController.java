package com.library.backend.controller;

import com.library.backend.entity.Book;
import com.library.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping
    public List<Book> getAll() {
        return bookService.getAllBooks();
    }

    @GetMapping("/available")
    public List<Book> getAvailable() {
        return bookService.getAvailableBooks();
    }

    @GetMapping("/search")
    public List<Book> search(@RequestParam String keyword) {
        return bookService.searchBooks(keyword);
    }
}