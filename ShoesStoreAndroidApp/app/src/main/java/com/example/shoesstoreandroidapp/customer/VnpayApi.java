package com.example.shoesstoreandroidapp.customer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VnpayApi {
    @GET("/api/payment/create")
    Call<PaymentResponse> createPayment(@Query("amount") double amount);
}

