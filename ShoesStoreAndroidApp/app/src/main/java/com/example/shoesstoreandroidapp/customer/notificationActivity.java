package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.UserProfileActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class notificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;
    private SharedPreferences sharedPreferences;
    private ImageButton imgbtnNoti;
    private Gson gson;
    private ImageButton homeActive;
    private ImageButton imgbtnOrderHistory;
    ImageButton imgbtnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        // Khởi tạo SharedPreferences và Gson
        sharedPreferences = getSharedPreferences("notification_prefs", MODE_PRIVATE);
        gson = new Gson();

        // Ánh xạ view
        imgbtnNoti = findViewById(R.id.imgbtnNoti); // Đảm bảo ID đúng trong activity_notification.xml
        imgbtnNoti.setImageResource(R.drawable.bell_icon_active);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách thông báo từ SharedPreferences
        notificationList = getNotifications();

        // Gắn adapter
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        homeActive = findViewById(R.id.imgbtnHome);
        homeActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notificationActivity.this, main_page.class);
                startActivity(intent);
            }
        });

        imgbtnUser = findViewById(R.id.imgbtnUser);
        imgbtnOrderHistory = findViewById(R.id.imgbtnOrderHistory);

        imgbtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notificationActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        imgbtnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notificationActivity.this, PurchaseHistory.class);
                startActivity(intent);
            }
        });
    }

    private List<Notification> getNotifications() {
        String json = sharedPreferences.getString("notifications", null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Notification>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private void saveNotifications(List<Notification> list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(list);
        editor.putString("notifications", json);
        editor.apply();
    }
}
