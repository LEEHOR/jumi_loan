package com.cashloan.jumidai.common;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeFeeDetailRec;
import com.commom.utils.ActivityManage;
import com.commom.utils.ConverterUtil;
import com.commom.utils.OnOnceClickListener;
import com.commom.utils.SizeUtils;
import com.commom.utils.StringFormat;
import com.commom.widget.NoDoubleClickButton;
import com.commom.widget.NoDoubleClickTextView;

/**
 * 作者： Ruby
 * 时间： 2018/8/20
 * 描述： dialog 弹出框util
 */
public class DialogUtils {

    /**
     * Activity是否可用
     */
    private static boolean activityIsRunning(Context context) {
        return !(context instanceof Activity) || !((Activity) context).isFinishing();
    }


    public static void showToastDialog(Context context, String contentText) {
        showConfirmDialog(context, contentText, null);
    }


    static AlertDialog dialog = null;

    //update 2018-8-8
    public static AlertDialog showGoRepay(Context context, String contentText, final goRepayConfirm confirmClick) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_home_go_pay, null, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_home_dialog_text);
        NoDoubleClickButton button = (NoDoubleClickButton) view.findViewById(R.id.btn_go_repay);

        textView.setText(contentText + "");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != confirmClick) {
                    confirmClick.goRepay();
                }
                if (null != dialog) {

                    dialog.dismiss();
                    dialog = null;
                }
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        return dialog;
    }

    public static AlertDialog showDefaultDialog(Context context, String contentText, final btnConfirmClick confirmClick) {

        AlertDialog dialog = showSureCancelDialog(context, contentText, "取消",
                "确定", null, confirmClick);

        return dialog;
    }

    public static AlertDialog showPermisssionDialog(Context context) {

        AlertDialog dialog = showSureCancelDialog(context,
                "我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！",
                "取消",
                "设置",
                null,
                new btnConfirmClick() {
                    @Override
                    public void confirm() {
                        // 去设置中设置权限
                        try {
                            PackageInfo info = ActivityManage.peek().getPackageManager()
                                    .getPackageInfo(ActivityManage.peek().getPackageName(), 0);

                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + info.packageName));
                            ActivityManage.peek().startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        return dialog;
    }


    public static AlertDialog showConfirmDialog(Context context, String contentText, final btnConfirmClick confirmClick) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_confirm_dialog, null, false);
        TextView content = (TextView) view.findViewById(R.id.dialog_text);
        TextView tvConfirm = (TextView) view.findViewById(R.id.dialog_submit);

        content.setText(contentText);

        tvConfirm.setOnClickListener(new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                if (null != confirmClick) {
                    confirmClick.confirm();
                }
                if (null != dialog) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (SizeUtils.getScreenWidth(ActivityManage.peek()) * 0.8);
//      params.height = 200 ;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_common_dialog_bg);

        return dialog;

    }


    public static AlertDialog showSureCancelDialog(Context context, String contentText,
                                                   String cancelText,
                                                   String confirmText,
                                                   final btnCancelClick cancelClick,
                                                   final btnConfirmClick confirmClick) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_base_dialog, null, false);
        TextView title = (TextView) view.findViewById(R.id.dialog_contact_title);
        TextView content = (TextView) view.findViewById(R.id.dialog_contact_text);
        TextView tvCancel = (TextView) view.findViewById(R.id.dialog_contact_cancel);
        TextView tvConfirm = (TextView) view.findViewById(R.id.dialog_contact_submit);

        title.setVisibility(View.GONE);

        tvConfirm.setText(confirmText);
        tvCancel.setText(cancelText);

        content.setText(contentText);
        tvConfirm.setOnClickListener(new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                if (null != confirmClick) {
                    confirmClick.confirm();
                }
                if (null != dialog) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
        tvCancel.setOnClickListener(new OnOnceClickListener() {

            @Override
            public void onOnceClick(View v) {
                if (null != cancelClick) {
                    cancelClick.cancel();
                }
                if (null != dialog) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (SizeUtils.getScreenWidth(ActivityManage.peek()) * 0.8);
//      params.height = 200 ;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_common_dialog_bg);

        return dialog;
    }

    //弹窗--服务费用
    @SuppressLint("SetTextI18n")
    public static AlertDialog showCostDialog(Context context, HomeFeeDetailRec rec, final btnConfirmClick confirmClick) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.cost_dialog, null, false);

        NoDoubleClickTextView tvConfirm = view.findViewById(R.id.dialog_button);
        TextView loanInterest = view.findViewById(R.id.tv_loan_cost_lixi);
        TextView zhengXinCheckFee = view.findViewById(R.id.tv_loan_cost_zhengxin);
        TextView serviceFee = view.findViewById(R.id.tv_loan_cost_fee);
        TextView total = view.findViewById(R.id.tv_loan_cost_total);

        //借款利息
        loanInterest.setText(StringFormat.subZeroAndDot(rec.getInterest()) + "元");
        //征信查询费
        zhengXinCheckFee.setText(StringFormat.subZeroAndDot(rec.getInfoAuthFee()) + "元");
        //服务费
        serviceFee.setText(StringFormat.subZeroAndDot(rec.getServiceFee()) + "元");
        //合计
        total.setText(StringFormat.subZeroAndDot(ConverterUtil.getDouble(rec.getInterest()) +
                ConverterUtil.getDouble(rec.getInfoAuthFee()) +
                ConverterUtil.getDouble(rec.getServiceFee())) + "元");

        tvConfirm.setOnClickListener(new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                if (null != confirmClick) {
                    confirmClick.confirm();
                }
                if (null != dialog) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (SizeUtils.getScreenWidth(ActivityManage.peek()) * 0.8);
//      params.height = 200 ;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_common_dialog_bg);

        return dialog;
    }


    public interface goRepayConfirm {
        void goRepay();
    }


    public interface btnConfirmClick {
        void confirm();
    }

    public interface btnCancelClick {
        void cancel();
    }
}
