package com.bs.lk.newoamvptest.view.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.cache.CSharedPreferences;
import com.bs.lk.newoamvptest.util.WebServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyServerIPFragment extends BaseFragment implements View.OnClickListener {

    EditText mIPEditView;
    EditText mPortEditView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "修改服务器地址";
        mShowBtnBack = View.VISIBLE;
        mLogo = View.INVISIBLE;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_modify_ip, container, savedInstanceState);
    }


    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mIPEditView = (EditText) mRootView.findViewById(R.id.et_ipaddress);
        mPortEditView = (EditText) mRootView.findViewById(R.id.et_port);
        mRootView.findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            String ip = mIPEditView.getText().toString();
            String port = mPortEditView.getText().toString();
            JSONObject json = new JSONObject();
            if (!TextUtils.isEmpty(ip)) {
                try {
                    json.put("host", ip);
                    if (!TextUtils.isEmpty(port)) {
                        json.put("port", Integer.parseInt(port));
                    }
                    CSharedPreferences.getInstance().saveServerInfo(json.toString());
                    WebServiceUtil.getInstance().initServerInfo();
                    Toast.makeText(getContext(),"服务器地址修改成功",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
