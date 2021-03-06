package com.bs.lk.newoamvptest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.util.BitmapUtil;
import com.bs.lk.newoamvptest.view.activity.fragment.BaseFragment;
import com.bs.lk.newoamvptest.view.activity.fragment.ContactsNewManagerFragment;
import com.bs.lk.newoamvptest.view.activity.fragment.UserDetailInfoFragment;
import java.util.List;

/**
 * 人员列表适配器
 * @author lk
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    private Context mcontext;
    private List<UserNewBean> mUsersList;
    private BaseFragment mPreFragment;

    public UsersAdapter(List<UserNewBean> mUsersList,BaseFragment mPreFragment) {
        this.mUsersList = mUsersList;
        this.mPreFragment= mPreFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mcontext == null){
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.llUserItem.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            UserNewBean user = mUsersList.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt(BaseFragment.PARAM_CHILD_TYPE, ContactsNewManagerFragment.CHILD_TYPE_USERINFO);
            bundle.putSerializable(UserDetailInfoFragment.PARAM_USER, user);
            mPreFragment.showChildFragment(bundle);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserNewBean userNewBean = mUsersList.get(position);
        holder.userName.setText(userNewBean.getEmpname());
        Bitmap bp = BitmapUtil.GetUserImageByNickName(mcontext,userNewBean.getEmpname());
        holder.userImage.setImageBitmap(bp);
//        Glide.with(mcontext).load(userNewBean.getImageId()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llUserItem;
        ImageView userImage;
        TextView userName;
        ViewHolder(View view){
            super(view);
            llUserItem = view.findViewById(R.id.ll_user_item);
            userImage = view.findViewById(R.id.iv_head);
            userName = view.findViewById(R.id.tv_username);
        }
    }
}
