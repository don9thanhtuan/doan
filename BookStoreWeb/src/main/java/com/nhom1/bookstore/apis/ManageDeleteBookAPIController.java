package com.nhom1.bookstore.apis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.nhom1.bookstore.services.BookService;

@RestController
public class ManageDeleteBookAPIController {
    private final BookService bookService;

    public ManageDeleteBookAPIController(BookService bookService) {
        this.bookService = bookService;
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") String id) {
        if (id == null || id.isEmpty()) {
            return new ResponseEntity<>("ID is required", HttpStatus.BAD_REQUEST);
        }
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
    }
}
