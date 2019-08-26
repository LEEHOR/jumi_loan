package com.cashloan.jumidai.ui.homeMine.bean.submit;

/**
 * Created by mingchen on 17/3/17.
 * 签约响应类
 */
public class AuthSignSub {
    /** 签约协议号 */
    private String agreeNo;
    /** 签约响应结果 */
    private String signResult;
    /** 签约响应user_id */
    private String uuid;
    /** 开户行 */
    private String bank;
    /** 卡号 */
    private String cardNo;
    /** 预留手机号 */
    private String phone;

    public String getAgreeNo() {
        return agreeNo;
    }

    public void setAgreeNo(String agreeNo) {
        this.agreeNo = agreeNo;
    }

    public String getSignResult() {
        return signResult;
    }

    public void setSignResult(String signResult) {
        this.signResult = signResult;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
