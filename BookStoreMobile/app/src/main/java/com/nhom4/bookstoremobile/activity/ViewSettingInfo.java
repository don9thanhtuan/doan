package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.Account;
import com.nhom4.bookstoremobile.entities.AccountResponse;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.AccountService;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.sqlite.AccountTable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewSettingInfo extends AppCompatActivity {
    private Account account;
    private View editInfo_Layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);
        setListener();

        AccountResponse accountResponse = getAccountData();
        getAccountFromAPI(accountResponse.getUserID());
    }

    @Override
    public void onBackPressed() {
        redirectToAccountSetting();
        super.onBackPressed();
    }

    private void redirectToAccountSetting() {
        Intent intent = new Intent(ViewSettingInfo.this, ViewSetting.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    private void setListener() {
        findViewById(R.id.backBtn).setOnClickListener(v -> redirectToAccountSetting());

        findViewById(R.id.nameChangeBtn).setOnClickListener(v -> openEditor(1));
        findViewById(R.id.emailChangeBtn).setOnClickListener(v -> openEditor(2));
        findViewById(R.id.phoneChangeBtn).setOnClickListener(v -> openEditor(3));
        findViewById(R.id.addressChangeBtn).setOnClickListener(v -> openEditor(4));
        findViewById(R.id.overlayLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeEditor();
                return false;
            }
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
                    setAccountData(account);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
            }
        });
    }

    private AccountResponse getAccountData() {
        AccountTable accountTable = new AccountTable(this);
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

    private void setAccountData(Account account) {
        TextView userID_TextView = findViewById(R.id.userID);
        TextView userName_TextView = findViewById(R.id.userName);
        TextView userEmail_TextView = findViewById(R.id.userEmail);
        TextView userPhone_TextView = findViewById(R.id.userPhone);
        TextView userAddress_TextView = findViewById(R.id.userAddress);

        userID_TextView.setText(account.getTenTaiKhoan());
        userName_TextView.setText(account.getHoTen());
        userEmail_TextView.setText(account.getEmail());
        userPhone_TextView.setText(account.getSoDienThoai());
        userAddress_TextView.setText(account.getDiaChi());
    }

    private void openEditor(int choice) {
        LayoutInflater inflater = getLayoutInflater();
        editInfo_Layout = inflater.inflate(R.layout.main_edit_info, null);

        editInfo_Layout.setId(choice);
        setListenerEditor(editInfo_Layout);

        TextView titleInfo = editInfo_Layout.findViewById(R.id.titleInfo);
        TextView info = editInfo_Layout.findViewById(R.id.info);
        EditText infoEditText = editInfo_Layout.findViewById(R.id.infoEditText);

        String choiceString = "";
        String editInfo = "";
        switch (choice) {
            case 1:
                choiceString = "Họ tên";
                editInfo = account.getHoTen();
                break;
            case 2:
                choiceString = "Email";
                editInfo = account.getEmail();
                break;
            case 3:
                choiceString = "Số điện thoại";
                editInfo = account.getSoDienThoai();

                InputFilter[] filters = new InputFilter[1];
                filters[0] = new InputFilter.LengthFilter(10);

                infoEditText.setFilters(filters);
                break;
            case 4:
                choiceString = "Địa chỉ";
                editInfo = account.getDiaChi();
                break;
        }

        titleInfo.setText(choiceString);
        info.setText(choiceString);
        infoEditText.setText(editInfo);

        editInfo_Layout.findViewById(R.id.closeBtn).setOnClickListener(v -> closeEditor());

        FrameLayout layoutContainer = findViewById(R.id.editInfoLayout);
        layoutContainer.addView(editInfo_Layout);

        editInfo_Layout.setClickable(true);
        editInfo_Layout.setFocusable(true);
        findViewById(R.id.overlayLayout).setVisibility(View.VISIBLE);

        Animation slideUpAnimation = AnimationUtils.loadAnimation(ViewSettingInfo.this, R.anim.slide_up);
        editInfo_Layout.startAnimation(slideUpAnimation);
    }

    private void closeEditor() {
        FrameLayout layoutContainer = findViewById(R.id.editInfoLayout);

        findViewById(R.id.overlayLayout).setVisibility(View.GONE);

        editInfo_Layout.setClickable(false);
        editInfo_Layout.setFocusable(false);

        Animation slideDownAnimation = AnimationUtils.loadAnimation(ViewSettingInfo.this, R.anim.slide_down);
        editInfo_Layout.startAnimation(slideDownAnimation);

        layoutContainer.removeAllViewsInLayout();
    }

    private void setListenerEditor(View view) {
        view.findViewById(R.id.saveBtn).setOnClickListener(v -> {
            Account newAccount = new Account();
            newAccount.setTenTaiKhoan(account.getTenTaiKhoan());
            newAccount.setAdmin(account.isAdmin());

            EditText infoEditText = findViewById(R.id.infoEditText);
            String info = infoEditText.getText().toString();

            switch (view.getId()) {
                case 1:
                    newAccount.setHoTen(info);
                    break;
                case 2:
                    newAccount.setEmail(info);
                    break;
                case 3:
                    newAccount.setSoDienThoai(info);
                    break;
                case 4:
                    newAccount.setDiaChi(info);
                    break;
            }

            editPersonalAccountByAPI(newAccount);
        });
    }

    private void editPersonalAccountByAPI(Account account) {
        AccountService accountService = RetrofitAPI.getInstance().create(AccountService.class);
        Call<String> call = accountService.editAccount(account.getTenTaiKhoan(), account);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(ViewSettingInfo.this, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ViewSettingInfo.this, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                recreate();
            }
        });
    }
}
