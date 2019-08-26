package com.cashloan.jumidai.ui.homeRepay.bean;

import com.commom.base.BaseBean;
import com.cashloan.jumidai.ui.homeLoan.bean.LoanProgressRec;

import java.util.List;

;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/15 15:30
 * <p/>
 * Description: 还款详情
 */
public class RepayDetailsRec extends BaseBean {
    private String realRepayAmount;//用户需要还款的金额
    /**
     * amount : 1000
     * bank : 工商银行
     * cardId : 1
     * cardNo : 6212261212121212121
     * client : 10
     * createTime : 2017-02-20 10:47:30
     * fee : 150
     * id : 1
     * infoAuthFee : 30
     * interest : 7.5
     * orderNo : 1
     * realAmount : 850
     * serviceFee : 112.5
     * state : 30
     * timeLimit : 14
     * userId : 1
     */

    //  update 2018-8-10
    private String extensionAmount;   //续借费用

    public String getExtensionAmount() {
        return extensionAmount;
    }

    public void setExtensionAmount(String extensionAmount) {
        this.extensionAmount = extensionAmount;
    }

    private List<RepayDetailsContentRec> borrow;
    private List<LoanProgressRec>        list;
    /**
     * amount : 1000
     * borrowId : 1
     * id : 1
     * repayTime : 2017-02-27 10:47:30
     * state : 10
     * userId : 1
     */
    private List<RepayBean>              repay;

    public List<LoanProgressRec> getList() {
        return list;
    }

    public void setList(List<LoanProgressRec> list) {
        this.list = list;
    }

    public List<RepayDetailsContentRec> getBorrow() {
        return borrow;
    }

    public void setBorrow(List<RepayDetailsContentRec> borrow) {
        this.borrow = borrow;
    }

    public List<RepayBean> getRepay() {
        return repay;
    }

    public void setRepay(List<RepayBean> repay) {
        this.repay = repay;
    }

    public static class RepayBean {
        private String amount;
        private String borrowId;
        private String id;
        private String repayTime;
        /** 还款状态 10-已还款 20-未还款 */
        private String state;
        private String userId;
        private String repayTimeStr;
        /** 实际还款时间 */
        private String realRepayTime;
        /** 实际还款金额 */
        private String realRepayAmount;

        public String getAmount() {
            return amount;
        }

        public String getRepayTimeStr() {
            return repayTimeStr;
        }

        public void setRepayTimeStr(String repayTimeStr) {
            this.repayTimeStr = repayTimeStr;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBorrowId() {
            return borrowId;
        }

        public void setBorrowId(String borrowId) {
            this.borrowId = borrowId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRepayTime() {
            return repayTime;
        }

        public void setRepayTime(String repayTime) {
            this.repayTime = repayTime;
        }

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

        public String getRealRepayTime() {
            return realRepayTime;
        }

        public void setRealRepayTime(String realRepayTime) {
            this.realRepayTime = realRepayTime;
        }

        public String getRealRepayAmount() {
            return realRepayAmount;
        }

        public void setRealRepayAmount(String realRepayAmount) {
            this.realRepayAmount = realRepayAmount;
        }

    }

    public String getRealRepayAmount() {
        return realRepayAmount;
    }

    public void setRealRepayAmount(String realRepayAmount) {
        this.realRepayAmount = realRepayAmount;
    }
}
