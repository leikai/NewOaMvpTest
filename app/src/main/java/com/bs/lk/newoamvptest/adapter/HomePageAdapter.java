package com.bs.lk.newoamvptest.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.AppBean;
import com.bs.lk.newoamvptest.view.activity.fragment.BaseFragment;
import com.bs.lk.newoamvptest.view.activity.fragment.HomePageFragment;
import com.bs.lk.newoamvptest.view.activity.fragment.HomePageManagerFragment;

import java.util.List;

public class HomePageAdapter extends BaseAdapter {
    private final int TYPE_LEFT_ITEM = 0;
    private final int TYPE_RIGHT_ITEM = 1;
    private final int TYPE_COUNT = 2;
    private HomePageFragment mFragment;
    List<AppBean> mApps;

    public HomePageAdapter(HomePageFragment fragment, List<AppBean> apps) {
        this.mFragment = fragment;
        this.mApps = apps;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            int app_type = 0;
            AppBean app = (AppBean) view.getTag();
            switch (app.getFragmentIndex()) {
                case AppBean.FRAGMENT_TO_DO_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_TODOTASKLIST;
                    break;
                }
                case AppBean.FRAGMENT_HISTORY_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_HISTORYTASKLIST;
                    break;
                }
                case AppBean.FRAGMENT_NOTICE_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_NOTICETASKLIST;
                    break;
                }
                case AppBean.FRAGMENT_MANAGER_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_TASKMANAGER;
                    break;
                }
                case AppBean.FRAGMENT_CABINET_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_CABINETTASKLIST;
                    break;
                }
                case AppBean.FRAGMENT_MESSAGE_MACHINE: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_MESSAGEMACHINE;
                    break;
                }
                default: {
                    Toast.makeText(mFragment.getContext(), "暂不支持该功能", Toast.LENGTH_LONG).show();
//                    app_type = AppsManagerFragment.CHILD_TYPE_TODOTASKLIST;
                    break;
                }
            }
            bundle.putInt(BaseFragment.PARAM_CHILD_TYPE, app_type);
            mFragment.showChildFragment(bundle);
        }
    };

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
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
            /*switch (getItemViewType(i)) {
                case TYPE_LEFT_ITEM:
                    view = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.app_left_item, null,
                            false);
                    break;
                case TYPE_RIGHT_ITEM:
                    view = LayoutInflater.from(mFragment.getContext()).inflate(R.layout
                                    .app_right_item, null,
                            false);
                    break;
            }*/
            view = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.app_homepage_item, null,
                    false);
            holder = new AppViewHolder();
            holder.mContainerView = view.findViewById(R.id.home_page_view_ll_dbsy);
            holder.mAppNameView = (TextView) view.findViewById(R.id.tv_todo);
            holder.mAppIconView = (TextView) view.findViewById(R.id.tv_click);
            view.setTag(holder);
        } else {
            holder = (AppViewHolder) view.getTag();
        }
        AppBean app = mApps.get(i);
        holder.mContainerView.setTag(app);
        holder.mContainerView.setOnClickListener(mOnClickListener);
//        holder.mAppIconView.setImageResource(app.getResourceId());
        holder.mAppNameView.setText(app.getName());
        holder.mContainerView.setBackgroundResource(app.getResourceId());
//        holder.mContainerView.setBackgroundColor(app.getResourceId());
        return view;
    }

    class AppViewHolder {
        private View mContainerView;
        private TextView mAppNameView;
        private TextView mAppIconView;
    }

}
