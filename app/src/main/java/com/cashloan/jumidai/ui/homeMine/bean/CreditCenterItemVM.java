package com.cashloan.jumidai.ui.homeMine.bean;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import com.commom.base.BaseBean;
import com.commom.utils.ContextHolder;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;

/**
 * 作者： Ruby
 * 时间： 2018/8/2
 * 描述： 人中中心条
 */
public class CreditCenterItemVM extends BaseBean {
    /**
     * 图标
     */
    private Drawable icon;
    /**
     * 图标
     */
    private int      iconFont;
    /**
     * 图标
     */
    private int      iconId;
    /**
     * 标题
     */
    private String   title;
    /**
     * 提示语
     */
    private String   tips;
    /**
     * 是否必填
     */
    private boolean  must;
    private boolean  mustStr;
    /**
     * 是否已完成
     */
    private String   complete;
    /**
     * 是否已完成 是否显示
     */
    private boolean  completeV;

    public Drawable getIcon() {
        if (must) {
            return icon;
        } else {
            icon.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            return icon;
        }
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public boolean isMust() {
        return must;
    }

    public void setMust(boolean must) {
        this.must = must;
    }

    public Spannable isMustStr() {
        return getSpan(must);
    }

    public String isComplete() {
        if (Constant.STATUS_30.equals(complete)) {
            return ContextHolder.getContext().getString(R.string.credit_complete);
        } else if (Constant.STATUS_20.equals(complete)) {
            return ContextHolder.getContext().getString(R.string.credit_completeing);

        } else if (Constant.STATUS_25.equals(complete)) {
            return ContextHolder.getContext().getString(R.string.credit_failed);
        } else {
            return ContextHolder.getContext().getString(R.string.credit_no_complete);
        }
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    private Spannable getSpan(boolean isMust) {
        int color;
        String stateStr;
        if (!isMust) {
            color = R.color.text_grey;
            stateStr = ContextHolder.getContext().getString(R.string.credit_no_must);
        } else {
            color = R.color.app_color_secondary;
            stateStr = ContextHolder.getContext().getString(R.string.credit_must);

        }
        Spannable span = new SpannableString(stateStr);
        span.setSpan(new ForegroundColorSpan(ContextHolder.getContext().getResources().getColor(color)), 0, stateStr.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }


    public boolean isCompleteV() {
        if (Constant.STATUS_30.equals(complete)) {
            return true;
        } else {
            return false;
        }
    }

    public void setCompleteV(boolean completeV) {
        this.completeV = completeV;
    }

    public int getIconFont() {
        return iconFont;
    }

    public void setIconFont(@StringRes int iconFont) {
        this.iconFont = iconFont;
    }
}
