package com.example.shoesstoreandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shoesstoreandroidapp.customer.API.AccountAPI;
import com.example.shoesstoreandroidapp.customer.AccountDetailRequest;
import com.example.shoesstoreandroidapp.customer.ApiResponse;
import com.example.shoesstoreandroidapp.customer.LoginActivity;
import com.example.shoesstoreandroidapp.customer.PurchaseHistory;
import com.example.shoesstoreandroidapp.customer.Response.UserDetailResponse;
import com.example.shoesstoreandroidapp.customer.RetrofitClient;
import com.example.shoesstoreandroidapp.customer.main_page;
import com.example.shoesstoreandroidapp.customer.notificationActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    ImageButton imgbtnUser;
    ImageButton imgbtnHome;
    ImageView imgAvatar, logoutBtn;
    ImageButton imgbtnNoti;
    ImageButton imgbtnOrderHistory;
    private EditText edtFullName, edtPhone, edtDob;
    private Button btnUpdateProfile;
    private static final int REQUEST_IMAGE_PICK = 1002;

    private AccountAPI userApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", 4);
        thamchieu();
        fillInBlank(userId);

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

//        userApi = RetrofitClient.getRetrofit().create(AccountApi.class);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtFullName.getText().toString();
                String phone = edtPhone.getText().toString();
                String dobString = edtDob.getText().toString();

                    AccountDetailRequest accountDetailRequest = new AccountDetailRequest(name, phone, dobString);
                    updateUserDetail(userId, accountDetailRequest);


            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                long accountId = sharedPreferences.getLong("userId", 4);
                AccountAPI accountAPI = RetrofitClient.getRetrofit().create(AccountAPI.class);
                accountAPI.getImageUrl(userId).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Glide.with(UserProfileActivity.this)
                                .load(response)
                                .into(imgAvatar);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("userId");
                editor.apply();

                // Thông báo người dùng đã đăng xuất thành công
                Toast.makeText(UserProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Chuyển hướng người dùng đến màn hình đăng nhập
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);

                // Kết thúc Activity hiện tại (để người dùng không thể quay lại bằng nút back)
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("userId", 4);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                new UploadAvatarTask(this, accountId, imageUri, avatarUrl -> {
                    Picasso.get().load(avatarUrl).into(imgAvatar);
                }).execute();
            }
        }
    }

//    private void uploadImageToCloudinary(Uri imageUri) {
//        // Chuyển URI thành Bitmap
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//
//            // Upload lên Cloudinary
//            new UploadImageTask(this).execute(bitmap);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void fillInBlank(long userId){
        userApi = RetrofitClient.getRetrofit().create(AccountAPI.class);
        userApi.getUserDetailByUserId(userId).enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                if (response.isSuccessful()) {
                    UserDetailResponse userDetailResponse = response.body();
                    Glide.with(UserProfileActivity.this)
                            .load(userDetailResponse.getImage())
                            .into(imgAvatar);
                    edtFullName.setText(userDetailResponse.getName());
                    edtPhone.setText(userDetailResponse.getNumber());
                    Log.d("CCC","name "+  userDetailResponse.getName());
                    Log.d("CCC","name "+  userDetailResponse.getNumber());
                    Log.d("CCC","name "+  userDetailResponse.getBirthday());
                    if (userDetailResponse.getBirthday() != null) {
                        DateTimeFormatter formatter = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate dob = LocalDate.parse(userDetailResponse.getBirthday(), formatter);

                            edtDob.setText(dob.format(formatter));
                        }

                    }

                    Log.d("CCC", "Error response: " + response.errorBody());


                } else {
                    Toast.makeText(UserProfileActivity.this, "Profile fail to be filled", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Profile fail to be filled", Toast.LENGTH_LONG).show();
                Log.e("Retrofit Error", "API Call Failed: " + t.getMessage());
                Toast.makeText(UserProfileActivity.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    private void updateUserDetail(long id, AccountDetailRequest accountDetailRequest) {
        Log.d("UserProfile", "Updating details for user ID: " + id);
        Log.d("UserProfile", "New Details: " + accountDetailRequest);

        userApi = RetrofitClient.getRetrofit().create(AccountAPI.class);
        userApi.updateUserDetails(id, accountDetailRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserProfileActivity.this, "Profile updated successfully", Toast.LENGTH_LONG).show();
                } else {
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
        imgAvatar = findViewById(R.id.imgAvatar);
        logoutBtn = findViewById(R.id.logoutBtn);
    }
}