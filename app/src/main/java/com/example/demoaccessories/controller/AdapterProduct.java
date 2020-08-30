package com.example.demoaccessories.controller;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoaccessories.R;
import com.example.demoaccessories.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.demoaccessories.api.RetrofitClient.URL;


public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {
    Context context;
    List<Product> productList;

    public AdapterProduct(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tv_name_seller.setText(product.getNameSeller());
        holder.tv_date_time.setText(product.getCreateAt());
        holder.tv_description.setText(product.getDescription());
        holder.tv_title_notification.setText(product.getTitleNotification());
        Picasso.get().load(URL+"uploads/"+product.getImage()).error(R.drawable.ic_add_picture).into(holder.img_product);
        Picasso.get().load(URL+"uploads/"+product.getImageSeller()).error(R.drawable.avatar).into(holder.img_seller);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_seller;
        ImageView img_product;
        TextView tv_name_seller, tv_title_notification, tv_description, tv_date_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            tv_name_seller = itemView.findViewById(R.id.tv_name_seller);
            tv_title_notification = itemView.findViewById(R.id.tv_title_notification);
            tv_description = itemView.findViewById(R.id.tv_description_product);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            img_seller = itemView.findViewById(R.id.img_seller);
        }
    }
}
