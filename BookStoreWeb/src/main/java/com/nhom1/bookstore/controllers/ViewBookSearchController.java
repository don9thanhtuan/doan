package com.nhom1.bookstore.controllers;

import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.services.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class ViewBookSearchController {

    private final BookService bookService;

    @Autowired
    public ViewBookSearchController(BookService bookService) {
        this.bookService = bookService;
    }

    // Phương thức xử lý tìm kiếm sách theo tiêu đề
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam(name = "title", required = false) String title) {
        List<Book> books;
        
        if (title != null && !title.isEmpty()) {
            books = bookService.findBooksByTitle(title);
            System.out.println("Searching for books with title: " + title);
        } else {
            books = bookService.getBookList();
            System.out.println("Returning all books as no title was provided.");
        }

        return ResponseEntity.ok(books);
    }

}
