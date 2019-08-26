package com.cashloan.jumidai.ui.homeMine.adapter;

import android.content.Context;
import android.view.View;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.ui.homeMine.bean.CreditWorkPhotoVM;
import com.commom.utils.EmptyUtils;
import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.ViewHolder;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述： 借款记录
 */

public class WorkImagesAdapter extends CommonAdapter<CreditWorkPhotoVM> {

    private Context                 mContext;
    private List<CreditWorkPhotoVM> mList;
    private View.OnClickListener    mOnceClickListener;

    public WorkImagesAdapter(Context context, List<CreditWorkPhotoVM> data, View.OnClickListener onClickListener) {
        super(context, data);
        this.mOnceClickListener = onClickListener;
    }


    @Override
    protected void convert(ViewHolder holder, CreditWorkPhotoVM item, int position) {
        if (EmptyUtils.isNotEmpty(item)) {
            if (EmptyUtils.isNotEmpty(item.getUrl())) {
                //Glide加载需要清空tag，否则报错
                holder.setTag(R.id.iv_pic, null);
                holder.setImageStr(R.id.iv_pic, item.getUrl());
                holder.setTag(R.id.iv_pic, position);
            }

            if (EmptyUtils.isNotEmpty(item.getIsUpload())) {
                holder.setVisibility(R.id.iv_delete, item.getIsUpload());
            } else {
                holder.setVisibility(R.id.iv_delete, View.GONE);
            }

            if (EmptyUtils.isNotEmpty(item.getUploadEnable())) {
                holder.setVisibility(R.id.iv_upload, item.getUploadEnable());
            } else {
                holder.setVisibility(R.id.iv_upload, View.GONE);
            }
        }

        holder.setTag(R.id.iv_delete, position);
        holder.setTag(R.id.iv_upload, position);

        holder.setOnClickListener(R.id.iv_pic, mOnceClickListener);
        holder.setOnClickListener(R.id.iv_delete, mOnceClickListener);
        holder.setOnClickListener(R.id.iv_upload, mOnceClickListener);
    }

    @Override
    protected int getItemViewLayoutId(int position, CreditWorkPhotoVM item) {
        return R.layout.item_work_images;
    }

}
