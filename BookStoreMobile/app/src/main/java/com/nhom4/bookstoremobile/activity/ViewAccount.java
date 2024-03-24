package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.MainActivity;
import com.nhom4.bookstoremobile.R;

public class ViewAccount extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_user);
        setListener();
    }

    private void setListener() {
        findViewById(R.id.homeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAccount.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.cartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAccount.this, ViewCart.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.accountBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }
}
