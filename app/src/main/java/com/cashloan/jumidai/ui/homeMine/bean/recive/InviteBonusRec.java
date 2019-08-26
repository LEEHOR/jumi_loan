package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/24 10:51
 * <p>
 * Description:我的奖金信息
 */
public class InviteBonusRec {
    private String cashed;//历史提现奖金	number	    	是	@mock=500
    private String noCashed;//当前可提现奖金	number	    	是	@mock=500
    /**
     * id : 13
     * userId : 13
     * total : 0
     * state : 10
     */
    private String state;

    public String getCashed() {
        return cashed;
    }

    public String getNoCashed() {
        return noCashed;
    }

    public String getState() {
        return state;
    }
}
