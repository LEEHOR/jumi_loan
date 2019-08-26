package com.cashloan.jumidai.network.api;

import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.net.OkHttp.entity.PageMo;
import com.cashloan.jumidai.network.RequestParams;
import com.cashloan.jumidai.ui.homeMine.bean.recive.BorrowRec;
import com.cashloan.jumidai.ui.homeRepay.bean.RepayAccountRec;
import com.cashloan.jumidai.ui.homeRepay.bean.RepayDetailsRec;
import com.cashloan.jumidai.ui.homeRepay.bean.RepayRecordItemRec;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/14 17:45
 * <p>
 * Description:还款接口
 */
public interface RepayService {
    /**
     * 借款记录
     */
    @POST("act/mine/borrow/page.htm")
    Call<HttpResult<ListData<BorrowRec>>> getBorrow(@Body PageMo pageMo);

    /**
     * 还款记录
     */
    @POST("act/borrow/findAll.htm")
    Call<HttpResult<ListData<RepayRecordItemRec>>> getRecordList(@Body PageMo pageMo);

    /**
     * 还款详情
     */
    @FormUrlEncoded
    @POST("act/mine/borrow/findProgress.htm")
    Call<HttpResult<RepayDetailsRec>> getRepayDetails(@Field("borrowId") String id);


    /**
     * 还款申请
     */
    @GET("act/borrow/repay/collectionInfo.htm")
    Call<HttpResult<RepayAccountRec>> getRepayType(@Query(RequestParams.TYPE) String type);
}
