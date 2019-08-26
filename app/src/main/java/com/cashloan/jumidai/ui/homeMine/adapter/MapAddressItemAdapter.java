package com.cashloan.jumidai.ui.homeMine.adapter;

import android.content.Context;

import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.ViewHolder;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.ui.homeMine.bean.PioSearchItemVM;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述： 高德地图定位地址
 */

public class MapAddressItemAdapter extends CommonAdapter<PioSearchItemVM> {

    private Context context;

    public MapAddressItemAdapter(Context context, List<PioSearchItemVM> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, PioSearchItemVM item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());//
        holder.setText(R.id.tv_address, item.getSnippet());//

    }

    @Override
    protected int getItemViewLayoutId(int position, PioSearchItemVM item) {
        return R.layout.item_poi_layout;
    }

}
