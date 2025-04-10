package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

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

    Button process_payment;
    TextView totalMoney;
    RadioButton radioCOD;
    RadioButton radioVNPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout_page);
        RecyclerView rvProductList = findViewById(R.id.rvProductList);
        radioCOD = findViewById(R.id.radioCOD);
        radioVNPay = findViewById(R.id.radioVNPay);
        //List<Product> productList = new ArrayList<>();
        ArrayList<CartItem> receivedItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");

        List<Product> productList = new ArrayList<>();
        for (CartItem cartItem : receivedItems) {
            productList.add(new Product(
                    cartItem.getProduct().getName(),
                    cartItem.getQuantity(),
                    (int) cartItem.getProduct().getPrice()
            ));
        }
        totalMoney = findViewById(R.id.totalMoney);
        double total = getIntent().getDoubleExtra("totalMoney", 0); // 👈 Nhận tổng tiền
        totalMoney.setText(String.format("%,.0f", total));



        ProductAdapter adapter = new ProductAdapter(productList);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        rvProductList.setAdapter(adapter);

        process_payment = findViewById(R.id.processPaymentBtn);
        process_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioVNPay.isChecked()){
                    Intent intent = new Intent(checkout_page.this, PaymentActivity.class);
                    intent.putExtra("total", total);
                    startActivity(intent);
                }
                if(radioCOD.isChecked()){
                    Intent intent = new Intent(checkout_page.this, PaymentSuccess.class);
                    intent.putExtra("total", total);
                    startActivity(intent);
                }
            }
        });



    }
}