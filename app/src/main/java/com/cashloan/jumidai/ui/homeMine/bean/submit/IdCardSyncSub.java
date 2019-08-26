package com.cashloan.jumidai.ui.homeMine.bean.submit;

/**
 * Created by chenming
 * Created Date 17/4/19 11:50
 * mail:cm1@erongdu.com
 * Describe: 身份证识别同步提交信息
 */
public class IdCardSyncSub {
    /** 身份证 */
    private String idCard;
    /** 用户姓名 */
    private String name;
    /** 10活体识别 20ocr身份证识别 */
    private String type;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
