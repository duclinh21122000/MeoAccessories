package com.example.demoaccessories.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.model.User;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout view1, view2, view3;
    EditText edt_email, edt_code, edt_new_password, edt_reNew_password;
    Button btn_send_mail, btn_send_code, btn_reset_password;
    ImageView img_back;
    ApiService apiService;
    String code;
    ProgressBar progressBar, progressBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        intiView();
    }

    private void intiView() {
        //view1
        view1 = findViewById(R.id.view1);
        edt_email = findViewById(R.id.edt_email);
        progressBar = findViewById(R.id.progressBar);
        btn_send_mail = findViewById(R.id.btn_send_email);
        img_back = findViewById(R.id.img_back);
        //view2
        view2 = findViewById(R.id.view2);
        edt_code = findViewById(R.id.edt_code);
        btn_send_code = findViewById(R.id.btn_send_code);
        //view3
        view3 = findViewById(R.id.view3);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_reNew_password = findViewById(R.id.edt_reNew_password);
        btn_reset_password = findViewById(R.id.btn_reset_password);
        progressBar1 = findViewById(R.id.progressBar1);
        setOnclick();
    }

    private void setOnclick() {
        btn_send_mail.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_send_code.setOnClickListener(this);
        btn_reset_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_email:
                send_mail(v);
                break;
            case R.id.img_back:
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                break;
            case R.id.btn_send_code:
                send_code(v);
                break;
            case R.id.btn_reset_password:
                reset_password(v);
                break;
        }
    }

    private void reset_password(View v) {
        if (edt_new_password.getText().toString().isEmpty() || edt_reNew_password.getText().toString().isEmpty()){
            Snackbar.make(v, "Vui lòng nhập mật khẩu mới của bạn", Snackbar.LENGTH_LONG).show();
        } else {
            if (edt_new_password.getText().toString().equals(edt_reNew_password.getText().toString())){
                progressBar1.setVisibility(View.VISIBLE);
                resetPassword(v);
            } else {
                Snackbar.make(v, "Mật khẩu phải trùng nhau", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void resetPassword(final View v) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.resetPassword(edt_email.getText().toString(), edt_new_password.getText().toString()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar1.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    edt_new_password.getText().clear();
                    edt_reNew_password.getText().clear();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar1.setVisibility(View.GONE);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void send_code(View v) {
        if (edt_code.getText().toString().isEmpty()){
            Snackbar.make(v, "Vui lòng nhập mã xác nhận của bạn", Snackbar.LENGTH_LONG).show();
        } else {
            if (edt_code.getText().toString().equals(code)){
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(v, "Mã xác nhận không hợp lệ", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void send_mail(View v) {
        if (edt_email.getText().toString().isEmpty()){
            Snackbar.make(v, "Vui lòng nhập Email của bạn", Snackbar.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            apiService = RetrofitClient.getInstance().create(ApiService.class);
            apiService.sendMailResetPass(edt_email.getText().toString()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        code = response.body().getMessage();
                        if (response.body().getStatus()){
                            Toast.makeText(ForgotPasswordActivity.this, "Chúng tôi đã gửi mã xác nhận vào Email của bạn", Toast.LENGTH_SHORT).show();
                            view1.setVisibility(View.GONE);
                            view2.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("TAG", "onFailure: " + t.getMessage());
                }
            });
        }
    }
}