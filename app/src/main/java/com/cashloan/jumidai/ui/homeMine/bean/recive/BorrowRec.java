package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2017/2/21 下午5:39
 * <p/>
 * Description:
 */
public class BorrowRec {
    /**
     * id : 1
     * userId : 6
     * orderNo : 01183393174
     * amount : 100
     * realAmount : 90.2
     * fee : 9.8
     * createTime : 2017-03-02 15:58:33
     * timeLimit : 7
     * state : 已拒绝
     * cardId : 3
     * serviceFee : 7.35
     * infoAuthFee : 1.96
     * interest : 0.49
     * client : 10
     */
    private int    id;
    private int    userId;
    private String orderNo;
    private String amount;
    private double realAmount;
    private double fee;
    private String createTime;
    private String timeLimit;
    private String state;
    private String stateStr;
    private int    cardId;
    private double serviceFee;
    private double infoAuthFee;
    private double interest;
    private String client;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(double realAmount) {
        this.realAmount = realAmount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public double getInfoAuthFee() {
        return infoAuthFee;
    }

    public void setInfoAuthFee(double infoAuthFee) {
        this.infoAuthFee = infoAuthFee;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }
}
