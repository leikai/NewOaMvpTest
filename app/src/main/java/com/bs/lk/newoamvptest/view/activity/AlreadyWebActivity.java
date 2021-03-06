package com.bs.lk.newoamvptest.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.bs.lk.newoamvptest.util.file.FileAccessUtil;
import com.bs.lk.newoamvptest.util.file.FileOpenHelper;
import com.bs.lk.newoamvptest.widget.CustomProgress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.bs.lk.newoamvptest.common.util.URLRoot.BASE_URL_H5_ROOT_CLIENT_V2;

/**
 * @author lk
 */
public class AlreadyWebActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private WebView webView;
    private CustomProgress mCustomProgress;
    /**
     * 从页面中获取目前H5所在的界面
     */
    public static String pageLocationForH5 = "2" ;
    private String ceshi;
    private String mFileUrl;
    private String mFileName;
    private GetDownloadFileUrlTask mGetDownloadFileUrlTask;
    private boolean mInterceptFlag = false;
    private Thread mDownLoadThread;
    private int mProgress;
    private static final int PROGRESS_UPDATE = 1;
    private static final int DOWNLOAD_FINISHED = 2;
    private ProgressBar mProgressBar;
    private Dialog mDownloadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_web);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webView = (WebView) findViewById(R.id.webView);

        mCustomProgress = (CustomProgress)findViewById(R.id.loading);
        ceshi = CApplication.getInstance().getCurrentToken();
        initWebView();

        webView.loadUrl(BASE_URL_H5_ROOT_CLIENT_V2+"/mcourtoa/moffice/sign/list_yiban.html?token="+ceshi);
    }
    private void initWebView() {
        WebSettings settings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient());
        //打开DOM存储API
        webView.getSettings().setDomStorageEnabled(true);
        // 设置支持JavaScript
        settings.setJavaScriptEnabled(true);
        // 设置webview的缩放级别
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                pageLocationForH5 = message;
                Log.e("pageLocationForH5",""+pageLocationForH5  );
                result.confirm();
                return true;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http"))
                {
                    mFileUrl = url;
                    mFileName = mFileUrl.substring(mFileUrl.length()-36);
                    if (mFileName.endsWith(".zip")){
                        showNotSupportTypeDialog();
                        return;
                    } else if (mFileName.endsWith(".rar")){
                        showNotSupportTypeDialog();
                        return;
                    }
                    showPromptDownloadDialog();

                }
            }
        });
        //给webview设置WebChromeClient   当ui发生改变的时候的回调
        if (webView != null) {
            webView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (progressBar != null) {
                        //显示progre 控件
                        progressBar.setVisibility(View.VISIBLE);
                        //设置最大进度
                        progressBar.setMax(100);
                        //设置当前的进度
                        progressBar.setProgress(newProgress);
                        if (newProgress == 100) {
                            //隐藏进度条
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                    super.onProgressChanged(view, newProgress);

                }

                @Override
                public void onReceivedTitle(WebView view, String title) {

                    super.onReceivedTitle(view, title);
                }
            });
        } else {
            Toast.makeText(this, "WebView为空", Toast.LENGTH_SHORT).show();
        }
    }


    private void showPromptDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    this);
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
                    Toast.makeText(AlreadyWebActivity.this, "附件的下载地址无效", Toast.LENGTH_LONG).show();
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
                filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName) ;
                File file = new File(filePath);
                file.createNewFile();
                file.setWritable(true);
                FileOutputStream fos = new FileOutputStream(file);
                int count = 0;
                byte buf[] = new byte[1024];
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
    @SuppressLint("HandlerLeak")
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

    @SuppressLint("HandlerLeak")
    private Handler mDownloadError = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AlreadyWebActivity.this);
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
    private void showDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("文件下载");
            final LayoutInflater inflater = LayoutInflater
                    .from(this);
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

    /**
     * 窗口：提示不支持压缩文件手机端查看
     */
    private void showNotSupportTypeDialog(){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    this);
            builder.setTitle("提醒");
            builder.setMessage("压缩文件手机端无法查看，请于电脑端查看");
            builder.setCancelable(false);
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private void showPromptOpenFileDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    this);
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
            intent = FileOpenHelper.getWordFileIntent(this,file);
        }else if (mFileName.endsWith(".docx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getWordFileIntent(this,file);
        }else if (mFileName.endsWith(".xls")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(this,file);
        }else if (mFileName.endsWith(".xlsx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(this,file);
        }else if (mFileName.endsWith(".txt")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getTextFileIntent(this,file);
        } else if (mFileName.endsWith(".pdf")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getPdfFileIntent(this,file);
        } else if (mFileName.endsWith(".jpg")  ) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(this,file);
        }else if (mFileName.endsWith(".png")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(this,file);
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
                    this);
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
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            if(webView.getUrl().equals(BASE_URL_H5_ROOT_CLIENT_V2+"/mcourtoa/moffice/sign/list_yiban.html?token="+ceshi)){
                super.onBackPressed();
            }else{ webView.goBack();
            }
        }else{
            super.onBackPressed(); }
    }

}
