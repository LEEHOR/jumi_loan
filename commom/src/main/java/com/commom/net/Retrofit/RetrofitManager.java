package com.commom.net.Retrofit;

import com.commom.net.OkHttp.OkHttpManager;
import com.commom.net.OkHttp.converter.RDConverterFactory;
import java.util.TreeMap;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 作者： Ruby
 * 时间： 2018/8/2$
 * 描述：
 */

public class RetrofitManager {

    private static final String BASE_URL = "https://api.douban.com/";

    /*****RetrofitManager 单例*****/
    private static RetrofitManager sRetrofitManager;

    public static RetrofitManager getInstence() {
        if (sRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (sRetrofitManager == null)
                    sRetrofitManager = new RetrofitManager();
            }
        }
        return sRetrofitManager;
    }

    /*****构建Retrofit*****/
    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .client(OkHttpManager.getClient().getOkHttp())
                .addConverterFactory(RDConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    /******Service*****/
    private static TreeMap<String, Object> serviceMap;

    private static TreeMap<String, Object> getServiceMap() {
        if (serviceMap == null)
            serviceMap = new TreeMap<>();
        return serviceMap;
    }

    /**
     * @return 指定的service实例
     */
    public static <T> T getService(Class<T> clazz) {
        if (getServiceMap().containsKey(clazz.getSimpleName())) {
            return (T) getServiceMap().get(clazz.getSimpleName());
        }

        T service = RetrofitManager.getInstence().getRetrofit().create(clazz);
        getServiceMap().put(clazz.getSimpleName(), service);
        return service;
    }
}
