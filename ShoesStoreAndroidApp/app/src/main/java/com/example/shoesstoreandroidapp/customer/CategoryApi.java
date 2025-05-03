package com.example.shoesstoreandroidapp.customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {
    @GET("/api/category/all")
    Call<List<Category>> getCategories();
}

