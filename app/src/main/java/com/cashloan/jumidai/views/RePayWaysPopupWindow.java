package com.cashloan.jumidai.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commom.utils.ActivityManage;
import com.commom.widget.NoDoubleClickTextView;
import com.cashloan.jumidai.R;

/**
 * Created by lichunfu on 2017/8/23.
 * 支付方式选择 支付宝、连连主动还款
 */
public class RePayWaysPopupWindow extends PopupWindow implements View.OnClickListener {
    private ImageView mImgClose;

    private LinearLayout mLlRepayBank;
    private LinearLayout mLlRepayAliPay;

    public ImageView mImgCardBind;
    private TextView mTvCardBind;
    private LinearLayout mLlCardBind;

    public ImageView mImgBankcard, mImgAliPay;
    private TextView mTvCardInfo;
    private TextView mTvAliPayHint;

    public NoDoubleClickTextView ndcRepay;

    private View mPopView;
    private OnItemClickListener mListener;

    public String bankcardInfo, userid, borrowId, realRepayAmount;


    String feeMoney = "0";
    java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");

    public RePayWaysPopupWindow(Context context, String bankcardInfo, String userid, String borrowId, String realRepayAmount) {
        super(context);
        this.userid = userid;
        this.borrowId = borrowId;
        this.realRepayAmount = realRepayAmount;
        this.bankcardInfo = bankcardInfo;
        this.feeMoney = String.valueOf(df.format(Double.parseDouble(realRepayAmount) * 0.002));
        init(context, bankcardInfo);
        setPopupWindow();
        mImgClose.setOnClickListener(this);
        mLlRepayBank.setOnClickListener(this);
        mLlRepayAliPay.setOnClickListener(this);
        ndcRepay.setOnClickListener(this);

        mLlCardBind.setOnClickListener(this);

        Log.e("RePayWaysPopupWindow", "bankcardInfo=" + bankcardInfo + "\nuserid=" + userid + "\nborrowid=" + borrowId + "\nrealpayamount=" + realRepayAmount);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context, String bankcardInfo) {

        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.repay_pay_ways, null);

        mImgClose = (ImageView) mPopView.findViewById(R.id.img_close);

        mLlRepayBank = (LinearLayout) mPopView.findViewById(R.id.ll_repay_bank);
        mLlRepayAliPay = (LinearLayout) mPopView.findViewById(R.id.ll_repay_aliPay);

        mImgBankcard = (ImageView) mPopView.findViewById(R.id.img_bankcard);
        mImgAliPay = (ImageView) mPopView.findViewById(R.id.img_aliPay);

        mTvCardInfo = (TextView) mPopView.findViewById(R.id.tv_card_info);
        mTvAliPayHint = (TextView) mPopView.findViewById(R.id.tv_aliPayHint);

        //update new payWays 2018-10-9 绑定银行卡支付

        mLlCardBind = (LinearLayout) mPopView.findViewById(R.id.ll_repay_bindBank);
        mImgCardBind = (ImageView) mPopView.findViewById(R.id.img_bankBind);
        mTvCardBind = (TextView) mPopView.findViewById(R.id.tv_card_bind);
        mTvCardBind.setText("未绑定银行卡转账");
        mLlCardBind.setVisibility(View.GONE);


        mTvCardInfo.setText("银行卡转账 （" + bankcardInfo + ")");

        ndcRepay = (NoDoubleClickTextView) mPopView.findViewById(R.id.ndc_repay);
        if ("0".equals(feeMoney)) {
            feeMoney = "0";
        }


        mTvAliPayHint.setVisibility(View.GONE);
        ndcRepay.setText("确认支付" + realRepayAmount + "元");

    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明


        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                        BackgroudAlpha(1.0f);
                    }
                }
                return true;
            }
        });

    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }

    //设置屏幕背景透明度
    private void BackgroudAlpha(float alpha) {
        // TODO Auto-generated method stub
        WindowManager.LayoutParams l = ActivityManage.peek().getWindow().getAttributes();
        l.alpha = alpha;
        ActivityManage.peek().getWindow().setAttributes(l);
    }

}



