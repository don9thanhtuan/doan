package com.nhom4.bookstoremobile.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.AccountResponse;

public class CheckOut extends AppCompatActivity {
    private AccountResponse accountResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setListener();
    }

    private void setListener() {
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
//        redirectToAccount();
        super.onBackPressed();
    }
}
