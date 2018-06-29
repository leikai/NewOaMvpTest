package com.bs.lk.newoamvptest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bumptech.glide.Glide;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    private Context mcontext;
    private List<UserNewBean> mUsersList;

    public UsersAdapter(List<UserNewBean> mUsersList) {
        this.mUsersList = mUsersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mcontext == null){
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = holder.getAdapterPosition();
//                UserNewBean fruit = mUsersList.get(position);
////                Intent intent = new Intent(mcontext,FruitActivity.class);
////                intent.putExtra(FruitActivity.FRUIT_NAME,fruit.getName());
////                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID,fruit.getImageId());
////                mcontext.startActivity(intent);
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserNewBean userNewBean = mUsersList.get(position);
        holder.userName.setText(userNewBean.getEmpname());
//        Glide.with(mcontext).load(userNewBean.getImageId()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage;
        TextView userName;
        public ViewHolder(View view){
            super(view);
            userImage = view.findViewById(R.id.iv_head);
            userName = view.findViewById(R.id.tv_username);
        }
    }
}
