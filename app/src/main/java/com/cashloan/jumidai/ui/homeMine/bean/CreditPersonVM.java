package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;
import com.commom.utils.ConverterUtil;

/**
 * 作者： Ruby
 * 时间： 2018/8/2
 * 描述： 个人信息
 */
public class CreditPersonVM extends BaseBean {
    /**
     * 人脸识别图片
     */
    private String faceImg;
    /**
     * 身份证正面
     */
    private String cardPositive;
    /**
     * 身份证反面
     */
    private String cardOpposite;
    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String cardNo;

    // update 2019-7-15
    /**性别*/
    private String gender;
    /** 年龄 */
    private Integer age;

    /** 签发机关 */
    private String issuingAuthority;

    /** 身份证有效期 */
    private String validityPeriod;

    /** 民族 */
    private String nation;

    /**
     * 学历
     */
    private String education;
    /**
     * 地址
     */
    private String address;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 地址-门牌号
     */
    private String addressDetail;
    /**
     * 婚姻状态
     */
    private String marriage;
    /**
     * 居住时长
     */
    private String liveTime;
    private boolean enable = true;
    /**
     * 活体识别可操作次数
     */
    private String faceTime;
    private int    faceTimeInt;
    /**
     * ocr识别可操作次数
     */
    private String ocrTime;
    private int    ocrTimeInt;

    
    public String getFaceImg() {
        return faceImg;
    }

    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    
    public String getCardPositive() {
        return cardPositive;
    }

    public void setCardPositive(String cardPositive) {
        this.cardPositive = cardPositive;
    }

    
    public String getCardOpposite() {
        return cardOpposite;
    }

    public void setCardOpposite(String cardOpposite) {
        this.cardOpposite = cardOpposite;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    
    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    
    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    
    public String getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
    }

    
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getFaceTime() {
        return faceTime;
    }

    public void setFaceTime(String faceTime) {
        this.faceTime = faceTime;
        setFaceTimeInt(ConverterUtil.getInteger(faceTime));
    }

    public int getFaceTimeInt() {
        return faceTimeInt;
    }

    public void setFaceTimeInt(int faceTimeInt) {
        this.faceTimeInt = faceTimeInt;
    }

    public String getOcrTime() {
        return ocrTime;
    }

    public void setOcrTime(String ocrTime) {
        this.ocrTime = ocrTime;
        setOcrTimeInt(ConverterUtil.getInteger(ocrTime));
    }

    public int getOcrTimeInt() {
        return ocrTimeInt;
    }

    public void setOcrTimeInt(int ocrTimeInt) {
        this.ocrTimeInt = ocrTimeInt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
}
