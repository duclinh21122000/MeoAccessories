package com.example.demoaccessories.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoaccessories.R;
import com.example.demoaccessories.model.ChatList;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.demoaccessories.api.RetrofitClient.URL;

public class AdapterMessUser extends RecyclerView.Adapter<AdapterMessUser.ViewHolder> {
    Context context;
    List<ChatList> chatLists;

    public AdapterMessUser(Context context, List<ChatList> chatLists) {
        this.context = context;
        this.chatLists = chatLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_messenger, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatList chatList = chatLists.get(position);
        holder.tv_name_user.setText(chatList.getUser());
        holder.tv_content_messenger.setText(chatList.getMessenger());
        holder.tv_date_time.setText(chatList.getCreateAt());
        Picasso.get().load(URL+"uploads/"+chatList.getImageUser()).error(R.drawable.avatar).into(holder.img_user);
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user;
        TextView tv_name_user, tv_content_messenger, tv_date_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user = itemView.findViewById(R.id.img_user);
            tv_name_user = itemView.findViewById(R.id.tv_name);
            tv_content_messenger = itemView.findViewById(R.id.tv_content_messenger);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
        }
    }
}
