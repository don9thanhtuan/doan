package com.nhom1.bookstore.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.repositories.BookDAOController;

@Service
public class BookServiceImpl implements BookService{
        private final BookDAOController bookDAOController;

    public BookServiceImpl(BookDAOController bookDAOController) {
        this.bookDAOController = bookDAOController;
    }

    @Override
    public List<Book> getBookList() {
        return bookDAOController.getBookList();
    }

    @Override
    public Book getBook(String id) {
        return bookDAOController.getBook(id);
    }

    @Override
    public void editBook(Book newBook) {
        Book currentBook = bookDAOController.getBook(newBook.getBookID());
        if(!newBook.getBookImage().equals(currentBook.getBookImage())) {
            deleteImage(currentBook.getBookImage());
        } 
        bookDAOController.editBook(newBook);
    }

    @Override
    public List<Book> search(String tuKhoa) {
        return bookDAOController.search(tuKhoa);
    }

    @Override
    public void deleteBook(String id) {
        Book currentBook = bookDAOController.getBook(id);
        deleteImage(currentBook.getBookImage());
        
        bookDAOController.deleteBook(id);
    }

    @Override
    public void addBook(Book newBook) {
        bookDAOController.addBook(newBook);
    }

    @Override
    public String fileToFilePathConverter(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = "D:/bookstore/images/product/";

            // Tạo thư mục nếu chưa tồn tại
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File destinationFile = new File(uploadDir + fileName);
            file.transferTo(destinationFile);

            System.out.println("File saved at: " + destinationFile.getAbsolutePath());

            // Trả về đúng đường dẫn ảnh cho API
            return "/img/product/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "/img/product/default.jpg";
        }
    }







    @Override
    public List<Book> getTopSelling() {
        List<Book> booklist = bookDAOController.getBookList();
        Collections.sort(booklist, Comparator.comparingInt(Book::getBookSold).reversed());
        
        return booklist.subList(0, Math.min(5, booklist.size()));
    }

    @Override
    public void updateQuantity(String id, int daBan) {
        bookDAOController.updateQuantity(id, daBan);
    }

    private void deleteImage(String imagePath) {
        String baseFolderPath = "src/main/resources/static";

        // Tạo đối tượng Path từ đường dẫn tương đối
        Path fullPath = Paths.get(baseFolderPath, imagePath);

        // Lấy đường dẫn tuyệt đối
        String absolutePath = fullPath.toAbsolutePath().toString();

        File fileToDelete = new File(absolutePath);
        System.out.println(fileToDelete.getName());
        fileToDelete.delete();
    }
    @Override
    public List<Book> findBooksByTitle(String title) {
        return bookDAOController.findBooksByTitle(title);
    }

    
}
