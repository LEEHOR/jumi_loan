package com.cashloan.jumidai.ui.homeLoan.bean;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/18 15:45
 * <p/>
 * Description: 还款进度
 */
public class LoanProgressRec {
    private String createTime;//进度生成时间	string	        	是	@mock=2017-02-16 15:08:15
    private String loanTime;//放款时间	string	        	是	@mock=2017-02-16 15:08:15
    private String remark;//进度描述	string	        	是	@mock=还款中
    private String repayTime;//还款时间	string	        	是	@mock=2017-02-23 15:08:14
    private int    type;
    private String state;
    private String userId;
    private String borrowId;

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public int getType() {
        return type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getLoanTime() {
        return loanTime;
    }

    public String getRemark() {
        return remark;
    }

    public String getRepayTime() {
        return repayTime;
    }
}
