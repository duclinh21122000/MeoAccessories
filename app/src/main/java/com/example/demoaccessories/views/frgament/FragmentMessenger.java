package com.example.demoaccessories.views.frgament;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;

import com.example.demoaccessories.Interface.SharedPreferencesToken;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.controller.AdapterMessAdmin;
import com.example.demoaccessories.controller.AdapterMessUser;
import com.example.demoaccessories.model.Chat;
import com.example.demoaccessories.model.ChatList;
import com.example.demoaccessories.model.ProductList;
import com.example.demoaccessories.model.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMessenger extends Fragment {
    EditText edt_search;
    RecyclerView rv_list_messenger_user, rv_list_messenger_admin;
    AdapterMessAdmin adapterMessAdmin;
    AdapterMessUser adapterMessUser;
    List<ChatList> chatLists;
    ApiService apiService;
    public FragmentMessenger() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messenger, container, false);
        initView(view);
        getUser(new SharedPreferencesToken(getContext()).getUser());
        return view;
    }

    private void getUser(String user) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getTokenAPI(user).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()){
                    if (response.body().getPermission()){
                        rv_list_messenger_user.setVisibility(View.GONE);
                        setAdapterUser();
                    } else {
                        rv_list_messenger_admin.setVisibility(View.GONE);
                        setAdapterAdmin();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setAdapterAdmin() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.get_all_messenger().enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                Log.d("TAG", "onResponse: " + response.body());
                dialog.dismiss();
                if (response.isSuccessful()){
                    chatLists = response.body().getData();
                    adapterMessAdmin = new AdapterMessAdmin(getContext(), chatLists);
                    rv_list_messenger_user.setAdapter(adapterMessAdmin);
                    rv_list_messenger_user.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterMessAdmin.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setAdapterUser() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.get_all_messenger().enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                dialog.dismiss();
                Log.d("TAG", "onResponse: " + response.body());
                if (response.isSuccessful()){
                    chatLists = response.body().getData();
                    adapterMessUser = new AdapterMessUser(getContext(), chatLists);
                    rv_list_messenger_admin.setAdapter(adapterMessUser);
                    rv_list_messenger_admin.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterMessUser.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initView(View view) {
        edt_search = view.findViewById(R.id.edt_search);
        rv_list_messenger_user = view.findViewById(R.id.rv_list_messenger_user);
        rv_list_messenger_admin = view.findViewById(R.id.rv_list_messenger_admin);
    }
}