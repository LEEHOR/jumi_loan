package com.cashloan.jumidai.ui.homeMine.adapter;

import android.content.Context;
import android.view.View;

import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.ViewHolder;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.ui.homeMine.bean.LendRecordItemVM;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述： 借款记录
 */

public class LoanRecordAdapter extends CommonAdapter<LendRecordItemVM> {

    private Context              context;
    private View.OnClickListener mClickListener;

    public LoanRecordAdapter(Context context, List<LendRecordItemVM> list, View.OnClickListener clickListener) {
        super(context, list);
        this.context = context;
        this.mClickListener = clickListener;
    }


    @Override
    protected void convert(ViewHolder holder, LendRecordItemVM item, int position) {
        holder.setText(R.id.tv_transaction_title, item.getMoney());//
        holder.setText(R.id.tv_transaction_time, item.getTime());//
        holder.setText(R.id.tv_transaction_status, item.getStatuStr());//
        holder.setTextColor(R.id.tv_transaction_status, item.getStatusColor());

        holder.setTag(R.id.rl_item_record, position);
        holder.setOnClickListener(R.id.rl_item_record, mClickListener);
    }

    @Override
    protected int getItemViewLayoutId(int position, LendRecordItemVM item) {
        return R.layout.item_loan_record;
    }

}
