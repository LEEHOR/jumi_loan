package com.cashloan.jumidai.ui.homeRepay.bean;

import com.commom.base.BaseBean;

/**
 * Created by lichunfu on 2017/8/24.
 */

public class JuheDataRec extends BaseBean {

    /**
     * charset : UTF-8
     * nonce_str : 9pfotfe0tbqboh5nol1
     * appid : ca2017081510000132
     * result_code : SUCCESS
     * mch_id : cm2017081510000132
     * sign_type : MD5
     * return_code : SUCCESS
     * version : 2.0.0
     * prepay_id : 071B1B1F1C5540401E1D410E03061F0E16410C0002400D0E175F585A5D5C075615090A060E1C1D02060A575F5E5E
     */

    private String charset;
    private String nonce_str;
    private String appid;
    private String result_code;
    private String mch_id;
    private String sign_type;
    private String return_code;
    private String version;
    private String prepay_id;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    /**
     * update  2018-7-8
     */

    private String req;//返回的链接

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    private String picCode;

    public String getPicCode() {
        return picCode;
    }

    public void setPicCode(String picCode) {
        this.picCode = picCode;
    }
}
