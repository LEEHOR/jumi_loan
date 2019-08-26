package com.cashloan.jumidai.network.api;

import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CommonRec;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/25 18:49
 * <p/>
 * Description:
 */
public interface CommonService {
    /** 协议清单 */
    @GET("protocol/list.htm")
    Call<HttpResult<ListData<CommonRec>>> protocolList();

    /** H5列表 */
    @GET("h5/list.htm")
    Call<HttpResult<ListData<CommonRec>>> h5List();

    /** 备注清单 */
    @GET("remark/list.htm")
    Call<HttpResult<ListData<CommonRec>>> remarkList();
}
