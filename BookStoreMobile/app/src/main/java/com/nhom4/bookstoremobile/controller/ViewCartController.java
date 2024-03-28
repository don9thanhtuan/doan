package com.nhom4.bookstoremobile.controller;

import android.content.Intent;
import android.database.Cursor;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.MainActivity;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.CheckOut;
import com.nhom4.bookstoremobile.activity.ViewAccount;
import com.nhom4.bookstoremobile.activity.ViewCart;
import com.nhom4.bookstoremobile.adapter.CartItemAdapter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.CartItem;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.service.ConfirmPopup;
import com.nhom4.bookstoremobile.sqlite.CartTable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCartController {
    private final ViewCart view;
    private final CartTable cartTable;
    private final CheckBox totalCheckBox;
    private List<CartItem> cart;
    private CartItemAdapter adapter;

    public ViewCartController(ViewCart view) {
        this.view = view;
        cartTable = new CartTable(view);
        totalCheckBox = view.findViewById(R.id.totalCheckBox);
        cart = new ArrayList<>();
    }

    public void getCartData() {
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

            RecyclerView recyclerView = view.findViewById(R.id.cartItemList);

            adapter = new CartItemAdapter(view, cart, recyclerView);

            adapter.setTotalCheckBox(totalCheckBox);
            adapter.setPaymentBtn(view.findViewById(R.id.paymentBtn));
            adapter.setTotalPriceTxtView(view.findViewById(R.id.totalPrice));

            recyclerView.setLayoutManager(new LinearLayoutManager(view, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
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
                    Toast.makeText(view, "Sản phẩm " + cartItem.getBookID() + " không còn được bán", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });
    }

    public void showConfirmationPopup() {
        ConfirmPopup.show(view, "Xác nhận", "Bạn muốn xóa những sản phẩm được chọn?", (dialog, which) -> {
            adapter.deleteCartItem();
            totalCheckBox.setChecked(false);
        });
    }

    public void redirectToMain() {
        Intent intent = new Intent(view, MainActivity.class);
        view.startActivity(intent);
        view.finish();
    }

    public void checkAll() {
        adapter.checkAllCartItem(totalCheckBox.isChecked());
    }

    public void redirectBack(boolean isFromMain) {
        if (isFromMain) {
            redirectToMain();
        } else {
            view.finish();
            view.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
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

    public void redirectToCheckOut() {
        Intent intent = new Intent(view, CheckOut.class);
        intent.putExtra("orderList", adapter.checkOut());
        view.startActivity(intent);
        view.finish();
    }
}
