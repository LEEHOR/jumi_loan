package com.cashloan.jumidai.ui.homeMine.bean.submit;

import com.commom.net.OkHttp.annotation.SerializedEncryption;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/22 11:28
 * <p>
 * Description:
 */
public class UpdatePwdSub {
    @SerializedEncryption(type = "MD5")
    private String newPwd;
    @SerializedEncryption(type = "MD5")
    private String tradePwd;
    @SerializedEncryption(type = "MD5")
    private String oldPwd;
    @SerializedEncryption(type = "MD5")
    private String pwd;

    public UpdatePwdSub() {
    }

    public UpdatePwdSub(String tradePwd) {
        this.tradePwd = tradePwd;
    }

    public UpdatePwdSub(String newPwd, String oldPwd) {
        this.newPwd = newPwd;
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
