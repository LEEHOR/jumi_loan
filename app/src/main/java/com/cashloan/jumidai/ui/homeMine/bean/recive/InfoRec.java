package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2017/2/20 下午8:47
 * <p>
 * Description:
 */
public class InfoRec {
    /**
     * creditTotal : 1000
     * creditUnused : 800
     * creditUsed : 200
     * invitationCode : a1bd2c
     * phone : 15911111111
     */
    private double creditTotal;
    private double creditUnused;
    private double creditUsed;
    private String invitationCode;
    private String phone;
    private String profitRate;
    /** 个人信息提交状态 */
    private String idState;
    /** =银行卡绑定状态 */
    private String bankCardState;

    public String getProfitRate() {
        return profitRate;
    }

    public double getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(double creditTotal) {
        this.creditTotal = creditTotal;
    }

    public double getCreditUnused() {
        return creditUnused;
    }

    public void setCreditUnused(double creditUnused) {
        this.creditUnused = creditUnused;
    }

    public double getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(double creditUsed) {
        this.creditUsed = creditUsed;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdState() {
        return idState;
    }

    public String getBankCardState() {
        return bankCardState;
    }
}
