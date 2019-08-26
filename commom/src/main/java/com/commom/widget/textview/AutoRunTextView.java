package com.commom.widget.textview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 作者： zxb
 * 时间： 2018/4/9
 * 描述： 设置焦点获取返回为True实现Marqueen滚动
 */

public class AutoRunTextView extends AppCompatTextView {

    public AutoRunTextView(Context context) {
        super(context);
    }

    public AutoRunTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoRunTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
