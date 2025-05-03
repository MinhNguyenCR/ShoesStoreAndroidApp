package com.example.shoesstoreandroidapp.customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductApi {
    @GET("/shoestore/product/all")
    Call<ApiResponse<List<shoesModel>>> getProducts();
}
