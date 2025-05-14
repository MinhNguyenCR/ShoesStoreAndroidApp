package com.example.shoesstoreandroidapp.customer.API;

import com.example.shoesstoreandroidapp.customer.Category;
import com.example.shoesstoreandroidapp.customer.Request.FeedbackRequest;
import com.example.shoesstoreandroidapp.customer.Response.BooleanResponse;
import com.example.shoesstoreandroidapp.customer.Response.FeedbackResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FeedbackAPI {
    @GET("/api/feedback/{name}")
    Call<FeedbackResponse>  getFeedbacksByProduct(@Path("name") String name);

    @POST("/api/feedback/create_feedback")
    Call<BooleanResponse>  createFeedback (@Body FeedbackRequest feedbackRequest);
}
