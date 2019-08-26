package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/21 下午12:17
 * <p/>
 * Description:
 */
public class CreditBankRec {
    /** 开户行 */
    private String bank;
    /** 绑定时间 */
    private String bindTime;
    /** 卡号 */
    private String cardNo;
    /**  */
    private String id;
    /** 预留手机号 */
    private String phone;
    /** 用户id */
    private String userId;

    public String getBank() {
        return bank;
    }

    public String getBindTime() {
        return bindTime;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserId() {
        return userId;
    }
}
