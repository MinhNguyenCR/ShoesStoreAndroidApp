package com.example.shoesstoreandroidapp.customer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;

import java.util.ArrayList;
import java.util.List;

public class checkout_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout_page);
        RecyclerView rvProductList = findViewById(R.id.rvProductList);
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Nike Mercurial Superfly 9 Elite SE", 10, 7239000));
        productList.add(new Product("Adidas Predator Accuracy", 2, 4000000));
        productList.add(new Product("Puma Ultra Ultimate", 1, 3100000));

        ProductAdapter adapter = new ProductAdapter(productList);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        rvProductList.setAdapter(adapter);

    }
}