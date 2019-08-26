package com.cashloan.jumidai.ui.homeMine.bean;

import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import com.commom.base.BaseBean;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2017/2/16 下午7:55
 * <p/>
 * Description: 我的首页item
 */
public class MineItemVM extends BaseBean {
    /** 标题 */
    private int      title;
    /** 图标 */
    private Drawable icon;
    /** 图标文字 */
    private int      iconFont;
    /** 提示语 */
    private String   tips;
    /** 是否pading */
    private boolean  isPading;
    /** 是否分割线 */
    private boolean  isdivide;

    public MineItemVM(int title, Drawable icon, String tips, boolean isPading, boolean isdivide) {
        this.title = title;
        this.icon = icon;
        this.tips = tips;
        this.isPading = isPading;
        this.isdivide = isdivide;
    }

    public MineItemVM(int title, Drawable icon) {
        this.title = title;
        this.icon = icon;
    }

    public MineItemVM(int title, Drawable icon, boolean isPading) {
        this.title = title;
        this.icon = icon;
        this.isPading = isPading;
    }

    public MineItemVM(int title, @StringRes int icon) {
        this.title = title;
        this.iconFont = icon;
    }

    public MineItemVM(int title, @StringRes int icon, boolean isPading) {
        this.title = title;
        this.iconFont = icon;
        this.isPading = isPading;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public boolean isPading() {
        return isPading;
    }

    public void setPading(boolean pading) {
        isPading = pading;
    }

    public boolean isdivide() {
        return isdivide;
    }

    public void setIsdivide(boolean isdivide) {
        this.isdivide = isdivide;
    }

    public int getIconFont() {
        return iconFont;
    }

    public void setIconFont(int iconFont) {
        this.iconFont = iconFont;
    }
}
