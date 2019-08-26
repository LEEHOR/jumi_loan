package com.cashloan.jumidai.ui.homeMine.bean;

/**
 * 作者： Ruby
 * 时间： 2018/8/7
 * 描述：
 */

public class MessageVM {

    private String time;
    private String content;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageVM(String time, String content) {
        this.time = time;
        this.content = content;
    }
}
