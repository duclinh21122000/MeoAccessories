package com.example.demoaccessories.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoaccessories.R;
import com.example.demoaccessories.model.DataUser;
import com.example.demoaccessories.model.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.demoaccessories.api.RetrofitClient.URL;

public class AdapterAccountUser extends RecyclerView.Adapter<AdapterAccountUser.ViewHolder> {
    Context context;
    List<UserInfo> dataUserList;

    public AdapterAccountUser(Context context, List<UserInfo> dataUserList) {
        this.context = context;
        this.dataUserList = dataUserList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAccountUser.ViewHolder holder, int position) {
        UserInfo dataUser = dataUserList.get(position);
        holder.tv_name_user.setText(dataUser.getFullName());
        Picasso.get().load(URL+"uploads/"+dataUser.getImage()).error(R.drawable.avatar).into(holder.img_user);
    }

    @Override
    public int getItemCount() {
        return dataUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user;
        TextView tv_name_user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user = itemView.findViewById(R.id.img_user);
            tv_name_user = itemView.findViewById(R.id.tv_name_user);
        }
    }
}
