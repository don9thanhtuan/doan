package com.nhom4.bookstoremobile.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.CartItem;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.sqlite.CartTable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOut extends AppCompatActivity {
    private final List<TextView> btnList = new ArrayList<>();
    private List<CartItem> cart = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        boolean[] booleanArray = getIntent().getBooleanArrayExtra("orderList");


        setListener();
    }

    private void setListener() {
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        TextView codBtn = findViewById(R.id.codBtn);
        TextView waitBtn = findViewById(R.id.zaloBtn);
        TextView momoBtn = findViewById(R.id.momoBtn);
        TextView creditBtn = findViewById(R.id.creditBtn);

        btnList.add(codBtn);
        btnList.add(waitBtn);
        btnList.add(momoBtn);
        btnList.add(creditBtn);

        codBtn.setOnClickListener(v -> {
            setEffect(codBtn);
        });
        waitBtn.setOnClickListener(v -> {
            setEffect(waitBtn);
        });
        momoBtn.setOnClickListener(v -> {
            setEffect(momoBtn);
        });
        creditBtn.setOnClickListener(v -> {
            setEffect(creditBtn);
        });
    }

    @Override
    public void onBackPressed() {
//        redirectToAccount();
        super.onBackPressed();
    }

    private void setEffect(TextView clickedBtn) {
        for (TextView button : btnList) {
            if (button == clickedBtn) {
                button.setBackgroundResource(R.drawable.selection_border_rounded);
                continue;
            }
            button.setBackgroundResource(R.drawable.gray_border_rounded);
        }
    }

    private void getCartData(boolean[] booleans) {
        List<CartItem> cart = new ArrayList<>();

        CartTable cartTable = new CartTable(this);
        Cursor cursor = cartTable.getAllCartItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String bookID = cursor.getString(cursor.getColumnIndex("bookID"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                cart.add(new CartItem(bookID, quantity));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (cart.size() != 0) {
            for (CartItem cartItem : cart) {
                getBookDetailFromAPI(cartItem);
            }
        }
    }

    private void getBookDetailFromAPI(CartItem cartItem) {
        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<Book> call = bookService.getBookDetailsFromRestAPI(cartItem.getBookID());
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });
    }
}
