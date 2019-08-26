package com.cashloan.jumidai.ui.homeMine.bean;

import android.view.View;
import com.commom.base.BaseBean;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/3/16 16:28
 * <p/>
 * Description:
 */
public class CreditWorkPhotoVM extends BaseBean {
    private String url;
    private int uploadEnable = View.VISIBLE;    //是否可以新增
    private int isUpload     = View.GONE;       //是否可以删除
    private int isComplete   = View.GONE;       //是否完成上传

     
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

     
    public int getUploadEnable() {
        return uploadEnable;
    }

    public void setUploadEnable(int uploadEnable) {
        this.uploadEnable = uploadEnable;
    }

     
    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }
     
    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }
}
