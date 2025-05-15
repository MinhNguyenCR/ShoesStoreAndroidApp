package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.customer.API.AccountAPI;
import com.example.shoesstoreandroidapp.customer.Request.AccountSignUpRequest;
import com.example.shoesstoreandroidapp.customer.Request.OTPRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText edtFullName, edtEmail, edtPassword, edtConfirmPassword, edtPhone, edtOtp;
    Button btnSendOtp, btnRegister;
    TextView textSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViewReferences();

        btnSendOtp.setOnClickListener(v -> sendOtp());

        btnRegister.setOnClickListener(v -> {
            if (validateForm()) {
                verifyOtpAndRegister();
            }
        });
        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViewReferences() {
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtOtp = findViewById(R.id.edtOtp);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnRegister = findViewById(R.id.btnRegister);
        textSignIn = findViewById(R.id.textSignIn);
    }

    private boolean validateForm() {
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if (edtFullName.getText().toString().isEmpty() ||
                edtEmail.getText().toString().isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() ||
                edtPhone.getText().toString().isEmpty() ||
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
                    Toast.makeText(SignUpActivity.this, "OTP sent to email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verifyOtpAndRegister() {
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
                    registerAccount();
                } else {
                    Log.d("OTP_CHECK", "Email: " + otpRequest.getEmail() + " | OTP: " + otpRequest.getOtp());

                    Toast.makeText(SignUpActivity.this, "OTP verification failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerAccount() {
        AccountSignUpRequest request = new AccountSignUpRequest();
        request.setFullName(edtFullName.getText().toString());
        request.setEmail(edtEmail.getText().toString());
        request.setPassword(edtPassword.getText().toString());
        request.setConfirmPassword(edtConfirmPassword.getText().toString());
        request.setPhone(edtPhone.getText().toString());
        request.setOtp(edtOtp.getText().toString());

        AccountAPI api = RetrofitClient.getRetrofit().create(AccountAPI.class);
        api.register(request).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
