package com.example.shoesstoreandroidapp.customer;

import com.example.shoesstoreandroidapp.customer.Request.NotificationRequest;
import com.google.android.gms.common.api.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationApi {
    @GET("/notifications/{id}")
    Call<List<NotificationResponse>> getNotifications(@Path("id") long userId);
    @POST("/notifications/push_notification")
    Call<ApiResponse<Boolean>> pushNotification(@Body NotificationRequest notificationRequest);
}
