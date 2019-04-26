package com.bs.lk.newoamvptest.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSONObject;
import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.Interface.IOAInterfaceV3Service;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.ApkUrlAndVersionBean;
import com.bs.lk.newoamvptest.bean.SessionGsonFormatBean;
import com.bs.lk.newoamvptest.model.ILoginModel;
import com.bs.lk.newoamvptest.util.NetUtils;
import com.bs.lk.newoamvptest.util.file.FileOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;

import static com.bs.lk.newoamvptest.util.NetUtils.UPDATE_PATH;

/**
 * @author lk
 */
public class UpdateManager extends ILoginModel {
    public boolean interceptFlag = false;
    private String apkUrl;
    private int mState;
    private int mSize;
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    private static final String savePath = "/sdcard/updatedemo/";
    private static final String saveFileName = savePath
            + "courtoa_mobile.apk";
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 0;
    private static final int DOWN_OVER = 1;
    private static final int SHOW_DIALOG = 2;
    private int progress;
    private Thread downLoadThread;
    private int mVersionCode;
    private Activity mActivity;
    /**
     * 定义时创建 service,并且实例化
     */
    private IOAInterfaceV3Service service=createService(IOAInterfaceV3Service.class);
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    downloadDialog.dismiss();
                    installApk();
                    break;
                case SHOW_DIALOG:
                    showNoticeDialog();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Activity activity) {
        mActivity = activity;
        interceptFlag = true;
        PackageManager packageManager = CApplication.getInstance().getBaseContext().getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(CApplication.getInstance().getBaseContext().getPackageName(), 0);
            mVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new CheckVersionTask().execute();
    }

    public UpdateManager(String url, String message, int state, String version) {
        apkUrl = url;
        mState = state;
    }

    private void showNoticeDialog() {
        synchronized (CApplication.getInstance().getBaseContext()) {
            try {
                Builder builder = new Builder(
                        mActivity);
                builder.setTitle("更新提醒");
                builder.setMessage("有新版本，可自行前往商城进行更新");
                builder.setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNegativeButton("更新", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDownloadDialog();
                        interceptFlag = false;
                        downloadApk();
                        dialog.dismiss();
                    }
                });
                noticeDialog = builder.create();
                noticeDialog.show();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    public void startUpdate() {
        showDownloadDialog();
        interceptFlag = false;
        downloadApk();
    }

    private void showDownloadDialog() {
        try {
            Builder builder = new Builder(mActivity);
            builder.setTitle("软件更新");
            final LayoutInflater inflater = LayoutInflater
                    .from(mActivity);
            View v = inflater.inflate(R.layout.download_apk_progress, null);
            mProgress = (ProgressBar) v.findViewById(R.id.progress);
            builder.setView(v);
            builder.setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Builder builder = new Builder(
                            mActivity);
                    builder.setTitle("软件更新");
                    builder.setMessage("取消下载后将退出应用程序");
                    builder.setPositiveButton("确定",
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    interceptFlag = true;
                                    android.os.Process
                                            .killProcess(android.os.Process
                                                    .myPid());
                                }
                            });
                    builder.setCancelable(false);
                    builder.create().show();
                    interceptFlag = true;
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            downloadDialog = builder.create();
            downloadDialog.show();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler downloadError = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Builder builder = new Builder(mActivity);
            builder.setTitle("版本更新失败");
            builder.setMessage("网络不给力，再试下呗");
            builder.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    downloadApk();
                }
            });
            builder.setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    };
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                mSize = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File apkFile = new File(saveFileName);
                if (apkFile.exists()) {
                    apkFile.delete();
                }
                apkFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                int preProgress = progress;
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / mSize) * 100);
                    if (preProgress != progress) {
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        preProgress = progress;
                    }
                    fos.write(buf, 0, numread);
                    if (count == mSize) {
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    //点击取消就停止下载
                } while (!interceptFlag);
                fos.close();
                is.close();
            } catch (Throwable e) {
                interceptFlag = true;
                downloadError.sendEmptyMessage(0);
            }
        }
    };

    /**
     * 下载apk
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */

    private void installApk() {
        interceptFlag = true;
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = FileOpenHelper.getApkFileIntent(apkfile);
        CApplication.getInstance().startActivity(i);
    }

    @Override
    public void doLoginData(String name, String password) {

    }

    @Override
    public Observable<SessionGsonFormatBean> loadAndroidVersion(String url, int type) {
        return null;
    }


    private class CheckVersionTask extends AsyncTask<Void, Void, ApkUrlAndVersionBean> {
        String resp;
        ApkUrlAndVersionBean apkUrlAndVersion;
        public CheckVersionTask() {
        }

        @Override
        protected ApkUrlAndVersionBean doInBackground(Void... params) {

            try {
                resp = NetUtils.requestByPost("",UPDATE_PATH);
                Log.e("服务器APK版本号：",""+resp);
                apkUrlAndVersion = JSONObject.parseObject(resp, ApkUrlAndVersionBean.class);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return apkUrlAndVersion;
        }
        @Override
        protected void onPostExecute(ApkUrlAndVersionBean apk) {
            if (apk!=null){
                int apkVersonCode = Integer.parseInt(apk.getVersion());
                Log.e("apkVersonCode：",""+apkVersonCode);
                if (mVersionCode<apkVersonCode){
                    if (!TextUtils.isEmpty(apk.getApkpath())) {
                        apkUrl = apk.getApkpath();
                        mHandler.sendEmptyMessage(SHOW_DIALOG);
                    } else {
                    }
                }else {
                    Log.e("apkVersonCode：",""+apkVersonCode);
                }
            }

        }
    }

    public boolean isDownloading() {
        return !interceptFlag;
    }
}
