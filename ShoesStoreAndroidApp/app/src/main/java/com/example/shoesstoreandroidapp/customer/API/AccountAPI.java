package com.example.shoesstoreandroidapp.customer.API;

import com.example.shoesstoreandroidapp.customer.Request.LoginRequest;
import com.example.shoesstoreandroidapp.customer.Response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountAPI {
        @POST("/shoestore/account/login")
        Call<LoginResponse> login(@Body LoginRequest request);
}
