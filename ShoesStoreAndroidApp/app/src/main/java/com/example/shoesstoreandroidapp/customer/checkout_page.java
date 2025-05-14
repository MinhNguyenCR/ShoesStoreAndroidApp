package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.customer.Request.NotificationRequest;
import com.example.shoesstoreandroidapp.customer.Request.OrderResquest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class checkout_page extends AppCompatActivity {

    Button process_payment;
    TextView totalMoney;
    RadioButton radioCOD;
    RadioButton radioVNPay;
    EditText edtProvince, edtFullname, edtPhone, edtDistrict, edtWard, edtCommute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout_page);
        thamchieu();
        RecyclerView rvProductList = findViewById(R.id.rvProductList);
        radioCOD = findViewById(R.id.radioCOD);
        radioVNPay = findViewById(R.id.radioVNPay);
        //List<Product> productList = new ArrayList<>();
        ArrayList<CartItem> receivedItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");

        List<Product> productList = new ArrayList<>();
        for (CartItem cartItem : receivedItems) {
            productList.add(new Product(
                    cartItem.getProduct().getProductName(),
                    cartItem.getProduct().getSize(),
                    cartItem.getQuantity(),
                    (int) cartItem.getProduct().getTotal_price()
            ));
        }
        totalMoney = findViewById(R.id.totalMoney);
        double total = getIntent().getDoubleExtra("totalMoney", 0);
        totalMoney.setText(String.format("%,.0f", total));



        ProductAdapter adapter = new ProductAdapter(productList);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        rvProductList.setAdapter(adapter);

        process_payment = findViewById(R.id.processPaymentBtn);
        process_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderResquest orderResquest = new OrderResquest();
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                long userId = sharedPreferences.getLong("userId", 4);
                orderResquest.setAccountId(userId);
                orderResquest.setCommune(edtCommute.getText().toString().trim());
                orderResquest.setName(edtFullname.getText().toString().trim());
                orderResquest.setDistrict(edtDistrict.getText().toString().trim());
                orderResquest.setPhone(edtPhone.getText().toString().trim());
                orderResquest.setDetailedAddress(edtWard.getText().toString().trim());
                orderResquest.setProvince(edtProvince.getText().toString().trim());
                if(radioVNPay.isChecked()){
                    orderResquest.setPaymentMethod("VNPAY");

                    saveOrder(orderResquest);
                    Intent intent = new Intent(checkout_page.this, PaymentActivity.class);
                    intent.putExtra("total", total);
                    startActivity(intent);
                }
                if(radioCOD.isChecked()){
                    orderResquest.setPaymentMethod("CASH_ON_DELIVERY");
                    saveOrder(orderResquest);
                    Intent intent = new Intent(checkout_page.this, PaymentSuccess.class);
                    intent.putExtra("total", total);
                    startActivity(intent);
                }
            }
        });



    }
    private void pushNotification(double total){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setContent("Bạn đã đặt hàng thành công, số tiền: "+String.valueOf(total)+ "₫");

        notificationRequest.setTitle("Đặt hàng thành công");
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", 4);
        notificationRequest.setUser_id(userId);
        NotificationApi notificationApi = RetrofitClient.getRetrofit().create(NotificationApi.class);
        notificationApi.pushNotification(notificationRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                Toast.makeText(checkout_page.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {

            }
        });
    }
    public void saveOrder(OrderResquest orderResquest){
        OrderApi orderApi = RetrofitClient.getRetrofit().create(OrderApi.class);
        orderApi.checkout(orderResquest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {

            }
        });
    }
    public void thamchieu(){
        edtCommute = findViewById(R.id.edtCommute);
        edtDistrict = findViewById(R.id.edtDistrict);
        edtFullname = findViewById(R.id.edtFullname);
        edtWard = findViewById(R.id.edtWard);
        edtPhone = findViewById(R.id.edtPhone);
        edtProvince = findViewById(R.id.edtProvince);
    }
}