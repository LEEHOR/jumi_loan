package com.cashloan.jumidai.ui.homeMine.bean;

import com.cashloan.jumidai.common.Constant;

/**
 * 作者： Ruby
 * 时间： 2018/8/7
 * 描述： 认证中心
 */

public class VerifyItemVM {

    private int     imageRes;
    private String  title;
    private String  status;
    private String  verifyText;
    private boolean isVerifyed;

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        isVerifyed();
        setVerifyText(status);
    }

    public String getVerifyText() {
        return verifyText;
    }

    public void setVerifyText(String status) {
        if (status.equals(Constant.STATUS_30) || status.equals(Constant.STATUS_20)) {
            this.verifyText = "已完成";
        } else {
            this.verifyText = "未完善";
        }
    }

    public void setVerifyed(boolean verifyed) {
        isVerifyed = verifyed;
    }

    public boolean isVerifyed() {
        if (status.equals(Constant.STATUS_30) || status.equals(Constant.STATUS_20)) {
            isVerifyed = true;
        } else {
            isVerifyed = false;
        }
        return isVerifyed;
    }


}
