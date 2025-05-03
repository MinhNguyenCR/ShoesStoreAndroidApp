package com.example.shoesstoreandroidapp.customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderApi {
    @GET("/shoestore/order/history/{user_id}")
    Call<ApiResponse<List<OrderHistoryResponse>>> getOrderHistory(@Path("user_id") long userId);
}
