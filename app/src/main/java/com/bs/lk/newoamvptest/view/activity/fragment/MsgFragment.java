package com.bs.lk.newoamvptest.view.activity.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
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
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.bs.lk.newoamvptest.common.util.URLRoot.BASE_URL_H5_ROOT_CLIENT_V2;


/**
 * @author lk
 */
public class MsgFragment extends BaseFragment{
    private Context context;
    private int resId;
    private String ceshi;
    private WebView webviewMsg;
    /**
     * 下拉刷新
     */
    private SwipeRefreshLayout swipeRefresh;
    /**
     * 网页加载进度
     */
    private ProgressBar pgl;
    private CustomProgress mCustomProgress;
    private ProgressBar mProgressBar;
    private GetDownloadFileUrlTask mGetDownloadFileUrlTask;
    private String mFileUrl;
    private boolean mInterceptFlag = false;
    private int mProgress;
    private Thread mDownLoadThread;

    private GeekThread mDownLoadThreadNew;


    private Dialog mDownloadDialog;
    private String mFileName;
    /**
     * 从页面中获取目前H5所在的界面
     */
    private String pageLocationForH5 = "";

//    private String currentPageUrl = "http://111.53.181.200:8087/mcourtoa/moffice/sign/list_daiban.html?token=";

    private String currentPageUrl = BASE_URL_H5_ROOT_CLIENT_V2+"/mcourtoa/moffice/sign/list_daiban.html?token=";


    private static final int PROGRESS_UPDATE = 1;
    private static final int DOWNLOAD_FINISHED = 2;

    @SuppressLint({"NewApi", "ValidFragment"})
    public MsgFragment() {

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public MsgFragment(Context context, int resId) {
        this.context = context;
        this.resId = resId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "消息";
        mLogo = View.INVISIBLE;
        mShowBtnBack = View.INVISIBLE;
        mShowSearchBar = View.GONE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_msg, container,
                savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initFragment(container, savedInstanceState);
        mCustomProgress = (CustomProgress) mRootView.findViewById(R.id.loading);
        pgl = (ProgressBar) mRootView.findViewById(R.id.progressBar1);
        webviewMsg = (WebView) mRootView.findViewById(R.id.fg_webview_msg);
        ceshi  = CApplication.getInstance().getCurrentToken();
        swipeRefresh = mRootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWebview();
            }
        });

        clearWebViewCache();

        WebSettings webSettings = webviewMsg.getSettings();
        //设置支持JS
        webSettings.setJavaScriptEnabled(true);
        //设置缓存模式：不使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //打开DOM存储API
        webSettings.setDomStorageEnabled(true);

        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        if(Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
        webviewMsg.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webviewMsg.setHorizontalScrollBarEnabled(true);
        webviewMsg.requestFocus();






        webviewMsg.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                pageLocationForH5 = message;
                Log.e("pageLocationForH5",""+pageLocationForH5  );
                result.confirm();
                return true;
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    pgl.setVisibility(View.GONE);

                }else {
                    pgl.setVisibility(View.VISIBLE);
                    pgl.setProgress(newProgress);
                }
            }

        });

        webviewMsg.setWebViewClient(new WebViewClient());
        //-----------------------------------------------正式----------------------------------------------//
        currentPageUrl = currentPageUrl+ceshi;
        Log.e("currentPageUrl",""+currentPageUrl);
        webviewMsg.loadUrl(currentPageUrl);
        //--------------------------------------------finish--------------------------------------//
        webviewMsg.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http"))
                {
                    mFileUrl = url;
                    mFileName = mFileUrl.substring(mFileUrl.length()-36);
                    showPromptDownloadDialog();
                }
            }
        });


    }

    private void refreshWebview() {
        webviewMsg.loadUrl(currentPageUrl);
        swipeRefresh.setRefreshing(false);
}


    private void showPromptDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getContext());
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
                    Toast.makeText(getContext(), "附件的下载地址无效", Toast.LENGTH_LONG).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("文件下载");
            final LayoutInflater inflater = LayoutInflater
                    .from(getContext());
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
            e.printStackTrace();
        }
    }

    private void downloadFile() {
//        mDownLoadThread = new Thread(mDownFileRunnable);
//        mDownLoadThread.start();

        GeekThreadManager.getInstance().execute(mDownLoadThreadNew, ThreadType.NORMAL_THREAD);
        mDownLoadThreadNew = new GeekThread(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                super.run();
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





    }

//    private Runnable mDownFileRunnable = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                URL url = new URL(mFileUrl);
//                HttpURLConnection conn = (HttpURLConnection) url
//                        .openConnection();
//                conn.connect();
//                int length = conn.getContentLength();
//                InputStream is = conn.getInputStream();
//
//                Log.e("lk",""+mFileUrl.substring(mFileUrl.length()-36));
//                String filePath = "";
//                if (mFileName.endsWith(".doc") ) {
//                     filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"1"+".doc") ;
//                }else if (mFileName.endsWith(".docx")){
//                     filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"2"+".docx") ;
//
//                }else if (mFileName.endsWith(".xls") ) {
//                     filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"3"+".xls") ;
//                }else if (mFileName.endsWith(".xlsx")){
//                     filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"4"+".xlsx") ;
//
//                }else if (mFileName.endsWith(".txt")) {
//                     filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"5"+".txt") ;
//                } else if (mFileName.endsWith(".pdf")) {
//                     filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"6"+".pdf") ;
//                } else if (mFileName.endsWith(".jpg") ) {
//                     filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"7"+".jpg") ;
//                }else if (mFileName.endsWith(".png")){
//                     filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"8"+".png") ;
//
//                }
//                File file = new File(filePath);
//                file.createNewFile();
//                file.setWritable(true);
//                FileOutputStream fos = new FileOutputStream(file);
//                int count = 0;
//                byte buf[] = new byte[1024];
//                int preProgress = mProgress;
//                do {
//                    int numread = is.read(buf);
//                    count += numread;
//                    mProgress = (int) (((float) count / length) * 100);
//                    if (preProgress != mProgress) {
//                        // 更新进度
//                        mHandler.sendEmptyMessage(PROGRESS_UPDATE);
//                        preProgress = mProgress;
//                    }
//                    fos.write(buf, 0, numread);
//                    if (count == length) {
//                        mHandler.sendEmptyMessage(DOWNLOAD_FINISHED);
//                        break;
//                    }
//                    // 点击取消就停止下载.
//                } while (!mInterceptFlag);
//                fos.close();
//                is.close();
//            } catch (Throwable e) {
//                mInterceptFlag = true;
//                mDownloadError.sendEmptyMessage(0);
//            }
//        }
//    };

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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    getContext());
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
//        String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR) +
//                mComponent.getLabelValue();
//        File file = new File(filePath);
//        Intent intent = FileOpenHelper.getFileIntent(file);
//        if (intent != null) {
//            getContext().startActivity(intent);
//        }

        Intent intent = null;
        if (mFileName.endsWith(".doc")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getWordFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".docx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getWordFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".xls")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".xlsx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".txt")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getTextFileIntent(getActivity(),file);
        } else if (mFileName.endsWith(".pdf")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getPdfFileIntent(getActivity(),file);
        } else if (mFileName.endsWith(".jpg")  ) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".png")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(getActivity(),file);
        }
        if (intent != null) {
            getActivity().startActivity(intent);
        } else {
            showSelectFileTypeDialog();
        }
    }


    private void showSelectFileTypeDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getContext());
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


    private static final String APP_CACAHE_DIRNAME = "/webcache";
    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache(){

        //清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(context.getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME);
        Log.e("", "appCacheDir path="+appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath()+"/webviewCache");
        Log.e("", "webviewCacheDir path="+webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        Log.i("", "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e("", "delete file no exists " + file.getAbsolutePath());
        }
    }


    @Override
    public boolean onBackPressed() {
        boolean handle = webviewMsg.canGoBack();
        if (!webviewMsg.getUrl().equals(currentPageUrl)){
            Log.e("handle",""+handle);
            if (webviewMsg.canGoBack()){
                webviewMsg.goBack();
                currentPageUrl = webviewMsg.getOriginalUrl();
                return true;
            }
        }
        return super.onBackPressed();
    }

}
