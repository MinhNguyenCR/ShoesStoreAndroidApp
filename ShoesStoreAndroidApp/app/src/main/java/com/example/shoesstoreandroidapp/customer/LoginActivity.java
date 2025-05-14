package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.customer.API.AccountAPI;
import com.example.shoesstoreandroidapp.customer.Request.AccountSignUpRequest;
import com.example.shoesstoreandroidapp.customer.Request.LoginRequest;
import com.example.shoesstoreandroidapp.customer.Response.LoginResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    AccountAPI accountAPI;
    TextView textSignUp, textForgotPassword;
    ImageView loginGoogleBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textSignUp = findViewById(R.id.textSignUp);
        textForgotPassword = findViewById(R.id.textForgotPassword);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        loginGoogleBtn = findViewById(R.id.imgGoogleLogo);
        loginGoogleBtn.setOnClickListener(v -> signIn());
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        accountAPI = RetrofitClient.getRetrofit().create(AccountAPI.class);
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(email, password);
            accountAPI.login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1000) {
                        long userId = response.body().getResult().getUserId();

                        // Lưu userId vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("userId", userId);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang trang sản phẩm
                        Intent intent = new Intent(LoginActivity.this, main_page.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginError", t.getMessage());
                }
            });
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            registerAccount(account);
            Login(account);
              } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ccccc", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Login ngu vc", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Nếu người dùng đã đăng nhập trước đó
            String displayName = account.getDisplayName();
            String email = account.getEmail();
        }
    }
    private void registerAccount(GoogleSignInAccount account) {
        AccountSignUpRequest request = new AccountSignUpRequest();
        request.setEmail(account.getEmail());
        request.setPassword("12345678");
        request.setFullName("");
        request.setPhone("");
        AccountAPI api = RetrofitClient.getRetrofit().create(AccountAPI.class);
        api.register(request).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                } else {
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void Login(GoogleSignInAccount account){
        LoginRequest request = new LoginRequest(account.getEmail(), "12345678");
        accountAPI.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    long userId = response.body().getResult().getUserId();

                    // Lưu userId vào SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("userId", userId);
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang trang sản phẩm
                    Intent intent = new Intent(LoginActivity.this, main_page.class);
                    startActivity(intent);
                    finish();
                } else {
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginError", t.getMessage());
            }
        });
    }


}