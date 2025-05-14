package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseHistory extends AppCompatActivity {
    ImageButton imgbtnOrderHistory;
    ImageButton imgbtnUser;
    ImageButton imgbtnHome;
    ImageButton imgbtnNoti;

    private RecyclerView recyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<OrderHistoryModel> orderHistoryModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase_history);
        thamchieu();
        imgbtnOrderHistory.setImageResource(R.drawable.list_icon_active);
        imgbtnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseHistory.this, notificationActivity.class);
                startActivity(intent);
            }
        });
        imgbtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseHistory.this, main_page.class);
                startActivity(intent);
            }
        });
        imgbtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseHistory.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", 4);

        OrderApi orderApi = RetrofitClient.getRetrofit().create(OrderApi.class);
        OrderHistoryAdapter adapter = new OrderHistoryAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        orderApi.getOrderHistory(userId).enqueue(new Callback<ApiResponse<List<OrderHistoryResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<OrderHistoryResponse>>> call, Response<ApiResponse<List<OrderHistoryResponse>>> response) {
                if(response.isSuccessful() && response.body() != null)
                    adapter.setOrderList(response.body().getResult());
            }

            @Override
            public void onFailure(Call<ApiResponse<List<OrderHistoryResponse>>> call, Throwable t) {
                Log.e("ORDER_API", "Lỗi: " + t.getMessage());
                Toast.makeText(PurchaseHistory.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void thamchieu(){
        imgbtnOrderHistory = findViewById(R.id.imgbtnOrderHistory);
        imgbtnUser = findViewById(R.id.imgbtnUser);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnNoti = findViewById(R.id.imgbtnNoti);

        recyclerView = findViewById(R.id.rcvOrderHistory);
    }
}