package com.cashloan.jumidai.ui.user.bean.submit;

import com.commom.net.OkHttp.annotation.SerializedEncryption;
import com.google.gson.annotations.SerializedName;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/17 16:18
 * <p/>
 * Description: 注册需要提交的数据
 */
public class RegisterSub {
    /**
     * 手机号
     */
    @SerializedName("loginName")
    private String phone;
    /**
     * 密码
     */
    @SerializedEncryption(type = "MD5")
    @SerializedName("loginPwd")
    private String pwd;
    @SerializedName("vcode")
    private String code;
    /**
     * 邀请人手机号
     */
    @SerializedName("invitationCode")
    private String inviter;

    /**
     * 设备指纹
     */
    @SerializedName("blackBox")
    private String box;
    /**
     * 设备类型
     */
    private String client;

    //update2019-7-19 登录已日志分离，以下字段无需使用
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

    /**
     * 是否同意协议
     */
    private int agree = 1;
    private String browser;

    public RegisterSub() {
    }

    /**
     * 注册地址
     */
    private String registerAddr;
    /**
     * 注册经纬度
     */
    private String registerCoordinate;
    /**
     * 添加渠道信息
     */
    private String channelCode;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public RegisterSub(String phone, String pwd, String inviter) {
        this.phone = phone;
        this.pwd = pwd;
        this.inviter = inviter;
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
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public int getAgree() {
        return agree;
    }

    public void setAgree(int agree) {
        this.agree = agree;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
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

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }
}
