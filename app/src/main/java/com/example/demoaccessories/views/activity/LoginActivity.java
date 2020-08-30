package com.example.demoaccessories.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoaccessories.Interface.SharedPreferencesToken;
import com.example.demoaccessories.MainActivity;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.model.User;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_email, edt_password;
    TextView tv_forgot_pass, tv_register;
    Button btn_login;
    ProgressBar progressBar;
    String email, password;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        tv_forgot_pass = findViewById(R.id.tv_forgot_pass);
        tv_register = findViewById(R.id.tv_register);
        progressBar = findViewById(R.id.progressBar);
        btn_login = findViewById(R.id.btn_login);
        setOnclick();
    }

    private void setOnclick() {
        tv_forgot_pass.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.btn_login:
                loginApp(v);
                break;
            case R.id.tv_forgot_pass:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void loginApp(View v) {
        email = edt_email.getText().toString().trim();
        password = edt_password.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.isEmpty() || password.isEmpty()){
            Snackbar.make(v, "Vui lòng nhập đầy đủ thông tin", Snackbar.LENGTH_LONG).show();
        }else {
            if (!email.matches(emailPattern)){
                Snackbar.make(v, "Định dạng email không đúng", Snackbar.LENGTH_LONG).show();
            }else {
                progressBar.setVisibility(View.VISIBLE);
                loginToApp(v);
            }
        }
    }

    private void loginToApp(final View v) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.loginApp(email, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null){
                    if (response.body().getStatus()){
                        if (response.body().getData().getIsActive()){
                            Toast.makeText(LoginActivity.this, "Đã đăng nhập với tên " + response.body().getData().getFullName(), Toast.LENGTH_SHORT).show();
                            SharedPreferencesToken sharedPreferencesToken = new SharedPreferencesToken(LoginActivity.this);
                            sharedPreferencesToken.isSaveLogin(email, password, response.body().getStatus());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Vui lòng xác thực tài khoản của bạn", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}