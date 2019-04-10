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
import com.bs.lk.newoamvptest.bean.AttendanceDataBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.util.BitmapUtil;
import com.bs.lk.newoamvptest.view.activity.fragment.BaseFragment;
import com.bs.lk.newoamvptest.view.activity.fragment.ContactsNewManagerFragment;
import com.bs.lk.newoamvptest.view.activity.fragment.UserDetailInfoFragment;

import java.util.List;

/**
 * 考勤详情适配器
 * @author lk
 */
public class AttendanceStatisticsDetailAdapter extends RecyclerView.Adapter<AttendanceStatisticsDetailAdapter.ViewHolder>{
    private Context mcontext;
    private List<AttendanceDataBean> attendanceDataBeanList;
    private String emptyDBValue = "null";

    public AttendanceStatisticsDetailAdapter(List<AttendanceDataBean> attendanceDataBeanList) {
        this.attendanceDataBeanList = attendanceDataBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mcontext == null){
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_attendance_statistics_detail,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.llUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                AttendanceDataBean user = attendanceDataBeanList.get(position);


            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceDataBean attendanceDataBean = attendanceDataBeanList.get(position);
        holder.date.setText("考勤日期:"+attendanceDataBean.getDate());
        holder.userName.setText(attendanceDataBean.getUsername());

        if (emptyDBValue.equals(attendanceDataBean.getSsignintimte())){
            holder.sSignTime.setText("缺卡");
        }else {
            holder.sSignTime.setText(attendanceDataBean.getSsignintimte());
        }
        if (emptyDBValue.equals(attendanceDataBean.getAmbdkwz())){
            holder.sSignPosition.setText("未知位置");
        }else {
            holder.sSignPosition.setText(attendanceDataBean.getAmbdkwz());
        }
        if (emptyDBValue.equals(attendanceDataBean.getAmbstate())){
            holder.sSignState.setText("正常");
        }else {
            holder.sSignState.setText(attendanceDataBean.getAmbstate());
        }


        if (emptyDBValue.equals(attendanceDataBean.getSsignbacktime())){
            holder.sSignWorkedTime.setText("缺卡");
        }else {
            holder.sSignWorkedTime.setText(attendanceDataBean.getSsignintimte());
        }
        if (emptyDBValue.equals(attendanceDataBean.getAmedkwz())){
            holder.sSignWorkedPosition.setText("未知位置");
        }else {
            holder.sSignWorkedPosition.setText(attendanceDataBean.getAmedkwz());
        }
        if (emptyDBValue.equals(attendanceDataBean.getAmestate())){
            holder.sSignWorkedState.setText("正常");
        }else {
            holder.sSignWorkedState.setText(attendanceDataBean.getAmestate());
        }

        if (emptyDBValue.equals(attendanceDataBean.getXsignintimte())){
            holder.sPmSignTime.setText("缺卡");
        }else {
            holder.sPmSignTime.setText(attendanceDataBean.getXsignintimte());
        }
        if (emptyDBValue.equals(attendanceDataBean.getPmbdkwz())){
            holder.sPmSignPosition.setText("未知位置");
        }else {
            holder.sPmSignPosition.setText(attendanceDataBean.getPmbdkwz());
        }
        if (emptyDBValue.equals(attendanceDataBean.getPmbstate())){
            holder.sPmSignState.setText("正常");
        }else {
            holder.sPmSignState.setText(attendanceDataBean.getPmbstate());
        }

        if (emptyDBValue.equals(attendanceDataBean.getXsignbacktime())){
            holder.sPmSignWorkedTime.setText("缺卡");
        }else {
            holder.sPmSignWorkedTime.setText(attendanceDataBean.getXsignbacktime());
        }
        if (emptyDBValue.equals(attendanceDataBean.getPmedkwz())){
            holder.sPmSignWorkedPosition.setText("未知位置");
        }else {
            holder.sPmSignWorkedPosition.setText(attendanceDataBean.getPmedkwz());
        }
        if (emptyDBValue.equals(attendanceDataBean.getPmestate())){
            holder.sPmSignWorkedState.setText("正常");
        }else {
            holder.sPmSignWorkedState.setText(attendanceDataBean.getPmestate());
        }
        }

    @Override
    public int getItemCount() {
        return attendanceDataBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView userName;

        LinearLayout llUserItem;
        TextView sSignTime;
        TextView sSignPosition;
        TextView sSignState;
        TextView sSignWorkedTime;
        TextView sSignWorkedPosition;
        TextView sSignWorkedState;
        TextView sPmSignTime;
        TextView sPmSignPosition;
        TextView sPmSignState;
        TextView sPmSignWorkedTime;
        TextView sPmSignWorkedPosition;
        TextView sPmSignWorkedState;
        public ViewHolder(View view){
            super(view);
            date = view.findViewById(R.id.tv_sign_date);
            userName = view.findViewById(R.id.tv_sign_user_name);
            llUserItem = view.findViewById(R.id.ll_user_item);
            sSignTime = view.findViewById(R.id.tv_ssign_time);
            sSignPosition = view.findViewById(R.id.tv_ssign_position);
            sSignState = view.findViewById(R.id.tv_ssign_state);
            sSignWorkedTime = view.findViewById(R.id.tv_ssign_worked_time);
            sSignWorkedPosition = view.findViewById(R.id.tv_ssign_worked_position);
            sSignWorkedState = view.findViewById(R.id.tv_ssign_worked_state);
            sPmSignTime = view.findViewById(R.id.tv_pm_ssign_time);
            sPmSignPosition = view.findViewById(R.id.tv_pm_ssign_position);
            sPmSignState = view.findViewById(R.id.tv_pm_ssign_state);
            sPmSignWorkedTime = view.findViewById(R.id.tv_pm_ssign_worked_time);
            sPmSignWorkedPosition = view.findViewById(R.id.tv_pm_ssign_worked_position);
            sPmSignWorkedState = view.findViewById(R.id.tv_pm_ssign_worked_state);


        }
    }
}
