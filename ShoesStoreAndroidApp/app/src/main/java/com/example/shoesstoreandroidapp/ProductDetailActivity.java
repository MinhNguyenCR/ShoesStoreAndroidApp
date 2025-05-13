package com.example.shoesstoreandroidapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesstoreandroidapp.customer.API.CartAPI;
import com.example.shoesstoreandroidapp.customer.API.FeedbackAPI;
import com.example.shoesstoreandroidapp.customer.API.ProductAPI;
import com.example.shoesstoreandroidapp.customer.Model.FeedbackModel;
import com.example.shoesstoreandroidapp.customer.Model.ProductDetailModel;
import com.example.shoesstoreandroidapp.customer.Request.AddToCartRequest;
import com.example.shoesstoreandroidapp.customer.Response.BooleanResponse;
import com.example.shoesstoreandroidapp.customer.Response.FeedbackResponse;
import com.example.shoesstoreandroidapp.customer.Response.ProductDetailResponse;
import com.example.shoesstoreandroidapp.customer.RetrofitClient;
import com.example.shoesstoreandroidapp.customer.sizeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameTextView, priceTextView, descTextView, txtQuantity;
    private RatingBar ratingBar;
    private RecyclerView reviewRecyclerView, sizeRecyclerView;
    private Button btnAddToCart;
    private TextView btnIncrease, btnDecrease;

    private ReviewAdapter reviewAdapter;
    private ProductAPI productAPI;
    private CartAPI cartAPI;
    private long productId;
    private int selectedSize = 0; // Mặc định size là 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        initViews();

        String productName = getIntent().getStringExtra("productName");
        if (productName != null) {
            loadProductDetails(productName);
            loadFeedbacks(productName);
        } else {
            Toast.makeText(this, "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
        }

        handleQuantityButtons();
        handleAddToCart();
    }

    private void initViews() {
        imageView = findViewById(R.id.imgShoe);
        nameTextView = findViewById(R.id.txtProductName);
        priceTextView = findViewById(R.id.txtPrice);
        descTextView = findViewById(R.id.txtDescription);
        ratingBar = findViewById(R.id.ratingBar);
        sizeRecyclerView = findViewById(R.id.sizeRecyclerView);
        reviewRecyclerView = findViewById(R.id.rc_reviews);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        txtQuantity = findViewById(R.id.txtQuantity);
    }

    private void loadProductDetails(String productName) {
        productAPI = RetrofitClient.getRetrofit().create(ProductAPI.class);
        productAPI.getProductByName(productName).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductDetailModel product = response.body().getResult();
                    if (product != null) {
                        productId = product.getId();
                        displayProductInfo(product);
                        setupSizeRecyclerView(product.getSize());
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không tìm thấy chi tiết sản phẩm!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProductInfo(ProductDetailModel product) {
        nameTextView.setText(product.getName());
        priceTextView.setText(product.getPrice() + " VND");
        descTextView.setText(product.getDescription());
        ratingBar.setRating(product.getFeedbackStar());
        Glide.with(this).load(product.getImage()).into(imageView);
    }

    private void setupSizeRecyclerView(List<Integer> sizes) {
        List<String> sizeStrings = sizes.stream().map(String::valueOf).collect(Collectors.toList());
        sizeAdapter adapter = new sizeAdapter(this, sizeStrings, selectedSizeStr -> {
            selectedSize = Integer.parseInt(selectedSizeStr);
        });
        sizeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sizeRecyclerView.setAdapter(adapter);
    }

    private void handleQuantityButtons() {
        btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            txtQuantity.setText(String.valueOf(quantity + 1));
        });

        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            if (quantity > 1) {
                txtQuantity.setText(String.valueOf(quantity - 1));
            }
        });
    }

    private void handleAddToCart() {
        btnAddToCart.setOnClickListener(v -> {
            if (selectedSize == 0) {
                Toast.makeText(this, "Vui lòng chọn size!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            long accountId = prefs.getLong("userId", 4); // mặc định 4

            int quantity = Integer.parseInt(txtQuantity.getText().toString());

            AddToCartRequest request = new AddToCartRequest(accountId, productId, selectedSize, quantity);
            cartAPI = RetrofitClient.getRetrofit().create(CartAPI.class);
            cartAPI.addToCart(request).enqueue(new Callback<BooleanResponse>() {
                @Override
                public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getResult()) {
                        Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Không thể thêm vào giỏ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BooleanResponse> call, Throwable t) {
                    Toast.makeText(ProductDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadFeedbacks(String productName) {
        FeedbackAPI feedbackAPI = RetrofitClient.getRetrofit().create(FeedbackAPI.class);
        feedbackAPI.getFeedbacksByProduct(productName).enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1000) {
                    List<FeedbackModel> feedbackList = response.body().getResult();
                    List<ReviewModal> reviews = new ArrayList<>();
                    for (FeedbackModel fb : feedbackList) {
                        reviews.add(new ReviewModal(fb.getUser_name(), fb.getRate().floatValue(), fb.getComment()));
                    }

                    reviewAdapter = new ReviewAdapter(ProductDetailActivity.this, reviews);
                    reviewRecyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this));
                    reviewRecyclerView.setAdapter(reviewAdapter);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không có đánh giá nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }
}
