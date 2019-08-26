package com.cashloan.jumidai.ui.homeLoan.bean;

import com.commom.net.OkHttp.annotation.SerializedEncryption;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/20 14:01
 * <p>
 * Description:申请借款信息
 */
public class LoanSub {
    private String amount;//借款金额	string		是
    private String cardId;//	银行卡关联id	string		是
    private String fee;//	综合费用	string		是
    private String realAmount;//实际到账金额	string		是
    private String timeLimit;//借款期限	string		是
    @SerializedEncryption(type = "MD5")
    private String tradePwd;//交易密码	string		是	MD5加密
    private String client = "Android"; //设备类型 Android ios
    private String address;
    private String coordinate;
    //update 2019-7-18 移除ip 添加deviceId 设备指纹IMEI; termModel 手机型号
    private String deviceId ;
    private String termModel;
  //  private String ip;
    //message  ,contacts
    private String message;
    private String contacts;


    public LoanSub(String tradePwd) {

        this.tradePwd = tradePwd;

    }

    public LoanSub(String amount, String cardId, String fee, String realAmount, String timeLimit,
                   String tradePwd, String address, String coordinate,  String message,
                   String contacts,String deviceId,String termModel,String client) {
        this.amount = amount;
        this.cardId = cardId;
        this.fee = fee;
        this.realAmount = realAmount;
        this.timeLimit = timeLimit;
        this.tradePwd = tradePwd;
        this.address = address;
        this.coordinate = coordinate;
        this.message = message;
        this.contacts = contacts;
        this.deviceId=deviceId;
        this.termModel=termModel;
        this.client=client;}


    public LoanSub(String amount, String cardId, String fee, String realAmount, String timeLimit,
                   String tradePwd, String address, String coordinate,String deviceId,String termModel,String client) {
        this.amount = amount;
        this.cardId = cardId;
        this.fee = fee;
        this.realAmount = realAmount;
        this.timeLimit = timeLimit;
        this.tradePwd = tradePwd;
        this.address = address;
        this.coordinate = coordinate;
        this.deviceId=deviceId;
        this.termModel=termModel;
        this.client=client;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTermModel() {
        return termModel;
    }

    public void setTermModel(String termModel) {
        this.termModel = termModel;
    }
}
