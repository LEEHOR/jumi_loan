package com.cashloan.jumidai.ui.homeMine.bean.submit;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/21 上午10:47
 * <p/>
 * Description: 联系人提交
 */
public class CreditLinkerSub {
    /** 新增就不传，更新传下 */
    private String id;
    /** 姓名 */
    private String name;
    /** 电话号码 */
    private String phone;
    /** 关系(中文) */
    private String relation;
    /** 是否直系,10直系，20其他	 */
    private String type;
    /** mac地址 */
    private String mac;
    /** 操作系统 */
    private String operatingSystem;
    /** 手机品牌 */
    private String phoneBrand;
    /** 手机设备标识 */
    private String phoneMark;
    /** 手机型号 */
    private String phoneType;
    /** 系统版本 */
    private String systemVersions;
    /** 应用build号 */
    private String versionCode;
    /** 应用版本号 */
    private String versionName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }

    public String getPhoneMark() {
        return phoneMark;
    }

    public void setPhoneMark(String phoneMark) {
        this.phoneMark = phoneMark;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getSystemVersions() {
        return systemVersions;
    }

    public void setSystemVersions(String systemVersions) {
        this.systemVersions = systemVersions;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
