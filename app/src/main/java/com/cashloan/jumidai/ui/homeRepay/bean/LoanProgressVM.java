package com.cashloan.jumidai.ui.homeRepay.bean;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.commom.utils.ContextHolder;
import com.commom.utils.TextUtil;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;


/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/18 18:37
 * <p/>
 * Description:
 */
public class LoanProgressVM {
    private String  loanTime;//放款时间	string	        	是	@mock=2017-02-16 15:08:15
    private String  remark;//进度描述	string	        	是	@mock=还款中
    private String  repayTime;//还款时间	string	        	是	@mock=2017-02-23 15:08:14
    private String  state;
    private boolean isFirst;  //是否列表第一个
    private boolean isEnd;    //是否最后一个
    private int     type;
    private boolean  grey_1    = true;
    private boolean  grey_2    = true;
    private Drawable stateIcon = ContextHolder.getContext().getResources().getDrawable(R.drawable.icon_wait);

    public boolean isGrey_1() {
        return grey_1;
    }

    public void setGrey_1(boolean grey_1) {
        this.grey_1 = grey_1;
    }

    public boolean isGrey_2() {
        return grey_2;
    }

    public void setGrey_2(boolean grey_2) {
        this.grey_2 = grey_2;
    }

    public Spannable getState() {
        return getSpan(isFirst, state);
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public Spannable getLoanTime() {
        if(TextUtil.isEmpty(loanTime)){
            return getSpan(isFirst, "");
        }else {
            return getSpan(isFirst, loanTime);
        }
    }

    public void setLoanTime(String loanTime) {
        this.loanTime = loanTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Drawable getStateIcon() {
        if (isFirst) {
            if(Constant.NUMBER_10 == type){
                stateIcon = ContextHolder.getContext().getResources().getDrawable(R.drawable.icon_success);
            } else if(Constant.NUMBER_20 == type){
                stateIcon = ContextHolder.getContext().getResources().getDrawable(R.drawable.icon_failed);
            } else {
                stateIcon = ContextHolder.getContext().getResources().getDrawable(R.drawable.icon_wait);
            }
        }else {
            stateIcon = ContextHolder.getContext().getResources().getDrawable(R.drawable.icon_wait);
        }
        return stateIcon;
    }

    private Spannable getSpan(boolean isFirst, String str) {
        int color;
        if (!isFirst) {
            color = R.color.text_grey;
        } else {
            if (Constant.NUMBER_10 != type && Constant.NUMBER_20 != type) {
                color = R.color.text_grey;
            } else {
                color = R.color.text_black;
            }
        }
        Spannable span = new SpannableString(str);
        span.setSpan(new ForegroundColorSpan(ContextHolder.getContext().getResources().getColor(color)), 0, str.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
}
