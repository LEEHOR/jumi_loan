package com.cashloan.jumidai.ui.homeRepay.bean;

import android.content.res.Resources;

import com.commom.utils.ContextHolder;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;


/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/20 15:53
 * <p/>
 * Description:
 */
public class RepayRecordItemVM {
    /**
     * amount : 1000
     * borrowId : 1
     * id : 1
     * penaltyAmout : 0
     * repayId : 1
     * repayTime : 2017-02-18 13:30:54
     */
    private String  amount;
    private String  fee;
    private boolean isPunish;
    private String  orderNo;
    private String  penalty;
    private String  realAmount;
    private String  repayTime;
    private String  msg;
    private String  repayTimeStr;
    private String  id;
    private String  borrowId;
    private String  penaltyAmout;
    private String  state;
    /** 状态颜色 */
    private int statusColor = ContextHolder.getContext().getResources().getColor(R.color.app_color_secondary);

    public String getPenaltyAmout() {
        return penaltyAmout;
    }

    public void setPenaltyAmout(String penaltyAmout) {
        this.penaltyAmout = penaltyAmout;
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

    public String getRepayTimeStr() {
        return repayTimeStr;
    }

    public void setRepayTimeStr(String repayTimeStr) {
        this.repayTimeStr = repayTimeStr;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public boolean isPunish() {
        return isPunish;
    }

    public void setPunish(boolean punish) {
        isPunish = punish;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
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
//        getColor();
    }

    public int getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }

    private void getColor() {
        Resources resources = ContextHolder.getContext().getResources();
        switch (state) {
            case Constant.STATUS_10://申请成功待审核(审核中)
                setStatusColor(resources.getColor(R.color.app_color_principal));
                break;
            case Constant.STATUS_20://自动审核通过
                break;
            case Constant.STATUS_21://自动审核不通过（审核不通过）
                setStatusColor(resources.getColor(R.color.red_btn));
                break;
            case Constant.STATUS_22://自动审核未决待人工复审(审核中)
                setStatusColor(resources.getColor(R.color.app_color_principal));
                break;
            case Constant.STATUS_26://人工复审通过
                setStatusColor(resources.getColor(R.color.app_color_principal));
                break;
            case Constant.STATUS_27://人工复审不通过(审核不通过)
                setStatusColor(resources.getColor(R.color.red_btn));
                break;
            case Constant.STATUS_30://放款成功
                setStatusColor(resources.getColor(R.color.home_tag_color));
                break;
            case Constant.STATUS_40://还款成功（已还款）
                setStatusColor(resources.getColor(R.color.text_grey));
                break;
            case Constant.STATUS_50://逾期
                setStatusColor(resources.getColor(R.color.home_tag_color));
                break;
            default:
                setStatusColor(resources.getColor(R.color.app_color_principal));
        }
    }
}
