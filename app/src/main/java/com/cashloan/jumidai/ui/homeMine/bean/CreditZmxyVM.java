package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;
import com.commom.utils.ContextHolder;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/21 下午6:00
 * <p/>
 * Description:
 */
public class CreditZmxyVM extends BaseBean {
    /**
     * 是否已绑定
     */
    private String  isCredit;
    /**
     * 芝麻信用分
     */
    private String  score;
    /**
     * 按钮文字
     */
    private String  btnStr;
    /**
     * 提示内容文字
     */
    private String  tipsStr;
    /**
     * 是否可点击
     */
    private boolean enable;
    private boolean visiable;

    public String getIsCredit() {
        return isCredit;
    }

    public void setIsCredit(String isCredit) {
        this.isCredit = isCredit;
        if (Constant.STATUS_10.equals(isCredit)) {
            enable = true;
        } else {
            enable = false;
        }
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


    public String getBtnStr() {
        return Constant.STATUS_10.equals(isCredit) ? ContextHolder.getContext().getString(R.string.zmxy_not_apply)
                : ContextHolder.getContext().getString(R.string.zmxy_applied);
    }


    public String getTipsStr() {
        return Constant.STATUS_10.equals(isCredit) ? ContextHolder.getContext().getString(R.string.zmxy_not_apply_tip)
                : ContextHolder.getContext().getString(R.string.zmxy_applied_tip);
    }


    public boolean isEnable() {
        return enable;
    }


    public boolean isVisiable() {
        return visiable;
    }

    public void setVisiable(boolean visiable) {
        this.visiable = visiable;
    }
}
