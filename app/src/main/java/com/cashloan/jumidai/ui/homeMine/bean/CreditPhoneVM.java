package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.utils.ContextHolder;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/3/2 上午9:39
 * <p/>
 * Description:
 */
public class CreditPhoneVM {

    /** 提示信息 */
    private String  tips;
    /** 按钮文字 */
    private String  btnStr;
    /** 状态 */
    private String  state;
    /** 按钮背景色 */
    private boolean enable;

    public String getTips() {
        if(Constant.STATUS_30.equals(state)){
            return ContextHolder.getContext().getString(R.string.phone_credited_tips);
        } else if(Constant.STATUS_20.equals(state)){
            return ContextHolder.getContext().getString(R.string.phone_crediting_tips);
        } else {
            return ContextHolder.getContext().getString(R.string.phone_no_credit_tips);
        }
    }

    public String getBtnStr() {
        if(Constant.STATUS_30.equals(state)){
            return ContextHolder.getContext().getString(R.string.phone_credited);
        } else if(Constant.STATUS_20.equals(state)){
            return ContextHolder.getContext().getString(R.string.phone_crediting);
        } else {
            return ContextHolder.getContext().getString(R.string.phone_go_credit);
        }
    }

    public boolean isEnable() {
        if(Constant.STATUS_30.equals(state) || Constant.STATUS_20.equals(state)){
            return false;
        } else {
            return true;
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
