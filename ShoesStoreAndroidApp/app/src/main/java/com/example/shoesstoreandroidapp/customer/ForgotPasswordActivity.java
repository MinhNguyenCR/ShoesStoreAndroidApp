package com.example.shoesstoreandroidapp.customer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.customer.API.AccountAPI;
import com.example.shoesstoreandroidapp.customer.Request.AccountSignUpRequest;
import com.example.shoesstoreandroidapp.customer.Request.OTPRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword, edtConfirmPassword, edtOtp;
    Button btnSendOtp, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        thamChieu();
        btnSendOtp.setOnClickListener(v -> sendOtp());

        btnReset.setOnClickListener(v -> {
            if(validateForm()) {
                verifyOtpAndReset();
            }
        });
    }

    private void thamChieu(){
        edtEmail = findViewById(R.id.edtEmailOrPhone);
        edtPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtOtp = findViewById(R.id.edtOtp);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnReset = findViewById(R.id.btnResetPassword);
    }
    private void sendOtp() {
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        AccountAPI api = RetrofitClient.getRetrofit().create(AccountAPI.class);
        Call<ApiResponse<String>> call = api.generateOtp(email);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ForgotPasswordActivity.this, "OTP sent to email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void verifyOtpAndReset() {
        String email = edtEmail.getText().toString().trim();
        String otp = edtOtp.getText().toString().trim();

        OTPRequest otpRequest = new OTPRequest();
        otpRequest.setEmail(email);
        otpRequest.setOtp(otp);

        AccountAPI api = RetrofitClient.getRetrofit().create(AccountAPI.class);
        api.verifyOtp(otpRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    resetPassword();
                } else {
                    Log.d("OTP_CHECK", "Email: " + otpRequest.getEmail() + " | OTP: " + otpRequest.getOtp());

                    Toast.makeText(ForgotPasswordActivity.this, "OTP verification failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void resetPassword(){
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest();
        accountSignUpRequest.setEmail(edtEmail.getText().toString().trim());
        accountSignUpRequest.setPassword(edtPassword.getText().toString().trim());
        AccountAPI api = RetrofitClient.getRetrofit().create(AccountAPI.class);
        api.resetPassword(accountSignUpRequest).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "OTP verification failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private boolean validateForm() {
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if (edtEmail.getText().toString().isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() ||
                edtOtp.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}