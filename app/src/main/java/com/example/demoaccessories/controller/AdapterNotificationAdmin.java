package com.example.demoaccessories.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoaccessories.R;
import com.example.demoaccessories.model.Comment;
import com.example.demoaccessories.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.demoaccessories.api.RetrofitClient.URL;

public class AdapterNotificationAdmin extends RecyclerView.Adapter<AdapterNotificationAdmin.ViewHolder> {
    Context context;
    List<Comment> commentList;

    public AdapterNotificationAdmin(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.tv_seller.setText(comment.getUser());
        holder.tv_notification.setText(comment.getTitleNotification());
        holder.tv_date_time.setText(comment.getCreateAt());
        Picasso.get().load(URL+"uploads/"+comment.getImageUser()).error(R.drawable.avatar).into(holder.img_seller);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
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
