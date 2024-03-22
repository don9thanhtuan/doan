package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom4.bookstoremobile.MainActivity;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.adapter.BookAdapter;
import com.nhom4.bookstoremobile.adapter.CartItemAdapter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.CartItem;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.sqlite.CartDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCart extends AppCompatActivity {
    private CartDB cartDB;
    private List<CartItem> cart;
    private CartItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        findViewById(R.id.homeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCart.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.cartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        cartDB = new CartDB(this);
//        cartDB.addToCart("BGEHN704", 10);
//        cartDB.addToCart("BGRMC730", 10);
//        cartDB.addToCart("BHKKX073", 10);
//        cartDB.addToCart("BHXGU988", 10);
//        cartDB.removeFromCart("BGEHN704");
//        cartDB.removeFromCart("BGRMC730");
//        cartDB.removeFromCart("BHKKX073");
//        cartDB.removeFromCart("BHXGU988");

        cart = getCartData();
        RecyclerView recyclerView = findViewById(R.id.cartItemList);
        getBookListFromAPI();

        adapter = new CartItemAdapter(this, cart, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private List<CartItem> getCartData() {
        List<CartItem> cart = new ArrayList<>();
        Cursor cursor = cartDB.getAllCartItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String bookID = cursor.getString(cursor.getColumnIndex("bookID"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                cart.add(new CartItem(bookID, quantity));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if(cart.size() != 0) {
            return cart;
        }
        return null;
    }

    private void getBookListFromAPI() {
        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<List<Book>> call = bookService.getBookFromRestAPI();

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> bookList = response.body();
                    for (CartItem cartItem : cart) {
                        for (Book book : bookList) {
                            if(cartItem.getBookID().equals(book.getId())) {
                                String imageUrl = book.getHinhAnh();
                                book.setHinhAnh("http://10.0.2.2:8080" + imageUrl);
                                cartItem.setBook(book);
                                break;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    adapter.getItemCount();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
            }
        });
    }

}
