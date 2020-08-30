package com.example.demoaccessories.views.frgament;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.demoaccessories.Interface.RecyclerItemClickListener;
import com.example.demoaccessories.Interface.SharedPreferencesToken;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.controller.AdapterAccountUser;
import com.example.demoaccessories.model.AccountInfo;
import com.example.demoaccessories.model.UserInfo;
import com.example.demoaccessories.views.activity.DetailsUserActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FragmentUserManager extends Fragment{
    EditText edt_search;
    RecyclerView rv_list_user;
    List<UserInfo> userInfoList;
    AdapterAccountUser adapterAccountUser;
    ApiService apiService;
    String email;
    private int REQUEST_CODE = 100;
    public FragmentUserManager() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_manager, container, false);
        initView(view);
        getPermission(new SharedPreferencesToken(getContext()).getUser());
        return view;
    }

    private void getPermission(final String user) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getTokenAPI(user).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()){
                    if (response.body().getPermission()){
                        setAdapter(user);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

    private void setAdapter(String user) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.get_account_user(user).enqueue(new Callback<AccountInfo>() {
            @Override
            public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    userInfoList = response.body().getData();
                    adapterAccountUser = new AdapterAccountUser(getContext(), userInfoList);
                    rv_list_user.setAdapter(adapterAccountUser);
                    rv_list_user.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterAccountUser.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AccountInfo> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initView(View view) {
        rv_list_user = view.findViewById(R.id.rv_list_user);
        edt_search = view.findViewById(R.id.edt_search);
        setOnclick();
    }

    private void setOnclick() {
        rv_list_user.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_list_user, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                email = userInfoList.get(position).getEmail();
                Intent intent = new Intent(getContext(), DetailsUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            setAdapter(new SharedPreferencesToken(getContext()).getUser());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}