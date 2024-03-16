package com.nhom4.bookstore.repositories;

import java.util.List;

import com.nhom4.bookstore.entity.Book;

public interface BookDAO {
    List<Book> getBookList();
    Book getBook(String id);
    void addBook(Book newBook);
    void editBook(Book newBook);
    void deleteBook(String id);
    List<Book> search(String tuKhoa);
    void updateSoldQuantity(String id, int daBan);
}
