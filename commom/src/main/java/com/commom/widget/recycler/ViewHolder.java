package com.commom.widget.recycler;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.commom.utils.EmptyUtils;
import com.commom.utils.ImageLoaderUtils.GlideUtils;
import com.commom.utils.StringUtils;


/**
 * Created by super南仔 on 07/28/16.
 * blog: http://supercwn.github.io/
 * GitHub: https://github.com/supercwn
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;
    private       Context           mContext;
    private       View              parentView;

    public ViewHolder(View itemView, Context context) {
        super(itemView);
        parentView = itemView;
        mViews = new SparseArray<>();
        mContext = context;
    }

    public View getParentView() {
        return parentView;
    }

    public <TView extends View> TView getView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (TView) view;
    }

    public ViewHolder setBtnText(int viewId, String value) {
        Button view = getView(viewId);
        view.setText(StringUtils.nullToStr(value));
        return this;
    }

    public ViewHolder setText(int viewId, String value) {
        TextView view = getView(viewId);
        view.setText(StringUtils.nullToStr(value));
        return this;
    }

    public ViewHolder setText(int viewId, CharSequence value, int color) {
        TextView view = getView(viewId);
        view.setText(value);
        view.setTextColor(color);
        return this;
    }

    public ViewHolder setImageURI(int viewId, Uri uri) {
        ImageView view = getView(viewId);
        view.setImageURI(uri);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    public ViewHolder setImageResource(int viewId, int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImageStr(int viewId, String imageStr) {
        ImageView view = getView(viewId);
//        if (EmptyUtils.isEmpty(imageStr)) {
//            ImageLoaderUtils.displayRound(view, R.mipmap.loadingview_empty);
//        } else {
//            ImageLoaderUtils.display(view, imageStr);
//        }
        GlideUtils.disPlayHolderImage(mContext, imageStr, view);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundResource(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder setVisibility(int viewId, int visiblility) {
        View view = getView(viewId);
        view.setVisibility(visiblility);
        return this;
    }

    public ViewHolder setGone(int viewId, boolean gone) {
        View view = getView(viewId);
        view.setVisibility(gone ? View.GONE : View.VISIBLE);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public ViewHolder setOnClickListener(int viewId, CommonAdapter.OnItemChildClickListener listener) {
        View view = getView(viewId);
        listener.mViewHolder = this;
        if (EmptyUtils.isNotEmpty(view)) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * Sets the on longClick listener of the view.
     *
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnLongClickListener(int viewId, CommonAdapter.OnItemChildLongClickListener listener) {
        View view = getView(viewId);
        listener.mViewHolder = this;
        view.setOnLongClickListener(listener);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setClickable(int viewId, boolean clickable) {
        View view = getView(viewId);
        view.setClickable(clickable);
        return this;
    }

    public ViewHolder setSelect(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }
}
