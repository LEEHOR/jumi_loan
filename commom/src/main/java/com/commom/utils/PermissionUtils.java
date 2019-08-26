package com.commom.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 作者： zxb
 * 时间： 2018/1/3
 * 描述：
 */

public class PermissionUtils {

    private Context context;


    /*** 短信  ***/
    public static final int REQUEST_SMS_PERMISSION = 1001;
    /*** 手机  ***/
    public static final int REQUEST_PHONE_PERMISSION = 1002;
    /*** 相机  ***/
    public static final int REQUEST_CAMERA_PERMISSION = 1003;
    /*** 存储卡  ***/
    public static final int REQUEST_STORAGE_PERMISSION = 1004;
    /*** 传感器  ***/
    public static final int REQUEST_SENSORS_PERMISSION = 1005;
    /*** 联系人  ***/
    public static final int REQUEST_CONTACTS_PERMISSION = 1006;
    /*** 定位  ***/
    public static final int REQUEST_LOCATION_PERMISSION = 1007;
    /*** 日历  ***/
    public static final int REQUEST_CALENDAR_PERMISSION = 1008;
    /*** 麦克风  ***/
    public static final int REQUEST_MICROPHONE_PERMISSION = 1009;

    public PermissionUtils(Context context) {
        this.context = context;
    }

    private static requestPermissionsCallBack requestPermissionsCallBack;

    public interface requestPermissionsCallBack {
        void requestOk();

        void requestFailed();

        void requestDenied();
    }

    //检测权限
    public static void checkSinglePermission(Activity activity, String permission, int requestCode, requestPermissionsCallBack permissionsCallBack) {
        requestPermissionsCallBack = permissionsCallBack;

        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            requestPermissionsCallBack.requestOk();
            return;
        }

        //6.0+系统做特殊处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
//                requestPermissionsCallBack.requestDenied();
//            } else {
//                //进行权限请求
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
//            }
        } else {
            if (null != requestPermissionsCallBack) {
                requestPermissionsCallBack.requestOk();
            }
        }
    }

    //检测权限
    public static void checkMultiPermission(Activity activity, String[] permissions, int requestCode, requestPermissionsCallBack permissionsCallBack) {
        //6.0+系统做特殊处理
        requestPermissionsCallBack = permissionsCallBack;

        // TODO: 2018/1/3   判断当前Activity是否已经获得了该权限
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else {
            if (null != requestPermissionsCallBack) {
                requestPermissionsCallBack.requestOk();
            }
        }
    }


    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    requestPermissionsCallBack.requestOk();
                } else {
                    //申请失败，可以继续向用户解释。
                    requestPermissionsCallBack.requestFailed();
                }
                break;

            case REQUEST_STORAGE_PERMISSION:
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    requestPermissionsCallBack.requestOk();
                } else {
                    //申请失败，可以继续向用户解释。
                    requestPermissionsCallBack.requestFailed();
                }
                break;

            case REQUEST_PHONE_PERMISSION:
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    requestPermissionsCallBack.requestOk();
                } else {
                    //申请失败，可以继续向用户解释。
                    requestPermissionsCallBack.requestFailed();
                }
                break;

            default:
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    requestPermissionsCallBack.requestOk();
                } else {
                    //申请失败，可以继续向用户解释。
                    requestPermissionsCallBack.requestFailed();
                }
                break;
        }
    }


    public static final String[] CALENDAR;
    public static final String[] CAMERA;
    public static final String[] CONTACTS;
    public static final String[] LOCATION;
    public static final String[] MICROPHONE;
    public static final String[] PHONE;
    public static final String[] SENSORS;
    public static final String[] SMS;
    public static final String[] STORAGE;

    static {
        //版本低于6.0 SDK<23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            CALENDAR = new String[]{};
            CAMERA = new String[]{};
            CONTACTS = new String[]{};
            LOCATION = new String[]{};
            MICROPHONE = new String[]{};
            PHONE = new String[]{};
            SENSORS = new String[]{};
            SMS = new String[]{};
            STORAGE = new String[]{};
        } else {
            CALENDAR = new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR};

            CAMERA = new String[]{
                    Manifest.permission.CAMERA};

            CONTACTS = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS};

            LOCATION = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};

            MICROPHONE = new String[]{
                    Manifest.permission.RECORD_AUDIO};

            PHONE = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.PROCESS_OUTGOING_CALLS};

            SENSORS = new String[]{
                    Manifest.permission.BODY_SENSORS};

            SMS = new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS};

            STORAGE = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }
}
