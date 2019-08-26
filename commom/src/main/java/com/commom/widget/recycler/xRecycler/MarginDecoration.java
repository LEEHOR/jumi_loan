package com.commom.widget.recycler.xRecycler;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.commom.R;


public class MarginDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL; //水平方向
    private static final int VERTICAL   = LinearLayoutManager.VERTICAL;     //垂直方向

    private int margin;
    private int itemCount = 2;
    private int headVsize = 1;
    private float   itemMargin;
    private boolean showEdge;
    private boolean isShowTop;
    private int     orientation;              //方向

    // 供列表使用
    public MarginDecoration(int orientation, int margin) {
        this.orientation = orientation;
        if (margin > 0) {
            this.margin = margin;
        }
        this.margin = margin > 0 ? margin : 15;
    }

    /**
     * @param orientation 列表方向
     * @param margin      分割线尺寸
     * @param isShowTop   第一条是否添加分割线
     */
    public MarginDecoration(int orientation, int margin, boolean isShowTop) {
        this.orientation = orientation;
        this.isShowTop = isShowTop;
        if (margin > 0) {
            this.margin = margin;
        }
        this.margin = margin > 0 ? margin : (int) context.getResources().getDimension(R.dimen.size_0_5);
    }

    public MarginDecoration(Context context, int itemCount) {
        this(context, 0, itemCount, false);
    }

    /**
     * @param margin    item之间的空间
     * @param itemCount 列数
     * @param showEdge  是否显示左右边缘
     */
    public MarginDecoration(Context context, int margin, int itemCount, boolean showEdge) {
        this.context = context;
        if (margin > 0) {
            this.margin = margin;
        }
        this.margin = margin > 0 ? margin : (int) context.getResources().getDimension(R.dimen.size_1);
        if (itemCount > this.itemCount) {
            this.itemCount = itemCount;
        }
        this.showEdge = showEdge;
        itemMargin = margin * 1.0f / itemCount;
    }

    /**
     * @param itemCount 列数
     */
    public MarginDecoration(Context context, int itemCount, int headVsize) {
        this.context = context;
        this.margin = margin > 0 ? margin : (int) context.getResources().getDimension(R.dimen.size_10);
        if (itemCount > this.itemCount) {
            this.itemCount = itemCount;
        }
        this.showEdge = true;
        this.headVsize += headVsize;
        itemMargin = margin * 1.0f / itemCount;
    }

    private int lastPosition;
    private int currentPosition;
    private int current;
    private int totalItem;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        //整个RecyclerView最后一个item的position
        lastPosition = state.getItemCount() - 1;
        //获取当前要进行布局的item的position
        current = parent.getChildLayoutPosition(view);

        if (current == -1) return;//holder出现异常时，可能为-1

        outRect.left = margin;
        outRect.right = margin;
        outRect.top = 0;
        outRect.bottom = margin;

        if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
            if (current < ((StaggeredGridLayoutManager) parent.getLayoutManager()).getSpanCount()) {
                // 利用item的margin配合RecyclerView的margin值使得间隔相等，这里只需设第一行item的相对顶部的高度
                outRect.top = margin;
            }
        } else if (parent.getLayoutManager() instanceof GridLayoutManager) {

            setOutRect(outRect);

        } else if (layoutManager instanceof LinearLayoutManager) { // LinearLayoutManager
            if (orientation == LinearLayoutManager.VERTICAL) {
                // 垂直
                if ((current < headVsize && !isShowTop) || current == lastPosition) {    // 判断为第一项或最后一项
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(0, 0, 0, margin);
                }
            } else {
                // 水平
                if (current == lastPosition || current == 0) {    // 判断为第一项或最后一项
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(0, 0, margin, 0);
                }
            }
        }
    }

    // 设置item间隔
    // 头部有两个viewgroup
    // 整体两边间隔是中间间隔的两倍
    private void setOutRect(Rect outRect) {

        totalItem = lastPosition - headVsize;
        outRect.top = margin;
        outRect.left = margin / 2;
        outRect.right = margin / 2;
        outRect.bottom = 0;

        // 头部不间隔
        if (current < headVsize) {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        } else {      // 除去头部剩下的item
            currentPosition = current - headVsize;
            if (currentPosition % itemCount == 0) {      // 最左边的item
                outRect.left = (margin * 3 / 2);
            } else if (currentPosition % itemCount == itemCount - 1) {
                outRect.right = (margin * 3 / 2);
            }
        }
    }
}