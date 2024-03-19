package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ViewBookList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookList.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookList.this, AddBook.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.list_RecyclerView);

        Retrofit retrofit = RetrofitAPI.getInstance();
        BookService bookService = retrofit.create(BookService.class);
        Call<List<Book>> call = bookService.getBookFromRestAPI();

        call.enqueue(new Callback<List<Book>>() {
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

                    BookAdapter adapter = new BookAdapter(ViewBookList.this, bookList, recyclerView);
                    recyclerView.setLayoutManager(new GridLayoutManager(ViewBookList.this, 2));
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
            }
        });
    }
}
