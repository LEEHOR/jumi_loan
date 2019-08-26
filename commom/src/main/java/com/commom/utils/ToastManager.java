package com.commom.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.commom.R;


/**
 * @desc: Toast管理类 防止重复多次弹出toast
 * @author: Leo
 * @date: 2016/10/26
 */
public class ToastManager {
    private static Toast sToast;
    private static TextView sContentTv;
    private static ImageView sContentIv;
    private static Context context;

    /**
     * @param context application的上下文
     */
    public static void init(Context context) {
        ToastManager.context = context;
    }

    public static void showShortToast(String msg) {
        showCustomToast(context, msg, -1, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(String msg, int res) {
        showCustomToast(context, msg, res, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(int msgId) {
        showCustomToast(context, msgId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(String msg) {
        showCustomToast(context, msg, Toast.LENGTH_LONG);
    }

    public static void showLongToast(int msgId) {
        showCustomToast(context, msgId, Toast.LENGTH_LONG);
    }

    /**
     * 创建运行在UI线程中的Toast
     *
     * @param activity
     * @param msg
     */
    public static void showToastInUiThread(final Activity activity, final String msg) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    showCustomToast(activity, msg);
                }
            });
        }
    }

    public static void showToastInUiThread(final Activity activity, final int stringId) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    showCustomToast(activity, stringId);
                }
            });
        }
    }

    private static void showCustomToast(Context context, int msgId) {
        final String msg = context.getResources().getString(msgId);
        showCustomToast(context, msg);
    }

    private static void showCustomToast(Context context, String msg) {
        showCustomToast(context, msg, Toast.LENGTH_SHORT);
    }

    private static void showCustomToast(Context context, int msgId, int duration) {
        final String msg = context.getResources().getString(msgId);
        showCustomToast(context, msg, duration);
    }

    private static void showCustomToast(final Context context, final String msg, final int res, final int duration) {
        if (context == null) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showToast(context, msg, res, duration);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showToast(context, msg, res, duration);
                }
            });
        }
    }

    private static void showCustomToast(final Context context, final String msg, final int duration) {
        if (context == null) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showToast(context, msg, -1, duration);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showToast(context, msg, -1, duration);
                }
            });
        }
    }

    private static void showToast(Context context, String msg, int res, int duration) {
        if (null != context) {
            if (sToast == null) {
                sToast = new Toast(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View layout = inflater.inflate(R.layout.layout_base_toast, null);
                sContentTv = (TextView) layout.findViewById(R.id.tv_toast);
                sContentIv = (ImageView) layout.findViewById(R.id.iv_toast);
                sContentTv.setText(msg);
                if (res == -1) {
                    sContentIv.setVisibility(View.GONE);
                } else {
                    sContentIv.setImageResource(res);
                }

                layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layout.setMinimumWidth((int) SizeUtils.dp2px(context, 210));
                layout.setMinimumHeight((int) SizeUtils.dp2px(context, 90));

                sToast.setGravity(Gravity.CENTER, 0, 0);
                sToast.setDuration(duration);
                sToast.setView(layout);
            } else {
                if (res == -1) {
                    sContentIv.setVisibility(View.GONE);
                } else {
                    sContentIv.setVisibility(View.VISIBLE);
                    sContentIv.setImageResource(res);
                }
                sContentTv.setText(msg);
            }
            sToast.show();
        }
    }
}