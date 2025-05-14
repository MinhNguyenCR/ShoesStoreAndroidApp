package com.example.shoesstoreandroidapp.customer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.customer.Request.NotificationRequest;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSuccess extends AppCompatActivity {
    Button btnToHomePage;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "my_channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_success);
        double total = getIntent().getDoubleExtra("total", 0);

        createNotificationChannel();

        showNotification("Thông báo", "Cảm ơn bạn đã đặt hàng. Chúng tôi sẽ giao hàng cho bạn sớm nhất");
        pushNotification(total);
        btnToHomePage = findViewById(R.id.btnBackToHome);
        btnToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentSuccess.this, main_page.class);
                startActivity(intent);
            }
        });
    }

    private void showNotification(String title, String message) {
        // Android 13+ yêu cầu quyền
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
                return;
            }
        }

        // Gửi thông báo
        Intent intent = new Intent(this, PaymentSuccess.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        String timestamp = new java.text.SimpleDateFormat("HH:mm dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date());
        Notification newNoti = new Notification(title, message, timestamp);
        NotificationStorage.saveNotification(this, newNoti);
    }


    private void createNotificationChannel() {
        // Chỉ tạo kênh trên Android 8.0 (API 26) trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Thông báo đơn hàng";
            String description = "Thông báo khi thanh toán thành công";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void pushNotification(double total){
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setContent("Bạn đã thanh toán đơn hàng thành công, số tiền: "+String.valueOf(total)+ "₫");
        notificationRequest.setTitle("Đặt hàng thành công");
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", 4);
        notificationRequest.setUser_id(userId);
        NotificationApi notificationApi = RetrofitClient.getRetrofit().create(NotificationApi.class);
        notificationApi.pushNotification(notificationRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                Toast.makeText(PaymentSuccess.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {

            }
        });
    }

}
