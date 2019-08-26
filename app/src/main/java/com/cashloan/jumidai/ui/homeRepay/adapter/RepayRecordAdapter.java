package com.cashloan.jumidai.ui.homeRepay.adapter;

import android.content.Context;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.ui.homeRepay.bean.RepayRecordItemRec;
import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.ViewHolder;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述：
 */

public class RepayRecordAdapter extends CommonAdapter<RepayRecordItemRec> {

    private Context context;

    public RepayRecordAdapter(Context context, List<RepayRecordItemRec> list) {
        super(context, list);
        this.context = context;
    }


    @Override
    protected void convert(ViewHolder holder, RepayRecordItemRec item, int position) {
//        // 30-待还款   50-已逾期
//        if ("30".equals(item.getState())) {
//            holder.setText(R.id.tv_record_status, "待还款");
//            holder.setImageResource(R.id.iv_status_bg, R.drawable.icon_to_repay);
//        } else if ("50".equals(item.getState())) {
//            holder.setText(R.id.tv_record_status, "已逾期");
//            holder.setImageResource(R.id.iv_status_bg, R.drawable.icon_repay_over);
//        }
        holder.setText(R.id.tv_record_dayLeft, item.getMsg());//剩余天数
        holder.setText(R.id.tv_record_loanMoneyShow, item.getAmount()+"元");//借款金额--本金
        holder.setText(R.id.tv_record_serviceFeeShow, item.getFee()+"元");//服务费用

        double shouldRepay = Double.parseDouble(item.getAmount()) + Double.parseDouble(item.getPenaltyAmout());

        holder.setText(R.id.tv_record_toRepayShow, shouldRepay + "元");//应还金额
        holder.setText(R.id.tv_record_overMoneyShow, item.getPenaltyAmout()+"元");//逾期费用
//        holder.setText(R.id.tv_record_loanTimeShow, item.getCreateTime().substring(0, item.getCreateTime().indexOf(" ")));//借款时间
        holder.setText(R.id.tv_record_repayTimeShow, item.getRepayTime().substring(0, item.getRepayTime().indexOf(" ")));//还款时间

    }

    @Override
    protected int getItemViewLayoutId(int position, RepayRecordItemRec item) {
        return R.layout.item_repay_record;
    }

}
