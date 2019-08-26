package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;
import com.commom.utils.TextUtil;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/21 下午2:20
 * <p>
 * Description:
 */
public class CreditBankVM extends BaseBean {
    /**
     * 是否重新绑定
     */
    private boolean isAgain = false;
    /**
     * 持卡人信命
     */
    private String  name;
    /**
     * 银行名称
     */
    private String  bankName;
    /**
     * 银行卡号
     */
    private String  cardNo;
    /**
     * 手机号
     */
    private String  phone;
    /**
     * 验证码
     */
    private String  code;
    /**
     * 是否可操作
     */
    private boolean enable;
    /**
     * 提示信息
     */
    private String  remark;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        checkInput();
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
        checkInput();
    }


    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
        checkInput();
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        checkInput();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        checkInput();
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    public boolean isAgain() {
        return isAgain;
    }

    public void setAgain(boolean again) {
        isAgain = again;
    }


    public String getRemark() {
        /*if(TextUtils.isEmpty(remark)){
            return Html.fromHtml("");
        }
        return Html.fromHtml(remark);*/
        return remark;
    }

    private void checkInput() {
        System.out.println("name" + name + "bankName" + bankName + "cardNo" + cardNo);
        if (TextUtil.isEmpty(name) || TextUtil.isEmpty(bankName)
                || TextUtil.isEmpty(cardNo) || TextUtil.isEmpty(code)) {
            setEnable(false);
        } else {
            setEnable(true);
        }
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
