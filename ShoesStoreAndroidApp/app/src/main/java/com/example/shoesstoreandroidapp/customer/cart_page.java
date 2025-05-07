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
import com.example.shoesstoreandroidapp.customer.Response.CartItemsResponse;

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
        long accountId = sharedPreferences.getLong("userId", 4);  // Mặc định trả về 4 nếu không có

        // Gọi API lấy danh sách sản phẩm trong giỏ hàng
        cartItemModelList = new ArrayList<>();
        cartAPI = RetrofitClient.getRetrofit().create(CartAPI.class);
        cartAPI.getCartItems(accountId).enqueue(new Callback<CartItemsResponse>() {
            @Override
            public void onResponse(Call<CartItemsResponse> call, Response<CartItemsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItemModelList.clear();
                    cartItemModelList.addAll(response.body().getResult());

                    // Gán adapter sau khi có dữ liệu
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
            ArrayList<CartItem> itemsToSend = new ArrayList<>();
            for (CartItemModel item : cartItemModelList) {
                int quantity = item.getQuantity();  // Dùng quantity từ model
                itemsToSend.add(new CartItem(item, quantity));
            }

           // double total = Double.parseDouble(totalMoney.getText().toString());

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



}