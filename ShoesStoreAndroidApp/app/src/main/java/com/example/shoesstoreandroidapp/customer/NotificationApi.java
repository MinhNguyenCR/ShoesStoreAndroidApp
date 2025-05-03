package com.example.shoesstoreandroidapp.customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NotificationApi {
    @GET("/notifications/{id}")
    Call<List<NotificationResponse>> getNotifications(@Path("id") long userId);
}
