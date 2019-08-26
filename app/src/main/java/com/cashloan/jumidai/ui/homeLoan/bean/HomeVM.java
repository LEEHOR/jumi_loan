package com.cashloan.jumidai.ui.homeLoan.bean;

import com.commom.base.BaseBean;
import com.commom.utils.TextUtil;

import java.util.List;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/18 13:40
 * <p/>
 * Description:
 */
public class HomeVM extends BaseBean {
    /**
     * authCount : 测试内容ob8r
     * borrowId : 测试内容65p1
     * cardName : 工商银行
     * cardNo : 6212261212121212121
     * count : 2
     * creditList : ["string1","string2","string3","string4","string5"]
     * dayList : ["string1","string2","string3","string4","string5"]
     * fee : 0.15
     * isBorrow : true
     * maxCredit : 0
     * maxDays : 测试内容f4i2
     * minCredit : 0
     * minDays : 测试内容5lhf
     * title : 测试内容g942
     * total : 测试内容3o6v
     */
    private String authCount = "0";
    private String authTotal = "0";
    private String borrowId;
    private String cardName;
    private String cardNo;
    private String count = "0";
    private String       fee;
    private boolean      isBorrow;
    private String       maxCredit;
    private String       maxDays;
    private String       minCredit;
    private String       minDays;
    private String       title;
    private String       total;
    private List<String> creditList;
    private List<String> dayList;
    private List<String> interests;
    private boolean      isPwd;

    public boolean isPwd() {
        return isPwd;
    }

    public void setPwd(boolean pwd) {
        isPwd = pwd;
    }

    public boolean isBorrow() {
        return isBorrow;
    }

    public void setBorrow(boolean borrow) {
        isBorrow = borrow;
    }

    private String cardId;

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getAuthCount() {
        return authCount;
    }

    public void setAuthCount(String authCount) {
        this.authCount = authCount;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getMaxCredit() {
        return maxCredit;
    }

    public void setMaxCredit(String maxCredit) {
        this.maxCredit = maxCredit;
    }


    public String getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(String maxDays) {
        this.maxDays = maxDays;
    }


    public String getMinCredit() {
        return minCredit;
    }

    public void setMinCredit(String minCredit) {
        this.minCredit = minCredit;
    }


    public String getMinDays() {
        return minDays;
    }

    public void setMinDays(String minDays) {
        this.minDays = minDays;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<String> getCreditList() {
        return creditList;
    }

    public void setCreditList(List<String> creditList) {
        this.creditList = creditList;
    }

    public List<String> getDayList() {
        return dayList;
    }

    public void setDayList(List<String> dayList) {
        this.dayList = dayList;
    }


    public String getAuthTotal() {
        return authTotal;
    }

    public void setAuthTotal(String authTotal) {
        this.authTotal = authTotal;

    }

    public boolean getAuth() {
        return !TextUtil.isEmpty(authCount) && !TextUtil.isEmpty(authTotal) && !authCount.equals(authTotal);
    }
}
