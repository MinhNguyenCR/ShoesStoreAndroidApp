package com.example.shoesstoreandroidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.shoesstoreandroidapp.customer.API.AccountAPI;
import com.example.shoesstoreandroidapp.customer.ApiResponse;
import com.example.shoesstoreandroidapp.customer.Request.ImgRequest;
import com.example.shoesstoreandroidapp.customer.RetrofitClient;

import java.io.InputStream;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadAvatarTask extends AsyncTask<Void, Void, String> {
    private Context context;
    private Long id;
    private Uri imageUri;
    private OnAvatarUploadSuccessListener listener;

    public interface OnAvatarUploadSuccessListener {
        void onAvatarUploadSuccess(String avatarUrl);
    }

    public UploadAvatarTask(Context context, Long id, Uri imageUri, OnAvatarUploadSuccessListener listener) {
        this.context = context;
        this.id = id;
        this.imageUri = imageUri;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dtlkcmwdm",
                    "api_key", "536931462984651",
                    "api_secret", "behdOs0wRtdiYp8i7z_gTOCHslE"
            ));

            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Map uploadResult = cloudinary.uploader().upload(inputStream, ObjectUtils.asMap(
                    "resource_type", "image",
                    "folder", "avatars"
            ));

            return (String) uploadResult.get("secure_url");

        } catch (Exception e) {
            Log.e("UploadAvatarTask", "Upload error", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String avatarUrl) {
        super.onPostExecute(avatarUrl);

        if (avatarUrl != null) {
            if (avatarUrl != null) {
                Log.d("UploadImageTask", "Image URL: " + avatarUrl);

                saveImageUrlToDatabase(avatarUrl);
                Toast.makeText(context, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();

                if (listener != null) {
                    listener.onAvatarUploadSuccess(avatarUrl);
                }
            } else {
                Toast.makeText(context, "Cập nhật ảnh đại diện thất bại", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void saveImageUrlToDatabase(String imageUrl) {
        ImgRequest imageRequest = new ImgRequest();
        imageRequest.setImage(imageUrl);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", 4);

        // Tạo Retrofit service để lưu URL vào MySQL
        AccountAPI accountAPI = RetrofitClient.getRetrofit().create(AccountAPI.class);
        accountAPI.uploadImage(userId, imageRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    // Kiểm tra kết quả
                    ApiResponse<Boolean> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getResult()) {
                        Toast.makeText(context, "Image URL saved to database", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to save image URL", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}