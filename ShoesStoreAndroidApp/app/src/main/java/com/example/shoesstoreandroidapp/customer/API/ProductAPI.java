package com.example.shoesstoreandroidapp.customer.API;


import com.example.shoesstoreandroidapp.customer.Response.ProductDetailResponse;
import com.example.shoesstoreandroidapp.customer.Response.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductAPI {
    @GET("shoestore/product/all")
    Call<ProductResponse> getProducts();

    @GET("shoestore/product/{name}")
    Call<ProductDetailResponse> getProductByName(@Path("name") String name);

}
