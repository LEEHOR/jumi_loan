package com.cashloan.jumidai.ui.homeLoan.bean;

import java.util.List;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/18 15:23
 * <p/>
 * Description:
 */
public class HomeRec {
    private String                borrowId;
    private String                cardName;
    private String                cardNo;
    private String                fee;
    private boolean               isBorrow;
    private String                maxCredit;
    private String                maxDays;
    private String                minCredit;
    private String                minDays;
    private String                title;
    /** 借款次数 */
    private String                count;
    /** 信用额度 */
    private String                total;
    /** 费率列表 */
    private List<String>          interests;
    /** 资金列表 */
    private List<String>          creditList;
    /** 日期列表 */
    private List<String>          dayList;
    /** 银行卡ID */
    private String                cardId;
    /** 认证状态 */
    private HomeAuthRec           auth;
    /** 借款进度 */
    private List<LoanProgressRec> list;
    /** 是否可还款 */
    private boolean               isRepay;
    /** 是否设置交易密码 */
    private boolean               isPwd;
    /** 最新一笔借款信息 */
    private LoanRec               borrow;

    //  update  2018-8-8 19:21:05  start
    private String  failDate;
    private boolean showPopup;

    public String getFailDate() {
        return failDate;
    }

    public void setFailDate(String failDate) {
        this.failDate = failDate;
    }

    public boolean isShowPopup() {
        return showPopup;
    }

    public void setShowPopup(boolean showPopup) {
        this.showPopup = showPopup;
    }

    //  update  2018-8-8 19:21:05  end

    public boolean isPwd() {
        return isPwd;
    }

    public String getCount() {
        return count;
    }

    public List<String> getInterests() {
        return interests;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getFee() {
        return fee;
    }

    public boolean isBorrow() {
        return isBorrow;
    }

    public String getMaxCredit() {
        return maxCredit;
    }

    public String getMaxDays() {
        return maxDays;
    }

    public String getMinCredit() {
        return minCredit;
    }

    public String getMinDays() {
        return minDays;
    }

    public String getTitle() {
        return title;
    }

    public String getTotal() {
        return total;
    }

    public List<String> getCreditList() {
        return creditList;
    }

    public List<String> getDayList() {
        return dayList;
    }

    public String getCardId() {
        return cardId;
    }

    public HomeAuthRec getAuth() {
        return auth;
    }

    public List<LoanProgressRec> getList() {
        return list;
    }

    public LoanRec getBorrow() {
        return borrow;
    }

    public boolean isRepay() {
        return isRepay;
    }


    /**
     * update
     */
    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setBorrow(boolean borrow) {
        isBorrow = borrow;
    }

    public void setMaxCredit(String maxCredit) {
        this.maxCredit = maxCredit;
    }

    public void setMaxDays(String maxDays) {
        this.maxDays = maxDays;
    }

    public void setMinCredit(String minCredit) {
        this.minCredit = minCredit;
    }

    public void setMinDays(String minDays) {
        this.minDays = minDays;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public void setCreditList(List<String> creditList) {
        this.creditList = creditList;
    }

    public void setDayList(List<String> dayList) {
        this.dayList = dayList;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setAuth(HomeAuthRec auth) {
        this.auth = auth;
    }

    public void setList(List<LoanProgressRec> list) {
        this.list = list;
    }

    public void setRepay(boolean repay) {
        isRepay = repay;
    }

    public void setPwd(boolean pwd) {
        isPwd = pwd;
    }

    public void setBorrow(LoanRec borrow) {
        this.borrow = borrow;
    }

    @Override
    public String toString() {
        return "HomeRec{" +
                "borrowId='" + borrowId + '\'' +
                ", cardName='" + cardName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", fee='" + fee + '\'' +
                ", isBorrow=" + isBorrow +
                ", maxCredit='" + maxCredit + '\'' +
                ", maxDays='" + maxDays + '\'' +
                ", minCredit='" + minCredit + '\'' +
                ", minDays='" + minDays + '\'' +
                ", title='" + title + '\'' +
                ", count='" + count + '\'' +
                ", total='" + total + '\'' +
                ", interests=" + interests +
                ", creditList=" + creditList +
                ", dayList=" + dayList +
                ", cardId='" + cardId + '\'' +
                ", auth=" + auth +
                ", list=" + list +
                ", isRepay=" + isRepay +
                ", isPwd=" + isPwd +
                ", borrow=" + borrow +
                ", failDate='" + failDate + '\'' +
                ", showPopup=" + showPopup +
                '}';
    }


    // 头部 申请额度  creditList 数组

    // 借款期限(天)   到账金额  服务费用  在borrow/choice
    // createTime    状态3  申请时间
    // repayTime     状态3  预计还款日期
    // remainderDay  状态3  剩余还款天数


    private double penaltyAmout;//   penaltyAmout + amount==状态3-待还款总金额
    private double amount;//
    private String remainderDay;
    private String createTime;
    private String repayTime;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPenaltyAmout() {
        return penaltyAmout;
    }

    public void setPenaltyAmout(double penaltyAmout) {
        this.penaltyAmout = penaltyAmout;
    }

    public String getRemainderDay() {
        return remainderDay;
    }

    public void setRemainderDay(String remainderDay) {
        this.remainderDay = remainderDay;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }
}
