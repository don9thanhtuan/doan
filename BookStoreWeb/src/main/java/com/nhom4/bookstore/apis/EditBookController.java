package com.nhom4.bookstore.apis;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nhom4.bookstore.entity.Book;
import com.nhom4.bookstore.services.BookService;

@RestController
public class EditBookController {
    private final BookService bookService;

    public EditBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/api/books/{id}")
    public String editBook(@PathVariable("id") String id,
            @RequestParam("image") MultipartFile file,
            @RequestParam("path") String filePath,
            @RequestParam("name") String name,
            @RequestParam("price") String price,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("weight") String weightRaw,
            @RequestParam("size") String size,
            @RequestParam("stock") String stockRaw,
            @RequestParam("introduction") String introduction) {
        Book newBook = new Book();
        newBook.setId(id);
        newBook.setTen(name);
        newBook.setGia(price);
        newBook.setTacGia(author);
        newBook.setNhaCungCap(publisher);

        double weight = 0;
        if (!weightRaw.isBlank()) {
            weight = Double.parseDouble(weightRaw);
        }
        newBook.setTrongLuong(weight);

        newBook.setKichThuoc(size);

        int stock = 0;
        if (stockRaw != null) {
            stock = Integer.parseInt(stockRaw);
        }
        newBook.setTonKho(stock);

        newBook.setGioiThieu(introduction);

        String path = filePath;
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("ID is required");
        }

        path = bookService.fileToFilePathConverter(file);
        newBook.setHinhAnh(path);

        bookService.editBook(newBook);
        return "Book edited successfully";
    }
}
