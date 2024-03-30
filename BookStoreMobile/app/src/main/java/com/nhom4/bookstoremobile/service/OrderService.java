package com.nhom4.bookstoremobile.service;

import com.nhom4.bookstoremobile.entities.Order;
import com.nhom4.bookstoremobile.entities.OrderDTO;
import com.nhom4.bookstoremobile.entities.OrderDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {
    @GET("accounts/{username}/orders")
    Call<List<Order>> getPersonalOrders(@Path("username") String userID);

    @POST("accounts/{username}/orders")
    Call<String> createOrder(@Path("username") String userID, @Body OrderDTO orderDTO);

    @GET("orders/{orderID}")
    Call<OrderDetails> getOrdersDetails(@Path("orderID") String orderID);
}