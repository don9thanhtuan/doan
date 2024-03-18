package com.nhom4.bookstoremobile.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom4.bookstoremobile.MainActivity;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.adapter.BookAdapter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewBookDetails extends AppCompatActivity {
    private Book book;

    public static void showConfirmationPopup(Context context, String title, String message, DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveClickListener)
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        String id = getIntent().getStringExtra("book_id");
        RecyclerView recyclerView = findViewById(R.id.detail_RecyclerView);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookDetails.this, EditBook.class);
                intent.putExtra("book_id", book.getId());
                intent.putExtra("book_name", book.getTen());
                intent.putExtra("book_HinhAnh", book.getHinhAnh());
                intent.putExtra("book_TacGia", book.getTacGia());
                intent.putExtra("book_NhaCungCap", book.getNhaCungCap());
                intent.putExtra("book_TonKho", book.getTonKho());
                intent.putExtra("book_Gia", book.getGia());
                intent.putExtra("book_TrongLuong", book.getTrongLuong());
                intent.putExtra("book_KickThuoc", book.getKichThuoc());
                intent.putExtra("book_GioiThieu", book.getGioiThieu());
                startActivity(intent);
            }
        });

        findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationPopup();
            }
        });

        Retrofit retrofit = RetrofitAPI.getInstance();
        BookService bookService = retrofit.create(BookService.class);

        Call<Book> call = bookService.getBookDetailsFromRestAPI(id);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    book = response.body();
                    setData(book);
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });

        Call<List<Book>> call2 = bookService.getBookFromRestAPI();

        call2.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> bookList = response.body();
                    for (Book book : bookList) {
                        String imageUrl = book.getHinhAnh();
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            book.setHinhAnh("http://10.0.2.2:8080" + imageUrl);
                        }
                    }

                    BookAdapter adapter = new BookAdapter(ViewBookDetails.this, bookList, recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewBookDetails.this, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
            }
        });
    }

    private void setData(Book book) {
        ImageView bookImage = findViewById(R.id.book_Image);
        TextView nameTextView = findViewById(R.id.name_TxtView);
        TextView soldTextView = findViewById(R.id.sold_TxtView);
        TextView priceTextView = findViewById(R.id.price_TxtView);
        TextView idTextView = findViewById(R.id.id_TxtView);
        TextView authorTextView = findViewById(R.id.author_TxtView);
        TextView publisherTextView = findViewById(R.id.publisher_TxtView);
        TextView weightTextView = findViewById(R.id.weight_TxtView);
        TextView sizeTextView = findViewById(R.id.size_TxtView);
        TextView introductionTextView = findViewById(R.id.introduction_TxtView);

        Glide.with(this)
                .load("http://10.0.2.2:8080" + book.getHinhAnh())
                .into(bookImage);

        nameTextView.setText(book.getTen());
        soldTextView.setText("Đã bán " + book.getDaBan());
        priceTextView.setText(book.getGia());
        idTextView.setText(book.getId());
        authorTextView.setText(book.getTacGia());
        publisherTextView.setText(book.getNhaCungCap());
        weightTextView.setText(book.getTrongLuong() + " gr");
        sizeTextView.setText(book.getKichThuoc());
        introductionTextView.setText(book.getGioiThieu());
    }

    private void showConfirmationPopup() {
        TextView idTextView = findViewById(R.id.id_TxtView);
        String id = idTextView.getText().toString();
        showConfirmationPopup(this, "Xác nhận", "Bạn muốn xóa sản phẩm " + id + "?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BookService bookService = RetrofitAPI.getInstance().create(BookService.class);


                Call<ResponseBody> call = bookService.deleteBook(book.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // Xử lý khi xóa sách thành công
                            Toast.makeText(ViewBookDetails.this, "Xóa thành công sản phẩm " + book.getId(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewBookDetails.this, ViewBookList.class);
                            startActivity(intent);
                        } else {
                            // Xử lý khi xóa sách không thành công
                            Toast.makeText(ViewBookDetails.this, "Failed to delete book", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Xử lý khi có lỗi xảy ra trong quá trình gửi yêu cầu
                        Toast.makeText(ViewBookDetails.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}