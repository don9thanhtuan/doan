package com.nhom1.bookstore.apis;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nhom1.bookstore.converter.ConverterBook;
import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.services.BookService;

@RestController
public class EditBookController {
    private final BookService bookService;

    public EditBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/api/books/{id}")
    public String editBook(@PathVariable("id") String id,
            @RequestParam("image") MultipartFile file,
            @RequestParam("book") String bookJSON) {
        Book newBook = ConverterBook.jsonToEntity(bookJSON);

        String path = bookService.fileToFilePathConverter(file);
        newBook.setHinhAnh(path);

        bookService.editBook(newBook);
        return "Book edited successfully";
    }
}
