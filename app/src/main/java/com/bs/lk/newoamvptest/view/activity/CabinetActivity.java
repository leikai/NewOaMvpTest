package com.bs.lk.newoamvptest.view.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.presenter.CabinetPresenter;
import com.bs.lk.newoamvptest.presenter.ICabinetPresenter;
import com.bs.lk.newoamvptest.util.QRCodeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CabinetActivity extends AppCompatActivity implements ICabinetView{

    @BindView(R.id.btn_messagemachine_content_back)
    Button btnMessagemachineContentBack;
    @BindView(R.id.left_view)
    LinearLayout leftView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_imbt)
    ImageButton rightImbt;
    @BindView(R.id.rl_messagemachine_content_title)
    RelativeLayout rlMessagemachineContentTitle;
    @BindView(R.id.tv_scanning_Center)
    TextView tvScanningCenter;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.rl_taskcabinet_title)
    RelativeLayout rlTaskcabinetTitle;
    @BindView(R.id.btn_deposit)
    Button btnDeposit;
    @BindView(R.id.tv_wait_assign_amount)
    TextView tvWaitAssignAmount;
    @BindView(R.id.btn_wait_assign)
    Button btnWaitAssign;
    @BindView(R.id.tv_wait_pickup_amount)
    TextView tvWaitPickupAmount;
    @BindView(R.id.btn_wait_pickup)
    Button btnWaitPickup;
    @BindView(R.id.ll_btn_first)
    LinearLayout llBtnFirst;
    @BindView(R.id.btn_deposit_file)
    Button btnDepositFile;
    @BindView(R.id.btn_picked_up)
    Button btnPickedUp;
    @BindView(R.id.ll_btn_second)
    LinearLayout llBtnSecond;
    @BindView(R.id.depositecabinetTask)
    RelativeLayout depositecabinetTask;

    ICabinetPresenter cabinetPresenter;
    private String filePath;

    /**
     * popuwindows属性
     */
    private TextView tvErweimaName;
    private Button btnErweimaNext;
    private ImageView ivErwemaBgTop;
    private ImageView ivErwemaBgBottom;
    private ImageView ivErwemaBgLeft;
    private ImageView ivErwemaBgRight;
    private ImageView iverweima;


    private String s = "";
    private UserNewBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        ButterKnife.bind(this);
        cabinetPresenter = new CabinetPresenter(this);
        filePath = Environment.getExternalStorageDirectory().getPath() + "/Json/" + "code_json.jpg";//创建的二维码地址
        showUserData();


    }

    private void showUserData() {
        user = CApplication.getInstance().getCurrentUser();
        tvScanningCenter.setText(user.getEmpname());
        tvDepartment.setText("山西省晋中市中级人民法院--"+user.getDeptname());
    }

    @OnClick({R.id.tv_scanning_Center, R.id.btn_deposit, R.id.btn_wait_assign, R.id.btn_wait_pickup, R.id.btn_deposit_file, R.id.btn_picked_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_scanning_Center:
                break;
            case R.id.btn_deposit://存件
                cabinetPresenter.doDeposit();
                break;
            case R.id.btn_wait_assign://待指派
                Intent jumptoWaitAssign = new Intent(CabinetActivity.this,FileDepositDetailNew1Activity.class);
                startActivity(jumptoWaitAssign);
                break;
            case R.id.btn_wait_pickup://待取件
                Intent jumptoWaitPickup = new Intent(CabinetActivity.this,FileReverseDetailNew1Activity.class);
                startActivity(jumptoWaitPickup);
                break;
            case R.id.btn_deposit_file://存件记录
                Intent jumptoDeposited = new Intent(CabinetActivity.this,FileDepositedDataActivity.class);
                startActivity(jumptoDeposited);
                break;
            case R.id.btn_picked_up://取件记录
                Intent jumptoPickedup = new Intent(CabinetActivity.this,FileReverseDataActivity.class);
                startActivity(jumptoPickedup);



                break;
            case R.id.btn_messagemachine_content_back:
                break;
        }
    }

    @Override
    public void onQRCodePData(String mQRCodeData) {
        //中间不添加图片 直接写null
        boolean isTF = QRCodeUtil.createQRImage(mQRCodeData, 400, 400,
                BitmapFactory.decodeResource(getResources(), R.drawable.logo),
                filePath);
        Log.e("verificationErWeiMa",""+mQRCodeData);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.view_erweima_popuwindow, null);

        tvErweimaName = (TextView) view.findViewById(R.id.tv_erwema_name);
        iverweima = (ImageView) view.findViewById(R.id.iv_erweima);
        btnErweimaNext = (Button) view.findViewById(R.id.btn_erweima_next);
        ivErwemaBgTop = (ImageView) view.findViewById(R.id.iv_erweima_bgtop);
        ivErwemaBgBottom = (ImageView) view.findViewById(R.id.iv_erweima_bgbottom);
        ivErwemaBgLeft = (ImageView) view.findViewById(R.id.iv_erweima_bgleft);
        ivErwemaBgRight = (ImageView) view.findViewById(R.id.iv_erweima_bgright);
        tvErweimaName.setText("身份认证二维码");
        btnErweimaNext.setText("下一步");
        iverweima.setImageBitmap(BitmapFactory.decodeFile(filePath));



        // 创建PopupWindow对象
        final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        pop.setAnimationStyle(R.style.mypopwindow_anim_style);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        pop.setBackgroundDrawable(dw);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);


        if(pop.isShowing()) {
            // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
            pop.dismiss();
        } else {
            // 显示窗口
            pop.showAtLocation(btnDeposit, Gravity.CENTER,0,0);

        }
        btnErweimaNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnErweimaNext.getText().toString().equals("完成")){
                    pop.dismiss();
                }else {
                    //生成M+时期+四位随机数
                    SimpleDateFormat format1    =   new    SimpleDateFormat ("yyyyMMddHHmmss");
                    Date curDate    =   new    Date(System.currentTimeMillis());
                    String    timeNow    =    format1.format(curDate);

                    int intCount = 0;
                    intCount = (new Random()).nextInt(9999);//
                    if (intCount < 1000) intCount += 1000;
                    s = "M"+timeNow+intCount + "";
                    Log.e("lk",s);
                    //中间不添加图片 直接写null
                    boolean isTF = QRCodeUtil.createQRImage(s, 400, 400,
                            BitmapFactory.decodeResource(getResources(), R.drawable.logo),
                            filePath);
                    btnErweimaNext.setText("完成");
                    iverweima.setImageBitmap(BitmapFactory.decodeFile(filePath));


                }
//                                        pop.dismiss();

            }
        });
        ivErwemaBgTop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        ivErwemaBgBottom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        ivErwemaBgLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        ivErwemaBgRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });







    }
}
