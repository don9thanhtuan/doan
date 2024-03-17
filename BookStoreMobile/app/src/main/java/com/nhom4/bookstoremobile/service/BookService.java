package com.nhom4.bookstoremobile.service;


import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.BookResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BookService {
    @GET("books")
    Call<List<Book>> getBookFromRestAPI();

    @GET("topselling")
    Call<List<Book>> getBookTopSellingFromRestAPI();

    @GET("books/{id}")
    Call<Book> getBookDetailsFromRestAPI(@Path("id") String bookId);

    @Multipart
    @POST("books")
    Call<BookResponse> addBook(
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

    @Multipart
    @PUT("books/{id}")
    Call<String> editBook(
            @Path("id") String bookId,
            @Part MultipartBody.Part image,
            @Part("path") RequestBody filePath,
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("author") RequestBody author,
            @Part("publisher") RequestBody publisher,
            @Part("weight") RequestBody weight,
            @Part("size") RequestBody size,
            @Part("stock") RequestBody stock,
            @Part("introduction") RequestBody introduction
    );

    @DELETE("books/{id}")
    Call<ResponseBody> deleteBook(@Path("id") String id);
}
