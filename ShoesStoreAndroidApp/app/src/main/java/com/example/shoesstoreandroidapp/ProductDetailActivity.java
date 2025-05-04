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
import com.example.shoesstoreandroidapp.customer.API.ProductAPI;
import com.example.shoesstoreandroidapp.customer.Model.ProductDetailModel;
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


        String productName = getIntent().getStringExtra("productName");
        Log.d("ProductName", "Name: " + productName);
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


        reviewRecyclerView = (RecyclerView)findViewById(R.id.rc_reviews);
//        recyclerReviews.setLayoutManager(new LinearLayoutManager(this));

        reviewList = new ArrayList<>();
        reviewList.add(new ReviewModal("John Doe", 4.5f, "Great quality! Very comfortable to wear."));
        reviewList.add(new ReviewModal("Alice Smith", 5.0f, "Absolutely love it! Highly recommend."));
        reviewList.add(new ReviewModal("Michael Lee", 3.5f, "Good shoes but a bit expensive."));
        reviewList.add(new ReviewModal("Emma Wilson", 4.0f, "Nice design, fits well."));
        reviewList.add(new ReviewModal("John Doe", 4.5f, "Great quality! Very comfortable to wear."));
        reviewList.add(new ReviewModal("Alice Smith", 5.0f, "Absolutely love it! Highly recommend."));
        reviewList.add(new ReviewModal("Michael Lee", 3.5f, "Good shoes but a bit expensive."));
        reviewList.add(new ReviewModal("Emma Wilson", 4.0f, "Nice design, fits well."));



        reviewAdapter = new ReviewAdapter(this, reviewList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(linearLayoutManager);
        reviewRecyclerView.setAdapter(reviewAdapter);

    }
}