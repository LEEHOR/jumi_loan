package com.commom.net.OkHttp;

import android.util.Log;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 作者： Ruby
 * 时间： 2018/7/23
 * 描述：
 */
public class OkHttpManager {


    private static final boolean IS_DEBUG = true;
    private static final String  TAG      = "OkHttpManager";

    private static final int DEFAULT_TIMEOUT = 30;


    private static OkHttpManager sHttpManager = null;

    public static OkHttpManager getClient() {
        if (sHttpManager == null) {
            synchronized (OkHttpManager.class) {
                if (sHttpManager == null) {
                    sHttpManager = new OkHttpManager();
                }
            }
        }
        return sHttpManager;
    }

    public OkHttpClient getOkHttp() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 禁用代理防止抓包
        builder.proxy(Proxy.NO_PROXY);
        // 设置链接失败尝试重连
        builder.retryOnConnectionFailure(true);
        // 设置网络请求超时时间
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

//       // 添加签名参数
//        Interceptor interceptor = new BasicParamsInject().getInterceptor();
//        builder.addInterceptor(interceptor);
        // 打印参数
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
//                if (AppConfig.IS_DEBUG) {   // TODO  去掉注释
                Log.i("RetrofitLog", "retrofitBack = " + message);
//                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

//        String inputUrl = (String) SharedInfo.getInstance().getValue("input_url", "");

        return builder.build();
    }
}
