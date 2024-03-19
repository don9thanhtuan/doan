package com.nhom1.bookstore.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nhom1.bookstore.entity.Book;

public class Optimization {
    final String DEFAULT_PATH = "/img/product/";
    final String DEFAULT_FOLDER = "src/main/resources/static/img/product";

    public void cleanImage(List<Book> bookList) {
        for (int i = 0; i < getAllImageFileName().size(); i++) {
            List<String> fileNames = getAllImageFileName();
            boolean found = false;
            for (Book book : bookList) {
                if (fileNames.get(i).equals(book.getHinhAnh())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                File fileToDelete = getAllFile()[i];
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                    System.out.println("Deleted file: " + fileToDelete.getName());
                }
                i--;
            }
        }
    }

    private List<String> getAllImageFileName() {
        File[] files = getAllFile();
        List<String> imageFiles = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImageFile(file.getName())) {
                    imageFiles.add(DEFAULT_PATH + file.getName());
                }
            }
        }
        return imageFiles;
    }

    private File[] getAllFile() {
        File folder = new File(DEFAULT_FOLDER);
        return folder.listFiles();
    }

    private boolean isImageFile(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
                || fileName.endsWith(".gif")) {
            return true;
        } else {
            return false;
        }
    }
}
