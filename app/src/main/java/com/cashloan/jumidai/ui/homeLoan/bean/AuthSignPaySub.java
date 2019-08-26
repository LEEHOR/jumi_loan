package com.cashloan.jumidai.ui.homeLoan.bean;

/**
 * Created by Cliff
 * 注释:SDK 签约授权支付响应回调请求参数
 * noOrder 商户唯一订单号
 *@param noOrder 商户唯一订单号
 * @param userId    用户userId
 * @param retCode   交易结果代码
 * @param retMsg    交易结果描述
 * @param resultPay 支付结果
 *                  Created by 2017/7/18 0018.
 */

public class AuthSignPaySub {
    private String borrowId;
    private String moneyOrder;
    private String noOrder;
    private String userId;
    private String retCode;
    private String retMsg;
    private String resultPay;

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getMoneyOrder() {
        return moneyOrder;
    }

    public void setMoneyOrder(String moneyOrder) {
        this.moneyOrder = moneyOrder;
    }

    public String getNoOrder() {
        return noOrder;
    }

    public void setNoOrder(String noOrder) {
        this.noOrder = noOrder;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getResultPay() {
        return resultPay;
    }

    public void setResultPay(String resultPay) {
        this.resultPay = resultPay;
    }

    @Override
    public String toString() {
        return "签约授权支付响应回调请求参数:{" +
                "borrowId='" + borrowId + '\'' +
                ", moneyOrder='" + moneyOrder + '\'' +
                ", noOrder='" + noOrder + '\'' +
                ", userId='" + userId + '\'' +
                ", retCode='" + retCode + '\'' +
                ", retMsg='" + retMsg + '\'' +
                ", resultPay='" + resultPay + '\'' +
                '}';
    }
}
