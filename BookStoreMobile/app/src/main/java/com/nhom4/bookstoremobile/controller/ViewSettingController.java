package com.nhom4.bookstoremobile.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.ViewAccount;
import com.nhom4.bookstoremobile.activity.ViewSettingInfo;
import com.nhom4.bookstoremobile.service.ConfirmPopup;
import com.nhom4.bookstoremobile.sqlite.AccountTable;


public class ViewSettingController {
    private final Activity activity;

    public ViewSettingController(Activity activity) {
        this.activity = activity;
    }

    public void showConfirmationPopup() {
        ConfirmPopup.show(activity, "Xác nhận", "Bạn muốn đăng xuất?", (dialog, which) -> {
            AccountTable accountTable = new AccountTable(activity);
            accountTable.clear();
            redirectToAccount();
        });
    }

    public void checkAccount(boolean isAdmin) {
        TextView manageProductBtn = activity.findViewById(R.id.manageProductBtn);
        TextView manageAccountBtn = activity.findViewById(R.id.manageAccountBtn);
        TextView manageOrderBtn = activity.findViewById(R.id.manageOrderBtn);

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

    public void redirectToAccount() {
        Intent intent = new Intent(activity, ViewAccount.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    public void redirectToSettingInfo() {
        Intent intent = new Intent(activity, ViewSettingInfo.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}
