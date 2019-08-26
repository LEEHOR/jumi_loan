package com.cashloan.jumidai.ui.homeMine.adapter;

import android.content.Context;
import android.view.View;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.ui.homeMine.bean.VerifyItemVM;
import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.ViewHolder;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述： 认证中心
 */

public class VerifyCenterAdapter extends CommonAdapter<VerifyItemVM> {

    private Context              context;
    private View.OnClickListener mClickListener;

    public VerifyCenterAdapter(Context context, List<VerifyItemVM> list, View.OnClickListener clickListener) {
        super(context, list);
        this.context = context;
        this.mClickListener = clickListener;
    }


    @Override
    protected void convert(ViewHolder holder, VerifyItemVM item, int position) {
        holder.setImageResource(R.id.iv_item_verify, item.getImageRes());//
        holder.setText(R.id.tv_item_verify, item.getTitle());

        holder.setText(R.id.tv_item_verifyStatus, item.getVerifyText());
        holder.setSelect(R.id.tv_item_verifyStatus, item.isVerifyed());

        holder.setTag(R.id.ll_item_verify, position);
        holder.setOnClickListener(R.id.ll_item_verify, mClickListener);
    }

    @Override
    protected int getItemViewLayoutId(int position, VerifyItemVM item) {
        return R.layout.item_rc_verify;
    }

}
