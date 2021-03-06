package com.bs.lk.newoamvptest.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.util.file.FileAccessUtil;
import com.bs.lk.newoamvptest.util.file.FileOpenHelper;
import com.bs.lk.newoamvptest.widget.CustomProgress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.bs.lk.newoamvptest.common.util.URLRoot.BASE_URL_H5_ROOT_CLIENT_V3;
import static com.bs.lk.newoamvptest.model.LoginModel.sessionId;

/**
 * 日/周报
 * @author lk
 */
public class DailyAndWeeklyActivity extends Activity {
    private Context context;
    private int resId;
    private String ceshi;
    private WebView webviewMsg;


    private CustomProgress mCustomProgress;

    private ProgressBar mProgressBar;
    private GetDownloadFileUrlTask mGetDownloadFileUrlTask;
    private String mFileUrl;

    private boolean mInterceptFlag = false;
    private int mProgress;

    private Thread mDownLoadThread;
    private Dialog mDownloadDialog;

    private String mFileName;
    /**
     * 从页面中获取目前H5所在的界面
     */
    private String pageLocationForH5 = "";

    private static final int PROGRESS_UPDATE = 1;
    private static final int DOWNLOAD_FINISHED = 2;
    private UserNewBean curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leave1);
        mCustomProgress = (CustomProgress) findViewById(R.id.loading);
        webviewMsg = (WebView) findViewById(R.id.fg_webview_msg);
        ceshi  = CApplication.getInstance().getCurrentToken();
        curUser = CApplication.getInstance().getCurrentUser();
        //设置支持js
        webviewMsg.getSettings().setJavaScriptEnabled(true);
        //打开DOM存储API
        webviewMsg.getSettings().setDomStorageEnabled(true);
        //设置缓存模式：不使用缓存
        webviewMsg.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webviewMsg.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                pageLocationForH5 = message;
                Log.e("pageLocationForH5",""+pageLocationForH5  );
                result.confirm();
                return true;
            }
        });
        webviewMsg.setWebViewClient(new WebViewClient());
        syncCookieToWebView(sessionId);
        //-----------------------------------------------正式----------------------------------------------//
        webviewMsg.loadUrl(BASE_URL_H5_ROOT_CLIENT_V3 + "/bo_dev/sys/mobileLogin/html/mobileLogin.html?username="+curUser.getUserName()+"&password="+curUser.getUserPassword());
        //--------------------------------------------finish--------------------------------------//
        webviewMsg.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http"))
                {
                    mFileUrl = url;
                    mFileName = mFileUrl.substring(mFileUrl.length()-36);
                    showPromptDownloadDialog();

//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            }
        });
    }
    private void showPromptDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    DailyAndWeeklyActivity.this);
            builder.setTitle("下载");
            builder.setMessage("确认要下载附件吗？");
            builder.setPositiveButton("下载",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mGetDownloadFileUrlTask = new GetDownloadFileUrlTask();
                            mGetDownloadFileUrlTask.execute();
                            dialog.dismiss();
                        }
                    });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private class GetDownloadFileUrlTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return mFileUrl;
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected void onPostExecute(String url) {
            try {
                if (!TextUtils.isEmpty(url)) {
                    mInterceptFlag = false;
                    downloadFile();
                    showDownloadDialog();
                } else {
                    Toast.makeText(DailyAndWeeklyActivity.this, "附件的下载地址无效", Toast.LENGTH_LONG).show();
                }
            } finally {
                mGetDownloadFileUrlTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mCustomProgress.stop();
            mGetDownloadFileUrlTask = null;
        }
    }


    private void showDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(DailyAndWeeklyActivity.this);
            builder.setTitle("文件下载");
            final LayoutInflater inflater = LayoutInflater
                    .from(DailyAndWeeklyActivity.this);
            View v = inflater.inflate(R.layout.download_attachment_progress, null);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
            builder.setView(v);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mInterceptFlag = true;
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            mDownloadDialog = builder.create();
            mDownloadDialog.show();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void downloadFile() {
        mDownLoadThread = new Thread(mDownFileRunnable);
        mDownLoadThread.start();
    }

    private Runnable mDownFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(mFileUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                Log.e("lk",""+mFileUrl.substring(mFileUrl.length()-36));
                String filePath = "";
//                if (mFileName.endsWith(".doc") ) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"1"+".doc") ;
//                }else if (mFileName.endsWith(".docx")){
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"2"+".docx") ;
//
//                }else if (mFileName.endsWith(".xls") ) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"3"+".xls") ;
//                }else if (mFileName.endsWith(".xlsx")){
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"4"+".xlsx") ;
//
//                }else if (mFileName.endsWith(".txt")) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"5"+".txt") ;
//                } else if (mFileName.endsWith(".pdf")) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"6"+".pdf") ;
//                } else if (mFileName.endsWith(".jpg") ) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"7"+".jpg") ;
//                }else if (mFileName.endsWith(".png")){
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"8"+".png") ;
//
//                }
                filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName) ;

//                String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileUrl.substring(mFileUrl.length()-36)) ;
                File file = new File(filePath);
                file.createNewFile();
                file.setWritable(true);
                FileOutputStream fos = new FileOutputStream(file);
                int count = 0;
                byte[] buf = new byte[1024];
                int preProgress = mProgress;
                do {
                    int numread = is.read(buf);
                    count += numread;
                    mProgress = (int) (((float) count / length) * 100);
                    if (preProgress != mProgress) {
                        // 更新进度
                        mHandler.sendEmptyMessage(PROGRESS_UPDATE);
                        preProgress = mProgress;
                    }
                    fos.write(buf, 0, numread);
                    if (count == length) {
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISHED);
                        break;
                    }
                    // 点击取消就停止下载.
                } while (!mInterceptFlag);
                fos.close();
                is.close();
            } catch (Throwable e) {
                mInterceptFlag = true;
                mDownloadError.sendEmptyMessage(0);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS_UPDATE:
                    mProgressBar.setProgress(mProgress);
                    break;
                case DOWNLOAD_FINISHED:
                    mDownloadDialog.dismiss();
                    showPromptOpenFileDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private Handler mDownloadError = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DailyAndWeeklyActivity.this);
            builder.setTitle("下载失败");
            builder.setMessage("网络不给力，再试下呗");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    downloadFile();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    };

    private void showPromptOpenFileDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    DailyAndWeeklyActivity.this);
            builder.setTitle("下载完成");
            builder.setMessage("是否打开文件？");
            builder.setPositiveButton("打开",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            openFile(which);
                        }
                    });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void openFile(int which) {
        Intent intent = null;
        if (mFileName.endsWith(".doc")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getWordFileIntent(DailyAndWeeklyActivity.this,file);
        }else if (mFileName.endsWith(".docx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getWordFileIntent(DailyAndWeeklyActivity.this,file);
        }else if (mFileName.endsWith(".xls")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(DailyAndWeeklyActivity.this,file);
        }else if (mFileName.endsWith(".xlsx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(DailyAndWeeklyActivity.this,file);
        }else if (mFileName.endsWith(".txt")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getTextFileIntent(DailyAndWeeklyActivity.this,file);
        } else if (mFileName.endsWith(".pdf")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getPdfFileIntent(DailyAndWeeklyActivity.this,file);
        } else if (mFileName.endsWith(".jpg")  ) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(DailyAndWeeklyActivity.this,file);
        }else if (mFileName.endsWith(".png")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(DailyAndWeeklyActivity.this,file);
        }
        if (intent != null) {
            startActivity(intent);
        } else {
            showSelectFileTypeDialog();
        }
    }


    private void showSelectFileTypeDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    DailyAndWeeklyActivity.this);
            builder.setTitle("打开为");
            builder.setItems(new String[]{"文本", "音频", "视频", "图片", "应用", ""}, new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    openFile(which);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * cookie同步
     */
    private void syncCookieToWebView(String cookies)
    {
        CookieSyncManager.createInstance(DailyAndWeeklyActivity.this);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);

        String c1 = cm.getCookie("http://47.92.241.228:8899/bo_dev/sys/daily/html/daily_list.html");
        if (c1!=null){
            cm.removeSessionCookie();
            String cs ="JSESSIONID="+cookies;
            cm.setCookie("http://47.92.241.228:8899/bo_dev/sys/daily/html/daily_list.html",cs);
            CookieSyncManager.getInstance().sync();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
