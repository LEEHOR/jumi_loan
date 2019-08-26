package com.cashloan.jumidai.ui.homeMine.bean.recive;

import android.view.View;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/25 17:40
 * <p/>
 * Description:邀请记录返回
 */
public class InviteRecordItemRec {
    /**
     * inviteId : 测试内容2v22
     * inviteName : 测试内容r91c
     * inviteTime : 测试内容2i7t
     * level : 测试内容151a
     */
    private String inviteId;
    private String inviteName;
    private String inviteTime;
    private String level;
    private int visibility = View.VISIBLE;

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }

    public String getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(String inviteTime) {
        this.inviteTime = inviteTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
