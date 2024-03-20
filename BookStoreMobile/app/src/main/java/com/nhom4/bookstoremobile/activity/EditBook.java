package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.Book;
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

public class EditBook extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    boolean changeImage = false;
    ImageView addBookImage;
    Uri selectedImage;
    Book current_Book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        TextView addBook = findViewById(R.id.addBook);
        addBook.setText("Chỉnh sửa sản phẩm");

        String book_ID = getIntent().getStringExtra("book_id");
        String book_Name = getIntent().getStringExtra("book_name");
        String book_HinhAnh = getIntent().getStringExtra("book_HinhAnh");
        String book_TacGia = getIntent().getStringExtra("book_TacGia");
        String book_NhaCungCap = getIntent().getStringExtra("book_NhaCungCap");
        int book_TonKho = getIntent().getIntExtra("book_TonKho", 0);
        String book_GiaR = getIntent().getStringExtra("book_Gia");
        double book_TrongLuong = getIntent().getDoubleExtra("book_TrongLuong", 0.0);
        String book_KickThuoc = getIntent().getStringExtra("book_KickThuoc");
        String book_GioiThieu = getIntent().getStringExtra("book_GioiThieu");

        current_Book = new Book(book_ID, book_Name, book_HinhAnh, book_TacGia, book_NhaCungCap, book_TonKho, book_GiaR, book_TrongLuong, book_KickThuoc, book_GioiThieu);

        addBookImage = findViewById(R.id.addBookImage);

        findViewById(R.id.addBookImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        Button saveBtn = findViewById(R.id.add_book_button);
        saveBtn.setText("Lưu");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBookByAPI();
            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditBook.this, ViewBookDetails.class);
                intent.putExtra("book_id", book_ID);
                startActivity(intent);
                finish();
            }
        });
        setBookData(current_Book);
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
                changeImage = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void editBookByAPI() {
        if(selectedImage == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }
        Book newBook = new ExceptionHandler().handleException(this);
        if(newBook == null) {
            return;
        }
        newBook.setId(current_Book.getId());
        MultipartBody.Part imagePart = prepareFilePart(selectedImage);

        RequestBody newBook_RB = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(newBook));

        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<String> call = bookService.editBook(newBook.getId(), imagePart, newBook_RB);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(EditBook.this, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditBook.this, ViewBookDetails.class);
                        intent.putExtra("book_id", current_Book.getId());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(EditBook.this, "Chỉnh sửa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(EditBook.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditBook.this, ViewBookDetails.class);
                intent.putExtra("book_id", current_Book.getId());
                startActivity(intent);
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

    void setBookData(Book book) {
        EditText nameEditText = findViewById(R.id.add_name);
        EditText priceEditText = findViewById(R.id.add_price);
        EditText authorEditText = findViewById(R.id.add_author);
        EditText publisherEditText = findViewById(R.id.add_publisher);
        EditText weightEditText = findViewById(R.id.add_weight);
        EditText sizeEditText = findViewById(R.id.add_size);
        EditText stockEditText = findViewById(R.id.add_stock);
        EditText introductionEditText = findViewById(R.id.add_introduction);

        Glide.with(this)
                .load("http://10.0.2.2:8080" + book.getHinhAnh())
                .into(addBookImage);

        nameEditText.setText(book.getTen());

        String priceRaw = book.getGia();
        String price = priceRaw.replaceAll("[^0-9]", "");
        priceEditText.setText(price);

        authorEditText.setText(book.getTacGia());
        publisherEditText.setText(book.getNhaCungCap());
        weightEditText.setText(String.valueOf(book.getTrongLuong()));
        sizeEditText.setText(book.getKichThuoc());
        stockEditText.setText(String.valueOf(book.getTonKho()));
        introductionEditText.setText(book.getGioiThieu());
    }
}
