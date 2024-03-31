package com.nhom4.bookstoremobile.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.controller.ViewOrderDetailsController;

public class ViewOrderDetails extends AppCompatActivity {
    private ViewOrderDetailsController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

        String orderID = getIntent().getStringExtra("orderID");

        controller = new ViewOrderDetailsController(this);
        controller.getOrderFromAPI(orderID);
        controller.getOrderDetailsFromAPI(orderID);
        controller.checkAdmin();
        setListener();
    }

    @Override
    public void onBackPressed() {
        controller.redirectBack();
        super.onBackPressed();
    }

    private void setListener() {
        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> controller.reload(pullToRefresh));

        findViewById(R.id.backButton).setOnClickListener(v -> controller.redirectBack());
    }
}
