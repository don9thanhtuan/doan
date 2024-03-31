package com.nhom4.bookstoremobile.controller;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.MainActivity;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.ViewCart;
import com.nhom4.bookstoremobile.activity.ViewLogin;
import com.nhom4.bookstoremobile.activity.ViewOrderList;
import com.nhom4.bookstoremobile.activity.ViewRegister;
import com.nhom4.bookstoremobile.activity.ViewSetting;
import com.nhom4.bookstoremobile.adapter.OrderAdapter;
import com.nhom4.bookstoremobile.entities.Account;
import com.nhom4.bookstoremobile.entities.AccountResponse;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.Order;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.AccountService;
import com.nhom4.bookstoremobile.service.OrderService;
import com.nhom4.bookstoremobile.sqlite.AccountTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAccountController {
    private final Activity activity;
    private Account account;

    public ViewAccountController(Activity activity) {
        this.activity = activity;
    }

    public void getAccountFromAPI(String userID) {
        AccountService accountService = RetrofitAPI.getInstance().create(AccountService.class);

        Call<Account> call = accountService.getAccount(userID);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    account = response.body();
                    setDataAccount(account);
                    getOrderFromAPI(account);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
            }
        });
    }

    private void setDataAccount(Account account) {
        TextView nameUser_TextView = activity.findViewById(R.id.nameUser);
        TextView titleTxtView_TextView = activity.findViewById(R.id.titleTxtView);

        if (account.isAdmin()) {
            nameUser_TextView.setText(account.getUserID());
            titleTxtView_TextView.setText("Quản lí đơn hàng");
        } else {
            nameUser_TextView.setText(account.getUserName());
            titleTxtView_TextView.setText("Đơn hàng của tôi");
        }

    }

    private void getOrderFromAPI(Account account) {
        OrderService orderService = RetrofitAPI.getInstance().create(OrderService.class);
        Call<List<Order>> call;
        if (account.isAdmin()) {
            call = orderService.getOrderList();
        } else {
            call = orderService.getPersonalOrders(account.getUserID());
        }
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> orderList = response.body();

                    for (int i = 0; i < orderList.size(); i++) {
                        if (orderList.get(i).getOrderStatusInt() >= 3) {
                            orderList.remove(i);
                            i--;
                        }
                    }

                    for (Order order : orderList) {
                        Book book = order.getOrderFirstBook();
                        String imageUrl = DefaultURL.getUrl() + book.getBookImage();
                        book.setBookImage(imageUrl);
                    }

                    RecyclerView recyclerView = activity.findViewById(R.id.orderList);
                    OrderAdapter adapter = new OrderAdapter(activity, orderList, recyclerView);

                    recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
            }
        });
    }

    public void redirectToMain() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void redirectToCart() {
        Intent intent = new Intent(activity, ViewCart.class);
        intent.putExtra("main", true);
        activity.startActivity(intent);
        activity.finish();
    }

    public void redirectToLogin() {
        Intent intent = new Intent(activity, ViewLogin.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void redirectToOrderList() {
        Intent intent = new Intent(activity, ViewOrderList.class);
        intent.putExtra("userID", account.getUserID());
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void redirectToSetting() {
        Intent intent = new Intent(activity, ViewSetting.class);
        intent.putExtra("isAdmin", account.isAdmin());
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void reload(SwipeRefreshLayout pullToRefresh) {
        activity.recreate();
        pullToRefresh.setRefreshing(false);
    }

    public void redirectToRegister() {
        Intent intent = new Intent(activity, ViewRegister.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}
