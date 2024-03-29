package com.nhom4.bookstoremobile.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.controller.ViewSettingInfoController;
import com.nhom4.bookstoremobile.entities.AccountResponse;

public class ViewSettingInfo extends AppCompatActivity {
    private ViewSettingInfoController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);
        controller = new ViewSettingInfoController(this);

        setListener();

        AccountResponse accountResponse = controller.getAccountData();
        controller.getAccountFromAPI(accountResponse.getUserID());
    }

    @Override
    public void onBackPressed() {
        controller.redirectToAccountSetting();
        super.onBackPressed();
    }

    private void setListener() {
        findViewById(R.id.backBtn).setOnClickListener(v -> controller.redirectToAccountSetting());

        findViewById(R.id.nameChangeBtn).setOnClickListener(v -> controller.openEditor(1));
        findViewById(R.id.emailChangeBtn).setOnClickListener(v -> controller.openEditor(2));
        findViewById(R.id.phoneChangeBtn).setOnClickListener(v -> controller.openEditor(3));
        findViewById(R.id.addressChangeBtn).setOnClickListener(v -> controller.openEditor(4));
        findViewById(R.id.overlayLayout).setOnTouchListener((v, event) -> {
            v.performClick();
            controller.closeEditor();
            return false;
        });
    }
}
