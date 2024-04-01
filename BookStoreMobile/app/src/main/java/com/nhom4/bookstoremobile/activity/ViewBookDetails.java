package com.nhom4.bookstoremobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.controller.ViewBookDetailsController;
import com.nhom4.bookstoremobile.entities.AccountResponse;
import com.nhom4.bookstoremobile.sqlite.AccountDAO;

public class ViewBookDetails extends AppCompatActivity {
    private ViewBookDetailsController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

        String id = getIntent().getStringExtra("book_id");
        controller = new ViewBookDetailsController(this, id);

        controller.getBookDetailFromAPI();
        controller.getBookListFromAPI();

        setListener();
        setUpAdminLayout();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    private void setListener() {
        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> controller.reload(pullToRefresh));

        findViewById(R.id.backButton).setOnClickListener(v -> controller.redirectToBookList());
        findViewById(R.id.cartBtn).setOnClickListener(v -> controller.redirectToCart());
        findViewById(R.id.addToCartBtn).setOnClickListener(v -> controller.openAddCartView(2));
        findViewById(R.id.buyNowBtn).setOnClickListener(v -> controller.openAddCartView(1));
        findViewById(R.id.overlayLayout).setOnTouchListener((v, event) -> {
            v.performClick();
            controller.closeAddCartView();
            return false;
        });
    }

    private void setUpAdminLayout() {
        Button editBtn = findViewById(R.id.editBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);

        AccountResponse accountResponse = AccountDAO.getInstance(this).getAccountData();
        if (accountResponse != null) {
            if (accountResponse.isAdmin()) {
                editBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
                editBtn.setOnClickListener(v -> controller.redirectToEditBook());
                deleteBtn.setOnClickListener(v -> controller.showDeleteConfirm());
                return;
            }
        }

        editBtn.setVisibility(View.GONE);
        editBtn.setOnClickListener(null);
        deleteBtn.setVisibility(View.GONE);
        deleteBtn.setOnClickListener(null);
    }
}