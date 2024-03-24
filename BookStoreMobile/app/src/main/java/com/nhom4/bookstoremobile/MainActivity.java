package com.nhom4.bookstoremobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom4.bookstoremobile.activity.ViewAccount;
import com.nhom4.bookstoremobile.activity.ViewBookList;
import com.nhom4.bookstoremobile.activity.ViewCart;
import com.nhom4.bookstoremobile.adapter.BookAdapter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListener();

        RecyclerView recyclerView = findViewById(R.id.home_RecyclerView);
        getTopSellingFromAPI(recyclerView);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Nhấn thêm một lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void redirectToCart() {
        Intent intent = new Intent(MainActivity.this, ViewCart.class);
        startActivity(intent);
    }

    private void getTopSellingFromAPI(RecyclerView recyclerView) {
        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<List<Book>> call = bookService.getBookTopSellingFromRestAPI();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> bookList = response.body();
                    for (Book book : bookList) {
                        String imageUrl = DefaultURL.getUrl() + book.getHinhAnh();
                        book.setHinhAnh(imageUrl);
                    }

                    BookAdapter adapter = new BookAdapter(MainActivity.this, bookList, recyclerView);

                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
            }
        });
    }

    private void setListener() {
        findViewById(R.id.viewListButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewBookList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
        findViewById(R.id.homeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        findViewById(R.id.top_cartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToCart();
            }
        });
        findViewById(R.id.bottom_cartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToCart();
            }
        });
        findViewById(R.id.accountBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewAccount.class);
                startActivity(intent);
            }
        });
    }
}
