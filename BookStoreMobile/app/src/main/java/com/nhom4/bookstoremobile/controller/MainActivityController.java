package com.nhom4.bookstoremobile.controller;

import android.app.Activity;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.R;
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

public class MainActivityController {
    private final Activity view;

    public MainActivityController(Activity view) {
        this.view = view;
    }

    public void getTopSellingFromAPI() {
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

                    RecyclerView recyclerView = view.findViewById(R.id.home_RecyclerView);

                    BookAdapter adapter = new BookAdapter(view, bookList, recyclerView);

                    recyclerView.setLayoutManager(new LinearLayoutManager(view, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
            }
        });
    }

    public void redirectToCart() {
        Intent intent = new Intent(view, ViewCart.class);
        intent.putExtra("main", true);
        view.startActivity(intent);
        view.finish();
    }

    public void redirectToBookList() {
        Intent intent = new Intent(view, ViewBookList.class);
        view.startActivity(intent);
        view.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void redirectToAccount() {
        Intent intent = new Intent(view, ViewAccount.class);
        view.startActivity(intent);
        view.finish();
    }

    public void reload(SwipeRefreshLayout pullToRefresh) {
        view.recreate();
        pullToRefresh.setRefreshing(false);
    }
}

