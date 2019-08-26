package com.cashloan.jumidai.ui.user.bean;

import com.commom.base.BaseBean;
import com.cashloan.jumidai.utils.InputCheck;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/17 18:50
 * <p/>
 * Description: 重置密码页面模型
 */
public class ResetPwdVM extends BaseBean {
    /** 旧密码 */
    private String  pwdOld;
    /** 新密码 */
    private String  pwdNew;
    /** 确认密码 */
    private String  pwdConfirm;
    /** 按钮是否可用 */
    private boolean enable;


    public String getPwdOld() {
        return pwdOld;
    }

    public void setPwdOld(String pwdOld) {
        this.pwdOld = pwdOld;
        checkInput();
    }


    public String getPwdNew() {
        return pwdNew;
    }

    public void setPwdNew(String pwdNew) {
        this.pwdNew = pwdNew;
        checkInput();
    }


    public String getPwdConfirm() {
        return pwdConfirm;
    }

    public void setPwdConfirm(String pwdConfirm) {
        this.pwdConfirm = pwdConfirm;
        checkInput();
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private void checkInput() {
        if (InputCheck.checkPwd(pwdOld) && InputCheck.checkPwd(pwdNew) && pwdNew.equals(pwdConfirm)) {
            setEnable(true);
        } else {
            setEnable(false);
        }
    }
}
