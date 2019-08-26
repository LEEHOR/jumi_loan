package com.cashloan.jumidai.ui.homeMine.bean.submit;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/22 下午4:40
 * <p/>
 * Description:
 */
public class UpdatePersonSub {
    /** 详细地址 */
    private String detailAddr;
    /** 学历 */
    private String education;
    /** 身份证号 */
    private String idNo;
    /** 现居地址 */
    private String liveAddr;

    /** 经\纬度 */
    private String liveCoordinate;
    /** 姓名 */
    private String realName;

    //  update 2019-7-15 性别、年龄、民族、签发机关、身份证有效期
    /**性别*/
    private String sex;
     /** 年龄 */
    private String age;

    /** 签发机关 */
    private String issuingAuthority;

    /** 身份证有效期 */
    private String validityPeriod;

    /** 民族 */
    private String national;


    private String youDunIdCardFlag;

    private String youDunIdCardOrderNo;

    public String getYouDunIdCardFlag() {
        return youDunIdCardFlag;
    }

    public void setYouDunIdCardFlag(String youDunIdCardFlag) {
        this.youDunIdCardFlag = youDunIdCardFlag;
    }

    public String getYouDunIdCardOrderNo() {
        return youDunIdCardOrderNo;
    }

    public void setYouDunIdCardOrderNo(String youDunIdCardOrderNo) {
        this.youDunIdCardOrderNo = youDunIdCardOrderNo;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getLiveAddr() {
        return liveAddr;
    }

    public void setLiveAddr(String liveAddr) {
        this.liveAddr = liveAddr;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
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

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public String getLiveCoordinate() {
        return liveCoordinate;
    }

    public void setLiveCoordinate(String liveCoordinate) {
        this.liveCoordinate = liveCoordinate;
    }


    //update  2018-8-7

    private String ocrSessionId;
    private String livingSessionId;

    public String getOcrSessionId() {
        return ocrSessionId;
    }

    public void setOcrSessionId(String ocrSessionId) {
        this.ocrSessionId = ocrSessionId;
    }

    public String getLivingSessionId() {
        return livingSessionId;
    }

    public void setLivingSessionId(String livingSessionId) {
        this.livingSessionId = livingSessionId;
    }
}
