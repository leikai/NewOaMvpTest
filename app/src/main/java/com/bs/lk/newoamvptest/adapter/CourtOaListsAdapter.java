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
import com.bs.lk.newoamvptest.bean.CourtOaUsageStatementBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.util.BitmapUtil;
import com.bs.lk.newoamvptest.view.activity.fragment.BaseFragment;
import com.bs.lk.newoamvptest.view.activity.fragment.ContactsNewManagerFragment;
import com.bs.lk.newoamvptest.view.activity.fragment.UserDetailInfoFragment;

import java.util.List;

/**
 * 法院列表适配器
 * 作用：选择启用手机OA的法院列表
 * @author lk
 */
public class CourtOaListsAdapter extends RecyclerView.Adapter<CourtOaListsAdapter.ViewHolder>{
    private Context mcontext;
    private List<CourtOaUsageStatementBean> mCoyrtOaList;
    private BaseFragment mPreFragment;

    public CourtOaListsAdapter(List<CourtOaUsageStatementBean> mCoyrtOaList, BaseFragment mPreFragment) {
        this.mCoyrtOaList = mCoyrtOaList;
        this.mPreFragment= mPreFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mcontext == null){
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.court_oa_usage_statement_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourtOaUsageStatementBean userNewBean = mCoyrtOaList.get(position);
        holder.courtName.setText(userNewBean.getCourtName());
        holder.courtType.setText(userNewBean.getUseType());
        holder.courtTime.setText(userNewBean.getUseTime());
    }

    @Override
    public int getItemCount() {
        return mCoyrtOaList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView courtType;
        TextView courtName;
        TextView courtTime;
        public ViewHolder(View view){
            super(view);
            courtName = view.findViewById(R.id.courtoa_usage_court_name);
            courtType = view.findViewById(R.id.courtoa_usage_use_type);
            courtTime = view.findViewById(R.id.courtoa_usage_use_time);
        }
    }
}
