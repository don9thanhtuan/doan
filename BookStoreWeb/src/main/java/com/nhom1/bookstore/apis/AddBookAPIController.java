package com.nhom1.bookstore.apis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nhom1.bookstore.converter.ConverterJSON;
import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.services.BookService;
import com.nhom1.bookstore.services.IDGenerator;

@RestController
@RequestMapping("/api/books")
public class AddBookAPIController {
    private final BookService bookService;

    public AddBookAPIController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @PostMapping
    public ResponseEntity<String> addBook(
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam(value = "book", required = false) String bookJSON) {

        // Kiểm tra nếu tham số 'book' bị thiếu
        if (bookJSON == null || bookJSON.isEmpty()) {
            return new ResponseEntity<>("Missing book data", HttpStatus.BAD_REQUEST);
        }

        // Chuyển đổi JSON thành đối tượng Book
        Book newBook = ConverterJSON.jsonToBookEntity(bookJSON);

        // Tạo ID duy nhất cho sách
        String id = generateUniqueBookID();
        newBook.setBookID(id);

        // Xử lý file ảnh nếu có
        if (file != null && !file.isEmpty()) {
            String filePath = bookService.fileToFilePathConverter(file);
            newBook.setBookImage(filePath);
            // Log để kiểm tra xem ảnh có được lưu không
            System.out.println("File uploaded to: " + filePath);
        } else {
            newBook.setBookImage("/img/product/default.jpg");  // Ảnh mặc định nếu không có ảnh
        }

        // Thêm sách vào cơ sở dữ liệu
        bookService.addBook(newBook);

        // Trả về ID của sách đã thêm
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }



    // Hàm tạo ID sách duy nhất
    private String generateUniqueBookID() {
        String id;
        do {
            id = IDGenerator.IDBook();
        } while (!bookService.search(id).isEmpty());
        return id;
    }
}
