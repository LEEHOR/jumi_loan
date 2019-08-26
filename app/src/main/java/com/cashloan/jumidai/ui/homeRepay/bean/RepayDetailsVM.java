package com.cashloan.jumidai.ui.homeRepay.bean;


import com.commom.base.BaseBean;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/15 15:29
 * <p>
 * Description:
 */
public class RepayDetailsVM extends BaseBean {
    private String bank   = "--";
    private String amount = "--";
    private String cardId;
    private String createTime = "--";
    private String fee        = "--";
    private String id;
    private String realAmount  = "--";
    private String repayAmount = "--";
    private String repayTime;
    private String timeLimit = "--";
    private String cardNo;
    private String creditTimeStr   = "--";
    private String repayTimeStr    = "--";
    private String passFeeStr      = "--";
    /** 实际还款金额 */
    private String realRepayAmount = "--";
    /** 实际还款时间 */
    private String realRepayTime   = "--";


    public String getCreditTimeStr() {
        return creditTimeStr;
    }

    public void setCreditTimeStr(String creditTimeStr) {
        this.creditTimeStr = creditTimeStr;
    }


    public String getRepayTimeStr() {
        return repayTimeStr;
    }

    public void setRepayTimeStr(String repayTimeStr) {
        this.repayTimeStr = repayTimeStr;
    }


    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }


    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }


    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }


    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }


    public String getPassFeeStr() {
        return passFeeStr;
    }

    public void setPassFeeStr(String passFeeStr) {
        this.passFeeStr = passFeeStr;
    }

    public String getRealRepayAmount() {
        return realRepayAmount;
    }

    public void setRealRepayAmount(String realRepayAmount) {
        this.realRepayAmount = realRepayAmount;
    }

    public String getRealRepayTime() {
        return realRepayTime;
    }

    public void setRealRepayTime(String realRepayTime) {
        this.realRepayTime = realRepayTime;
    }
}
