package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/20 下午7:42
 * <p/>
 * Description:
 */
public class CreditLinkerVM extends BaseBean {
    //联系人关系
    private String relation1;
    private String relationStr1;
    //联系人姓名
    private String name1;
    //联系人电话
    private String phone1;
    //联系人关系
    private String relation2;
    private String relationStr2;
    //联系人姓名
    private String name2;
    //联系人电话
    private String phone2;
    private boolean enable = true;


    public String getRelationStr1() {
        return relationStr1;
    }

    public void setRelationStr1(String relationStr1) {
        this.relationStr1 = relationStr1;
    }


    public String getRelationStr2() {
        return relationStr2;
    }

    public void setRelationStr2(String relationStr2) {
        this.relationStr2 = relationStr2;
    }


    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }


    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }


    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }


    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }


    public String getRelation1() {
        return relation1;
    }

    public void setRelation1(String relation1) {
        this.relation1 = relation1;
    }


    public String getRelation2() {
        return relation2;
    }

    public void setRelation2(String relation2) {
        this.relation2 = relation2;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
