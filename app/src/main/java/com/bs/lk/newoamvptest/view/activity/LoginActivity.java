package com.bs.lk.newoamvptest.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.TabsActivity;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.common.Contstants;
import com.bs.lk.newoamvptest.presenter.ILoginPresenter;
import com.bs.lk.newoamvptest.presenter.LoginPresenter;
import com.bs.lk.newoamvptest.util.PinYin.SharedPreferencesUtil;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lk
 */
public class LoginActivity extends AppCompatActivity implements ILoginView {

    @BindView(R.id.fahui)
    ImageView fahui;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.sign_in_button)
    Button signInButton;
//    @BindView(R.id.login_form)
//    LinearLayout loginForm;

    ILoginPresenter loginPresenter;
    @BindView(R.id.login_progress)
    ImageView loginProgress;
    @BindView(R.id.courtoa_name)
    TextView courtoaName;
    @BindView(R.id.user_account)
    RelativeLayout userAccount;
    @BindView(R.id.user_password)
    RelativeLayout userPassword;
    @BindView(R.id.tv_login_handpassword)
    TextView tvLoginHandpassword;
    private String name = "";
    private String password = "";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = LoginActivity.this;
        ButterKnife.bind(this);

        String name = SharedPreferencesUtil.getString(mContext, Contstants.KEY_SP_FILE, Contstants.KEY_SP_LoginName, null);
        String pass = SharedPreferencesUtil.getString(mContext, Contstants.KEY_SP_FILE, Contstants.KEY_SP_Password, null);
        etUsername.setText(name);
        etPassword.setText(pass);
        initEvent();
        loginPresenter = new LoginPresenter(this);
    }

    private void initEvent() {
        name = etUsername.getText().toString().trim();
        Log.e("name",""+name);
        password = etPassword.getText().toString().trim();
        Log.e("password",""+password);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                Glide.with(LoginActivity.this).load("http://img.lanrentuku.com/img/allimg/1212/5-121204193Q9-50.gif")
                        .into(loginProgress);


                if ("admin".equals(name) && "1231".equals(password)) {
                    SharedPreferencesUtil.setString(LoginActivity.this, Contstants.KEY_SP_FILE, Contstants.KEY_SP_LoginName, name);
                    SharedPreferencesUtil.setString(LoginActivity.this, Contstants.KEY_SP_FILE, Contstants.KEY_SP_Password, password);
                    SharedPreferencesUtil.setString(LoginActivity.this, Contstants.KEY_SP_FILE, Contstants.KEY_SP_Token, "");
                    UserNewBean user = new UserNewBean();
                    user.setUserName(name);
                    user.setUserPassword(password);
                    CApplication.getInstance().setUser(user);
                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), TabsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    name = etUsername.getText().toString().trim();
                    Log.e("name",""+name);
                    password = etPassword.getText().toString().trim();
                    Log.e("password",""+password);
                    loginPresenter.doLogin(name, password);
                }
            }
        });

        tvLoginHandpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignOnActivity.class);
                intent.putExtra("userAccount",name);
                startActivity(intent);
                finish();
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
            Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void showLoading(String msg, int progress) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void showErrorMsg(String msg, String content) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
