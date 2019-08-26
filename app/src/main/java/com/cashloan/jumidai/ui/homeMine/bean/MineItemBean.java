package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;

/**
 * 作者： Ruby
 * 时间： 2018/8/27$
 * 描述：
 */

public class MineItemBean  extends BaseBean{

    private  int itemTag;
    private  int imageRes;
    private  String itemText;

    public int getItemTag() {
        return itemTag;
    }

    public void setItemTag(int itemTag) {
        this.itemTag = itemTag;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
}
