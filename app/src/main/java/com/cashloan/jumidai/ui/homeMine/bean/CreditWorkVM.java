package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;
import com.cashloan.jumidai.common.Constant;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/20 下午5:32
 * <p/>
 * Description:
 */
public class CreditWorkVM extends BaseBean {
    /** 单位名称 */
    private String companyName;
    /** 单位电话 */
    private String companyPhone;
    /** 单位地址 */
    private String companyAddress;
    /** 门牌号 */
    private String addressDetail;
    /** 工作照 */
    private String workPhoto;
    /** 工作时长 */
    private String workTime;
    /**  */
    private String workTimeStr;
    /** 经纬度 */
    private String companyCoordinate;
    /** 10已上传 其他未上传 */
    private String workImgState;

    public String getWorkImgState() {
        return workImgState;
    }

    public void setWorkImgState(String workImgState) {
        this.workImgState = workImgState;
    }

    public String getCompanyCoordinate() {
        return companyCoordinate;
    }

    public void setCompanyCoordinate(String companyCoordinate) {
        this.companyCoordinate = companyCoordinate;
    }

      
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

      
    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

      
    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

      
    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

      
    public String getWorkPhoto() {
        if (Constant.STATUS_10.equals(workPhoto)) {
            return "已上传";
        } else {
            return "";
        }
    }

    public void setWorkPhoto(String workPhoto) {
        this.workPhoto = workPhoto;
    }

      
    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

      
    public String getWorkTimeStr() {
        return workTimeStr;
    }

    public void setWorkTimeStr(String workTimeStr) {
        this.workTimeStr = workTimeStr;
    }
}
