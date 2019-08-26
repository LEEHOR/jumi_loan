package com.cashloan.jumidai.ui.homeMine.bean.submit;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/3/16 10:24
 * <p>
 * Description: 更多信息提交
 */
public class CreditMoreSub {
    private String email;     // 常用邮箱	string		是
    private String qq;    // QQ账号	string		是
    private String taobao;//	淘宝账号	string		是
    private String wechat;//	微信账号	string		是

    public CreditMoreSub(String email, String qq, String taobao, String wechat) {
        this.email = email;
        this.qq = qq;
        this.taobao = taobao;
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getTaobao() {
        return taobao;
    }

    public void setTaobao(String taobao) {
        this.taobao = taobao;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
}
