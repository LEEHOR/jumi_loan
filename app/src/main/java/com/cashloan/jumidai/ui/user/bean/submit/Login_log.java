package com.cashloan.jumidai.ui.user.bean.submit;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * author : Leehor
 * date   : 2019/7/1919:01
 * version: 1.0
 * desc   :登录日志
 */
public class Login_log {
    /**
     * 用户Id
     */
    private long userId;

    /** 设备指纹 */
    @SerializedName("blackBox")
    private String box;

    /**设备类型*/
    private String client;
    /**
     * 注册地址
     */
    private String address;
    /**
     * 注册经纬度
     */
    private String coordinate;

    /**
     * 唯一识别码
     */
    private String deviceId;
    /**
     * 注册时间
     */
    private long updateTime;

    /**
     * 定位时间
     */
    private long locationTime;
    /**
     * 手机型号
     */
    private String termModel;

    /**
     * denglu、注册、
     */
    private String type;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(long locationTime) {
        this.locationTime = locationTime;
    }

    public String getTermModel() {
        return termModel;
    }

    public void setTermModel(String termModel) {
        this.termModel = termModel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Login_log() {
    }
}
