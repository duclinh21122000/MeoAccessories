package com.example.demoaccessories.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.model.ProductList;
import com.example.demoaccessories.model.User;
import com.example.demoaccessories.model.UserInfo;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsUserActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_back, img_delete;
    CircleImageView img_user;
    TextView tv_fullName, tv_email_user, tv_phone_user, tv_address_user;
    String email;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_user);
        initView();
        getData();
        getDataByEmail();
    }

    private void getDataByEmail() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(DetailsUserActivity.this);
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getTokenAPI(email).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    tv_fullName.setText(response.body().getFullName());
                    tv_address_user.setText(response.body().getAddress());
                    tv_email_user.setText(response.body().getEmail());
                    tv_phone_user.setText(response.body().getPhone());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");

    }

    private void initView() {
        img_back = findViewById(R.id.img_back);
        img_delete = findViewById(R.id.img_delete);
        img_user = findViewById(R.id.img_user);
        tv_fullName = findViewById(R.id.tv_fullName);
        tv_email_user = findViewById(R.id.tv_email_user);
        tv_phone_user = findViewById(R.id.tv_phone_user);
        tv_address_user = findViewById(R.id.tv_address_user);
        setOnClick();
    }

    private void setOnClick() {
        img_back.setOnClickListener(this);
        img_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.img_delete:
                deleteUser(v);
                break;
        }
    }

    private void deleteUser(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsUserActivity.this);
        builder.setTitle("Xoá tài khoản");
        builder.setMessage("Bạn có chắc muốn xoá tài khoản này không?");
        builder.setCancelable(false);
        builder.setNegativeButton("HUỶ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("ĐỒNG Ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final ProgressDialog dialog;
                dialog = new ProgressDialog(DetailsUserActivity.this);
                dialog.setTitle("Đang xoá tài khoản");
                dialog.setMessage("Vui lòng đợi...");
                dialog.show();
                apiService = RetrofitClient.getInstance().create(ApiService.class);
                apiService.deleteUser(email).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(DetailsUserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, new Intent());
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        dialog.dismiss();
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}