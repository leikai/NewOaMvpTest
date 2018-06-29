package com.bs.lk.newoamvptest.view.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.DepartmentAdapter;
import com.bs.lk.newoamvptest.adapter.UsersAdapter;
import com.bs.lk.newoamvptest.bean.DepartmentNewBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.common.Contstants;
import com.bs.lk.newoamvptest.util.WebServiceUtil;

import java.util.Collections;
import java.util.List;


public class ContactsFragment extends BaseFragment {
    private AppCompatSpinner spinnerCourtoa;
    private AppCompatSpinner spinnerDepartment;
    private RecyclerView rvUsers;//人员列表
    private RefreshGroupTask mRefreshGroupTask;
    private RefreshContactsTask mRefreshContactsTask;
    private String token;
    public List<DepartmentNewBean> mDepartments;//联系人分组集合
    public List<UserNewBean> mUsersList;//联系人分组集合




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "通讯录";
        mShowBtnBack = View.INVISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_contacts, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        spinnerCourtoa = mRootView.findViewById(R.id.spinner_courtoa);
        spinnerDepartment = mRootView.findViewById(R.id.spinner_department);
        rvUsers = mRootView.findViewById(R.id.rv_user);



    }

    protected void initData() {
        token = CApplication.getInstance().getCurrentToken();
        spinnerCourtoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String courtoaName = spinnerCourtoa.getItemAtPosition(position).toString();
                Log.e("courtoaName",""+courtoaName);
                if ("晋中市中级人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_JINZHONG);

                }
                if ("寿阳县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_SHOUYANG);
                }
                if ("榆社县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_YUSHE);
                }
                if ("平遥县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_PINGYAO);
                }
                if ("太谷县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_TAIGU);
                }
                if ("和顺县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_HESHUN);
                }
                if ("榆次区人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_YUCI);
                }
                if ("灵石县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_LINGSHI);
                }
                if ("左权县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_ZUOQUAN);
                }
                if ("昔阳县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_XIYANG);
                }
                if ("祁县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_QIXIAN);
                }
                mRefreshGroupTask.execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Handler mUpdateGroupsListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mDepartments = (List<DepartmentNewBean>) msg.obj;
            setDateToView(mDepartments);

        }
    };
    private  void  setDateToView(List<DepartmentNewBean> mList){
        DepartmentAdapter departmentAdapter = new DepartmentAdapter(getActivity(), mList);
        //传入的参数分别为 Context , 未选中项的textview , 数据源List
        spinnerDepartment.setAdapter(departmentAdapter);
        departmentAdapter.notifyDataSetChanged();
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String departmentId = mList.get(position).getOid();
                String departmentName = mList.get(position).getDeptName();


                mRefreshContactsTask = new RefreshContactsTask(null,departmentId,departmentName);
                mRefreshContactsTask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private Handler mUpdateUsersListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mUsersList = (List<UserNewBean>) msg.obj;
            setDataToView();
            String departmentName = msg.getData().getString("departmentName");
//            adapter.setContacts(list);
//            adapter.notifyDataSetChanged();
//            mSideBar.setVisibility(View.VISIBLE);
        }
    };
    private void setDataToView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvUsers.setLayoutManager(layoutManager);
        UsersAdapter usersAdapter = new UsersAdapter(mUsersList);
        rvUsers.setAdapter(usersAdapter);
        rvUsers.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        usersAdapter.notifyDataSetChanged();
//        adapter = new InvestTheerAdapter(context,list);
//        mContactsListView.getRefreshableView().setAdapter(adapter);
//        mSideBar.setListView(mContactsListView.getRefreshableView());
//        initEvent();
    }

    //请求部门的数据
    private class RefreshGroupTask extends AsyncTask<Void, Void, List<DepartmentNewBean>> {
        String mCourtoaName;

        RefreshGroupTask(String courtoaOid) {
            this.mCourtoaName = courtoaOid;//法院Oid
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<DepartmentNewBean> doInBackground(Void... params) {
            Log.e("法院名称",""+mCourtoaName);
            List<DepartmentNewBean> departments = WebServiceUtil.getInstance().getDepartmentList(token,mCourtoaName);
            return departments;
        }

        @Override
        protected void onPostExecute(List<DepartmentNewBean> departments) {
            if (departments != null) {
                Message msg = mUpdateGroupsListHandler.obtainMessage();
                msg.obj = departments;
                mUpdateGroupsListHandler.sendMessage(msg);
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    //**    // 请求 联系人数据
    private class RefreshContactsTask extends AsyncTask<Void, Void, List<UserNewBean>> {

        String mUserName;
        String mDepartmentId;
        String mDepartmentName;

        //        long time;
        public RefreshContactsTask(String userName, String departmentId, String departmentName) {
            mUserName = userName;
            mDepartmentId = departmentId;
            mDepartmentName = departmentName;
        }

        @Override
        protected void onPreExecute() {
            // mCustomProgress.start();
        }

        @Override
        protected List<UserNewBean> doInBackground(Void... params) {
            List<UserNewBean> users = WebServiceUtil.getInstance().getNewUserList(token,mDepartmentId);
            if (users != null) {   //如果得到的数据不是空的
//                Collections.sort(users, new PinyinComparator());   //联系人按照顺序排序
            }
            return users;
        }

        @Override
        protected void onPostExecute(List<UserNewBean> users) {
            // mRefreshContactsTask = null;
            // mCustomProgress.stop();
            if (users != null) {
                Message msg = mUpdateUsersListHandler.obtainMessage();
                msg.obj = users;
                Bundle bundle = new Bundle();
                bundle.putString("departmentName", mDepartmentName);
                msg.setData(bundle);
                mUpdateUsersListHandler.sendMessage(msg);
            }
        }

        @Override
        protected void onCancelled() {
            // mRefreshContactsTask = null;
        }
    }






    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
