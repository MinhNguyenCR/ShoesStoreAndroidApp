package com.example.shoesstoreandroidapp.customer.API;

import com.example.shoesstoreandroidapp.customer.AccountDetailRequest;
import com.example.shoesstoreandroidapp.customer.ApiResponse;
import com.example.shoesstoreandroidapp.customer.Request.AccountSignUpRequest;
import com.example.shoesstoreandroidapp.customer.Request.LoginRequest;
import com.example.shoesstoreandroidapp.customer.Request.OTPRequest;
import com.example.shoesstoreandroidapp.customer.Response.LoginResponse;
import com.example.shoesstoreandroidapp.customer.Response.UserDetailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AccountAPI {
        @POST("/shoestore/account/login")
        Call<LoginResponse> login(@Body LoginRequest request);
        @POST("/shoestore/account/register")
        Call<ApiResponse<Boolean>> register(@Body AccountSignUpRequest accountSignUpRequest);
        @POST("/shoestore/otp/generate/{email}")
        Call<ApiResponse<String>> generateOtp(@Path("email") String email);
        @POST("/shoestore/otp/verifyOTP")
        Call<ApiResponse<Boolean>> verifyOtp(@Body OTPRequest otpRequest);
        @GET("/shoestore/account/userDetail/{id}")
        Call<UserDetailResponse> getUserDetailByUserId(@Path("id") Long id);
        @PUT("/shoestore/account/update_accountDetail/{id}")
        Call<ApiResponse<Boolean>> updateUserDetails(
                @Path("id") long userId,
                @Body AccountDetailRequest accountDetailRequest
        );
        @POST("/shoestore/account/forget-password")
        Call<ApiResponse<Boolean>> resetPassword(@Body AccountSignUpRequest accountSignUpRequest);
}
