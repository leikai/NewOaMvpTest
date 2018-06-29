package com.bs.lk.newoamvptest.view.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.lk.newoamvptest.R;

import java.util.Vector;

public class SettingManagerFragment extends BaseFragment {
    public static final int CHILD_TYPE_SETTING = 0;
    public static final int CHILD_TYPE_MODIFYPWD = 1;
    public static final int CHILD_TYPE_MODIFYUSERNAME = 2;
    public static final int CHILD_TYPE_MODIFYSERVERIP = 3;
    private SettingFragment mSettingFragment;
    private ModifyPasswordFragment mModifyPwdFragment;
    private ModifyUserNameFragment mModifyUserNameFragment;
    private ModifyServerIPFragment mModifyServerIPFragment;
    private BaseFragment mCurChildFragment;
    private Vector<BaseFragment> mFragmentStack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentStack = new Vector<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_settingmanager, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mChildType = CHILD_TYPE_SETTING;
        showChildFragment(null);
    }

    @Override
    public void showChildFragment(Bundle bundle) {
        super.showChildFragment(bundle);
        switch (mChildType) {
            case CHILD_TYPE_SETTING:
                if (mSettingFragment == null) {
                    mSettingFragment = new SettingFragment();
                    mSettingFragment.setPreFragment(this);
                }
                addChildFragment(mSettingFragment, R.id.content);
                mCurChildFragment = mSettingFragment;
                break;
            case CHILD_TYPE_MODIFYPWD:
                if (mModifyPwdFragment == null) {
                    mModifyPwdFragment = new ModifyPasswordFragment();
                    mModifyPwdFragment.setPreFragment(mSettingFragment);
                }
                addChildFragment(mModifyPwdFragment, R.id.content);
                hideFragment(mSettingFragment);
                mCurChildFragment = mModifyPwdFragment;
                break;
            case CHILD_TYPE_MODIFYUSERNAME:
                if (mModifyUserNameFragment == null) {
                    mModifyUserNameFragment = new ModifyUserNameFragment();
                    mModifyUserNameFragment.setPreFragment(mSettingFragment);
                }
                addChildFragment(mModifyUserNameFragment, R.id.content);
                hideFragment(mSettingFragment);
                mCurChildFragment = mModifyUserNameFragment;
                break;
            case CHILD_TYPE_MODIFYSERVERIP:
                if (mModifyServerIPFragment == null) {
                    mModifyServerIPFragment = new ModifyServerIPFragment();
                    mModifyServerIPFragment.setPreFragment(mSettingFragment);
                }
                addChildFragment(mModifyServerIPFragment, R.id.content);
                hideFragment(mSettingFragment);
                mCurChildFragment = mModifyServerIPFragment;
                break;
        }
        mFragmentStack.add(mCurChildFragment);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mCurChildFragment.onHiddenChanged(hidden);
    }

    @Override
    public boolean onBackPressed() {
        boolean handle = false;
        if (mFragmentStack.size() > 1) {
            handle = mCurChildFragment.onBackPressed();
            if (!handle) {
                if (mCurChildFragment instanceof ModifyUserNameFragment) {
                    mModifyUserNameFragment = null;
                } else if (mCurChildFragment instanceof ModifyPasswordFragment) {
                    mModifyPwdFragment = null;
                }
                mFragmentStack.remove(mCurChildFragment);
                mCurChildFragment = mFragmentStack.get(mFragmentStack.size() - 1);
                return true;
            }
        }
        return handle;
    }



}
