package com.bs.lk.newoamvptest.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.CourtOaUsageStatementBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifyCourtOaListAddActivity extends AppCompatActivity {

    @BindView(R.id.et_court_name_add)
    EditText etCourtNameAdd;
    @BindView(R.id.et_use_type)
    EditText etUseType;
    @BindView(R.id.et_use_time)
    EditText etUseTime;
    @BindView(R.id.btn_add)
    LinearLayout btnAdd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_court_oa_list_add);
        ButterKnife.bind(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courtName = etCourtNameAdd.getText().toString().trim();
                Log.e("courtName",""+courtName);
                String useType = etUseType.getText().toString().trim();
                Log.e("useType",""+useType);
                String useTime = etUseTime.getText().toString().trim();
                Log.e("useTime",""+useTime);
                CourtOaUsageStatementBean court = new CourtOaUsageStatementBean();
                        court.setCourtName(courtName);
                        court.setUseType(useType);
                        court.setUseTime(useTime);
                        court.save();
                        finish();
            }
        });
    }
}
