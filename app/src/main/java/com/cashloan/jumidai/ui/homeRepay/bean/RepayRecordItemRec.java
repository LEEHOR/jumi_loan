package com.cashloan.jumidai.ui.homeRepay.bean;

import com.commom.base.BaseBean;

/**
 * 作者： Ruby
 * 时间： 2018/8/31
 * 描述：
 * <p>
 * "id":3,
 * "borrowId":9,
 * "createTime":"2018-08-18 13:04:19", //借款日期
 * "repayTime":"2018-08-24 23:59:59",  //还款日期
 * "repayTimeStr":"08-24", //
 * "amount":1000,//  借款金额
 * "realAmount":750,//
 * "fee":250,//  服务费用
 * "penaltyAmout":400,//  逾期金额
 * "penaltyDay":"5",//
 * "msg":"已逾期7天",//   距还款还有3天 --右上文字
 * "isPunish":"true",//
 * "state":"50"//  判断状态 30-待还款   50-已逾期
 * <p>
 * 应还金额 == amount+ penaltyAmout  (1000+400)
 */
public class RepayRecordItemRec extends BaseBean {
    /**
     * amount : 1000
     * createTime : 2017-02-20 10:47:30
     * fee : 150
     * id : 1
     * isPunish : false
     * msg : 7天后还款
     * orderNo : 1
     * penalty : 0
     * realAmount : 850
     * repayTime : 2017-02-20 10:47:30
     * state : 30
     * timeLimit : 14
     * userId : 1
     */
    private String  amount;
    private String  createTime;
    private String  fee;
    private String  id;
    private boolean isPunish;
    private String  msg;
    private String  orderNo;
    private String  penalty;
    private String  realAmount;
    private String  repayTime;
    private String  state;
    private String  timeLimit;
    private String  userId;
    private String  repayTimeStr;
    private String  borrowId;
    private String  penaltyAmout;

    public String getPenaltyAmout() {
        return penaltyAmout;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public String getRepayTimeStr() {
        return repayTimeStr;
    }

    public String getAmount() {
        return amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getFee() {
        return fee;
    }

    public String getId() {
        return id;
    }

    public boolean isPunish() {
        return isPunish;
    }

    public String getMsg() {
        return msg;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getPenalty() {
        return penalty;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public String getState() {
        return state;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public String getUserId() {
        return userId;
    }


    public RepayRecordItemRec(String state) {
        this.state = state;
    }
}
