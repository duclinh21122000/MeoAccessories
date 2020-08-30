package com.example.demoaccessories.views.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoaccessories.Interface.SharedPreferencesToken;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.controller.AdapterComment;
import com.example.demoaccessories.model.Chat;
import com.example.demoaccessories.model.Comment;
import com.example.demoaccessories.model.CommentList;
import com.example.demoaccessories.model.Product;
import com.example.demoaccessories.model.UserInfo;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.demoaccessories.api.RetrofitClient.URL;

public class DetailsProductUserActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_name_seller, tv_create_at, tv_description, tv_price, tv_address, tv_name_product, tv_like;
    ImageView img_product, img_send_comment, img_close, img_like;
    CircleImageView img_user,  img_admin;
    Button btn_send_message, btn_send_mess;
    RecyclerView rv_list_comment;
    RelativeLayout rlt_like, rlt_share;
    String id, title_notification, current_date_time, name_user, image_user, name_admin, image_admin, date_time_current;
    EditText edt_comment, edt_messenger;
    ApiService apiService;
    Boolean clicked = true;
    AdapterComment adapterComment;
    List<Comment> commentList;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product_user);
        initView();
        getData();
        getDataById();
        getUser(new SharedPreferencesToken(getApplicationContext()).getUser());
        setAdapterComment();
    }

    private void getUser(String user) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getTokenAPI(user).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()){
                    image_user = response.body().getImage();
                    name_user = response.body().getFullName();
                  Picasso.get().load(URL+"uploads/"+response.body().getImage()).error(R.drawable.avatar).into(img_user);
                    Log.d("TAG", "onResponse: " + image_user);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getDataById() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(DetailsProductUserActivity.this);
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getProductById(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    tv_name_seller.setText(response.body().getNameSeller());
                    tv_create_at.setText(response.body().getCreateAt());
                    tv_description.setText(response.body().getDescription());
                    tv_price.setText(response.body().getPrice());
                    tv_address.setText(response.body().getAddress());
                    tv_name_product.setText(response.body().getNameProduct());
                    title_notification = response.body().getTitleNotification();
                    Picasso.get().load(URL+"uploads/"+response.body().getImage()).error(R.drawable.ic_add_picture).into(img_product);
                    Picasso.get().load(URL+"uploads/"+response.body().getImageSeller()).error(R.drawable.meo).into(img_admin);
                    image_admin = response.body().getImageSeller();
                    name_admin = response.body().getNameSeller();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
    }

    private void initView() {
        tv_like = findViewById(R.id.tv_like);
        tv_name_seller = findViewById(R.id.tv_name_seller);
        tv_create_at = findViewById(R.id.tv_date_time);
        tv_description = findViewById(R.id.tv_description_product);
        tv_price = findViewById(R.id.tv_price_product);
        tv_address = findViewById(R.id.tv_address);
        tv_name_product = findViewById(R.id.tv_name_product);
        edt_comment = findViewById(R.id.edt_comment);
        img_like = findViewById(R.id.img_like);
        img_product = findViewById(R.id.img_product);
        img_send_comment = findViewById(R.id.img_send_comment);
        btn_send_message = findViewById(R.id.btn_send_messenger);
        rv_list_comment = findViewById(R.id.rv_list_comment);
        rlt_like = findViewById(R.id.rlt_like);
        rlt_share = findViewById(R.id.rlt_share);
        img_close = findViewById(R.id.img_close);
        img_admin = findViewById(R.id.img_admin);
        img_user = findViewById(R.id.img_user);
        setOnclick();
    }

    private void setOnclick() {
        img_close.setOnClickListener(this);
        img_send_comment.setOnClickListener(this);
        btn_send_message.setOnClickListener(this);
        rlt_like.setOnClickListener(this);
        rlt_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlt_like:
                setActiveLike();
                break;
            case R.id.rlt_share:
                share();
                break;
            case R.id.img_close:
                finish();
                break;
            case R.id.img_send_comment:
                sendComment();
                break;
            case R.id.btn_send_messenger:
                sendMessenger();
                break;
            case R.id.btn_send_mess:
                send_messenger();
                break;
        }
    }

    private void send_messenger() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.send_messenger(id, name_admin, name_user, image_admin, image_user, edt_messenger.getText().toString(), date_time_current).enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if (response.isSuccessful()){
                    Toast.makeText(DetailsProductUserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void sendMessenger() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsProductUserActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_send_messenger, null);
        builder.setView(view);
        dialog = builder.create();
        edt_messenger = view.findViewById(R.id.edt_mess);
        btn_send_mess = view.findViewById(R.id.btn_send_mess);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        date_time_current = sdf.format(new Date());
        btn_send_mess.setOnClickListener(this);
        dialog.show();
    }

    private void sendComment() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'lúc' HH:mm");
        current_date_time = sdf.format(new Date());
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.add_comment(id, edt_comment.getText().toString(), name_user, image_user, current_date_time).enqueue(new Callback<CommentList>() {
            @Override
            public void onResponse(Call<CommentList> call, Response<CommentList> response) {
                if (response.isSuccessful()){
                    setAdapterComment();
                    edt_comment.getText().clear();
                }
            }
            @Override
            public void onFailure(Call<CommentList> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setAdapterComment() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.get_commentByID(id).enqueue(new Callback<CommentList>() {
            @Override
            public void onResponse(Call<CommentList> call, Response<CommentList> response) {
                if (response.isSuccessful()){
                    commentList = response.body().getData();
                    adapterComment = new AdapterComment(getApplicationContext(), commentList);
                    rv_list_comment.setAdapter(adapterComment);
                    rv_list_comment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapterComment.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CommentList> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void share() {
        String message = tv_name_seller.getText().toString()+ " " + title_notification + "\n https://www.facebook.com/meoaccessories2x/";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "Chia sẻ lên"));
    }

    private void setActiveLike() {
        if (clicked){
            clicked = false;
            img_like.setBackgroundResource(R.drawable.ic_like2);
            tv_like.setTextColor(Color.parseColor("#f96506"));
        } else {
            clicked = true;
            img_like.setBackgroundResource(R.drawable.ic_like_1);
            tv_like.setTextColor(Color.parseColor("#999999"));
        }
    }
}