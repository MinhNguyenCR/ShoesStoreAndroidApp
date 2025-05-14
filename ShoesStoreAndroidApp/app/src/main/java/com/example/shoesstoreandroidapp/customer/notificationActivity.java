package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.UserProfileActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class notificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ImageButton imgbtnNoti, homeActive, imgbtnUser, imgbtnOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imgbtnNoti = findViewById(R.id.imgbtnNoti);
        imgbtnNoti.setImageResource(R.drawable.bell_icon_active);

        homeActive = findViewById(R.id.imgbtnHome);
        imgbtnUser = findViewById(R.id.imgbtnUser);
        imgbtnOrderHistory = findViewById(R.id.imgbtnOrderHistory);

        // Gọi API để lấy danh sách thông báo
        long userId = 4; // ⚠️ Đổi thành user thực tế
        NotificationApi api = RetrofitClient.getRetrofit().create(NotificationApi.class);
        api.getNotifications(userId).enqueue(new Callback<List<NotificationResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationResponse> notificationList = response.body();
                    adapter = new NotificationAdapter(notificationList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("NOTI_API", "Lỗi phản hồi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                Log.e("NOTI_API", "Lỗi gọi API: " + t.getMessage());
            }
        });

        // Sự kiện điều hướng
        homeActive.setOnClickListener(v -> startActivity(new Intent(this, main_page.class)));
        imgbtnUser.setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
        imgbtnOrderHistory.setOnClickListener(v -> startActivity(new Intent(this, PurchaseHistory.class)));
    }
}
