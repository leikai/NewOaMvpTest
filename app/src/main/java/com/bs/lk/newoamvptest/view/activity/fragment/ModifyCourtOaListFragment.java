package com.bs.lk.newoamvptest.view.activity.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.CourtOaListsAdapter;
import com.bs.lk.newoamvptest.adapter.UsersAdapter;
import com.bs.lk.newoamvptest.bean.CourtOaUsageStatementBean;
import com.bs.lk.newoamvptest.cache.CSharedPreferences;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.bs.lk.newoamvptest.view.activity.ModifyCourtOaListAddActivity;
import com.bs.lk.newoamvptest.view.activity.ModifyCourtOaListDeleteActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class ModifyCourtOaListFragment extends BaseFragment implements View.OnClickListener {

    RecyclerView mRVCourtOaList;
    private List<CourtOaUsageStatementBean> courts;
    private String courtName = "";
    private String useType = "";
    private String useTime = "";

    EditText etCourtName;
    EditText etUseType;
    EditText etUseTime;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "修改启用手机OA的法院列表";
        mShowBtnBack = View.VISIBLE;
        mLogo = View.INVISIBLE;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_modify_courtoa_list, container, savedInstanceState);
    }


    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRVCourtOaList = (RecyclerView) mRootView.findViewById(R.id.rv_courtoa_list);
        mRootView.findViewById(R.id.btn_add).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_delete).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_modify).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_query).setOnClickListener(this);


    }

    @Override
    public void onResume() {

        LitePal.getDatabase();
        courts = DataSupport.findAll(CourtOaUsageStatementBean.class);
        setDataToView();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                startActivity(new Intent(getActivity(), ModifyCourtOaListAddActivity.class));
                break;
            case R.id.btn_delete:
                startActivity(new Intent(getActivity(),ModifyCourtOaListDeleteActivity.class));
                break;
            case R.id.btn_modify:
                break;
            case R.id.btn_query:
                break;
                default:
                    break;
        }

    }
    /**
     * 将人员数据部署到页面
     */
    private void setDataToView() {
        if (mPreFragment == null){
            mPreFragment = (BaseFragment) getParentFragment();

        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRVCourtOaList.setLayoutManager(layoutManager);
        CourtOaListsAdapter usersAdapter = new CourtOaListsAdapter(courts,mPreFragment);
        mRVCourtOaList.setAdapter(usersAdapter);
        mRVCourtOaList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        usersAdapter.notifyDataSetChanged();
    }


}
