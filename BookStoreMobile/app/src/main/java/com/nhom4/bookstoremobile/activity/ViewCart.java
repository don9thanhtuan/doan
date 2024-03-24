package com.nhom4.bookstoremobile.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom4.bookstoremobile.MainActivity;
import com.nhom4.bookstoremobile.R;
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
    private final List<CartItem> cart = new ArrayList<>();
    private CartDB cartDB;
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
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CheckBox totalCheckBox = findViewById(R.id.totalCheckBox);
        totalCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.checkAllCartItem(isChecked);
            }
        });

        findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationPopup();
            }
        });

        cartDB = new CartDB(this);

//        cartDB.deleteAllCartItems();
//        cartDB.addToCart("BXFLU747", 10);
//        cartDB.addToCart("BWNPW735", 10);
//        cartDB.addToCart("BPNOW492", 10);
//        cartDB.addToCart("BLYVL195", 10);
//        cartDB.addToCart("BLSVL608", 10);
//        cartDB.addToCart("BHXGU988", 10);
//        cartDB.addToCart("BHKKX073", 10);
//        cartDB.addToCart("BGRMC730", 10);
//        cartDB.addToCart("BGEHN704", 10);
//        cartDB.addToCart("BEVKS715", 10);

        getCartData();

        RecyclerView recyclerView = findViewById(R.id.cartItemList);
        adapter = new CartItemAdapter(this, cart, recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        adapter.setPaymentBtn(findViewById(R.id.paymentBtn));
        adapter.setTotalPriceTxtView(findViewById(R.id.totalPrice));
    }

    private void getCartData() {
        Cursor cursor = cartDB.getAllCartItems();
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
                    Book book = response.body();
                    String imageUrl = book.getHinhAnh();
                    book.setHinhAnh("http://10.0.2.2:8080" + imageUrl);
                    cartItem.setBook(book);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });
    }

    private void showConfirmationPopup(Context context, String title, String message, DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveClickListener)
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showConfirmationPopup() {
        showConfirmationPopup(this, "Xác nhận", "Bạn muốn xóa những sản phẩm được chọn?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.deleteCartItem();
            }
        });
    }
}
