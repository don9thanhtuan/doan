package com.nhom4.bookstoremobile.controller;

import android.app.Activity;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.adapter.OrderItemAdapter;
import com.nhom4.bookstoremobile.converter.Converter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.CartItem;
import com.nhom4.bookstoremobile.entities.Order;
import com.nhom4.bookstoremobile.entities.OrderDetails;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.OrderService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrderDetailsController {
    private final Activity activity;
    private final Order order;

    public ViewOrderDetailsController(Activity activity, Order order) {
        this.activity = activity;
        this.order = order;
    }

    public void getOrderDetailsFromAPI() {
        OrderService orderService = RetrofitAPI.getInstance().create(OrderService.class);

        Call<OrderDetails> call = orderService.getOrdersDetails(order.getOrderID());
        call.enqueue(new Callback<OrderDetails>() {
            @Override
            public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        OrderDetails orderDetails = response.body();
                        setOrderDetailsToLayout(orderDetails);
                        setTotalQuantity(orderDetails);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDetails> call, Throwable t) {}
        });
    }

    public void setOrderToLayout() {
        TextView orderIDTextView = activity.findViewById(R.id.orderID);
        TextView orderStatusTextView = activity.findViewById(R.id.orderStatus);
        TextView orderTimeTextView = activity.findViewById(R.id.orderTime);

        TextView productPriceTextView = activity.findViewById(R.id.productPrice);
        TextView totalPriceTextView = activity.findViewById(R.id.totalPrice);

        orderIDTextView.setText(order.getOrderID());
        orderStatusTextView.setText(order.getOrderStatus());

        String orderTimeString = Converter.dateToString(order.getOrderTime());
        orderTimeTextView.setText(orderTimeString);

        totalPriceTextView.setText(order.getOrderPrice());
        String orderPrice = calculatePrice(order.getOrderPrice());
        productPriceTextView.setText(orderPrice);
    }

    private void setTotalQuantity(OrderDetails orderDetails) {
        TextView totalProductTextView = activity.findViewById(R.id.totalProduct);

        int totalQuantity = 0;
        for (OrderDetails.OrderItem orderItem : orderDetails.getOrderItemList()) {
            totalQuantity += orderItem.getQuantity();
        }

        String itemQuantity = "(" + totalQuantity + " sản phẩm):";
        totalProductTextView.setText(itemQuantity);
    }

    public void setOrderDetailsToLayout(OrderDetails orderDetails) {
        List<CartItem> orderItemList = new ArrayList<>();

        for (OrderDetails.OrderItem orderItem : orderDetails.getOrderItemList()) {
            CartItem cartItem = new CartItem(orderItem.getBookID(), orderItem.getQuantity());
            Book book = orderItem.getBook();
            String imageURL = DefaultURL.getUrl() + book.getBookImage();
            book.setBookImage(imageURL);
            cartItem.setBook(book);
            orderItemList.add(cartItem);
        }

        RecyclerView recyclerView = activity.findViewById(R.id.orderItemList);
        OrderItemAdapter adapter = new OrderItemAdapter(activity, orderItemList, null);
        adapter.setmRecyclerView(recyclerView)  ;
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private String calculatePrice(String totalPriceRaw) {
        totalPriceRaw = totalPriceRaw.replace("₫", "");
        totalPriceRaw = totalPriceRaw.replaceAll("\\s+", "");
        totalPriceRaw = totalPriceRaw.replace(".", "");

        int totalPrice = Integer.parseInt(totalPriceRaw);
        totalPrice -= 20000;

        return String.format("%,d", totalPrice).replace(',', '.') + " ₫";
    }

    public void reload(SwipeRefreshLayout pullToRefresh) {
        activity.recreate();
        pullToRefresh.setRefreshing(false);
    }

    public void redirectBack() {
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
