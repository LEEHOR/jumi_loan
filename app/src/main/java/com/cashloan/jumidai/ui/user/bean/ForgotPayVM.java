package com.cashloan.jumidai.ui.user.bean;


import android.text.TextUtils;

import com.commom.base.BaseBean;
import com.commom.utils.ContextHolder;
import com.cashloan.jumidai.R;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2017/2/25 下午7:49
 * <p/>
 * Description:
 */
public class ForgotPayVM extends BaseBean {
    private String title = ContextHolder.getContext().getString(R.string.settings_forgot_pay_title);
    private String phone;
    private String name;
    private String no;
    private String code;
    private boolean codeEnable = false;
    private boolean enable     = false;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        codeEnableCheck();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        checkInput();
    }


    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
        checkInput();

    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        checkInput();

    }


    public boolean isCodeEnable() {
        return codeEnable;
    }

    public void setCodeEnable(boolean codeEnable) {
        this.codeEnable = codeEnable;
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * TimeButton是否可用
     */
    private void codeEnableCheck() {
        if (TextUtils.isEmpty(phone)) {
            setCodeEnable(false);
        } else {
            setCodeEnable(true);
        }
    }

    /**
     * 输入校验
     */
    private void checkInput() {
        if (TextUtils.isEmpty(no) || no.length() < 16 || TextUtils.isEmpty(name) || name.length() < 2 || TextUtils.isEmpty(code)) {
            setEnable(false);
        } else {
            setEnable(true);
        }
    }
}
