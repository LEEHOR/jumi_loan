package com.cashloan.jumidai.ui.homeLoan.bean;

/**
 * Created by Cliff
 * 注释:主动还款参数实体类
 * Created by 2017/7/15 0015.
 */

public class AuthRepaySub {
    private String userId;
    private String borrowId;
    private String realRepayAmount;

    /***update 2018-8-8 ***/
    private String state;  //10--还款  20--展期

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getRealRepayAmount() {
        return realRepayAmount;
    }

    public void setRealRepayAmount(String realRepayAmount) {
        this.realRepayAmount = realRepayAmount;
    }

    @Override
    public String toString() {
        return "主动还款参数实体类{" +
                "userId='" + userId + '\'' +
                ", borrowId='" + borrowId + '\'' +
                '}';
    }
}
