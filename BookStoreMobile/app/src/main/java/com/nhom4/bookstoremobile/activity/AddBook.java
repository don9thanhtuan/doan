package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.BookResponse;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.service.ExceptionHandler;

import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBook extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    ImageView imagePreview;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        imagePreview = findViewById(R.id.imagePreview);

        findViewById(R.id.addImageButton).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        findViewById(R.id.add_book_button).setOnClickListener(v -> addBook());

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imagePreview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addBook() {
        if (selectedImage == null) {
            Rect rectangle = new Rect();
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            imagePreview.requestFocus();
            imagePreview.getGlobalVisibleRect(rectangle);
            imagePreview.requestRectangleOnScreen(rectangle);
            return;
        }

        MultipartBody.Part imagePart = prepareFilePart(selectedImage);

        Book newBook = new ExceptionHandler().handleExceptionBook(this);

        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<BookResponse> call = bookService.addBook(imagePart, newBook);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful()) {
                    BookResponse bookResponse = response.body();
                    if (bookResponse != null) {
                        Toast.makeText(AddBook.this, "Thêm sách thành công", Toast.LENGTH_SHORT).show();
                        clearFields();
                        Intent intent = new Intent(AddBook.this, ViewBookDetails.class);
                        intent.putExtra("book_id", bookResponse.getBookID());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(AddBook.this, "Thêm sách thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Toast.makeText(AddBook.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MultipartBody.Part prepareFilePart(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] fileBytes = new byte[inputStream.available()];
            inputStream.read(fileBytes);
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), fileBytes);
            return MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clearFields() {
        EditText nameEditText = findViewById(R.id.add_name);
        EditText priceEditText = findViewById(R.id.add_price);
        EditText authorEditText = findViewById(R.id.add_author);
        EditText publisherEditText = findViewById(R.id.add_publisher);
        EditText weightEditText = findViewById(R.id.add_weight);
        EditText sizeEditText = findViewById(R.id.add_size);
        EditText stockEditText = findViewById(R.id.add_stock);
        EditText introductionEditText = findViewById(R.id.add_introduction);

        nameEditText.setText("");
        priceEditText.setText("");
        authorEditText.setText("");
        publisherEditText.setText("");
        weightEditText.setText("");
        sizeEditText.setText("");
        stockEditText.setText("");
        introductionEditText.setText("");

        imagePreview.setImageResource(R.drawable.imagenotavailable);
    }
}
