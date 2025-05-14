package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.customer.API.FeedbackAPI;
import com.example.shoesstoreandroidapp.customer.Adapter.ReviewProductAdapter;
import com.example.shoesstoreandroidapp.customer.Model.ReviewProductModel;
import com.example.shoesstoreandroidapp.customer.Request.FeedbackRequest;
import com.example.shoesstoreandroidapp.customer.Response.BooleanResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReviewProductAdapter adapter;
    private Button btnSubmit;
    private Long userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_product);

        recyclerView = findViewById(R.id.recyclerViewReviews);
        btnSubmit = findViewById(R.id.btnSubmitReview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", 4);

        // Nhận danh sách tên sản phẩm và hình ảnh từ Intent
        Intent intent = getIntent();
        ArrayList<String> productNamesList = intent.getStringArrayListExtra("productNamesList");
        ArrayList<String> productImagesList = intent.getStringArrayListExtra("productImagesList");

        if (productNamesList != null && productImagesList != null && !productNamesList.isEmpty()) {
            List<ReviewProductModel> productList = new ArrayList<>();

            for (int i = 0; i < productNamesList.size(); i++) {
                String productName = productNamesList.get(i);
                String imageUrl = productImagesList.get(i);
                productList.add(new ReviewProductModel(productName, imageUrl));
            }

            adapter = new ReviewProductAdapter(this, productList, userId);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Không có sản phẩm để hiển thị", Toast.LENGTH_SHORT).show();
        }


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReviews();
            }
        });
    }

    private void submitReviews() {
        List<FeedbackRequest> feedbackList = adapter.getFeedbackRequests();

        // Kiểm tra nếu bất kỳ feedback nào có rating = 0
        for (FeedbackRequest feedback : feedbackList) {
            if (feedback.getRate() == 0) {
                Toast.makeText(this, "Bạn chưa hoàn thành đánh giá cho tất cả sản phẩm!", Toast.LENGTH_SHORT).show();
                return; // Dừng lại, không gửi API
            }
        }

        FeedbackAPI feedbackAPI = RetrofitClient.getRetrofit().create(FeedbackAPI.class);
        OrderApi orderApi = RetrofitClient.getRetrofit().create(OrderApi.class);

        long orderId = getIntent().getLongExtra("orderId", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy mã đơn hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (feedbackList.isEmpty()) {
            Toast.makeText(this, "Không có nội dung đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        final int totalFeedbacks = feedbackList.size();
        final int[] successCount = {0};
        final boolean[] hasError = {false};

        for (FeedbackRequest feedback : feedbackList) {
            feedbackAPI.createFeedback(feedback).enqueue(new Callback<BooleanResponse>() {
                @Override
                public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                    if (response.isSuccessful() && response.body() != null ) {
                        successCount[0]++;
                    } else {
                        hasError[0] = true;
                    }

                    if (successCount[0] + (hasError[0] ? 1 : 0) == totalFeedbacks) {
                        if (!hasError[0]) {
                            // Tất cả feedback thành công, gọi cập nhật trạng thái đơn hàng
                            reviewOrder(orderId);
                        } else {
                            Toast.makeText(ReviewProductActivity.this, "Có lỗi trong quá trình đánh giá", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BooleanResponse> call, Throwable t) {
                    hasError[0] = true;

                    if (successCount[0] + 1 == totalFeedbacks) {
                        Toast.makeText(ReviewProductActivity.this, "Lỗi khi gửi đánh giá!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void reviewOrder(long orderId) {
        OrderApi orderApi = RetrofitClient.getRetrofit().create(OrderApi.class);
        orderApi.reviewOrder(orderId).enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ReviewProductActivity.this, "Đánh giá đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình trước


                } else {
                    Toast.makeText(ReviewProductActivity.this, "Xảy ra lỗi trong quá trình nhận đánh giá!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable t) {
                Toast.makeText(ReviewProductActivity.this, "Lỗi kết nối khi đánh dấu đơn hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}