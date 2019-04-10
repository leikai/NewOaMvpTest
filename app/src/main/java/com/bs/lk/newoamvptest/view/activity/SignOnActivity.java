package com.bs.lk.newoamvptest.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.TabsActivity;
import com.bs.lk.newoamvptest.bean.TokenRoot;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.common.Contstants;
import com.bs.lk.newoamvptest.util.PinYin.SharedPreferencesUtil;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;
import com.wangnan.library.model.Point;
import com.wangnan.library.painter.AliPayPainter;

public class SignOnActivity extends AppCompatActivity {
    private static final String TAG = "lk";
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private Context _context;
    private GestureLockView mGestureLockView;
    private UseSignOnTask mAuthTask = null;
    private TextView tvUserAccounts;
    private Button btnHandGestureUnlockBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        _context = SignOnActivity.this;
        mGestureLockView = findViewById(R.id.glv);
        tvUserAccounts = findViewById(R.id.tv_user_accounts);
        btnHandGestureUnlockBack = findViewById(R.id.btn_hand_gesture_unlock_back);


        Intent intent = getIntent();
        String userAccount = intent.getStringExtra("userAccount");
        Log.e("帐号：",userAccount);
        tvUserAccounts.setText(userAccount);
        mGestureLockView.setPainter(new NiCaiFu360Painter());

        //设置手势监听器
        mGestureLockView.setGestureLockListener(new OnGestureLockListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(String progress) {

            }

            @Override
            public void onComplete(String result) {
                Log.d(TAG, "onComplete: "+"  "+result);
                if (TextUtils.isEmpty(result)){
                    return;
                }else {
                    String userName = "gaohongwei";
                    mAuthTask = new UseSignOnTask(userAccount, result);
                    mAuthTask.execute();
                    return;

                }
            }
        });

        btnHandGestureUnlockBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignOnActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UseSignOnTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mHandPassword;
        private String mMessage;

        UseSignOnTask(String username, String handPassword) {
            mUsername = username;
            mHandPassword = handPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    return pieces[1].equals(mHandPassword);
                }
            }
            UserNewBean user = new UserNewBean();
            String token = null;

            user.setUserName(mUsername);
            user.setHandPassword(mHandPassword);
//            user.setUserPassword(mHandPassword);

            String resultStr = WebServiceUtil.getInstance().createSession(mUsername,mHandPassword);
            Log.e("resultStr",""+resultStr);
            if (resultStr!=null){
                if (!TextUtils.isEmpty(resultStr)) {
                    TokenRoot resps = JSON.parseObject(resultStr,TokenRoot.class);
                    if (resps!=null){
                        Log.e("resps",""+resps.getMsginfo());
                        if (resps.getOpresult()&&resps.getMsg().equals("登录成功!")){
                            token = resps.getMsginfo().substring(10);
                            token = token.substring(0,token.length()-2);
                            Log.e("获取到的Token：",""+token);
                            UserNewBean userInfo = WebServiceUtil.getInstance().getUserInfo(mUsername,null);
                            String ceshi = userInfo.getOrgid();
                            Log.e("ceshi",""+ceshi);
//                            Contstants.titleName = userInfo.getOrgname();
                            user.copyUserInfo(userInfo);
//                            WebServiceUtil.getInstance().getUserDepartmentInfo(user);
//                            WebServiceUtil.getInstance().getUserRoleInfo(user);

                            CApplication.getInstance().setUser(user);
                            CApplication.getInstance().setToken(token);
                            return true;
                        }
                        mMessage = resps.getMsginfo();
                        return false;

                    }else {
                        mMessage = "网络问题稍后重试";
                        return false;
                    }

                }
            }
            mMessage = "网络问题稍后重试";
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
//            mProgressView.stop();
            if (!TextUtils.isEmpty(mMessage)) {
                Toast.makeText(getBaseContext(), mMessage, Toast.LENGTH_LONG).show();
                mGestureLockView.clearView();
                return;
            }
            if (success) {
                SharedPreferencesUtil.setString(_context, Contstants.KEY_SP_FILE, Contstants.KEY_SP_LoginName, mUsername);
                String passWord = CApplication.getInstance().getCurrentUser().getUserPassword();
                SharedPreferencesUtil.setString(_context, Contstants.KEY_SP_FILE, Contstants.KEY_SP_Password, passWord);
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), TabsActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
//            mProgressView.stop();
        }
    }



    /**
     * 仿360你财富绘制者
     */
    public static class NiCaiFu360Painter extends AliPayPainter {
        //绘制正常状态的点

        @Override
        public void drawNormalPoint(Point point, Canvas canvas, Paint normalPaint) {
            //1.绘制中心点
            normalPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(point.x,point.y,point.radius/4.0F,normalPaint);
//            //2.绘制边界圆
//            normalPaint.setStyle(Paint.Style.STROKE);
//            normalPaint.setStrokeWidth(6);
//            canvas.drawCircle(point.x,point.y,getGestureLockView().getRadius(),normalPaint);
        }

        @Override
        public void drawPressPoint(Point point, Canvas canvas, Paint pressPaint) {
            //1.绘制中心点
            pressPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(point.x,point.y,point.radius/4.0F,pressPaint);
            //2.绘制边界圆
            pressPaint.setStyle(Paint.Style.STROKE);
            pressPaint.setStrokeWidth(6);
            canvas.drawCircle(point.x,point.y,getGestureLockView().getRadius(),pressPaint);
        }

        @Override
        public void drawErrorPoint(Point point, Canvas canvas, Paint errorPaint) {
            //1.绘制中心点
            errorPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(point.x,point.y,point.radius/4.0F,errorPaint);
            //2.绘制边界圆
            errorPaint.setStyle(Paint.Style.STROKE);
            errorPaint.setStrokeWidth(6);
            canvas.drawCircle(point.x,point.y,getGestureLockView().getRadius(),errorPaint);
        }
    }
}
