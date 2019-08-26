package com.cashloan.jumidai.ui.homeRepay.bean;


import com.commom.base.BaseBean;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/15 15:29
 * <p>
 * Description:
 */
public class RepayDetailsContentRec extends BaseBean {
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
    private String creditTimeStr = "--";
    private String repayTimeStr  = "--";
    /** 是否逾期 10 逾期 20 未逾期 */
    private String penalty;
    /** 逾期罚金 */
    private String penaltyAmount;

    public String getBank() {
        return bank;
    }

    public String getAmount() {
        return amount;
    }

    public String getCardId() {
        return cardId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getFee() {
        return fee;
    }

    public String getId() {
        return id;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCreditTimeStr() {
        return creditTimeStr;
    }

    public String getRepayTimeStr() {
        return repayTimeStr;
    }

    public String getPenalty() {
        return penalty;
    }

    public String getPenaltyAmount() {
        return penaltyAmount;
    }
}
