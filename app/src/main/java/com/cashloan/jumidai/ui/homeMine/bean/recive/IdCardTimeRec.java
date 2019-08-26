package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Created by chenming
 * Created Date 17/4/19 11:46
 * mail:cm1@erongdu.com
 * Describe: 获取可扫描次数
 */
public class IdCardTimeRec {
    /** 活体识别可操作次数 */
    private String faceTime;
    /** ocr识别可操作次数 */
    private String ocrTime;

    public String getFaceTime() {
        return faceTime;
    }

    public String getOcrTime() {
        return ocrTime;
    }

}
