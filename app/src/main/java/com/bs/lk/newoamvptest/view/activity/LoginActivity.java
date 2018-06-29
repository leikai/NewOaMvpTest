package com.bs.lk.newoamvptest.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.TabsActivity;
import com.bs.lk.newoamvptest.common.Contstants;
import com.bs.lk.newoamvptest.presenter.ILoginPresenter;
import com.bs.lk.newoamvptest.presenter.LoginPresenter;
import com.bs.lk.newoamvptest.util.PinYin.SharedPreferencesUtil;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    @BindView(R.id.fahui)
    ImageView fahui;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.login_form)
    LinearLayout loginForm;

    ILoginPresenter loginPresenter;
    @BindView(R.id.login_progress)
    ImageView loginProgress;
    private String name = "";
    private String password = "";
    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        _context = LoginActivity.this;
        ButterKnife.bind(this);

        String name = SharedPreferencesUtil.getString(_context ,Contstants.KEY_SP_FILE, Contstants.KEY_SP_LoginName,null);
        String pass = SharedPreferencesUtil.getString(_context ,Contstants.KEY_SP_FILE, Contstants.KEY_SP_Password,null);
        etUsername.setText(name);
        etPassword.setText(pass);
        initEvent();
        loginPresenter = new LoginPresenter(this);
    }

    private void initEvent() {


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                Glide.with(LoginActivity.this).load("http://img.lanrentuku.com/img/allimg/1212/5-121204193Q9-50.gif")
                        .into(loginProgress);
                name = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                loginPresenter.doLogin(name, password);
            }
        });
    }

    @Override
    public void onClearText() {

    }

    @Override
    public void onLoginResult(Boolean result, int code, String userName, String passWord, String token) {
        loginProgress.setVisibility(View.GONE);
        if (result) {
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            SharedPreferencesUtil.setString(LoginActivity.this, Contstants.KEY_SP_FILE, Contstants.KEY_SP_LoginName, userName);
            SharedPreferencesUtil.setString(LoginActivity.this, Contstants.KEY_SP_FILE, Contstants.KEY_SP_Password, passWord);
            SharedPreferencesUtil.setString(LoginActivity.this, Contstants.KEY_SP_FILE, Contstants.KEY_SP_Token, token);
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), TabsActivity.class);
            startActivity(intent);
            finish();


        } else {
            Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
        }
    }
}
