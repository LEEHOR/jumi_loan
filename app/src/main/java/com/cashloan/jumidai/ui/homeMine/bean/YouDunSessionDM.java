package com.cashloan.jumidai.ui.homeMine.bean;

/**
 * Created by Admin on 2018/5/31.
 * <p>
 * "id":1,
 * "userId":21,
 * "ocrSessionId":"327078449833508867",
 * "livingSessionId":"327242375850295302",
 * "updateTime":"2018-05-31 20:28:04",
 * "createTime":"2018-05-31 09:52:58"
 */

public class YouDunSessionDM {

    private int    id;
    private int    userId;
    private String ocrSessionId;
    private String livingSessionId;
    private String updateTime;
    private String createTime;
    private String idNo;
    private String realName;


    public String getOcrSessionId() {
        return ocrSessionId;
    }

    public void setOcrSessionId(String ocrSessionId) {
        this.ocrSessionId = ocrSessionId;
    }

    public String getLivingSessionId() {
        return livingSessionId;
    }

    public void setLivingSessionId(String livingSessionId) {
        this.livingSessionId = livingSessionId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
}
