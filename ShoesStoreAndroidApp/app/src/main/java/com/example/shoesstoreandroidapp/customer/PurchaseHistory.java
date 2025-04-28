package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryModelList = new ArrayList<>();
        orderHistoryModelList.add(new OrderHistoryModel("1928172", "140000đ", "Oct 20, 2024 08:29",
                "3x Local Avocado, 5x Fresh Bananas..",
                "484 Lê Văn Việt, Phường Tăng Nhơn Phú A, Quận 9, HCM"));

        orderHistoryModelList.add(new OrderHistoryModel("1928173", "230000đ", "Oct 21, 2024 09:45",
                "2x Fresh Mango, 1x Organic Orange..",
                "100 Nguyễn Huệ, Quận 1, HCM"));

        orderHistoryAdapter = new OrderHistoryAdapter(this, orderHistoryModelList);
        recyclerView.setAdapter(orderHistoryAdapter);
    }
    private void thamchieu(){
        imgbtnOrderHistory = findViewById(R.id.imgbtnOrderHistory);
        imgbtnUser = findViewById(R.id.imgbtnUser);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnNoti = findViewById(R.id.imgbtnNoti);

        recyclerView = findViewById(R.id.rcvOrderHistory);
    }
}