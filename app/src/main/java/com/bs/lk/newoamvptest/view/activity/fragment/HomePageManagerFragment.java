package com.bs.lk.newoamvptest.view.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.lk.newoamvptest.R;

import java.util.Vector;

/**
 * 首页碎片管理器
 * @author lk
 */
public class HomePageManagerFragment extends BaseFragment{
    public static final int CHILD_TYPE_APP = 0;
    private HomePageFragment mHomePageFragment;
    private Vector<BaseFragment> mFragmentStack;
    private BaseFragment mCurChildFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentStack = new Vector<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_appsmanager, container, savedInstanceState);
    }

    @Nullable
    @Override
    public void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mChildType = CHILD_TYPE_APP;
        showChildFragment(null);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mCurChildFragment.onHiddenChanged(hidden);
    }

    @Override
    public void showChildFragment(Bundle bundle) {
        super.showChildFragment(bundle);
        switch (mChildType) {
            case CHILD_TYPE_APP:
                if (mHomePageFragment == null) {
                    mHomePageFragment = new HomePageFragment();
                    mHomePageFragment.setPreFragment(this);
                }
                addChildFragment(mHomePageFragment, R.id.content);
                mCurChildFragment = mHomePageFragment;
                break;
                default:
                    break;
        }
        mFragmentStack.add(mCurChildFragment);
        Log.e("mFragmentStack",""+mFragmentStack.size());
    }
}
