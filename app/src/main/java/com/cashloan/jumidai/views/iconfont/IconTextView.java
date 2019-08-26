package com.cashloan.jumidai.views.iconfont;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.cashloan.jumidai.MyApplication;


/**
 * Created by chenming
 * Created Date 17/4/19 22:29
 * mail:cm1@erongdu.com
 * Describe:
 */
public class IconTextView extends AppCompatTextView {
    public IconTextView(Context context) {
        this(context, null);
    }

    public IconTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //设置字体图标
    private void init() {
        this.setTypeface(MyApplication.getInstance().getIconTypeFace());
    }
}
