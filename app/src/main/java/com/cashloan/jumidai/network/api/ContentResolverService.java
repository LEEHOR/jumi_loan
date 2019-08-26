package com.cashloan.jumidai.network.api;

import com.cashloan.jumidai.ui.homeLoan.bean.LoanSub;
import com.commom.net.OkHttp.entity.HttpResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * author : Leehor
 * date   : 2019/7/1711:37
 * version: 1.0
 * desc   :
 */
public interface ContentResolverService {
    @FormUrlEncoded
    @POST("act/borrow/vevifyTradePwd.htm")
    Call<HttpResult> vevifyTradePwd(@Field("") String appList);

    @FormUrlEncoded
    @POST("act/savePosition.htm")
    Call<HttpResult> putLoacation(@FieldMap Map<String, Object> map);
}
