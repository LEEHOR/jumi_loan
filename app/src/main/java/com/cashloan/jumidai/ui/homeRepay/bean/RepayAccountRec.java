package com.cashloan.jumidai.ui.homeRepay.bean;

import com.commom.base.BaseBean;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/21 17:05
 * <p/>
 * Description:
 */
public class RepayAccountRec extends BaseBean {
    /**
     * alipayAccount : 123123
     * bank : 招商银行
     * bankCard : 622222222222
     * name : 张三
     * type : 10
     */
    private String alipayAccount;
    private String bank;
    private String bankCard;
    private String name;
    private String type;

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public String getBank() {
        return bank;
    }

    public String getBankCard() {
        return bankCard;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
