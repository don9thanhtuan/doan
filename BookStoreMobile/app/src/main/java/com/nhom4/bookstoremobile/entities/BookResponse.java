package com.nhom4.bookstoremobile.entities;

public class BookResponse {
    private String bookID;
    private String message;

    public BookResponse() {
    }

    public BookResponse(String bookID, String message) {
        this.bookID = bookID;
        this.message = message;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

