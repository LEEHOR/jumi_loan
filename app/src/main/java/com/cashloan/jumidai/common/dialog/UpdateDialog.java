package com.cashloan.jumidai.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.utils.ActivityManage;
import com.commom.utils.EmptyUtils;
import com.commom.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;


public class UpdateDialog extends BaseMvpActivity implements View.OnClickListener {

    private static UpdateDialog updateDialog;
    private static Dialog       dialog;

    private static TextView    tvDownText;
    private static TextView    tvUpdatebtn;
    private static TextView    tvClosebtn;
    private        View        rlUpdate;
    private        View        rlProgress;
    private        ProgressBar mProgressBar;
    private        TextView    mProgressText;

    private boolean isLoading;

    private static File    downLoadFile;
    private static Context mContext;


    private static String downUrl, downText, downupdatecode, updateway;

    public static synchronized UpdateDialog getInstance(Context context, String url, String content,
                                                        String updateCode, String appUpdateWay) {
        if (updateDialog == null) {
            updateDialog = new UpdateDialog(context);


            dialog = new AlertDialog.Builder(context, R.style.AppTheme_UpdateBox).setView(view).create();
        }
        downUrl = url;
        downText = content;
        downupdatecode = updateCode;
        updateway = appUpdateWay;
        tvDownText.setText(downText);
        //update：1 强制更新，只显示一个按钮 其他的则显示两个
        if ("0".equals(downupdatecode)) {
            tvClosebtn.setVisibility(View.VISIBLE);
            tvUpdatebtn.setVisibility(View.VISIBLE);
        } else if ("1".equals(downupdatecode)) {
            tvClosebtn.setVisibility(View.GONE);
            tvUpdatebtn.setVisibility(View.VISIBLE);
        }

        //针对8.0安装未知应用权限的申请回调监听
        mActivityResultListener = new BaseActivityResultListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void activityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_CODE_APP_INSTALL) {

                    if (EmptyUtils.isNotEmpty(downLoadFile)) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //判断是否有安装未知来源应用的权限
                            boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();

                            if (!haveInstallPermission) {//没有权限
                                ToastUtil.toast("授权失败");
                            } else {
                                installApk(mContext, data, downLoadFile);
                            }
                        } else {
                            installApk(mContext, data, downLoadFile);
                        }


                    }
                }
            }
        };


        return updateDialog;
    }

    private static View view;

    public UpdateDialog(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null, false);

        tvDownText = view.findViewById(R.id.down_text);
        tvUpdatebtn = view.findViewById(R.id.updatebtn);
        tvClosebtn = view.findViewById(R.id.closebtn);

        rlUpdate = view.findViewById(R.id.rl_update_msg_dialog);
        rlProgress = view.findViewById(R.id.rl_update_progress_dialog);

        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressText = view.findViewById(R.id.textView92);

        tvDownText.setText(downText);
        tvUpdatebtn.setOnClickListener(this);

        mProgressBar.setProgress(0);
        tvClosebtn.setOnClickListener(this);

        isLoading = false;
        rlUpdate.setVisibility(View.VISIBLE);
        rlProgress.setVisibility(View.GONE);
    }


    public UpdateDialog setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    public UpdateDialog show() {
        dialog.show();

        return this;
    }

    public void dismiss() {
        dialog.dismiss();
        dialog = null;
        updateDialog = null;
    }

    @Override
    public void onClick(View v) {
        if (v == tvClosebtn) {
            dismiss();
        } else if (v == tvUpdatebtn) {
            if ("oss".equals(updateway)) {
                downApp(v.getContext());
            } else if ("web".equals(updateway)) {
                openWebInstall(ActivityManage.peek());
            }

            isLoading = true;

            rlUpdate.setVisibility(View.GONE);
            rlProgress.setVisibility(View.VISIBLE);

        }
    }

    private String getDownLoadPath() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!file.exists()) {
            file.mkdirs();
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    private void downApp(final Context context) {

        OkHttpUtils.get()
                .url(downUrl)
                .build()
                .execute(new FileCallBack(getDownLoadPath(), "jmd.apk") {

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        mProgressBar.setProgress((int) (progress * 100));
                        mProgressText.setText((int) (progress * 100) + "%");
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                        dialog.cancel();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        installNormal(context, response);

                    }
                });
    }

    /**
     * 打开浏览器下载
     */

    private void openWebInstall(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(downUrl);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * 普通安装，区分系统是否为7.0
     *
     * @param context
     * @param path
     */
    private void installNormal(Context context, File path) {
        downLoadFile = path;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //版本在7.0以上是不能直接通过uri访问的 24代表7.0的系统
        if (Build.VERSION.SDK_INT >= 24) {

            // 兼容Android 8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                installAndroidO(context, intent, path);
            } else {

                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(context, "com.cashloan.jumidai", path);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(path),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
//        context.startActivity(intent);
    }

    //尝试适配8.0安装
    private void installAndroidO(final Context context, final Intent intent, File path) {
        boolean haveInstallPermission;
        // 兼容Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                startInstallPermissionSettingActivity(context, path);
            } else {
                installApk(context, intent, path);
            }
        } else {
            installApk(context, intent, path);
        }
    }

    /**
     * @param context
     * @param intent
     */
    public static void installApk(Context context, Intent intent, File apkFile) {

        Intent intent3 = new Intent(Intent.ACTION_VIEW);
        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
        Uri apkUri = FileProvider.getUriForFile(context, "com.cashloan.jumidai", apkFile);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent3.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent3.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent3);

    }


    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context, File file) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        ActivityManage.peek().startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
    }

    private static final int REQUEST_CODE_APP_INSTALL = 1005;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initFunc() {

    }


}