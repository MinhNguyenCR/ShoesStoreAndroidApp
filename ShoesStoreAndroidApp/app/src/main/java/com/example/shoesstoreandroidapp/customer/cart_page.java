package com.example.shoesstoreandroidapp.customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.customer.API.CartAPI;
import com.example.shoesstoreandroidapp.customer.Model.CartItemModel;
import com.example.shoesstoreandroidapp.customer.Request.CartItemUpdateRequest;
import com.example.shoesstoreandroidapp.customer.Response.BooleanResponse;
import com.example.shoesstoreandroidapp.customer.Response.CartItemsResponse;
import com.example.shoesstoreandroidapp.customer.Response.ErrorResponse;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cart_page extends AppCompatActivity {
    RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItemModel> cartItemModelList;
    private TextView totalMoney;
    private TextView itemCount;
    private Button goToCheckOut;
    CartAPI cartAPI;
    private double totalAmountValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        recyclerView = findViewById(R.id.cartItemsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        totalMoney = findViewById(R.id.totalMoney);
        itemCount = findViewById(R.id.cartItemCount);
        goToCheckOut = findViewById(R.id.makePaymentButton);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("userId", 4);

        cartItemModelList = new ArrayList<>();
        cartAPI = RetrofitClient.getRetrofit().create(CartAPI.class);
        cartAPI.getCartItems(accountId).enqueue(new Callback<CartItemsResponse>() {
            @Override
            public void onResponse(Call<CartItemsResponse> call, Response<CartItemsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItemModelList.clear();
                    cartItemModelList.addAll(response.body().getResult());
                    cartAdapter = new CartAdapter(cart_page.this, cartItemModelList);

                    cartAdapter.setOnQuantityChangeListener(() -> CalcTotalMoney());
                    cartAdapter.setOnDeletedChangeListener(position -> {
                        cartItemModelList.remove(position);
                        cartAdapter.notifyItemRemoved(position);
                        CalcTotalMoney();
                    });

                    recyclerView.setAdapter(cartAdapter);
                    CalcTotalMoney();
                } else {
                    Toast.makeText(getApplicationContext(), "Không tải được giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartItemsResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        goToCheckOut.setOnClickListener(v -> {
            updateCartToServer(new UpdateCartCallback() {
                @Override
                public void onSuccess() {
                    ArrayList<CartItem> itemsToSend = new ArrayList<>();
                    for (CartItemModel item : cartItemModelList) {
                        int quantity = item.getQuantity();
                        itemsToSend.add(new CartItem(item, quantity));
                    }
                    Intent intent = new Intent(cart_page.this, checkout_page.class);
                    intent.putExtra("cartItems", itemsToSend);
                    intent.putExtra("totalMoney", totalAmountValue);
                    startActivity(intent);
                }

                @Override
                public void onFailure(String message) {
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void CalcTotalMoney() {
        double total = 0;
        int itemCounter = 0;
        for (CartItemModel item : cartItemModelList) {
            int quantity = item.getQuantity();
            double unitPrice = item.getTotal_price();
            total += unitPrice * quantity;
            itemCounter += quantity;
        }
        totalAmountValue = total;
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        totalMoney.setText(formatter.format(total) + " VND");
        itemCount.setText(String.valueOf(itemCounter));
    }

    @Override
    public void onBackPressed() {
        cart_page.super.onBackPressed();
        updateCartToServer(new UpdateCartCallback() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onFailure(String message) {
                //showDetailedErrorDialog(message);
            }

        });

    }

    @Override
    protected void onDestroy() {
        updateCartToServer(new UpdateCartCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(String message) {

            }
        });
        super.onDestroy();
    }

    private void updateCartToServer(UpdateCartCallback callback) {
        ArrayList<CartItemUpdateRequest> cartItemUpdateRequestList = new ArrayList<>();
        for (CartItemModel item : cartItemModelList) {
            cartItemUpdateRequestList.add(new CartItemUpdateRequest(item.getId(), item.getQuantity()));
        }

        cartAPI.updateCartItems(cartItemUpdateRequestList).enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 1000) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Lỗi cập nhật giỏ hàng");
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            int errorCode = errorResponse.getCode();
                            String errorMessage = errorResponse.getMessage();
                            if (errorCode == 2007) {
                                showDetailedErrorDialog(errorMessage); // Sử dụng hộp thoại
                                callback.onFailure(errorMessage);
                            } else if (errorCode == 2005) {
                                callback.onFailure("Không tìm thấy sản phẩm trong giỏ hàng");
                            } else {
                                callback.onFailure("Lỗi: " + errorMessage);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure("Lỗi không xác định");
                    }
                }
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                callback.onFailure("Lỗi: " + t.getMessage());
            }
        });
    }

    private void showDetailedErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Lỗi cập nhật giỏ hàng")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }


}