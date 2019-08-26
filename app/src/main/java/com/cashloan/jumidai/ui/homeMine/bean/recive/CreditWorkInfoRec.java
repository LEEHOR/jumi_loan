package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/3/16 17:34
 * <p/>
 * Description:
 */
public class CreditWorkInfoRec {
    private String companyAddr;//  string	    	是	@mock=海蓝金融科技中心丰潭路508号
    private String companyCoordinate;//		string	    	是	@mock=
    private String companyName;//  string	    	是	@mock=杭州融都科技股份有限公司
    private String companyDetailAddr;
    private String companyPhone;//  string	    	是	@mock=
    private String id;//  number	    	是	@mock=1
    private String salary;//  string	    	是	@mock=5000
    private String userId;//  number	    	是	@mock=1
    private String workingImg;//  string	    	是	@mock=/112.jpg;/1121.jpg;1121.jpg
    private String workingYears;//  string	    	是	@mock=3年
    private String workImgState;

    public String getWorkImgState() {
        return workImgState;
    }

    public String getCompanyDetailAddr() {
        return companyDetailAddr;
    }

    public String getCompanyAddr() {
        return companyAddr;
    }

    public String getCompanyCoordinate() {
        return companyCoordinate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public String getId() {
        return id;
    }

    public String getSalary() {
        return salary;
    }

    public String getUserId() {
        return userId;
    }

    public String getWorkingImg() {
        return workingImg;
    }

    public String getWorkingYears() {
        return workingYears;
    }
}
