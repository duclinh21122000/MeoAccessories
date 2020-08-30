package com.example.demoaccessories.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.model.User;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout view1, view2;
    EditText edt_fullName, edt_email, ed_password, edt_rePassword, edt_code;
    Button btn_signUp, btn_send_code;
    TextView tv_login;
    ProgressBar progressBar, progressBar1;
    ApiService apiService;
    String fullName, email, password, re_password, code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        edt_fullName = findViewById(R.id.edt_fullName);
        edt_email = findViewById(R.id.edt_email);
        ed_password = findViewById(R.id.edt_password);
        edt_rePassword = findViewById(R.id.edt_rePassword);
        progressBar = findViewById(R.id.progressBar);
        btn_signUp = findViewById(R.id.btn_signUp);
        tv_login = findViewById(R.id.tv_login);
        //
        edt_code = findViewById(R.id.edt_code);
        btn_send_code = findViewById(R.id.btn_send_code);
        progressBar1 = findViewById(R.id.progressBar1);
        setOnclick();
    }

    private void setOnclick() {
        btn_signUp.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        btn_send_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signUp:
                signUpApp(v);
                break;
            case R.id.tv_login:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.btn_send_code:
                verifyAccount(v);
                break;
        }
    }

    private void verifyAccount(View v) {
        if (edt_code.getText().toString().isEmpty()){
            Snackbar.make(v, "Vui lòng nhập mã xác thực của bạn", Snackbar.LENGTH_LONG).show();
        } else {
            if (edt_code.getText().toString().equals(code)){
                progressBar1.setVisibility(View.VISIBLE);
                apiService = RetrofitClient.getInstance().create(ApiService.class);
                apiService.setIsActive(email).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        progressBar1.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body().getStatus()){
                                Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressBar1.setVisibility(View.GONE);
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });
            } else {
                Snackbar.make(v, "Mã xác thực không đúng", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void signUpApp(View v) {
        fullName = edt_fullName.getText().toString().trim();
        email = edt_email.getText().toString().trim();
        password = ed_password.getText().toString().trim();
        re_password = edt_rePassword.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || re_password.isEmpty()){
            Snackbar.make(v, "Vui lòng nhập đầy đủ thông tin", Snackbar.LENGTH_LONG).show();
        }else if (!email.matches(emailPattern)){
            Snackbar.make(v, "Định dạng email không đúng", Snackbar.LENGTH_LONG).show();
        }else {
            if (!password.equals(re_password)){
                Snackbar.make(v, "Mật khẩu phải giống nhau", Snackbar.LENGTH_LONG).show();
            }else {
                progressBar.setVisibility(View.VISIBLE);
                registerUser(v);
            }
        }
    }

    private void registerUser(final View v) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.registerUser(fullName, email, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().getIsActive()){
                        Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        view1.setVisibility(View.GONE);
                        view2.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this, "Chúng tôi đã gửi cho bạn một mã xác nhận", Toast.LENGTH_SHORT).show();
                        apiService.verifyEmail(email).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this, "Vui lòng kiếm tra Email của bạn", Toast.LENGTH_SHORT).show();
                                    code = response.body().getMessage();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}