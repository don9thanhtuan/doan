package com.nhom4.bookstoremobile.service;

import com.nhom4.bookstoremobile.entities.Account;
import com.nhom4.bookstoremobile.entities.AccountResponse;
import com.nhom4.bookstoremobile.entities.Book;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AccountService {
    @Multipart
    @POST("login")
    Call<AccountResponse> login(
            @Part("username") RequestBody userID,
            @Part("password") RequestBody userPassword
    );

    @GET("accounts/{username}")
    Call<Account> getAccount(@Path("username") String userID);
}
