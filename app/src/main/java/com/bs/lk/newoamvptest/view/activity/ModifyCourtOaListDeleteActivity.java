package com.bs.lk.newoamvptest.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.CourtOaUsageStatementBean;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifyCourtOaListDeleteActivity extends AppCompatActivity {

    @BindView(R.id.et_court_name_delete)
    EditText etCourtNameDelete;
    @BindView(R.id.et_use_type_delete)
    EditText etUseTypeDelete;
    @BindView(R.id.et_use_time_delete)
    EditText etUseTimeDelete;
    @BindView(R.id.btn_delete)
    LinearLayout btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_court_oa_list_delete);
        ButterKnife.bind(this);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CourtOaUsageStatementBean> courtOaLists = DataSupport.where("courtName = ?",etCourtNameDelete.getText().toString().trim()).find(CourtOaUsageStatementBean.class);
                DataSupport.delete(CourtOaUsageStatementBean.class,courtOaLists.get(0).getId());
                finish();
            }
        });




    }
}
