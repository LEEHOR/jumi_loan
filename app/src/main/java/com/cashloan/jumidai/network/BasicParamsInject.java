package com.cashloan.jumidai.network;


import com.commom.net.OkHttp.interceptor.BasicParamsInterceptor;
import com.commom.net.OkHttp.interceptor.IBasicDynamic;
import com.commom.utils.ActivityManage;
import com.commom.utils.ContextHolder;
import com.cashloan.jumidai.common.BaseParams;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.statistics.DeviceInfoUtils;
import com.umeng.analytics.AnalyticsConfig;


import java.util.Map;
import java.util.TreeMap;

import okhttp3.Interceptor;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/5 17:59
 * <p/>
 * Description: 拦截器 - 用于添加签名参数
 */
public class BasicParamsInject {
    private BasicParamsInterceptor interceptor;

    public BasicParamsInject() {
        // 设置静态参数
        interceptor = new BasicParamsInterceptor.Builder()
                .addBodyParam(Constant.MOBILE_TYPE, BaseParams.MOBILE_TYPE)
                .addBodyParam(Constant.VERSION_NUMBER, DeviceInfoUtils.getVersionName(ContextHolder.getContext()))
                .addBodyParam(Constant.UPDATE_CHANNEL, Util.changeChannelCodeToNumberId(AnalyticsConfig.getChannel(ActivityManage.peek())))
                .build();
        // 设置动态参数
        interceptor.setIBasicDynamic(new IBasicDynamic() {
            @Override
            public String signParams(String postBodyString) {
                //post提交动态添加参数
                return UrlUtils.getInstance().dynamicParams(postBodyString);
            }

            @Override
            public Map signParams(Map map) {
                //get提交动态添加参数
                TreeMap temp = new TreeMap(map);
                return UrlUtils.getInstance().dynamicParams(temp);
            }

            @Override
            public Map signHeadParams(Map headMap) {
                return UrlUtils.getInstance().signParams(headMap);
            }
        });
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }
}
