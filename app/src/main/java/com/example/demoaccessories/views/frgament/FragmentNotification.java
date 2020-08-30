package com.example.demoaccessories.views.frgament;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demoaccessories.Interface.RecyclerItemClickListener;
import com.example.demoaccessories.Interface.SharedPreferencesToken;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.controller.AdapterNotification;
import com.example.demoaccessories.controller.AdapterNotificationAdmin;
import com.example.demoaccessories.model.Comment;
import com.example.demoaccessories.model.CommentList;
import com.example.demoaccessories.model.Product;
import com.example.demoaccessories.model.ProductList;
import com.example.demoaccessories.model.UserInfo;
import com.example.demoaccessories.views.activity.DetailsProductUserActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNotification extends Fragment {
    RecyclerView rv_list_notification, rv_list_notification_admin;
    List<Product> productList;
    List<Comment> commentList;
    AdapterNotificationAdmin adapterComment;
    AdapterNotification adapterNotification;
    ApiService apiService;
    public FragmentNotification() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initView(view);
        setAdapter(new SharedPreferencesToken(getContext()).getUser());
        setOnclickItem();
        return view;
    }

    private void setOnclickItemAdmin() {
        rv_list_notification_admin.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_list_notification_admin, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = commentList.get(position).getIdProduct();
                Intent intent = new Intent(getContext(), DetailsProductUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void setOnclickItem() {
        rv_list_notification.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_list_notification, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = productList.get(position).getId();
                Intent intent = new Intent(getContext(), DetailsProductUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void setAdapter(String user) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getTokenAPI(user).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()){
                    if (response.body().getPermission()){
                        rv_list_notification.setVisibility(View.GONE);
                        setAdapterNotificationAdmin();
                        setOnclickItemAdmin();
                    } else {
                        rv_list_notification_admin.setVisibility(View.GONE);
                        setAdapterNotificationUser();
                        setOnclickItem();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setAdapterNotificationUser() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService.getAllProduct().enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    productList = response.body().getProductList();
                    adapterNotification = new AdapterNotification(getContext(), productList);
                    rv_list_notification.setAdapter(adapterNotification);
                    rv_list_notification.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterNotification.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setAdapterNotificationAdmin() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService.get_all_comment().enqueue(new Callback<CommentList>() {
            @Override
            public void onResponse(Call<CommentList> call, Response<CommentList> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    commentList = response.body().getData();
                    adapterComment = new AdapterNotificationAdmin(getContext(), commentList);
                    rv_list_notification_admin.setAdapter(adapterComment);
                    rv_list_notification_admin.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterComment.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CommentList> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initView(View view) {
        rv_list_notification = view.findViewById(R.id.rv_list_notification);
        rv_list_notification_admin = view.findViewById(R.id.rv_list_notification_admin);
    }
}