package com.nhom4.bookstoremobile.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.widget.EditText;
import android.widget.Toast;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.ViewAccount;
import com.nhom4.bookstoremobile.entities.AccountResponse;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.AccountService;
import com.nhom4.bookstoremobile.service.ExceptionHandler;
import com.nhom4.bookstoremobile.sqlite.AccountTable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewLoginController {
    private final Activity activity;

    public ViewLoginController(Activity activity) {
        this.activity = activity;
    }

    public void checkLogin() {
        EditText userID_EditText = activity.findViewById(R.id.userID);
        EditText userPassword_EditText = activity.findViewById(R.id.userPassword);

        String userID = userID_EditText.getText().toString();
        String userPassword = userPassword_EditText.getText().toString();

        if (userID.isEmpty()) {
            ExceptionHandler.forcusError(userID_EditText);
            Toast.makeText(activity, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        } else if (userPassword.isEmpty()) {
            ExceptionHandler.forcusError(userPassword_EditText);
            Toast.makeText(activity, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
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
                    AccountResponse accountResponse = response.body();
                    if (accountResponse.getUserID() != null) {
                        Toast.makeText(activity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        loginSuccess(accountResponse);
                        redirectToAccount();
                    } else {
                        Toast.makeText(activity, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loginSuccess(AccountResponse accountResponse) {
        AccountTable accountTable = new AccountTable(activity);
        accountTable.clear();
        int isAdminInt = 0;
        if (accountResponse.isAdmin()) {
            isAdminInt = 1;
        }
        accountTable.addAccount(accountResponse.getUserID(), isAdminInt);
    }

    public void redirectToAccount() {
        Intent intent = new Intent(activity, ViewAccount.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
