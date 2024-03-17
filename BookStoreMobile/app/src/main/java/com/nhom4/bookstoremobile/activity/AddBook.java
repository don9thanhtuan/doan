package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.MainActivity;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;

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
    ImageView addBookImage;
    Uri selectedImage;
    BookService bookService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookService = RetrofitAPI.getInstance().create(BookService.class);

        addBookImage = findViewById(R.id.addBookImage);

        findViewById(R.id.addBookImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        findViewById(R.id.add_book_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBook.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                addBookImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addBook() {
        EditText nameEditText = findViewById(R.id.add_name);
        EditText priceEditText = findViewById(R.id.add_price);
        EditText authorEditText = findViewById(R.id.add_author);
        EditText publisherEditText = findViewById(R.id.add_publisher);
        EditText weightEditText = findViewById(R.id.add_weight);
        EditText sizeEditText = findViewById(R.id.add_size);
        EditText stockEditText = findViewById(R.id.add_stock);
        EditText introductionEditText = findViewById(R.id.add_introduction);

        String name = nameEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String publisher = publisherEditText.getText().toString();
        String weight = weightEditText.getText().toString();
        String size = sizeEditText.getText().toString();
        String stock = stockEditText.getText().toString();
        String introduction = introductionEditText.getText().toString();

        if (name.isEmpty() || price.isEmpty() || author.isEmpty() || publisher.isEmpty() || weight.isEmpty() ||
                size.isEmpty() || stock.isEmpty() || introduction.isEmpty() || selectedImage == null) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin và chọn hình ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        MultipartBody.Part imagePart = prepareFilePart(selectedImage);

        Call<Book> call = bookService.addBook(imagePart,
                RequestBody.create(MediaType.parse("text/plain"), name),
                RequestBody.create(MediaType.parse("text/plain"), price),
                RequestBody.create(MediaType.parse("text/plain"), author),
                RequestBody.create(MediaType.parse("text/plain"), publisher),
                RequestBody.create(MediaType.parse("text/plain"), weight),
                RequestBody.create(MediaType.parse("text/plain"), size),
                RequestBody.create(MediaType.parse("text/plain"), stock),
                RequestBody.create(MediaType.parse("text/plain"), introduction));

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    Book book = response.body();
                    if (book != null) {
                        Toast.makeText(AddBook.this, "Thêm sách thành công", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                } else {
                    Toast.makeText(AddBook.this, "Thêm sách thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
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

        addBookImage.setImageResource(R.drawable.imagenotavailable);
    }
}
