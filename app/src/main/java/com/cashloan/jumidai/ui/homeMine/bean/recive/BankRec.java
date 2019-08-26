package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Created by mingchen on 17/3/16.
 */
public class BankRec {
    private String authSignData;
    private String authSignPayData;

    public String getAuthSignData() {
        return authSignData;
    }

    public String getAuthSignPayData() {
        return authSignPayData;
    }

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
