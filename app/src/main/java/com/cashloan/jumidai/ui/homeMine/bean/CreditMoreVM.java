package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;
import com.commom.utils.TextUtil;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/3/16 10:28
 * <p>
 * Description:
 */
public class CreditMoreVM extends BaseBean {
    private String  email;     // 常用邮箱	string		是
    private String  qq;    // QQ账号	string		是
    private String  taobao;//	淘宝账号	string		是
    private String  wechat;//	微信账号	string		是
    private boolean enable;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        checkInput();
    }


    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
        checkInput();
    }


    public String getTaobao() {
        return taobao;
    }

    public void setTaobao(String taobao) {
        this.taobao = taobao;
        checkInput();
    }


    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
        checkInput();
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private void checkInput() {
        if (!(TextUtil.isEmpty(qq)
                || TextUtil.isEmpty(wechat)
                || TextUtil.isEmpty(email)
                || TextUtil.isEmpty(taobao))) {
            setEnable(true);
        } else {
            setEnable(false);
        }
    }


    public boolean isEnable() {
        return enable;
    }
}
