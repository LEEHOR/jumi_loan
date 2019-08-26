package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;
import com.commom.utils.ContextHolder;
import com.commom.utils.StringFormat;
import com.cashloan.jumidai.R;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2017/2/16 下午7:16
 * <p>
 * Description: 我的
 */
public class MineVM extends BaseBean {
    private String totalMoney;
    private String phone;
    private String useMoney;
    private double progress;
    private String profitRate;

    public String getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(String profitRate) {
        this.profitRate = profitRate;
    }


    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = StringFormat.subZeroAndDot(totalMoney);
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(double useMoney) {
        this.useMoney = ContextHolder.getContext().getString(R.string.mine_usemoney, StringFormat.subZeroAndDot(useMoney));
    }


    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
