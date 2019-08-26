package com.commom.net.OkHttp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/5 15:23
 * <p/>
 * Description: 网络返回消息，最外层解析实体
 */
@SuppressWarnings("unused")
public class HttpResult<T> {


    /**
     * 错误码
     */
    @SerializedName(Params.RES_CODE)
    private int    code;
    /**
     * 错误信息
     */
    @SerializedName(Params.RES_MSG)
    private String msg;
    /**
     * 消息响应的主体
     */
    @SerializedName(Params.RES_DATA)
    private T      data;
    //    @SerializedName(Params.RES_PAGE)
    private PageMo page;

    /**
     * apk更新url
     */
    @SerializedName(Params.RES_DOWNURL)
    private String androidDownloadUrl;


    /**
     * apk是否强制更新字段
     */
    @SerializedName(Params.RES_UPDATECODE)
    private String updateCode;

    /**
     * app自动更新打开方式
     * 1、oss（app):应用内自动更新
     * 2、web(web)浏览器打开，用户手动下载
     */
    @SerializedName(Params.RES_APPUPDATENWAY)
    private String appUpdateWay;


    public String getUpdateCode() {
        return updateCode;
    }

    public void setUpdateCode(String updateCode) {
        this.updateCode = updateCode;
    }

    public String getAndroidDownloadUrl() {
        return androidDownloadUrl;
    }

    public void setAndroidDownloadUrl(String androidDownloadUrl) {
        this.androidDownloadUrl = androidDownloadUrl;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public PageMo getPage() {
        return page;
    }

    public void setPage(PageMo page) {
        this.page = page;
    }

    public String getAppUpdateWay() {
        return appUpdateWay;
    }

    public void setAppUpdateWay(String appUpdateWay) {
        this.appUpdateWay = appUpdateWay;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", page=" + page +
                ", androidDownloadUrl='" + androidDownloadUrl + '\'' +
                ", updateCode='" + updateCode + '\'' +
                ", appUpdateWay='" + appUpdateWay + '\'' +
                '}';
    }
}
