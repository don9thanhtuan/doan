package com.nhom4.bookstoremobile.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.sqlite.AccountTable;

public class ViewSetting extends AppCompatActivity {
    private boolean isAdmin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        checkAccount();

        setListener();
    }

    @Override
    public void onBackPressed() {
        redirectToAccount();
        super.onBackPressed();
    }

    private void redirectToAccount() {
        Intent intent = new Intent(ViewSetting.this, ViewAccount.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    private void setListener() {
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            redirectToAccount();
        });

        findViewById(R.id.logoutBtn).setOnClickListener(v -> {
            showConfirmationPopup();
        });

        findViewById(R.id.infoBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ViewSetting.this, ViewSettingInfo.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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
        showConfirmationPopup(this, "Xác nhận", "Bạn muốn đăng xuất?", (dialog, which) -> {
            AccountTable accountTable = new AccountTable(ViewSetting.this);
            accountTable.clear();
            redirectToAccount();
        });
    }

    private void checkAccount() {
        TextView manageProductBtn = findViewById(R.id.manageProductBtn);
        TextView manageAccountBtn = findViewById(R.id.manageAccountBtn);
        TextView manageOrderBtn = findViewById(R.id.manageOrderBtn);

        if (isAdmin) {
            manageProductBtn.setVisibility(View.VISIBLE);
            manageAccountBtn.setVisibility(View.VISIBLE);
            manageOrderBtn.setVisibility(View.VISIBLE);
        } else {
            manageProductBtn.setVisibility(View.GONE);
            manageAccountBtn.setVisibility(View.GONE);
            manageOrderBtn.setVisibility(View.GONE);
        }
    }
}
