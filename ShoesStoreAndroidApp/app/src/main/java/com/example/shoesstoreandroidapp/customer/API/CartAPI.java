package com.example.shoesstoreandroidapp.customer.API;

import com.example.shoesstoreandroidapp.customer.Response.CartItemsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CartAPI {
    @GET("shoestore/cart/{accountId}")
    Call<CartItemsResponse> getCartItems(@Path("accountId") Long accountId);
}
