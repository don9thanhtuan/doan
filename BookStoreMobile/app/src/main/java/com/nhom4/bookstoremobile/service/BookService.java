package com.nhom4.bookstoremobile.service;


import com.nhom4.bookstoremobile.entities.Book;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BookService {
    @GET("books")
    Call<List<Book>> getBookFromRestAPI();

    @GET("books/{id}")
    Call<List<Book>> getBookDetailsFromRestAPI();

    @Multipart
    @POST("books")
    Call<Book> addBook(
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("author") RequestBody author,
            @Part("publisher") RequestBody publisher,
            @Part("weight") RequestBody weight,
            @Part("size") RequestBody size,
            @Part("stock") RequestBody stock,
            @Part("introduction") RequestBody introduction
    );
}
