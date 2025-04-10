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

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.UserProfileActivity;

public class PurchaseHistory extends AppCompatActivity {
    ImageButton imgbtnOrderHistory;
    ImageButton imgbtnUser;
    ImageButton imgbtnHome;
    ImageButton imgbtnNoti;

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
    }
    private void thamchieu(){
        imgbtnOrderHistory = findViewById(R.id.imgbtnOrderHistory);
        imgbtnUser = findViewById(R.id.imgbtnUser);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnNoti = findViewById(R.id.imgbtnNoti);

    }
}