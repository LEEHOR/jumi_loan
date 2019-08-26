package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/24 下午12:02
 * <p/>
 * Description:
 */
public class CreditPersonRec {
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 身份证背面
     */
    private String backImg;
    /**
     * 详细地址
     */
    private String liveDetailAddr;
    /**
     * 学历
     */
    private String education;
    /**
     * 身份证正面
     */
    private String frontImg;
    /**
     * 身份证号
     */
    private String idNo;
    /**
     * 现居住地址
     */
    private String liveAddr;
    /**
     * 自拍
     */
    private String livingImg;
    /**
     * 身份证上照片
     */
    private String ocrImg;
    /**
     * 姓名
     */
    private String realName;

    // update 2019-7-15
    /**性别*/
    private String sex;
    /** 年龄 */
    private Integer age;

    /** 签发机关 */
    private String issuingAuthority;

    /** 身份证有效期 */
    private String validityPeriod;

    /** 民族 */
    private String national;

    public String getBackImg() {
        return backImg;
    }

    public String getLiveDetailAddr() {
        return liveDetailAddr;
    }

    public String getEducation() {
        return education;
    }

    public String getFrontImg() {
        return frontImg;
    }

    public String getIdNo() {
        return idNo;
    }

    public String getLiveAddr() {
        return liveAddr;
    }

    public String getLivingImg() {
        return livingImg;
    }

    public String getOcrImg() {
        return ocrImg;
    }

    public String getRealName() {
        return realName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }


    public Integer getAge() {
        return age;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public String getSex() {
        return sex;
    }

    public String getNational() {
        return national;
    }
}
