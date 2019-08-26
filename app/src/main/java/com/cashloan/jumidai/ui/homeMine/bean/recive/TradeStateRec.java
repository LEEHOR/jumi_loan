package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2017/2/25 下午4:10
 * <p/>
 * Description:
 */
public class TradeStateRec {
    /**
     * changeable : false  是否可修改
     * forgetable : true  是否可以忘记密码操作
     * setable : false  是否可设置
     */
    private boolean changeable;
    private boolean forgetable;
    private boolean setable;

    public boolean isChangeable() {
        return changeable;
    }

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }

    public boolean isForgetable() {
        return forgetable;
    }

    public void setForgetable(boolean forgetable) {
        this.forgetable = forgetable;
    }

    public boolean isSetable() {
        return setable;
    }

    public void setSetable(boolean setable) {
        this.setable = setable;
    }
}
