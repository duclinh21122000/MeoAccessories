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
import android.widget.Button;
import android.widget.EditText;

import com.example.demoaccessories.Interface.RecyclerItemClickListener;
import com.example.demoaccessories.Interface.SharedPreferencesToken;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.controller.AdapterProduct;
import com.example.demoaccessories.model.Product;
import com.example.demoaccessories.model.ProductList;
import com.example.demoaccessories.model.UserInfo;
import com.example.demoaccessories.views.activity.AddProductActivity;
import com.example.demoaccessories.views.activity.DetailsProductActivity;
import com.example.demoaccessories.views.activity.DetailsProductUserActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FragmentProduct extends Fragment implements View.OnClickListener {
    EditText edt_search;
    RecyclerView rv_list_product;
    List<Product> productList;
    AdapterProduct adapterProduct;
    FloatingActionButton btn_add_product;
    ApiService apiService;
    private int REQUEST_CODE = 100;
    public FragmentProduct() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        initView(view);
        setAdapter();
        setOnclickItem(new SharedPreferencesToken(getContext()).getUser());
        return view;
    }

    private void setOnclickItem(String user) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getTokenAPI(user).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()){
                    if (response.body().getPermission()){
                        btn_add_product.setVisibility(View.VISIBLE);
                        rv_list_product.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_list_product, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String id = productList.get(position).getId();
                                Intent intent = new Intent(getContext(), DetailsProductActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", id);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, REQUEST_CODE);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }));
                    } else {
                        rv_list_product.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_list_product, new RecyclerItemClickListener.OnItemClickListener() {
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
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setAdapter() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getAllProduct().enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    productList = response.body().getProductList();
                    adapterProduct = new AdapterProduct(getContext(), productList);
                    rv_list_product.setAdapter(adapterProduct);
                    rv_list_product.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterProduct.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initView(View view) {
        edt_search = view.findViewById(R.id.edt_search);
        rv_list_product = view.findViewById(R.id.rv_list_product);
        btn_add_product = view.findViewById(R.id.btn_add_product);
        setOnclick();
    }

    private void setOnclick() {
        btn_add_product.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_product){
            startActivityForResult(new Intent(getContext(), AddProductActivity.class), REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
                setAdapter();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}