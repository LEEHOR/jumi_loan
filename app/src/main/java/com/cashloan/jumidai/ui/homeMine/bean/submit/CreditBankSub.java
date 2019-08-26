package com.cashloan.jumidai.ui.homeMine.bean.submit;

import com.google.gson.annotations.SerializedName;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/21 下午12:17
 * <p/>
 * Description:
 */
public class CreditBankSub {
    /** 开户行 */
    private String name;
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
    /** 预留手机号 */
    @SerializedName("captcha")
    private String code;
    /** 用户id */
    private String userId;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBindTime() {
        return bindTime;
    }

    public void setBindTime(String bindTime) {
        this.bindTime = bindTime;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
