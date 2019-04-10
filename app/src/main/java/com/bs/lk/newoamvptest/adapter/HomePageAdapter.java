package com.bs.lk.newoamvptest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.AppBean;
import com.bs.lk.newoamvptest.bean.ToDoDatainfoBean;
import com.bs.lk.newoamvptest.view.activity.AlreadyWebActivity;
import com.bs.lk.newoamvptest.view.activity.NoticeWebActivity;
import com.bs.lk.newoamvptest.view.activity.TotoWebActivity;
import com.bs.lk.newoamvptest.view.activity.fragment.BaseFragment;
import com.bs.lk.newoamvptest.view.activity.fragment.HomePageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页适配器
 * @author lk
 */
public class HomePageAdapter extends BaseAdapter {
    private final int TYPE_LEFT_ITEM = 0;
    private final int TYPE_RIGHT_ITEM = 1;
    private final int TYPE_COUNT = 2;
    private HomePageFragment mFragment;
    private  Context context;
    List<AppBean> mApps;
    private List<ToDoDatainfoBean> toDoTasks = new ArrayList<>();
    private String toDoTaskName = "待办事宜";
    /**
     * 判断四大块的左右，
     * 方法：除以2 ，然后取余，余数为0为左，余数为1，则为右
     */
    private int judgeLeftOrRight = 2;
    public HomePageAdapter(Context context,HomePageFragment fragment, List<AppBean> apps, List<ToDoDatainfoBean> toDoTasks) {
        this.context = context;
        this.mFragment = fragment;
        this.mApps = apps;
        this.toDoTasks = toDoTasks;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            int appType = 0;
            AppBean app = (AppBean) view.getTag();
            switch (app.getFragmentIndex()) {
                case AppBean.FRAGMENT_TO_DO_TASK: {
                    context.startActivity(new Intent(context, TotoWebActivity.class));
                    break;
                }
                case AppBean.FRAGMENT_HISTORY_TASK: {
                    context.startActivity(new Intent(context, AlreadyWebActivity.class));
                    break;
                }
                case AppBean.FRAGMENT_NOTICE_TASK: {
                    context.startActivity(new Intent(context, NoticeWebActivity.class));
                    break;
                }
                default: {
                    Toast.makeText(mFragment.getContext(), "暂不支持该功能", Toast.LENGTH_LONG).show();
                    break;
                }
            }
            bundle.putInt(BaseFragment.PARAM_CHILD_TYPE, appType);
            mFragment.showChildFragment(bundle);
        }
    };

    @Override
    public int getItemViewType(int position) {
        if (position % judgeLeftOrRight == 0) {
            return TYPE_LEFT_ITEM;
        }
        return TYPE_RIGHT_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return mApps.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AppViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.app_homepage_item, null,
                    false);
            holder = new AppViewHolder();
            holder.mContainerView = view.findViewById(R.id.home_page_view_ll_dbsy);
            holder.mAppNameView = (TextView) view.findViewById(R.id.tv_todo);
            holder.mAppIconView = (TextView) view.findViewById(R.id.tv_click);
            holder.mTodoTaskBrdgeView = (TextView) view.findViewById(R.id.tv_todo_circle);
            view.setTag(holder);
        } else {
            holder = (AppViewHolder) view.getTag();
        }
        AppBean app = mApps.get(i);
        holder.mContainerView.setTag(app);
        holder.mContainerView.setOnClickListener(mOnClickListener);
        holder.mAppNameView.setText(app.getName());
        holder.mContainerView.setBackgroundResource(app.getResourceId());
        if (toDoTaskName.equals(app.getName())){
            holder.mTodoTaskBrdgeView.setText(String.valueOf(toDoTasks.size()));
        }else {
            holder.mTodoTaskBrdgeView.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    class AppViewHolder {
        private View mContainerView;
        private TextView mAppNameView;
        private TextView mAppIconView;
        private TextView mTodoTaskBrdgeView;
    }

}
