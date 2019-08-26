package com.commom.net.RxBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 作者： Ruby
 * 时间： 2018/9/6
 * 描述： RxBusSubscriberMethod
 */
public class RxBusSubscriberMethod {
    public Method          method;
    public RxBusThreadMode mRxBusThreadMode;
    public Class<?>        eventType;
    public Object          subscriber;
    public int             code;

    public RxBusSubscriberMethod(Object subscriber, Method method, Class<?> eventType, int code, RxBusThreadMode rxBusThreadMode) {
        this.method = method;
        this.mRxBusThreadMode = rxBusThreadMode;
        this.eventType = eventType;
        this.subscriber = subscriber;
        this.code = code;
    }


    /**
     * 调用方法
     * @param o 参数
     */
    public void invoke(Object o){
        try {
            Class[] parameterType = method.getParameterTypes();
            if(parameterType != null && parameterType.length == 1){
                method.invoke(subscriber, o);
            }else if(parameterType == null || parameterType.length == 0){
                method.invoke(subscriber);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}