package com.example.shoesstoreandroidapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerReviews;
    private ReviewAdapter reviewAdapter;
    private List<ReviewModal> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        recyclerReviews = (RecyclerView)findViewById(R.id.rc_reviews);
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
        recyclerReviews.setLayoutManager(linearLayoutManager);
        recyclerReviews.setAdapter(reviewAdapter);

    }
}