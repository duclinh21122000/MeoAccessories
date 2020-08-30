package com.example.demoaccessories.views.frgament;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoaccessories.Interface.RecyclerItemClickListener;
import com.example.demoaccessories.Interface.SharedPreferencesToken;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.controller.AdapterProduct;
import com.example.demoaccessories.model.Product;
import com.example.demoaccessories.model.ProductList;
import com.example.demoaccessories.model.UserInfo;
import com.example.demoaccessories.views.activity.DetailsProductActivity;
import com.example.demoaccessories.views.activity.DetailsProductUserActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FragmentHome extends Fragment implements View.OnClickListener {
    RecyclerView rv_list_product;
    TextView tv_view_more;
    ApiService apiService;
    List<Product> productList = new ArrayList<>();
    AdapterProduct adapterProduct;
    private int REQUEST_CODE = 100;
    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
                        for (int i = 0; i < 2; i++) {
                            productList.add(new Product(response.body().getProductList().get(i).getId(),
                                    response.body().getProductList().get(i).getNameSeller(),
                                    response.body().getProductList().get(i).getNameProduct(),
                                    response.body().getProductList().get(i).getPrice(),
                                    response.body().getProductList().get(i).getAddress(),
                                    response.body().getProductList().get(i).getCategory(),
                                    response.body().getProductList().get(i).getDescription(),
                                    response.body().getProductList().get(i).getImage(),
                                    response.body().getProductList().get(i).getImageSeller(),
                                    response.body().getProductList().get(i).getCreateAt(),
                                    response.body().getProductList().get(i).getTitleNotification(),
                                    response.body().getProductList().get(i).getV()));
                        }
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
        rv_list_product = view.findViewById(R.id.rv_list_product);
        tv_view_more = view.findViewById(R.id.tv_view_more);
        setOnclick();
    }

    private void setOnclick() {
        tv_view_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_view_more){
            loadFragment(new FragmentProduct());
        }
    }

    private void loadFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.layout_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
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