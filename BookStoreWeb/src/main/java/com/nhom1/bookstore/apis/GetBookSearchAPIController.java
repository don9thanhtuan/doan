package com.nhom1.bookstore.apis;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.services.BookService;

@RestController
@RequestMapping("/api/books/search/1")
public class GetBookSearchAPIController {
    private final BookService bookService;

    public GetBookSearchAPIController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(required = false) String title) {
        List<Book> bookList;
        if (title != null && !title.isEmpty()) {
            bookList = bookService.findBooksByTitle(title); // Giả sử có phương thức tìm sách theo tiêu đề
        } else {
            bookList = bookService.getBookList();
        }
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }
}
