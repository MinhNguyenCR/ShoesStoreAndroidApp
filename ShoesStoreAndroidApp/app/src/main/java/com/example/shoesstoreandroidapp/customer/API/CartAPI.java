package com.example.shoesstoreandroidapp.customer.API;

import com.example.shoesstoreandroidapp.customer.Request.CartItemUpdateRequest;
import com.example.shoesstoreandroidapp.customer.Request.LoginRequest;
import com.example.shoesstoreandroidapp.customer.Response.BooleanResponse;
import com.example.shoesstoreandroidapp.customer.Response.CartItemsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartAPI {
    @GET("shoestore/cart/{accountId}")
    Call<CartItemsResponse> getCartItems(@Path("accountId") Long accountId);

    @PUT("shoestore/cartItem")
    Call<BooleanResponse>  updateCartItems(@Body List<CartItemUpdateRequest> cartItemUpdateRequestList);

    @DELETE("shoestore/cart/delete/{cartItemId}")
    Call<BooleanResponse> deleteCartItemFromCart(@Path("cartItemId") Long cartItemId);

}
