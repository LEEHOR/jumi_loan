package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/24 16:03
 * <p>
 * Description:
 */
public class InviteAwardItemRec {
    /**
     * addTime : 2017-02-23 14:31:34
     * amount : 10
     * loginName : 15312345678
     * msg : 借款1000.0元,综合费用150.0元
     */
    private String addTime;
    private String amount;
    private String loginName;
    private String msg;

    public InviteAwardItemRec(String addTime, String amount, String loginName, String msg) {
        this.addTime = addTime;
        this.amount = amount;
        this.loginName = loginName;
        this.msg = msg;
    }

    public String getAddTime() {
        return addTime;
    }

    public String getAmount() {
        return amount;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMsg() {
        return msg;
    }
}
