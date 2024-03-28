package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.AccountResponse;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.AccountService;
import com.nhom4.bookstoremobile.sqlite.AccountTable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewLogin extends AppCompatActivity {
    private AccountResponse accountResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setListener();
    }

    @Override
    public void onBackPressed() {
        redirectToAccount();
        super.onBackPressed();
    }

    private void redirectToAccount() {
        Intent intent = new Intent(ViewLogin.this, ViewAccount.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    private void setListener() {
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        });

        findViewById(R.id.loginBtn).setOnClickListener(v -> checkLogin());

    }

    private void checkLogin() {
        EditText userID_EditText = findViewById(R.id.userID);
        EditText userPassword_EditText = findViewById(R.id.userPassword);

        String userID = userID_EditText.getText().toString();
        String userPassword = userPassword_EditText.getText().toString();

        Rect view = new Rect();
        if (userID.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            userID_EditText.requestFocus();
            userID_EditText.getGlobalVisibleRect(view);
            userID_EditText.requestRectangleOnScreen(view);
            return;
        } else if (userPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            userPassword_EditText.requestFocus();
            userPassword_EditText.getGlobalVisibleRect(view);
            userPassword_EditText.requestRectangleOnScreen(view);
            return;
        }

        RequestBody userIDRB = RequestBody.create(MediaType.parse("text/plain"), userID);
        RequestBody userPasswordRB = RequestBody.create(MediaType.parse("text/plain"), userPassword);

        AccountService accountService = RetrofitAPI.getInstance().create(AccountService.class);

        Call<AccountResponse> call = accountService.login(userIDRB, userPasswordRB);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                if (response.isSuccessful()) {
                    accountResponse = response.body();
                    if (accountResponse.getUserID() != null) {
                        Toast.makeText(ViewLogin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        loginSuccess();
                        redirectToAccount();
                    } else {
                        Toast.makeText(ViewLogin.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewLogin.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                Toast.makeText(ViewLogin.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loginSuccess() {
        AccountTable accountTable = new AccountTable(ViewLogin.this);
        accountTable.clear();
        int isAdminInt = 0;
        if (accountResponse.isAdmin()) {
            isAdminInt = 1;
        }
        accountTable.addAccount(accountResponse.getUserID(), isAdminInt);
    }
}


