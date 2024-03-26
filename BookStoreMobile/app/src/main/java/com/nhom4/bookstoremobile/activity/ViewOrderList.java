package com.nhom4.bookstoremobile.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.adapter.OrderAdapter;
import com.nhom4.bookstoremobile.entities.Account;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.Order;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.OrderService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrderList extends AppCompatActivity {
    private final List<TextView> btnList = new ArrayList<>();
    private String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        userID = getIntent().getStringExtra("userID");
        setListener();
    }

    private void setListener() {
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        });

        TextView allBtn = findViewById(R.id.allBtn);
        TextView waitBtn = findViewById(R.id.waitBtn);
        TextView confirmBtn = findViewById(R.id.confirmBtn);
        TextView deliveryBtn = findViewById(R.id.deliveryBtn);
        TextView completeBtn = findViewById(R.id.completeBtn);

        btnList.add(allBtn);
        btnList.add(waitBtn);
        btnList.add(confirmBtn);
        btnList.add(deliveryBtn);
        btnList.add(completeBtn);

        allBtn.setOnClickListener(v -> {
            setEffect(allBtn);
            getOrderFromAPI(4);
        });
        waitBtn.setOnClickListener(v -> {
            setEffect(waitBtn);
            getOrderFromAPI(0);
        });
        confirmBtn.setOnClickListener(v -> {
            setEffect(confirmBtn);
            getOrderFromAPI(1);
        });
        deliveryBtn.setOnClickListener(v -> {
            setEffect(deliveryBtn);
            getOrderFromAPI(2);
        });
        completeBtn.setOnClickListener(v -> {
            setEffect(completeBtn);
            getOrderFromAPI(3);
        });

        setEffect(allBtn);
        getOrderFromAPI(4);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    private void setEffect(TextView clickedBtn) {
        for (TextView textView : btnList) {
            if(textView == clickedBtn) {
                textView.setBackgroundResource(R.drawable.bottom_border_black);
                textView.setTextColor(Color.parseColor("#000000"));
                continue;
            }
            textView.setBackground(null);
            textView.setTextColor(Color.parseColor("#FF49454F"));
        }
    }

    private void getOrderFromAPI(int choice) {
        OrderService orderService = RetrofitAPI.getInstance().create(OrderService.class);

        Call<List<Order>> call = orderService.getPersonalOrders(userID);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> orderList = response.body();

                    if(choice != 4) {
                        for (int i = 0; i < orderList.size(); i++) {
                            if (orderList.get(i).getTrangThaiInt() != choice) {
                                orderList.remove(i);
                                i--;
                            }
                        }
                    }

                    for (Order order : orderList) {
                        Book book = order.getCuonSachDau();
                        String imageUrl = DefaultURL.getUrl() + book.getHinhAnh();
                        book.setHinhAnh(imageUrl);
                    }

                    RecyclerView recyclerView = findViewById(R.id.orderList);
                    OrderAdapter adapter = new OrderAdapter(ViewOrderList.this, orderList, recyclerView);

                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewOrderList.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });
    }
}
