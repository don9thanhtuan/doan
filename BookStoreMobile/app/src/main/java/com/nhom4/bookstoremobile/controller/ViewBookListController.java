package com.nhom4.bookstoremobile.controller;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.ViewBookList;
import com.nhom4.bookstoremobile.adapter.BookAdapter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBookListController {
    private final ViewBookList view;

    public ViewBookListController(ViewBookList view) {
        this.view = view;
    }

    public void getBookListFromAPI() {
        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<List<Book>> call = bookService.getBookFromRestAPI();

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> bookList = response.body();

                    for (Book book : bookList) {
                        String imageUrl = DefaultURL.getUrl() + book.getHinhAnh();
                        book.setHinhAnh(imageUrl);
                    }

                    RecyclerView recyclerView = view.findViewById(R.id.list_RecyclerView);

                    BookAdapter adapter = new BookAdapter(view, bookList, recyclerView, false);

                    recyclerView.setLayoutManager(new GridLayoutManager(view, 2));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
            }
        });
    }

    public void redirectToMain() {
        view.finish();
        view.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    public void reload(SwipeRefreshLayout pullToRefresh) {
        view.recreate();
        pullToRefresh.setRefreshing(false);
    }
}
