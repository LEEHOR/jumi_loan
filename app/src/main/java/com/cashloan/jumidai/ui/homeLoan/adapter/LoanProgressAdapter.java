package com.cashloan.jumidai.ui.homeLoan.adapter;

import android.content.Context;

import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.ViewHolder;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.ui.homeLoan.bean.LoanProgressVM;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述：
 */

public class LoanProgressAdapter extends CommonAdapter<LoanProgressVM> {

    private Context mContext;

    public LoanProgressAdapter(Context context, List<LoanProgressVM> list) {
        super(context, list);
        this.mContext = context;
    }


    @Override
    protected void convert(ViewHolder holder, LoanProgressVM item, int position) {

        holder.setImageDrawable(R.id.iv_progress, item.getStateIcon());

        //  progress_divide_line-->app_color_principal
        int color1 = item.isGrey_1() ? mContext.getResources().getColor(R.color.app_color_principal)
                : mContext.getResources().getColor(R.color.app_color_principal);
        int color2 = item.isGrey_2() ? mContext.getResources().getColor(R.color.app_color_principal)
                : mContext.getResources().getColor(R.color.app_color_principal);

        holder.setBackgroundColor(R.id.v_line1, color1);
        holder.setBackgroundColor(R.id.v_line2, color2);

        holder.setVisible(R.id.v_line1, !item.isEnd());//不为最后一条设置可见
        holder.setVisible(R.id.v_line2, !item.isEnd());

        holder.setText(R.id.tv_item_loan_status, item.getState().toString());
        holder.setText(R.id.tv_item_loan_time, item.getLoanTime().toString());
        holder.setText(R.id.tv_item_loan_remark, item.getRemark());
    }

    @Override
    protected int getItemViewLayoutId(int position, LoanProgressVM item) {
        return R.layout.item_loan_progress;
    }


}
