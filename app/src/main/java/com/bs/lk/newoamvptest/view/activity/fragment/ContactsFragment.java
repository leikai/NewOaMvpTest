package com.bs.lk.newoamvptest.view.activity.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.DepartmentAdapter;
import com.bs.lk.newoamvptest.adapter.UsersAdapter;
import com.bs.lk.newoamvptest.bean.DepartmentNewBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.util.PinYin.PinYinUtil;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import org.litepal.LitePal;
import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * 碎片：通讯录
 * @author baggio
 * @date 2017/2/3
 */

public class ContactsFragment extends BaseFragment {

    /**
     * 文字图片的颜色值
     */
    public static final String[] ImageBgColor={
            "#568AAD",
            "#17c295",
            "#4DA9EB",
            "#F2725E",
            "#B38979",
            "#568AAD"
    };

    private TextView tvCourtoaName;
    private AppCompatSpinner spinnerDepartment;
    /**
     * 人员列表
     */
    private RecyclerView rvUsers;
    private RefreshGroupTask mRefreshGroupTask;
    private RefreshContactsTask mRefreshContactsTask;
    private ImageView ivSearch;
    private EditText etSearch;
    private TextView tvEmpty;

    /**
     * 当前登录人的个人信息
     */
    private UserNewBean user;
    private String token;
    /**
     * 法院ID
     */
    private String orgId;
    /**
     * 法院名称
     */
    private String orgName;
    /**
     * 部门分组集合
     */
    public List<DepartmentNewBean> mDepartments = new ArrayList<>();
    /**
     * 联系人分组集合
     */
    public List<UserNewBean> mUsersList =  new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private UsersAdapter usersAdapter;
    private static final String TAG = "Constraints";

    /**
     * 删除数据库数据
     */
    private Button btnDeleteDB;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "通讯录";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_contacts, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tvCourtoaName = mRootView.findViewById(R.id.tv_courtoa_name);
        spinnerDepartment = mRootView.findViewById(R.id.spinner_department);
        tvEmpty = mRootView.findViewById(R.id.tv_empty);
        rvUsers = mRootView.findViewById(R.id.rv_user);
        ivSearch = mRootView.findViewById(R.id.iv_search);
        etSearch = mRootView.findViewById(R.id.et_search);
        btnDeleteDB = mRootView.findViewById(R.id.btn_delete_db);
        btnDeleteDB.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        token = CApplication.getInstance().getCurrentToken();
        user = CApplication.getInstance().getCurrentUser();
        Log.d(TAG, "initData: "+user.getEmpname());
        String courtoaName = user.getOrgname();
        tvCourtoaName.setText(courtoaName);
        //查询数据库中的数据
        mDepartments = LitePal.where("orgname = ?",courtoaName).find(DepartmentNewBean.class);

        if (mDepartments.size() == 0){
            mRefreshGroupTask = new RefreshGroupTask(user.getOrgid());
            mRefreshGroupTask.execute();
        }else {
            setDeptData();
        }
    }

    private void setDeptData() {
        DepartmentNewBean departmentNewBean = new DepartmentNewBean();
        departmentNewBean.setDeptName("所有部门");
        departmentNewBean.setDeptPid("");
        orgId = mDepartments.get(1).getOrgid();
        orgName = mDepartments.get(1).getOrgname();
        departmentNewBean.setOrgid(orgId);
        departmentNewBean.setOrgname(orgName);
        mDepartments.add(0,departmentNewBean);
        setDateToView(mDepartments);
    }

    /**
     * 主线程接收部门数据
     */
    @SuppressLint("HandlerLeak")
    private Handler mUpdateGroupsListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mDepartments = (List<DepartmentNewBean>) msg.obj;
            DepartmentNewBean departmentNewBean = new DepartmentNewBean();
            departmentNewBean.setDeptName("所有部门");
            departmentNewBean.setDeptPid("");
            orgId = mDepartments.get(1).getOrgid();
            orgName = mDepartments.get(1).getOrgname();
            departmentNewBean.setOrgid(orgId);
            departmentNewBean.setOrgname(orgName);
            mDepartments.add(0,departmentNewBean);
            setDateToView(mDepartments);

        }
    };

    /**
     * 将显示部门数据
     * @param mList
     */
    private  void  setDateToView(final List<DepartmentNewBean> mList){
        DepartmentAdapter departmentAdapter = new DepartmentAdapter(getActivity(), mList);
        //传入的参数分别为 Context , 未选中项的textview , 数据源List
        spinnerDepartment.setAdapter(departmentAdapter);
        departmentAdapter.notifyDataSetChanged();
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String departmentId = mList.get(position).getOid();
                String departmentName = mList.get(position).getDeptName();
                String courtoaName = mList.get(position).getOrgname();
                Log.e("所属法院",""+courtoaName);
                List<UserNewBean> usersList = mList.get(position).getUserNewBeanList();
                mUsersList.clear();
                for (int i= 0; i< usersList.size();i++){
                    if (courtoaName.equals(usersList.get(i).getOrgname())){
                        mUsersList.add(usersList.get(i));
                    }
                }
                Log.e("所属法院人员",""+mUsersList.size());
                int ceshi = mUsersList.size();
                Log.e("部门人员数据",""+ceshi);
                if (mUsersList.size() == 0){
                    mRefreshContactsTask = new RefreshContactsTask(null,departmentId,departmentName,orgId);
                    mRefreshContactsTask.execute();
                }else {
                    setDataToView();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setDataToView();
    }

    /**
     * 主线程接收人员数据
     */
    @SuppressLint("HandlerLeak")
    private Handler mUpdateUsersListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            List<UserNewBean> lists = (List<UserNewBean>) msg.obj;
            mUsersList.clear();
            if (lists != null){
                mUsersList.addAll(lists);
            }
            setDataToView();
            String departmentName = msg.getData().getString("departmentName");
        }
    };

    /**
     * 将人员数据部署到页面
     */
    private void setDataToView() {
        if (mPreFragment == null){
            mPreFragment = (BaseFragment) getParentFragment();

        }
        if (mUsersList == null){
            rvUsers.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }else {
            rvUsers.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            if (layoutManager == null){
                layoutManager = new LinearLayoutManager(getActivity());
                rvUsers.setLayoutManager(layoutManager);
            }

            if (usersAdapter == null){
                usersAdapter = new UsersAdapter(mUsersList,mPreFragment);
                rvUsers.setAdapter(usersAdapter);

                rvUsers.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

            }else {
                int i = mUsersList.size();
                Log.e("mUsersList",""+i);
                usersAdapter.notifyDataSetChanged();
            }
        }



        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ceshi  = etSearch.getText().toString().trim();
                Log.e("ceshi",""+ceshi);
                List<DepartmentNewBean> departments =  LitePal.findAll(DepartmentNewBean.class);
                //判断数据库中的部门数据不为空，则从数据库中查询数据
                if (departments.size() != 0){

                }
                mRefreshContactsTask = new RefreshContactsTask(ceshi,null,null,orgId);
                mRefreshContactsTask.execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    /**
     * 请求部门的数据
     */
    private class RefreshGroupTask extends AsyncTask<Void, Void, List<DepartmentNewBean>> {
        String mCourtoaName;

        RefreshGroupTask(String courtoaOid) {
            //法院Oid
            this.mCourtoaName = courtoaOid;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<DepartmentNewBean> doInBackground(Void... params) {
            Log.e("法院名称",""+mCourtoaName);
            List<DepartmentNewBean> departments = WebServiceUtil.getInstance().getDepartmentList(token,mCourtoaName);
            LitePal.saveAll(departments);
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

    /**
     * 请求联系人数据
     */
    private class RefreshContactsTask extends AsyncTask<Void, Void, List<UserNewBean>> {

        String mUserName = "";
        String mDepartmentId = "";
        String mDepartmentName = "";
        String mCourtId = "";

        public RefreshContactsTask(String userName, String departmentId, String departmentName, String orgId) {
            mUserName = userName;
            mDepartmentId = departmentId;
            mDepartmentName = departmentName;
            mCourtId = orgId;
        }

        @Override
        protected void onPreExecute() {
            // mCustomProgress.start();
        }

        @Override
        protected List<UserNewBean> doInBackground(Void... params) {
            List<UserNewBean> users;
            users = WebServiceUtil.getInstance().getNewUserList(token,mCourtId,mDepartmentId,mUserName);
            if (users != null){
                Collections.sort(users, new PinyinComparator());
            }
            LitePal.saveAll(users);
            return users;
        }

        @Override
        protected void onPostExecute(List<UserNewBean> users) {
            Message msg = mUpdateUsersListHandler.obtainMessage();
            msg.obj = users;
            Bundle bundle = new Bundle();
            bundle.putString("departmentName", mDepartmentName);
            msg.setData(bundle);
            mUpdateUsersListHandler.sendMessage(msg);
        }

        @Override
        protected void onCancelled() {
        }
    }


    /**
     * 将联系人数据进行排序
     */
    private class PinyinComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            String str1 = PinYinUtil.getPinYin(((UserNewBean) o1).getUserName())
                    .toUpperCase();
            String str2 = PinYinUtil.getPinYin(((UserNewBean) o2).getUserName())
                    .toUpperCase();
            return str1.compareTo(str2);
        }

    }

    private class AlphabetComparator implements Comparator<UserNewBean> {
        private RuleBasedCollator collator;

        public AlphabetComparator() {
            collator = (RuleBasedCollator) Collator
                    .getInstance(Locale.CHINA);
        }

        @Override
        public int compare(UserNewBean a1, UserNewBean a2) {
            CollationKey c1 = collator.getCollationKey(a1.getUserName());
            CollationKey c2 = collator.getCollationKey(a2.getUserName());
            return collator.compare(((CollationKey) c1).getSourceString(),
                    ((CollationKey) c2).getSourceString());
        }
    }



    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
