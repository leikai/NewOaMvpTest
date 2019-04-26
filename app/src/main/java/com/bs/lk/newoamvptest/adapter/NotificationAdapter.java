package com.bs.lk.newoamvptest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.NotificationAnnouncementBean;
import com.bs.lk.newoamvptest.view.activity.NotificationActivity;

import java.util.List;

/**
 * 首页通知适配器
 * @author lk
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {
    private List<NotificationAnnouncementBean> mFruitList;
    private Context context;
    private String emptyNotification = "暂无通知信息";
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        private final TextView info;

        public ViewHolder(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.news_title);
            info = itemView.findViewById(R.id.news_content);
        }
    }

    public NotificationAdapter(Context context, List<NotificationAnnouncementBean> mFruitList) {
        this.context = context;
        this.mFruitList = mFruitList;
    }

    /**
     *  创建ViewHolder的实例
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_news_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emptyNotification.equals(mFruitList.get(0).getTitle())){
                    Intent jumptoNews = new Intent(context, NotificationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("oid",mFruitList.get((int) holder.itemView.getTag()).getOid());
                    jumptoNews.putExtras(bundle);
                    context.startActivity(jumptoNews);
                }
            }
        });
        return holder;
    }
    /**
     * 用于对RecyclerView子项的数据进行赋值
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mFruitList.get(position).getTitle());
        holder.info.setText(mFruitList.get(position).getSendtime());
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}
