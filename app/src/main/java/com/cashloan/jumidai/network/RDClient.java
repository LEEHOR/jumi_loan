package com.cashloan.jumidai.network;

import android.util.Log;

import com.cashloan.jumidai.BuildConfig;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.BaseParams;
import com.cashloan.jumidai.utils.SharedInfo;
import com.commom.net.OkHttp.converter.RDConverterFactory;
import com.commom.net.OkHttp.interceptor.MyHttpLogging;
import com.commom.utils.EmptyUtils;
import com.commom.utils.TextUtil;
import com.commom.utils.log.Logger;

import java.net.Proxy;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author Cliff
 * @Data:2017/7/7
 * @Description:okhttp网络请求工具类管理,(超时，拦截器)
 */
public class RDClient {
    // 网络请求超时时间值(s)
    private static final int DEFAULT_TIMEOUT = 30;
    // 请求地址
    private static final String BASE_URL = BaseParams.URI;
    // retrofit实例
    private Retrofit retrofit;

    /**
     * 私有化构造方法
     */
    private RDClient() {
        // 创建一个OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //禁用代理防止抓包
        builder.proxy(Proxy.NO_PROXY);
        // 设置网络请求超时时间
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // 添加签名参数
        Interceptor interceptor = new BasicParamsInject().getInterceptor();
        builder.addInterceptor(interceptor);
        // 打印参数（修改参数打印 update 2019-7-17 ）
 /*       HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("RetrofitLog", "retrofitBack = " + message);
            }
        });

        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }*/


        //DEBUG模式下配Log参数拦截拦截器
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new MyHttpLogging());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        // 失败后尝试重新请求
        builder.retryOnConnectionFailure(true);
        String inputUrl = (String) SharedInfo.getInstance().getValue("input_url", "");


          //  Log.d("网络2", "RDClient: " + getRealUrl());
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.URI_AUTHORITY_BETA)
                    .client(builder.build())
                    .addConverterFactory(RDConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

    }

    private String getRealUrl() {

        String testUrl = (String) SharedInfo.getInstance().getValue("test_url", "");
        if (EmptyUtils.isEmpty(testUrl)) {
         //   Log.i("getRealUrl--", "BASE_URL = " + BASE_URL);
            return BASE_URL;
        } else {
            if (!BuildConfig.DEBUG) {
                return BASE_URL;
            } else {
              //  Log.i("getRealUrl--", "testUrl = " + testUrl + "/api/");
                return testUrl + "/api/";
            }
        }


    }

    /**
     * 调用单例对象
     */
    private static RDClient getInstance() {
        return RDClientInstance.instance;
    }

    /**
     * 创建单例对象
     */
    private static class RDClientInstance {
        static RDClient instance = new RDClient();
    }

    ///////////////////////////////////////////////////////////////////////////
    // service
    ///////////////////////////////////////////////////////////////////////////
    private static TreeMap<String, Object> serviceMap;

    private static TreeMap<String, Object> getServiceMap() {
        if (serviceMap == null)
            serviceMap = new TreeMap<>();
        return serviceMap;
    }

    /**
     * @return 指定service实例
     */
    public static <T> T getService(Class<T> clazz) {
        if (getServiceMap().containsKey(clazz.getSimpleName())) {
            return (T) getServiceMap().get(clazz.getSimpleName());
        }
       // Logger.w("RDClient", "need to create a new " + clazz.getSimpleName());
        T service = RDClient.getInstance().retrofit.create(clazz);
        getServiceMap().put(clazz.getSimpleName(), service);
        return service;
    }
}
