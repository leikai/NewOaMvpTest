package com.bs.lk.newoamvptest.view.activity.fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bs.lk.newoamvptest.R;

import java.util.Vector;


/**
 * 通信录碎片管理器
 * @author asus
 */
public class ContactsNewManagerFragment extends BaseFragment {
    public static final int CHILD_TYPE_CONTACTS = 0;
    public static final int CHILD_TYPE_USERINFO = 1;
    private ContactsFragment mContactsFragment;
    private UserDetailInfoFragment mUserInfoFragment;
    private BaseFragment mCurChildFragment;
    private Vector<BaseFragment> mFragmentStack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentStack = new Vector<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_contactsmanager_new, container, savedInstanceState);
    }

    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mChildType = CHILD_TYPE_CONTACTS;
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
            case CHILD_TYPE_CONTACTS:
                if (mContactsFragment == null) {
                    mContactsFragment = new ContactsFragment();
                    mContactsFragment.setPreFragment(this);
                }
                addChildFragment(mContactsFragment, R.id.content);
                mCurChildFragment = mContactsFragment;
                break;
            case CHILD_TYPE_USERINFO:
                if (mUserInfoFragment == null) {
                    mUserInfoFragment = new UserDetailInfoFragment();
                    mUserInfoFragment.setPreFragment(mContactsFragment);
                }
                mUserInfoFragment.setArguments(bundle);
                addChildFragment(mUserInfoFragment, R.id.content);
                hideFragment(mContactsFragment);
                mCurChildFragment = mUserInfoFragment;
                break;
                default:
                    break;
        }
        mFragmentStack.add(mCurChildFragment);
    }

    @Override
    public boolean onBackPressed() {
        boolean handle = false;
        if (mFragmentStack.size() > 1) {
            handle = mCurChildFragment.onBackPressed();
            if (!handle) {
                if (mCurChildFragment instanceof UserDetailInfoFragment) {
                    mUserInfoFragment = null;
                }
                mFragmentStack.remove(mCurChildFragment);
                mCurChildFragment = mFragmentStack.get(mFragmentStack.size() - 1);


                return true;
            }
        }
        return handle;
    }
}
