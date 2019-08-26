package com.cashloan.jumidai.ui.homeRepay.bean;

import com.commom.base.BaseBean;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/21 17:29
 * <p/>
 * Description:
 */
public class RepayAccountVM extends BaseBean {
    private String alipayAccount = "---";
    private String bank          = "---";
    private String bankCard      = "---";
    private String name          = "---";


    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }


    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }


    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
