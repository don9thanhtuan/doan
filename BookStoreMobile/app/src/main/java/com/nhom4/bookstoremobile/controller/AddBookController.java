package com.nhom4.bookstoremobile.controller;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.AddBook;
import com.nhom4.bookstoremobile.activity.ViewBookDetails;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.BookResponse;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.service.ExceptionHandler;

import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookController {
    private final AddBook view;
    private final ImageView imagePreview;
    private Uri selectedImage;

    public AddBookController(AddBook view) {
        this.view = view;
        imagePreview = view.findViewById(R.id.imagePreview);
    }

    public Uri getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Uri selectedImage) {
        this.selectedImage = selectedImage;
    }

    public void addBook() {
        if (selectedImage == null) {
            Rect rectangle = new Rect();
            Toast.makeText(view, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            imagePreview.requestFocus();
            imagePreview.getGlobalVisibleRect(rectangle);
            imagePreview.requestRectangleOnScreen(rectangle);
            return;
        }

        MultipartBody.Part imagePart = prepareFilePart(selectedImage);

        Book newBook = new ExceptionHandler().handleExceptionBook(view);

        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<BookResponse> call = bookService.addBook(imagePart, newBook);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful()) {
                    BookResponse bookResponse = response.body();
                    if (bookResponse != null) {
                        Toast.makeText(view, "Thêm sách thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(view, ViewBookDetails.class);
                        intent.putExtra("book_id", bookResponse.getBookID());
                        view.startActivity(intent);
                        view.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Toast.makeText(view, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MultipartBody.Part prepareFilePart(Uri uri) {
        try {
            InputStream inputStream = view.getContentResolver().openInputStream(uri);
            byte[] fileBytes = new byte[inputStream.available()];
            inputStream.read(fileBytes);
            RequestBody requestFile = RequestBody.create(MediaType.parse(view.getContentResolver().getType(uri)), fileBytes);
            return MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageView getImagePreview() {
        return imagePreview;
    }
}
