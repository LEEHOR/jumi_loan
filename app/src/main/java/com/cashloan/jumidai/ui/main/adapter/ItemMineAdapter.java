package com.cashloan.jumidai.ui.main.adapter;

import android.content.Context;
import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.ViewHolder;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.ui.homeMine.bean.MineItemBean;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述： 我的item
 */

public class ItemMineAdapter extends CommonAdapter<MineItemBean> {

    private Context context;

    public ItemMineAdapter(Context context, List<MineItemBean> list) {
        super(context, list);
        this.context = context;
    }


    @Override
    protected void convert(ViewHolder holder, MineItemBean item, int position) {
        holder.setImageResource(R.id.iv_item_mine, item.getImageRes());
        holder.setText(R.id.tv_item_mine, item.getItemText());
    }

    @Override
    protected int getItemViewLayoutId(int position, MineItemBean item) {
        return R.layout.item_rc_mine;
    }

}
