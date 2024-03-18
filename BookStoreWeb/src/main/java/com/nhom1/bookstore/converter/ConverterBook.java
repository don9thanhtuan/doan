package com.nhom1.bookstore.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.bookstore.entity.Book;

public class ConverterBook {
    public static Book jsonToEntity(String bookJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Book book = objectMapper.readValue(bookJson, Book.class);
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
