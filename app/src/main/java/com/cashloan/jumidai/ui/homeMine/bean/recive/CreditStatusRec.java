package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2017/2/21 下午5:35
 * <p>
 * Description:
 */
public class CreditStatusRec {


    private  String taobaoState;

    public String getTaobaoState() {
        return taobaoState;
    }

    public void setTaobaoState(String taobaoState) {
        this.taobaoState = taobaoState;
    }

    /**
     * 银行卡状态(10未认证/未完善，20认证中/完善中，30已认证/已完善)
     */
    private String bankCardState;
    /**
     * 紧急联系人状态
     */
    private String contactState;
    /**
     * 详细资料状态
     */
    private String detailState;
    /**
     * 人脸识别状态
     */
    private String livingIdentifyState;
    /**
     * 个人信息认证状态
     */
    private String idState;
    /**
     * 手机运营商认证状态
     */
    private String phoneState;
    /**
     * 芝麻授信状态
     */
    private String zhimaState;
    /**
     * 工作状态
     */
    private String workInfoState;
    /**
     * 公积金状态
     */
    private String accFundState;
    /** 更多状态 *//*
    private String otherInfoState;*/
    /**
     * 更多状态
     */
    private String otherInfoState;

    public String getOtherInfoState() {
        return otherInfoState;
    }

    public void setOtherInfoState(String otherInfoState) {
        this.otherInfoState = otherInfoState;
    }

    public String getWorkInfoState() {
        return workInfoState;
    }

    public void setWorkInfoState(String workInfoState) {
        this.workInfoState = workInfoState;
    }

    public String getBankCardState() {
        return bankCardState;
    }

    public void setBankCardState(String bankCardState) {
        this.bankCardState = bankCardState;
    }

    public String getContactState() {
        return contactState;
    }

    public void setContactState(String contactState) {
        this.contactState = contactState;
    }

    public String getDetailState() {
        return detailState;
    }

    public void setDetailState(String detailState) {
        this.detailState = detailState;
    }

    public String getLivingIdentifyState() {
        return livingIdentifyState;
    }

    public void setLivingIdentifyState(String livingIdentifyState) {
        this.livingIdentifyState = livingIdentifyState;
    }

    public String getIdState() {
        return idState;
    }

    public void setIdState(String idState) {
        this.idState = idState;
    }

    public String getPhoneState() {
        return phoneState;
    }

    public void setPhoneState(String phoneState) {
        this.phoneState = phoneState;
    }

    public String getZhimaState() {
        return zhimaState;
    }

    public void setZhimaState(String zhimaState) {
        this.zhimaState = zhimaState;
    }

    public String getAccFundState() {
        return accFundState;
    }
}
