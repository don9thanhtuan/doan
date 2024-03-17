package com.nhom1.bookstore.apis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.services.BookService;
import com.nhom1.bookstore.services.IDGenerator;

@RestController
@RequestMapping("/api/books")
public class AddBookController {
    private final BookService bookService;

    public AddBookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @PostMapping
    public ResponseEntity<BookResponse> addBook(
            @RequestParam("image") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("price") String price,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("weight") String weightRaw,
            @RequestParam("size") String size,
            @RequestParam("stock") String stockRaw,
            @RequestParam("introduction") String introduction) {
        // if (bookService.bookExists(name, author, publisher)) {
        // // Nếu cuốn sách đã tồn tại, trả về thông báo lỗi
        // return new ResponseEntity<>(new BookResponse(null, "Book already exists"),
        // HttpStatus.CONFLICT);
        // }
        Book newBook = new Book();
        newBook.setId(IDGenerator.IDBook());
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

        int stock = Integer.parseInt(stockRaw);
        newBook.setTonKho(stock);

        newBook.setGioiThieu(introduction);

        String filePath = bookService.fileToFilePathConverter(file);
        newBook.setHinhAnh(filePath);

        bookService.addBook(newBook);

        BookResponse response = new BookResponse(newBook, "Book added successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
