package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/24 16:32
 * <p>
 * Description: 提现信息
 */
public class InviteWithdrawItemRec {
    private String addTime;     //时间
    private String amount;      //金额

    public InviteWithdrawItemRec(String addTime, String amount) {
        this.addTime = addTime;
        this.amount = amount;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
