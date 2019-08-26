package com.cashloan.jumidai.ui.homeLoan.bean;

import com.commom.base.BaseBean;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/14 15:58
 * <p/>
 * Description:
 */
public class LoanVM extends BaseBean {
    private String bank;
    private String cardNo;
    private String cardId;
    private boolean clickable = true;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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


    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
