package com.cashloan.jumidai.ui.homeMine.bean;

import com.commom.base.BaseBean;
import com.commom.utils.TextUtil;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2017/2/24 下午5:12
 * <p/>
 * Description:
 */
public class SettingsIdeaVM extends BaseBean {
    private final int MAX_LEN = 160;
    private final int MIN_LEN = 0;
    private String idea;
    private String  count  = MIN_LEN + "/" + MAX_LEN;
    private boolean enable = false;

        
    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
        change();
    }

        
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

        
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private void change() {
        setCount(MIN_LEN + idea.length() + "/" + MAX_LEN);
        setEnable(!TextUtil.isEmpty(idea));
    }
}
