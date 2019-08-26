package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/21 上午11:25
 * <p/>
 * Description: 联系人信息
 */
public class CreditLinkerRec {

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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRelation() {
        return relation;
    }

    public String getType() {
        return type;
    }
}
