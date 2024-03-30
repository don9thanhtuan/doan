package com.nhom4.bookstoremobile.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.controller.ViewOrderDetailsController;
import com.nhom4.bookstoremobile.controller.ViewSettingInfoController;
import com.nhom4.bookstoremobile.converter.Converter;
import com.nhom4.bookstoremobile.entities.Order;

import java.util.Date;

public class ViewOrderDetails extends AppCompatActivity {
    private ViewOrderDetailsController orderController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

        String orderID = getIntent().getStringExtra("orderID");
        String userID = getIntent().getStringExtra("userID");
        String orderTimeString = getIntent().getStringExtra("orderTime");
        String orderPhone = getIntent().getStringExtra("orderPhone");
        String orderAddress = getIntent().getStringExtra("orderAddress");
        String orderStatus = getIntent().getStringExtra("orderStatus");
        String orderPrice = getIntent().getStringExtra("orderPrice");
        int orderItemQuantity = getIntent().getIntExtra("orderItemQuantity", 0);

        Date orderTime = Converter.stringtoDate(orderTimeString);
        Order order = new Order(orderID, userID, orderTime, orderPhone, orderAddress, orderStatus, orderPrice, orderItemQuantity);

        orderController = new ViewOrderDetailsController(this, order);
        orderController.setOrderToLayout();
        orderController.getOrderDetailsFromAPI();

        ViewSettingInfoController infoController = new ViewSettingInfoController(this);
        infoController.getAccountFromAPI(userID);
        setListener();
    }

    private void setListener() {
        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> orderController.reload(pullToRefresh));

        findViewById(R.id.backButton).setOnClickListener(v -> orderController.redirectBack());
    }
}
