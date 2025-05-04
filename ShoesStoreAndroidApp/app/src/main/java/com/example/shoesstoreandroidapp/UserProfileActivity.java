package com.example.shoesstoreandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstoreandroidapp.customer.AccountApi;
import com.example.shoesstoreandroidapp.customer.AccountDetailRequest;
import com.example.shoesstoreandroidapp.customer.ApiResponse;
import com.example.shoesstoreandroidapp.customer.OrderHistoryModel;
import com.example.shoesstoreandroidapp.customer.PurchaseHistory;
import com.example.shoesstoreandroidapp.customer.RetrofitClient;
import com.example.shoesstoreandroidapp.customer.main_page;
import com.example.shoesstoreandroidapp.customer.notificationActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    ImageButton imgbtnUser;
    ImageButton imgbtnHome;
    ImageButton imgbtnNoti;
    ImageButton imgbtnOrderHistory;
    private EditText edtFullName, edtPhone, edtDob;
    private Button btnUpdateProfile;
    private long userId = 6;
    private AccountApi userApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        thamchieu();
        imgbtnUser.setImageResource(R.drawable.user_icon_active);

        imgbtnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, notificationActivity.class);
                startActivity(intent);
            }
        });
        imgbtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, main_page.class);
                startActivity(intent);
            }
        });
        imgbtnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, PurchaseHistory.class);
                startActivity(intent);
            }
        });

        userApi = RetrofitClient.getRetrofit().create(AccountApi.class);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtFullName.getText().toString();
                String phone = edtPhone.getText().toString();
                String dobString = edtDob.getText().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

                try {
                    LocalDate dob = LocalDate.parse(dobString, formatter);
                    AccountDetailRequest accountDetailRequest = new AccountDetailRequest(name, phone, dob);
                    updateUserDetail(userId, accountDetailRequest);
                } catch (DateTimeParseException e) {
                    Toast.makeText(UserProfileActivity.this, "Invalid date format. Please use yyyy/MM/dd.", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void updateUserDetail(long id, AccountDetailRequest accountDetailRequest) {
        Log.d("UserProfile", "Updating details for user ID: " + id);
        Log.d("UserProfile", "New Details: " + accountDetailRequest);

        userApi.updateUserDetails(id, accountDetailRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserProfileActivity.this, "Profile updated successfully", Toast.LENGTH_LONG).show();
                } else {
                    // Đọc lỗi từ phản hồi và hiển thị
                    String errorMessage = "Failed to update profile";
                    int code =0;
                    if (response.body() != null && response.body().getResult() != null) {
                        code = response.body().getCode();
                    }
                    Toast.makeText(UserProfileActivity.this, errorMessage + " "+ code, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Log.e("Retrofit Error", "API Call Failed: " + t.getMessage());
                Toast.makeText(UserProfileActivity.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void thamchieu(){
        imgbtnUser = findViewById(R.id.imgbtnUser);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnNoti = findViewById(R.id.imgbtnNoti);
        imgbtnOrderHistory = findViewById(R.id.imgbtnOrderHistory);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtDob = findViewById(R.id.edtDob);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
    }
}