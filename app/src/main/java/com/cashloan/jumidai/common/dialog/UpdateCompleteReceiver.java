package com.cashloan.jumidai.common.dialog;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class UpdateCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // to do here
        openDownload(context, intent);
    }


    private void openDownload(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            SharedPreferences sPreferences = context.getSharedPreferences(context.getPackageName(), 0);
            long referneceDownloadId = sPreferences.getLong("downloadId", 0);
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            Log.d("=====", "下载完成的downloadId: " + downloadId);

            if (referneceDownloadId == downloadId) {
                DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = dManager.getUriForDownloadedFile(downloadId);

                installApk(context, uri);
            }

        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
//            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //点击通知栏取消下载
//            manager.remove(ids);
//            Toast.makeText(context, "已经取消下载", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 安装APK
     *
     * @param context
     * @param apkPath 安装包的路径
     */
    private void installApk(Context context, Uri apkPath) {
        if (apkPath == null) {
            Log.e("Url", "-----------------url失效----------------------");
            Toast.makeText(context, "更新异常请重试！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //此处因为上下文是Context，所以要加此Flag，不然会报错
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkPath, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
