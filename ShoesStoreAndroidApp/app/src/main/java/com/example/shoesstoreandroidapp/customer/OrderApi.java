package com.example.shoesstoreandroidapp.customer;


import com.example.shoesstoreandroidapp.customer.Request.OrderResquest;

import com.example.shoesstoreandroidapp.customer.Response.BooleanResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

import retrofit2.http.POST;

import retrofit2.http.PATCH;

import retrofit2.http.Path;

public interface OrderApi {
    @GET("/shoestore/order/history/{user_id}")
    Call<ApiResponse<List<OrderHistoryResponse>>> getOrderHistory(@Path("user_id") long userId);


    @POST("shoestore/order/add")
    Call<ApiResponse<Boolean>> checkout(@Body OrderResquest orderResquest);

    @PATCH("review/{orderId}")
    Call<BooleanResponse> reviewOrder(@Path("orderId") Long orderId);

}
