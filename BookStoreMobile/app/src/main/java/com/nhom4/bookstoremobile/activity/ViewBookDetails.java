package com.nhom4.bookstoremobile.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewBookDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_page);
        String id = String.valueOf(getIntent().getStringExtra("book_id"));
        RecyclerView recyclerView = findViewById(R.id.detail_RecyclerView);

        Retrofit retrofit = RetrofitAPI.getInstance();
        BookService bookService = retrofit.create(BookService.class);

        Call<Book> call = bookService.getBookDetailsFromRestAPI(id);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    Book book = response.body();
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

}