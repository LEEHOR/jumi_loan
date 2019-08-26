package com.cashloan.jumidai.ui.user.bean.submit;

import com.commom.net.OkHttp.annotation.SerializedEncryption;
import com.google.gson.annotations.SerializedName;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/17 16:18
 * <p/>
 * Description: 登录需要提交的数据
 */
public class LoginSub {
    /** 手机号 */
    @SerializedName("loginName")
    private String id;
    /** 登录密码 */
    @SerializedEncryption(type = "MD5")
    @SerializedName("loginPwd")
    private String pwd;
    /** 设备指纹 */
    @SerializedName("blackBox")
    private String box;

    /**设备类型*/
    private String client;
    /**
     * 注册地址
     */
    private String registerAddr;
    /**
     * 注册经纬度
     */
    private String registerCoordinate;

    /**
     * 唯一识别码
     */
    private String device_id;
    /**
     * 注册时间
     */
    private long update_time;

    /**
     * 定位时间
     */
    private long location_time;
    /**
     * 手机型号
     */
    private String term_model;

    public LoginSub() {
    }

    public LoginSub(String id, String pwd) {
        this.id = id;
        this.pwd = pwd;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    /** 短信验证码 */
    @SerializedName("vcode")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }

    public String getRegisterCoordinate() {
        return registerCoordinate;
    }

    public void setRegisterCoordinate(String registerCoordinate) {
        this.registerCoordinate = registerCoordinate;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getLocation_time() {
        return location_time;
    }

    public void setLocation_time(long location_time) {
        this.location_time = location_time;
    }

    public String getTerm_model() {
        return term_model;
    }

    public void setTerm_model(String term_model) {
        this.term_model = term_model;
    }
}
