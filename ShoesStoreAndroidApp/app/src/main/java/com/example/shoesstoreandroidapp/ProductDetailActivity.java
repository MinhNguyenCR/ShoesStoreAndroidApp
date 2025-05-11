package com.example.shoesstoreandroidapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesstoreandroidapp.customer.API.FeedbackAPI;
import com.example.shoesstoreandroidapp.customer.API.ProductAPI;
import com.example.shoesstoreandroidapp.customer.Model.FeedbackModel;
import com.example.shoesstoreandroidapp.customer.Model.ProductDetailModel;
import com.example.shoesstoreandroidapp.customer.Response.FeedbackResponse;
import com.example.shoesstoreandroidapp.customer.Response.ProductDetailResponse;
import com.example.shoesstoreandroidapp.customer.RetrofitClient;
import com.example.shoesstoreandroidapp.customer.categoryAdapter;
import com.example.shoesstoreandroidapp.customer.sizeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private RecyclerView reviewRecyclerView;
    private RecyclerView sizeRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<ReviewModal> reviewList;
    private List<FeedbackModel> feedbackModelList;
    private ImageView imageView;
    private TextView nameTextView, priceTextView, descTextView;
    private RatingBar ratingBar;


    ProductAPI productAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ
        imageView = findViewById(R.id.imgShoe);
        nameTextView = findViewById(R.id.txtProductName);
        priceTextView = findViewById(R.id.txtPrice);
        descTextView = findViewById(R.id.txtDescription);
        ratingBar = findViewById(R.id.ratingBar);
        sizeRecyclerView = (RecyclerView)findViewById(R.id.sizeRecyclerView);
        reviewRecyclerView = (RecyclerView)findViewById(R.id.rc_reviews);
        TextView btnDecrease = findViewById(R.id.btnDecrease);
        TextView btnIncrease = findViewById(R.id.btnIncrease);
        TextView txtQuantity = findViewById(R.id.txtQuantity);


        // Lấy productName truyền từ ListProductAdapter
        String productName = getIntent().getStringExtra("productName");
        Log.d("ProductName", "Name: " + productName);

        // Gọi API hiển thị chi tiết sản phẩm
        productAPI = RetrofitClient.getRetrofit().create(ProductAPI.class);
        productAPI.getProductByName(productName).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductDetailModel productDetailModel = response.body().getResult();

                    nameTextView.setText(productDetailModel.getName());
                    priceTextView.setText(productDetailModel.getPrice() + " VND");
                    descTextView.setText(productDetailModel.getDescription());
                    float star = productDetailModel.getFeedbackStar();
                    ratingBar.setRating(star);


                    Glide.with(ProductDetailActivity.this).load(productDetailModel.getImage()).into(imageView);

                    // Hiển thị danh sách size
                    List<Integer> sizes = productDetailModel.getSize();
                    // Chuyển từ List size Integer thành String
                    List<String> sizeStrings = sizes.stream()
                            .map(String::valueOf)
                            .collect(Collectors.toList());

                    sizeAdapter adapter = new sizeAdapter(ProductDetailActivity.this, sizeStrings);
                    sizeRecyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    sizeRecyclerView.setAdapter(adapter);
                }
                else
                {
                    Log.e("Check", "body == null");
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show();
            }
        });

        // Lắng nghe sự kiện người dùng nhấn nút +
        btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
        });

        // Lắng nghe sự kiện người dùng nhấn nút -
        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });


        //Gọi API hiển thị đánh giá
        loadFeedbacks(productName);

    }

    private void loadFeedbacks(String productName) {
        FeedbackAPI feedbackAPI = RetrofitClient.getRetrofit().create(FeedbackAPI.class);

        feedbackAPI.getFeedbacksByProduct(productName).enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FeedbackResponse feedbackResponse = response.body();

                    if (feedbackResponse.getCode() == 1000) {
                        List<FeedbackModel> feedbackList = feedbackResponse.getResult();

                        // Log feedbacks
                        for (int i = 0; i < feedbackList.size(); i++) {
                            FeedbackModel fb = feedbackList.get(i);
                            Log.d("Feedback", "User: " + fb.getUser_name()
                                    + ", Rate: " + fb.getRate()
                                    + ", Comment: " + fb.getComment());
                        }

                         //Convert FeedbackModel -> ReviewModal
                        List<ReviewModal> reviewList = new ArrayList<>();
                        for (FeedbackModel fb : feedbackList) {
                            reviewList.add(new ReviewModal(
                                    fb.getUser_name(),
                                    fb.getRate().floatValue(),
                                    fb.getComment()
                            ));
                        }

                        // Hiển thị lên RecyclerView
                        reviewAdapter = new ReviewAdapter(ProductDetailActivity.this, reviewList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                        reviewRecyclerView.setLayoutManager(linearLayoutManager);
                        reviewRecyclerView.setAdapter(reviewAdapter);
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "No feedback found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to load feedback", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }


}