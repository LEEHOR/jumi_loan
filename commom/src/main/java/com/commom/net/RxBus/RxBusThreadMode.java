package com.commom.net.RxBus;

/**
 * 作者： Ruby
 * 时间： 2018/9/6
 * 描述： RxBusThreadMode
 */
public enum RxBusThreadMode {
    /**
     * current thread
     */
    CURRENT_THREAD,

    /**
     * android main thread
     */
    MAIN,


    /**
     * new thread
     */
    NEW_THREAD
}
