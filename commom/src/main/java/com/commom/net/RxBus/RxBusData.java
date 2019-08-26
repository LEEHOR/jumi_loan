package com.commom.net.RxBus;

/**
 * 作者： Ruby
 * 时间： 2018/9/6
 * 描述： RxBusData
 */
public class RxBusData {
    String id;
    String status;

    public RxBusData() {
    }

    public RxBusData(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}