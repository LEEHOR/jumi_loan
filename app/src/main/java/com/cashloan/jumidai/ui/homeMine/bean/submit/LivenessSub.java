package com.cashloan.jumidai.ui.homeMine.bean.submit;

import java.io.File;

/**
 * Created by yhd on 2017/6/5 0005.
 */

public class LivenessSub {

    private File livingImg;

    private String youDunIdCardFlag;

    public String getYouDunIdCardFlag() {
        return youDunIdCardFlag;
    }

    public void setYouDunIdCardFlag(String youDunIdCardFlag) {
        this.youDunIdCardFlag = youDunIdCardFlag;
    }

    public File getLivingImg() {
        return livingImg;
    }

    public void setLivingImg(File livingImg) {
        this.livingImg = livingImg;
    }

    private String livingSessionId;

    public String getLivingSessionId() {
        return livingSessionId;
    }

    public void setLivingSessionId(String livingSessionId) {
        this.livingSessionId = livingSessionId;
    }
}
