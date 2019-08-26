package com.cashloan.jumidai.common;

import android.view.View;

/**
 * 作者： Ruby
 * 时间： 2018/8/20
 * 描述： Click事件控件接收类
 */
public abstract class ViewClick implements View.OnClickListener{
    /** adapter中的position */
    private int    position;
    private Object object;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
