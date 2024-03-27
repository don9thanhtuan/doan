package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.MainActivity;
import com.nhom4.bookstoremobile.R;
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

public class ViewAccount extends AppCompatActivity {
    private AccountTable accountTable;
    private Account account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountTable = new AccountTable(this);
        AccountResponse accountResponse = getAccountData();

        if (accountResponse == null) {
            setContentView(R.layout.activity_non_login);
            setListenerNonLogin();
        } else {
            setContentView(R.layout.activity_view_account_user);
            setListenerAccount();
            getAccountFromAPI(accountResponse.getUserID());
        }
        setBaseListener();
    }

    @Override
    public void onBackPressed() {
        redirectToMain();
        super.onBackPressed();
    }

    private void redirectToMain() {
        Intent intent = new Intent(ViewAccount.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setBaseListener() {
        findViewById(R.id.homeBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ViewAccount.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.cartBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ViewAccount.this, ViewCart.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.accountBtn).setOnClickListener(v -> recreate());
    }

    private AccountResponse getAccountData() {
        Cursor cursor = accountTable.getAccount();
        if (cursor != null && cursor.moveToFirst()) {
            String userID = cursor.getString(cursor.getColumnIndex("userID"));
            int isAdminInt = cursor.getInt(cursor.getColumnIndex("isAdmin"));

            if (isAdminInt == 0) {
                return new AccountResponse(userID, false);
            } else {
                return new AccountResponse(userID, true);
            }
        }
        return null;
    }

    private void setListenerNonLogin() {
        findViewById(R.id.loginBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ViewAccount.this, ViewLogin.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.registerBtn).setOnClickListener(v -> {

        });
    }

    private void setListenerAccount() {
        findViewById(R.id.settingBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ViewAccount.this, ViewSetting.class);
            intent.putExtra("isAdmin", account.isAdmin());
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        });

        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            recreate();
            pullToRefresh.setRefreshing(false);
        });

        findViewById(R.id.viewOrderListBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ViewAccount.this, ViewOrderList.class);
            intent.putExtra("userID", account.getTenTaiKhoan());
            startActivity(intent);

            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        });
    }

    private void getAccountFromAPI(String userID) {
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
        TextView nameUser_TextView = findViewById(R.id.nameUser);
        if (account.isAdmin()) {
            nameUser_TextView.setText(account.getTenTaiKhoan());
        } else {
            nameUser_TextView.setText(account.getHoTen());
        }
    }

    private void getOrderFromAPI(Account account) {
        OrderService orderService = RetrofitAPI.getInstance().create(OrderService.class);

        Call<List<Order>> call = orderService.getPersonalOrders(account.getTenTaiKhoan());
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> orderList = response.body();

                    for (int i = 0; i < orderList.size(); i++) {
                        if (orderList.get(i).getTrangThaiInt() == 3) {
                            orderList.remove(i);
                            i--;
                        }
                    }

                    for (Order order : orderList) {
                        Book book = order.getCuonSachDau();
                        String imageUrl = DefaultURL.getUrl() + book.getHinhAnh();
                        book.setHinhAnh(imageUrl);
                    }

                    RecyclerView recyclerView = findViewById(R.id.orderList);
                    OrderAdapter adapter = new OrderAdapter(ViewAccount.this, orderList, recyclerView);

                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewAccount.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
            }
        });
    }
}
