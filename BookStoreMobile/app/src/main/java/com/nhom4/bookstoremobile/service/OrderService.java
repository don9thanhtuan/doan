package com.nhom4.bookstoremobile.service;

import com.nhom4.bookstoremobile.entities.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderService {
    @GET("accounts/{username}/orders")
    Call<List<Order>> getPersonalOrders(@Path("username") String userID);
}
