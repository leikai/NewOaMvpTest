package com.bs.lk.newoamvptest.view.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.CourtBean;

import java.util.Vector;

public class MsgManagerFragment extends BaseFragment {
    private MsgFragment mMsgFragment;
    private BaseFragment mCurChildFragment;
    private Vector<BaseFragment> mFragmentStack;
    public static final int CHILD_TYPE_MSG = 2;//消息

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentStack = new Vector<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, R.layout.fragment_msgmanager, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(mRootView);
        initData();
        mChildType = CHILD_TYPE_MSG;
        showChildFragment(null);
    }
    @Override
    public void showChildFragment(Bundle bundle) {
        super.showChildFragment(bundle);
        switch (mChildType) {
            case CHILD_TYPE_MSG:
                mMsgFragment = new MsgFragment(getActivity(), R.layout.fragment_msg);
                mMsgFragment.setPreFragment(this);
                addChildFragment(mMsgFragment, R.id.content);
                mCurChildFragment = mMsgFragment;
                break;

        }
        mFragmentStack.add(mCurChildFragment);
    }


    @Override
    public boolean onBackPressed() {
        boolean handle = false;
        if (mFragmentStack.size() > 0) {
            handle = mCurChildFragment.onBackPressed();
            if (handle) {
                if (mCurChildFragment instanceof MsgFragment) {
                    return true;
                }
                return true;
            }
        }
        return super.onBackPressed();
    }

    /*当Fragment由可见状态变为不可见状态时保存Fragment的页面显示状态，以备再次加载时使用*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mCurChildFragment.onHiddenChanged(hidden);
    }
    /*隐藏所有fragment*/
    private void hideAllFragment() {
        if (mMsgFragment!=null){
            hideFragment(mMsgFragment);
        }
    }
    private void initView(View mRootView) {

    }


}
