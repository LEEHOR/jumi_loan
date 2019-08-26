package com.cashloan.jumidai.ui.homeMine.bean.submit;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/3/16 10:31
 * <p>
 * Description:
 */
public class CreditWorkSub {
    private String companyAddr;//单位地址	string		是
    private String companyCoordinate;//单位坐标(经度，纬度)	string		是
    private String companyDetailAddr;//单位详细地址（门牌号）	string		是
    private String companyName;//单位名称	string		是
    private String companyPhone;//单位电话	string		是
    private String workingYears;//工作时长	string		是

    public CreditWorkSub(String companyAddr, String companyCoordinate,
                         String companyDetailAddr, String companyName,
                         String companyPhone, String workingYears) {
        this.companyAddr = companyAddr;
        this.companyCoordinate = companyCoordinate;
        this.companyDetailAddr = companyDetailAddr;
        this.companyName = companyName;
        this.companyPhone = companyPhone;
        this.workingYears = workingYears;
    }

    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(String companyAddr) {
        this.companyAddr = companyAddr;
    }

    public String getCompanyCoordinate() {
        return companyCoordinate;
    }

    public void setCompanyCoordinate(String companyCoordinate) {
        this.companyCoordinate = companyCoordinate;
    }

    public String getCompanyDetailAddr() {
        return companyDetailAddr;
    }

    public void setCompanyDetailAddr(String companyDetailAddr) {
        this.companyDetailAddr = companyDetailAddr;
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

    public String getWorkingYears() {
        return workingYears;
    }

    public void setWorkingYears(String workingYears) {
        this.workingYears = workingYears;
    }
}
