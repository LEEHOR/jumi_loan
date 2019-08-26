package com.cashloan.jumidai.ui.homeMine.bean;

import android.content.res.Resources;
import com.commom.base.BaseBean;
import com.commom.utils.ContextHolder;
import com.commom.utils.DateUtil;
import com.commom.utils.StringFormat;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2017/2/16 下午10:11
 * <p/>
 * Description:
 */
public class LendRecordItemVM extends BaseBean {
    /** 借款金额 */
    private String money;
    /** 借款时间 */
    private String time;
    /** 记录状态 */
    private String status;
    /** 记录信息 */
    private String statuStr;
    private String id;
    /** 状态颜色 */
    private int statusColor = ContextHolder.getContext().getResources().getColor(R.color.app_color_secondary);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

       
    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = ContextHolder.getContext().getString(R.string.lendrecord_item_money, StringFormat.subZeroAndDot(money));
    }

       
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
//        getColor();
    }

       
    public String getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = DateUtil.formatter(DateUtil.Format.MINUTE, time);
    }

    public void setTime(String time) {
        this.time = time;
    }

       
    public String getStatuStr() {
        return statuStr;
    }

    public void setStatuStr(String statuStr) {
        this.statuStr = statuStr;
    }

       
    public int getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }

    private void getColor() {
        Resources resources = ContextHolder.getContext().getResources();
        switch (status) {
            case Constant.STATUS_10://申请成功待审核(审核中)
                setStatusColor(resources.getColor(R.color.app_color_secondary));
                break;
            case Constant.STATUS_20://自动审核通过
                break;
            case Constant.STATUS_21://自动审核不通过（审核不通过）
                setStatusColor(resources.getColor(R.color.red_btn));
                break;
            case Constant.STATUS_22://自动审核未决待人工复审(审核中)
                setStatusColor(resources.getColor(R.color.app_color_secondary));
                break;
            case Constant.STATUS_26://人工复审通过
                setStatusColor(resources.getColor(R.color.app_color_secondary));
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
                setStatusColor(resources.getColor(R.color.app_color_secondary));
        }
    }
}
