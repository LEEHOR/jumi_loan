package com.cashloan.jumidai.ui.user.bean;

import com.commom.base.BaseBean;
import com.commom.utils.StringFormat;
import com.commom.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/16 16:17
 * <p/>
 * Description: 登录页面模型
 */
public class LoginVM extends BaseBean {
    /** 手机号 */
    private String  phone;
    private String  phoneHide;
    /** 密码 */
    private String  pwd;
    /** 按钮是否可用 */
    private boolean enable;
    /** 按钮是否可用 */
    private boolean stepEnable;
    /** 是否为下一步 */
    private boolean step = true;

    /** 短信验证码 */
    private  String loginCode;
    //设备指纹和唯一标志码
    private String MEMI;
    private String IMSI;

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public LoginVM() {
        /*phone = "13758213355";
        pwd = "123456";*/
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        checkPhoneInput();
    }

    public String getPhoneHide() {
        return StringFormat.phoneHideFormat(phone);
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        checkPwdInput();
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    public boolean isStepEnable() {
        return stepEnable;
    }

    public void setStepEnable(boolean stepEnable) {
        this.stepEnable = stepEnable;
    }

    public boolean isStep() {
        return step;
    }

    public void setStep(boolean step) {
        this.step = step;
    }

    private void checkPhoneInput() {
        if (TextUtil.isEmpty(phone) || phone.length() != 11) {
            setStepEnable(false);
        } else {
            setStepEnable(true);
        }
    }

    private void checkPwdInput() {
        if (TextUtil.isEmpty(pwd) || pwd.length() < 6) {
            setEnable(false);
        } else {
            setEnable(true);
        }
    }

    public void setPhoneHide(String phoneHide) {
        this.phoneHide = phoneHide;
    }

    public String getMEMI() {
        return MEMI;
    }

    public void setMEMI(String MEMI) {
        this.MEMI = MEMI;
    }

    public String getIMSI() {
        return IMSI;
    }

    public void setIMSI(String IMSI) {
        this.IMSI = IMSI;
    }

}
