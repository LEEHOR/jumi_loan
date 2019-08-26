package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/24 18:20
 * <p>
 * Description: 我的邀请码
 */
public class MineInviteRec {
    /**
     * isPhone : false
     * phone : 13312345678
     */
    private boolean isPhone;
    private String  phone;

    public boolean isIsPhone() {
        return isPhone;
    }

    public void setIsPhone(boolean isPhone) {
        this.isPhone = isPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
