package com.nhom1.bookstore.services;

import java.util.Base64;

public class Encoder {
    public static String encodeString(String originalString) {
        byte[] encodedBytes = Base64.getEncoder().encode(originalString.getBytes());
        return new String(encodedBytes);
    }

    public static String decodeString(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString.getBytes());
        return new String(decodedBytes);
    }
}
