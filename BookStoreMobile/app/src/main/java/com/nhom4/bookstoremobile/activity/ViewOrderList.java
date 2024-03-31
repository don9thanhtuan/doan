package com.nhom4.bookstoremobile.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.controller.ViewOrderListController;

public class ViewOrderList extends AppCompatActivity {
    private ViewOrderListController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        String userID = getIntent().getStringExtra("userID");

        controller = new ViewOrderListController(this, userID);

        setListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setListener() {
        findViewById(R.id.backBtn).setOnClickListener(v -> controller.redirectBack());

        TextView allBtn = findViewById(R.id.allBtn);
        TextView waitBtn = findViewById(R.id.waitBtn);
        TextView confirmBtn = findViewById(R.id.confirmBtn);
        TextView deliveryBtn = findViewById(R.id.deliveryBtn);
        TextView completeBtn = findViewById(R.id.completeBtn);

        controller.addBtnToList(allBtn);
        controller.addBtnToList(waitBtn);
        controller.addBtnToList(confirmBtn);
        controller.addBtnToList(deliveryBtn);
        controller.addBtnToList(completeBtn);

        allBtn.setOnClickListener(v -> {
            controller.setEffect(allBtn);
            controller.getOrderFromAPI(4);
        });
        waitBtn.setOnClickListener(v -> {
            controller.setEffect(waitBtn);
            controller.getOrderFromAPI(0);
        });
        confirmBtn.setOnClickListener(v -> {
            controller.setEffect(confirmBtn);
            controller.getOrderFromAPI(1);
        });
        deliveryBtn.setOnClickListener(v -> {
            controller.setEffect(deliveryBtn);
            controller.getOrderFromAPI(2);
        });
        completeBtn.setOnClickListener(v -> {
            controller.setEffect(completeBtn);
            controller.getOrderFromAPI(3);
        });

        controller.setEffect(allBtn);
        controller.getOrderFromAPI(4);
    }
}
