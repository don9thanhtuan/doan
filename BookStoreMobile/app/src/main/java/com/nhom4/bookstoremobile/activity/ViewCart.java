package com.nhom4.bookstoremobile.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.MainActivity;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.adapter.CartItemAdapter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.CartItem;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.sqlite.CartTable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCart extends AppCompatActivity {
    private final List<CartItem> cart = new ArrayList<>();
    private CartTable cartTable;
    private CartItemAdapter adapter;
    private CheckBox totalCheckBox;
    private boolean isFromMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        isFromMain = getIntent().getBooleanExtra("main", false);

        totalCheckBox = findViewById(R.id.totalCheckBox);
        setListener();

        cartTable = new CartTable(this);

//        cartTable.deleteAllCartItems();
//        cartTable.addToCart("BXFLU747", 10);
//        cartTable.addToCart("BWNPW735", 10);
//        cartTable.addToCart("BPNOW492", 10);
//        cartTable.addToCart("BLYVL195", 10);
//        cartTable.addToCart("BLSVL608", 10);
//        cartTable.addToCart("BHXGU988", 10);
//        cartTable.addToCart("BHKKX073", 10);
//        cartTable.addToCart("BGRMC730", 10);
//        cartTable.addToCart("BGEHN704", 10);
//        cartTable.addToCart("BEVKS715", 10);

        getCartData();

        RecyclerView recyclerView = findViewById(R.id.cartItemList);
        adapter = new CartItemAdapter(this, cart, recyclerView);

        adapter.setTotalCheckBox(totalCheckBox);
        adapter.setPaymentBtn(findViewById(R.id.paymentBtn));
        adapter.setTotalPriceTxtView(findViewById(R.id.totalPrice));

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (isFromMain) {
            redirectToMain();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }

    private void redirectToMain() {
        Intent intent = new Intent(ViewCart.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getCartData() {
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
                    Book book = response.body();
                    if (book != null) {
                        String imageUrl = DefaultURL.getUrl() + book.getHinhAnh();
                        book.setHinhAnh(imageUrl);
                        cartItem.setBook(book);
                    }
                } else {
                    cartTable.removeFromCart(cartItem.getBookID());
                    cart.remove(cartItem);
                    Toast.makeText(ViewCart.this, "Sản phẩm " + cartItem.getBookID() + " không còn được bán", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
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
        showConfirmationPopup(this, "Xác nhận", "Bạn muốn xóa những sản phẩm được chọn?", (dialog, which) -> {
            adapter.deleteCartItem();
            totalCheckBox.setChecked(false);
        });
    }

    private void setListener() {
        findViewById(R.id.homeBtn).setOnClickListener(v -> redirectToMain());
        findViewById(R.id.backButton).setOnClickListener(v -> {
            if (isFromMain) {
                redirectToMain();
            } else {
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });

        totalCheckBox.setOnClickListener(view -> adapter.checkAllCartItem(totalCheckBox.isChecked()));

        findViewById(R.id.deleteBtn).setOnClickListener(v -> showConfirmationPopup());

        findViewById(R.id.accountBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ViewCart.this, ViewAccount.class);
            startActivity(intent);
            finish();
        });

        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            recreate();
            pullToRefresh.setRefreshing(false);
        });

        findViewById(R.id.paymentBtn).setOnClickListener(v -> startActivity(new Intent(ViewCart.this, CheckOut.class)));
    }
}
