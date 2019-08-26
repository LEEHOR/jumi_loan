package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;
import com.commom.utils.TextUtil;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/22 9:27
 * <p/>
 * Description:
 */
public class SettingsUpdatePwdVM extends BaseBean {
    String phone;
    String pwd;
    String newPwd;
    String confirmPwd;
    boolean enable = false;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        checkInput();
    }


    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
        checkInput();
    }


    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
        checkInput();
    }

    public void checkInput() {
        setEnable(!(TextUtil.isEmpty(pwd)
                || TextUtil.isEmpty(newPwd)
                || TextUtil.isEmpty(confirmPwd)
                || pwd.length() < 6
                || newPwd.length() < 6
                || confirmPwd.length() < 6));
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
