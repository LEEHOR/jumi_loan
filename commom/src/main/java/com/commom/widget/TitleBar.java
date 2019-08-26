package com.commom.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commom.R;
import com.commom.utils.EmptyUtils;
import com.commom.utils.OnOnceClickListener;

/**
 * 作者： Ruby
 * 时间： 2018/8/6$
 * 描述：
 */

public class TitleBar extends RelativeLayout {

    private Context mContext;

    private ConstraintLayout mLeftMenu;
    private ConstraintLayout mRightMenu;

    private ImageView ivLeft;
    private ImageView ivCenter;
    private ImageView ivRight;
    private ImageView ivRight2;

    private TextView tvLeft;
    private TextView tvCenter;
    private TextView tvRight;


    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        LayoutInflater m_inflate = LayoutInflater.from(context);
        m_inflate.inflate(R.layout.base_toolbar, this, true);

        mLeftMenu = findViewById(R.id.csl_title_left);
        mRightMenu = findViewById(R.id.csl_title_right);

        ivLeft = findViewById(R.id.iv_title_left);
        ivCenter = findViewById(R.id.iv_title_center);
        ivRight = findViewById(R.id.iv_title_right);
        ivRight2 = findViewById(R.id.iv_title_right2);

        tvLeft = findViewById(R.id.tv_title_left);
        tvCenter = findViewById(R.id.tv_title_center);
        tvRight = findViewById(R.id.tv_title_right);
    }

    public ConstraintLayout getLeftMenu() {
        return mLeftMenu;
    }

    public ConstraintLayout getRightMenu() {
        return mRightMenu;
    }

    public ImageView getIvLeft() {
        return ivLeft;
    }

    public ImageView getIvCenter() {
        return ivCenter;
    }

    public ImageView getIvRight() {
        return ivRight;
    }

    public ImageView getIvRight2() {
        return ivRight2;
    }

    public TextView getTvLeft() {
        return tvLeft;
    }

    public TextView getTvCenter() {
        return tvCenter;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    /****----------------对外公开的方法------------------****/


    /**
     * 设置左按钮文字
     *
     * @param leftText 左文字
     */
    final public void setLeftText(String leftText) {

        if (EmptyUtils.isNotEmpty(leftText)) {
            tvLeft.setText(leftText);
        }
    }

    /**
     * 设置左按钮文字
     *
     * @param leftText 左文字--string类型资源引用
     */
    final public void setLeftText(int leftText) {

        if (EmptyUtils.isNotEmpty(leftText)) {
            tvLeft.setVisibility(VISIBLE);
            tvLeft.setText(leftText);
        }
    }

    /**
     * 左侧按钮
     *
     * @param leftIcon 按钮图片
     * @param leftText 按钮文字
     */
    final public void setLeftMenu(int leftIcon, String leftText, OnOnceClickListener onceClickListener) {
        if (EmptyUtils.isNotEmpty(leftIcon)) {
            ivLeft.setImageResource(leftIcon);
        } else {
            ivLeft.setVisibility(GONE);
        }

        if (EmptyUtils.isNotEmpty(leftText)) {
            tvLeft.setVisibility(VISIBLE);
            tvLeft.setText(leftText);
        }

        if (EmptyUtils.isNotEmpty(onceClickListener)) {
            mLeftMenu.setOnClickListener(onceClickListener);
        }
    }


    /**
     * 设置右按钮文字
     *
     * @param rightText 右文字
     */
    final public void setRightText(String rightText) {

        if (EmptyUtils.isNotEmpty(rightText)) {
            tvRight.setText(rightText);
        }
    }

    /**
     * 设置右按钮文字
     *
     * @param rightText 右文字--string类型资源引用
     */
    final public void setRightText(int rightText) {

        if (EmptyUtils.isNotEmpty(rightText)) {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(rightText);
        }
    }

    /**
     * 右侧按钮
     *
     * @param rightIcon 按钮图片
     * @param rightText 按钮文字
     */
    final public void setRightMenu(int rightIcon, String rightText, OnOnceClickListener onceClickListener) {

        mRightMenu.setVisibility(VISIBLE);
        if (EmptyUtils.isNotEmpty(rightIcon) && rightIcon != -1) {
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageResource(rightIcon);
        }

        if (EmptyUtils.isNotEmpty(rightText)) {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(rightText);
        }

        if (EmptyUtils.isNotEmpty(onceClickListener)) {
            mRightMenu.setOnClickListener(onceClickListener);
        }
    }

    /**
     * 设置标题
     *
     * @param titleText 页面标题文字
     */
    final public void setTitleText(int titleText) {
        if (EmptyUtils.isNotEmpty(titleText)) {
            tvCenter.setText(titleText);
        }
    }

    /**
     * 设置标题
     *
     * @param titleText 页面标题文字
     */
    final public void setTitleText(String titleText) {
        if (EmptyUtils.isNotEmpty(titleText)) {
            tvCenter.setText(titleText);
            tvCenter.setTextColor(0xffffffff);
        }
    }


}
