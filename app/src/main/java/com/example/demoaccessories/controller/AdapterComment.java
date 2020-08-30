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
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.demoaccessories.api.RetrofitClient.URL;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {
    Context context;
    List<Comment> commentList;

    public AdapterComment(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.tv_fullName.setText(comment.getUser());
        holder.tv_comment.setText(comment.getComment());
        Picasso.get().load(URL+"uploads/"+comment.getImageUser()).error(R.drawable.avatar).into(holder.img_user);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user;
        TextView tv_fullName, tv_comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user = itemView.findViewById(R.id.img_user);
            tv_fullName = itemView.findViewById(R.id.tv_fullName);
            tv_comment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
