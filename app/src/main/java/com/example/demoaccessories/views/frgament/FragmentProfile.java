package com.example.demoaccessories.views.frgament;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoaccessories.Interface.SharedPreferencesToken;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.controller.AdapterAccountUser;
import com.example.demoaccessories.model.AccountInfo;
import com.example.demoaccessories.model.DataUser;
import com.example.demoaccessories.model.ProductList;
import com.example.demoaccessories.model.User;
import com.example.demoaccessories.model.UserInfo;
import com.example.demoaccessories.views.activity.DetailsProductActivity;
import com.example.demoaccessories.views.activity.DetailsProfileActivity;
import com.example.demoaccessories.views.activity.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.demoaccessories.api.RetrofitClient.URL;

public class FragmentProfile extends Fragment implements View.OnClickListener {
    RelativeLayout rlt_edit_user;
    LinearLayout layout_user_manager, layout_share, layout_changePass, layout_logOut;
    CircleImageView img_profile;
    TextView tv_fullName;
    ApiService apiService;
    LinearLayout view1, view2;
    EditText edt_old_password, edt_new_password, edt_reNew_password;
    Button btnChangePassword, btn_send_password;
    ProgressBar progressBar, progressBar1;
    Dialog dialog;
    private int REQUEST_CODE = 100;
    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        getPermission(new SharedPreferencesToken(getContext()).getUser());
        return view;
    }

    private void getPermission(final String user) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getTokenAPI(user).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getPermission()) {
                        Picasso.get().load(URL + "uploads/" + response.body().getImage()).error(R.drawable.meo).into(img_profile);
                        tv_fullName.setText(response.body().getFullName());
                    } else {
                        layout_user_manager.setVisibility(View.GONE);
                        Picasso.get().load(URL + "uploads/" + response.body().getImage()).error(R.drawable.avatar).into(img_profile);
                        tv_fullName.setText(response.body().getFullName());
                    }
                }
            }
            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

    private void initView(View view) {
        rlt_edit_user = view.findViewById(R.id.rlt_editUser);
        layout_share = view.findViewById(R.id.layout_share);
        layout_changePass = view.findViewById(R.id.layout_changePass);
        layout_logOut = view.findViewById(R.id.layout_logOut);
        layout_user_manager = view.findViewById(R.id.layout_user_manager);
        img_profile = view.findViewById(R.id.img_user);
        tv_fullName = view.findViewById(R.id.tv_fullName);
        setOnclick();
    }

    private void setOnclick() {
        rlt_edit_user.setOnClickListener(this);
        layout_logOut.setOnClickListener(this);
        layout_changePass.setOnClickListener(this);
        layout_share.setOnClickListener(this);
        layout_user_manager.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlt_editUser:
                goToProfile();
                break;
            case R.id.layout_share:
                break;
            case R.id.layout_changePass:
                showDialogChangePass();
                break;
            case R.id.layout_logOut:
                logOut();
                break;
            case R.id.layout_user_manager:
                loadFragment(new FragmentUserManager());
                break;
            case R.id.btn_change_password:
                progressBar1.setVisibility(View.VISIBLE);
                changePassword(new SharedPreferencesToken(getContext()).getUser());
                break;
            case R.id.btn_send_password:
                progressBar.setVisibility(View.VISIBLE);
                sendPassword(new SharedPreferencesToken(getContext()).getUser());
                break;
        }
    }

    private void sendPassword(String user) {
        if (edt_old_password.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Vui lòng nhập mật khẩu của bạn", Toast.LENGTH_SHORT).show();
        } else {
            apiService = RetrofitClient.getInstance().create(ApiService.class);
            apiService.changePassword(user, edt_old_password.getText().toString()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            view1.setVisibility(View.GONE);
                            view2.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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


    private void changePassword(String user) {
        if (edt_new_password.getText().toString().isEmpty() || edt_reNew_password.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Vui lòng nhập mật khẩu mới của bạn", Toast.LENGTH_SHORT).show();
        } else {
            if (edt_new_password.getText().toString().equals(edt_reNew_password.getText().toString())){
                apiService = RetrofitClient.getInstance().create(ApiService.class);
                apiService.resetPassword(user, edt_new_password.getText().toString()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        progressBar1.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            edt_new_password.getText().clear();
                            edt_reNew_password.getText().clear();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressBar1.setVisibility(View.GONE);
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });
            } else {
                Toast.makeText(getContext(), "Mật khẩu phải trùng nhau", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialogChangePass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(view);
        dialog = builder.create();
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        edt_old_password = view.findViewById(R.id.edt_old_password);
        edt_new_password = view.findViewById(R.id.edt_new_password);
        edt_reNew_password = view.findViewById(R.id.edt_reNew_password);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btn_send_password = view.findViewById(R.id.btn_send_password);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar1 = view.findViewById(R.id.progressBar1);
        btn_send_password.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        dialog.show();
    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("ĐĂNG XUẤT");
        builder.setMessage("Bạn có muốn đăng xuất không?");
        builder.setCancelable(false);
        builder.setNegativeButton("KHÔNG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void goToProfile() {
        String email = new SharedPreferencesToken(getContext()).getUser();
        Intent intent = new Intent(getContext(), DetailsProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            getPermission(new SharedPreferencesToken(getContext()).getUser());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.layout_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}