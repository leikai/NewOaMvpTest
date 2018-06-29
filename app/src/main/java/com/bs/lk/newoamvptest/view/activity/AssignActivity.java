package com.bs.lk.newoamvptest.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bs.lk.newoamvptest.R;

public class AssignActivity extends AppCompatActivity implements IAssignView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
    }
}
