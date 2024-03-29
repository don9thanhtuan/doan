package com.nhom4.bookstoremobile.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.controller.CheckOutController;
import com.nhom4.bookstoremobile.controller.ViewSettingInfoController;
import com.nhom4.bookstoremobile.entities.AccountResponse;
import com.nhom4.bookstoremobile.entities.CartItem;

public class CheckOut extends AppCompatActivity {
    private CheckOutController orderController;
    private ViewSettingInfoController infoController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        infoController = new ViewSettingInfoController(this);
        AccountResponse accountResponse = infoController.getAccountData();
        infoController.getAccountFromAPI(accountResponse.getUserID());

        orderController = new CheckOutController(this);
        if (getIntent().getBooleanExtra("isBuyNow", false)) {
            String bookID = getIntent().getStringExtra("bookID");
            int quantity = getIntent().getIntExtra("quantity", 0);
            CartItem cartItem = new CartItem(bookID, quantity);
            orderController.buyNow(cartItem);
        } else {
            boolean[] checkedArray = getIntent().getBooleanArrayExtra("orderList");
            orderController.getOrderItemList(checkedArray);
        }

        setListener();
    }

    private void setListener() {
        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);

        pullToRefresh.setOnRefreshListener(() -> orderController.reload(pullToRefresh));
        findViewById(R.id.phoneChange).setOnClickListener(v -> infoController.openEditor(3));
        findViewById(R.id.addressChange).setOnClickListener(v -> infoController.openEditor(4));
        findViewById(R.id.backButton).setOnClickListener(v -> orderController.redirectToCart());
        findViewById(R.id.overlayLayout).setOnTouchListener((v, event) -> {
            v.performClick();
            infoController.closeEditor();
            return false;
        });

        TextView codBtn = findViewById(R.id.codBtn);
        TextView waitBtn = findViewById(R.id.zaloBtn);
        TextView momoBtn = findViewById(R.id.momoBtn);
        TextView creditBtn = findViewById(R.id.creditBtn);

        orderController.addBtnToList(codBtn);
        orderController.addBtnToList(waitBtn);
        orderController.addBtnToList(momoBtn);
        orderController.addBtnToList(creditBtn);

        codBtn.setOnClickListener(v -> {
            orderController.setEffect(codBtn);
        });
        waitBtn.setOnClickListener(v -> {
            orderController.setEffect(waitBtn);
        });
        momoBtn.setOnClickListener(v -> {
            orderController.setEffect(momoBtn);
        });
        creditBtn.setOnClickListener(v -> {
            orderController.setEffect(creditBtn);
        });

        orderController.setEffect(codBtn);
        findViewById(R.id.orderBtn).setOnClickListener(v -> orderController.createOrder(infoController.getAccount()));
    }

    @Override
    public void onBackPressed() {
        orderController.redirectToCart();
        super.onBackPressed();
    }
}
