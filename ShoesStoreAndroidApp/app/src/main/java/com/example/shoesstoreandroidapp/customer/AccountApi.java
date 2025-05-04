package com.example.shoesstoreandroidapp.customer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AccountApi {
    @PUT("/shoestore/account/update_accountDetail/{id}")
    Call<ApiResponse<Boolean>> updateUserDetails(
            @Path("id") long userId,
            @Body AccountDetailRequest accountDetailRequest
    );
}
