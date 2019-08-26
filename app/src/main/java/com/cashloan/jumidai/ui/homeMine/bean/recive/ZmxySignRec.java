package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/20 下午8:36
 * <p/>
 * Description:
 */
public class ZmxySignRec {

    /** appId */
    private String appId;
    /** 请求参数 */
    private String params;
    /** 签名内容 */
    private String signs;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSigns() {
        return signs;
    }

    public void setSigns(String signs) {
        this.signs = signs;
    }
}
