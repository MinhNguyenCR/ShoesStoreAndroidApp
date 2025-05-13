package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

        // Khởi tạo View
        recyclerView = findViewById(R.id.cartItemsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        totalMoney = findViewById(R.id.totalMoney);
        itemCount = findViewById(R.id.cartItemCount);
        goToCheckOut = findViewById(R.id.makePaymentButton);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("userId", 4);

        // Gọi API lấy danh sách sản phẩm trong giỏ hàng
        cartItemModelList = new ArrayList<>();
        cartAPI = RetrofitClient.getRetrofit().create(CartAPI.class);
        cartAPI.getCartItems(accountId).enqueue(new Callback<CartItemsResponse>() {
            @Override
            public void onResponse(Call<CartItemsResponse> call, Response<CartItemsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItemModelList.clear();
                    cartItemModelList.addAll(response.body().getResult());
                    cartAdapter = new CartAdapter(cart_page.this, cartItemModelList);

                    // Xử lý khi số lượng thay đổi
                    cartAdapter.setOnQuantityChangeListener(() -> CalcTotalMoney());

                    // Xử lý khi xóa sản phẩm
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

        // Xử lý nút thanh toán
        goToCheckOut.setOnClickListener(v -> {
            // Cập nhật lại quantity của mỗi CartItem
            updateCartToServer();
            // Thành công, chuyển sang trang thanh toán
            ArrayList<CartItem> itemsToSend = new ArrayList<>();
            for (CartItemModel item : cartItemModelList) {
                int quantity = item.getQuantity();
                Long id = item.getId();
                itemsToSend.add(new CartItem(item, quantity));
            }
            Intent intent = new Intent(cart_page.this, checkout_page.class);
            intent.putExtra("cartItems", itemsToSend);
            intent.putExtra("totalMoney", totalAmountValue);
            startActivity(intent);
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

        totalAmountValue = total; // lưu lại giá trị thực sự để truyền sang check_out page

        DecimalFormat formatter = new DecimalFormat("#,###.##");
        String formattedTotal = formatter.format(total);
        totalMoney.setText(formattedTotal + " VND");
        itemCount.setText(String.valueOf(itemCounter));
    }

    @Override
    public void onBackPressed() {
        updateCartToServer(); // Gửi cập nhật lên server
        super.onBackPressed(); // Quay về Activity trước đó
    }

    @Override
    protected void onDestroy() {
        updateCartToServer(); // Gửi cập nhật trước khi bị huỷ
        super.onDestroy();
    }

    // Cập nhật lại quantity của mỗi CartItem
    private void updateCartToServer() {
        ArrayList<CartItemUpdateRequest> cartItemUpdateRequestList = new ArrayList<>();
        for (CartItemModel item : cartItemModelList) {
            int quantity = item.getQuantity();
            Long id = item.getId();
            cartItemUpdateRequestList.add(new CartItemUpdateRequest(id, quantity));
        }

        // Cập nhật lại số lượng mỗi CartItem dưới csdl
        cartAPI.updateCartItems(cartItemUpdateRequestList).enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getCode() == 1000) {
                        // Thành công, xử lý tiếp...
                    }
                } else {
                    // Nếu response không thành công, parse lỗi từ errorBody
                    try {
                        // Nếu không có body, nghĩa là đã xảy ra lỗi
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);

                        if (errorResponse != null) {
                            int errorCode = errorResponse.getCode();
                            String errorMessage = errorResponse.getMessage();

                            // Xử lý thông báo lỗi
                            if (errorCode == 2007) {
                                Toast.makeText(getApplicationContext(), "Số lượng hàng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                            } else if (errorCode == 2005) {
                                Toast.makeText(getApplicationContext(), "Không tìm thấy sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}