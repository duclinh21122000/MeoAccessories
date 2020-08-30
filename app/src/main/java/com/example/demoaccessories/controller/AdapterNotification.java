package com.example.demoaccessories.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoaccessories.R;
import com.example.demoaccessories.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.demoaccessories.api.RetrofitClient.URL;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {
    Context context;
    List<Product> productList;

    public AdapterNotification(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tv_seller.setText(product.getNameSeller());
        holder.tv_notification.setText(product.getTitleNotification());
        holder.tv_date_time.setText(product.getCreateAt());
        Picasso.get().load(URL+"uploads/"+product.getImageSeller()).error(R.drawable.avatar).into(holder.img_seller);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_seller;
        TextView tv_seller, tv_notification, tv_date_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_seller = itemView.findViewById(R.id.img_admin);
            tv_seller = itemView.findViewById(R.id.tv_name_admin);
            tv_notification = itemView.findViewById(R.id.tv_title_notification);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
        }
    }
}
