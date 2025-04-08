package com.example.shoesstoreandroidapp.customer;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoesstoreandroidapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private Button btnPay;
    private WebView webView;
    private VnpayApi vnpayApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnPay = findViewById(R.id.btnCreatePayment);
        webView = findViewById(R.id.webViewVnpay);
        webView.getSettings().setJavaScriptEnabled(true);

        // Gắn WebViewClient để theo dõi quá trình thanh toán
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("vnp_ResponseCode=00")) {
                    Toast.makeText(PaymentActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                } else if (url.contains("vnp_ResponseCode")) {
                    Toast.makeText(PaymentActivity.this, "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        vnpayApi = RetrofitClient.getRetrofit().create(VnpayApi.class);

        btnPay.setOnClickListener(v -> callPaymentApi(10000));
    }

    private void callPaymentApi(int amount) {
        vnpayApi.createPayment(amount).enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String paymentUrl = response.body().getRedirectUrl();
                    webView.loadUrl(paymentUrl);
                } else {
                    Toast.makeText(PaymentActivity.this, "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Không kết nối được server", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
