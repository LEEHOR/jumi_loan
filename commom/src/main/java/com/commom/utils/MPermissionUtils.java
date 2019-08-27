package com.commom.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * public void callPhone(View v) { MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.CALL_PHONE}
 * , new MPermissionUtils.OnPermissionListener() {
 *
 * @Override public void onPermissionGranted() {
 * Toast.makeText(MainActivity.this, "授权成功,执行拨打电话操作!", Toast.LENGTH_SHORT).show();
 * }
 * @Override public void onPermissionDenied() {
 * MPermissionUtils.showTipsDialog(MainActivity.this);
 * }
 * });
 * }
 * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 * MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
 * super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 * }
 */
public class MPermissionUtils {

    private static int mRequestCode = -1;
    private Context mContext;

    public MPermissionUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 需要进行检测的权限数组
     */
    public static final String[] PERMISSION_ALL = new String[]{
            Permission.CAMERA,
            Permission.WRITE_EXTERNAL_STORAGE,
            Permission.READ_PHONE_STATE,
            Permission.READ_CONTACTS,
            Permission.READ_SMS,
            Permission.RECEIVE_SMS,
            Permission.ACCESS_COARSE_LOCATION,
            Permission.ACCESS_FINE_LOCATION};

    public static final String[] PERMISSION_LINK = new String[]{
            Permission.READ_PHONE_STATE,
            Permission.READ_CONTACTS,
            Permission.RECEIVE_SMS};


    public void requestPermission(final OnPermissionListener permissionListener, String... permissions) {

        AndPermission.with(mContext)
                .runtime()
                .permission(permissions)
//                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
//                        toast(R.string.successfully);
//                        Toast.makeText(SplashActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                        if (EmptyUtils.isNotEmpty(permissionListener)) {
                            permissionListener.onPermissionsCallBack(true);
                        }

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {

                        if (EmptyUtils.isNotEmpty(permissionListener)) {
                            permissionListener.onPermissionsCallBack(false);
                        }

                        //用户拒绝了开启权限,且选择了不再询问
                        if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                            // 弹出前往设置的提示
                            showTipsDialog(mContext);
                        } else {//用户本次拒绝了开启权限
//                            Toast.makeText(mContext, "部分授权大失败22222", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .start();
    }

    /**
     * 获取上下文
     */
    private static Context getContext(Object object) {
        Context context;
        if (object instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else if (object instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        } else {
            context = (Activity) object;
        }
        return context;
    }


    /**
     * 显示提示对话框
     */
    public static void showTipsDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 验证权限是否都已经授权
     */
    private static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限列表中所有需要授权的权限
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return
     */
    private static List<String> getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 检查所传递对象的正确性
     *
     * @param object 必须为 activity or fragment
     */
    private static void checkCallingObjectSuitability(Object object) {
        if (object == null) {
            throw new NullPointerException("Activity or Fragment should not be null");
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof android.support.v4.app.Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;

        if (!(isActivity || isSupportFragment || isAppFragment)) {
            throw new IllegalArgumentException(
                    "Caller must be an Activity or a Fragment");
        }
    }

    /**
     * 检查所有的权限是否已经被授权
     *
     * @param permissions 权限列表
     * @return
     */
    private static boolean checkPermissions(Context context, String... permissions) {
        if (isOverMarshmallow()) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断当前手机API版本是否 >= 6.0
     */
    private static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public interface OnPermissionListener {
        void onPermissionsCallBack(boolean isGranted);

    }

    private OnPermissionListener mOnPermissionListener;

}