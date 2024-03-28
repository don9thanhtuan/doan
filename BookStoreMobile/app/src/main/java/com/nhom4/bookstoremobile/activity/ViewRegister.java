package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.AccountResponse;

public class ViewRegister extends AppCompatActivity {
    private AccountResponse accountResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setListener();
    }

    private void setListener() {
        findViewById(R.id.userID);
        findViewById(R.id.userPassword);
        findViewById(R.id.userConfirmPassword);

        findViewById(R.id.registerBtn).setOnClickListener(v -> {

        });
        findViewById(R.id.backBtn).setOnClickListener(v -> redirectToAccount());


    }

    @Override
    public void onBackPressed() {
        redirectToAccount();
        super.onBackPressed();
    }

    private void redirectToAccount() {
        Intent intent = new Intent(ViewRegister.this, ViewAccount.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    private void checkInfo() {
        EditText userPasswordEditText = findViewById(R.id.userPassword);
        EditText userConfirmPasswordEditText = findViewById(R.id.userConfirmPassword);

        String userPassword = userPasswordEditText.getText().toString();
        String userConfirmPassword = userConfirmPasswordEditText.getText().toString();

        if (userPassword.equals(userConfirmPassword)) {
            EditText userIDEditText = findViewById(R.id.userID);
            String userID = userIDEditText.getText().toString();


        } else {
            Toast.makeText(ViewRegister.this, "Vui lòng xác nhận lại mật khẩu", Toast.LENGTH_LONG).show();
        }
    }
}
