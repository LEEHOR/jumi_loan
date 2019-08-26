package com.cashloan.jumidai.network.api;

import com.cashloan.jumidai.network.RequestParams;
import com.cashloan.jumidai.ui.homeLoan.bean.AuthRepaySub;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeChoiceRec;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeRec;
import com.cashloan.jumidai.ui.homeLoan.bean.LoanRec;
import com.cashloan.jumidai.ui.homeLoan.bean.LoanSub;
import com.cashloan.jumidai.ui.homeLoan.bean.NoticeRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.BankRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.BorrowRec;
import com.cashloan.jumidai.ui.homeRepay.bean.JuheDataRec;
import com.cashloan.jumidai.ui.homeRepay.bean.NoBindCardQuickPayRec;
import com.cashloan.jumidai.ui.user.bean.submit.Login_log;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.net.OkHttp.entity.ResultAppList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Author: JinFu
 * E-mail: jf@erongdu.com
 * Date: 2017/2/14 17:45
 * <p/>
 * Description:借款接口
 */
public interface LoanService {
    /**
     * 获取首页详情
     */
    @POST("borrow/findIndex.htm")
    Call<HttpResult<HomeRec>> getHomeIndex();

    /**
     * 获取公告列表
     */
    @POST("borrow/listIndex.htm")
    Call<HttpResult<ListData<NoticeRec>>> getNoticeList();


    /**
     * 获取借款详情
     */
    @FormUrlEncoded
    @POST("act/borrow/findBorrow.htm")
    Call<HttpResult<LoanRec>> getLoanDetails(@Field(RequestParams.ORDER_NO) String orderNo);

    /**
     * 申请借款
     */
    @POST("act/borrow/save.htm")
    Call<HttpResult> getLoanApply(@Body LoanSub sub);

    /**
     * 选择借款金额和期限，获取实际到账和手续费信息
     */
    @FormUrlEncoded
    @POST("borrow/choice.htm")
    Call<HttpResult<HomeChoiceRec>> getHomeChoice(@Field(RequestParams.AMOUNT) String amount,
                                                  @Field(RequestParams.TIME_LIMIT) String timeLimit);

    @POST("act/borrow/findAll.htm")
    Call<HttpResult<ListData<BorrowRec>>> findAll();

    /**
     * 新的支付接口--update 2018-7-11   主动还款地址
     */
    @POST("act/mine/quickPay.htm")
    Call<HttpResult<Object>> newQuickPay(@Body AuthRepaySub sub);

    /**
     * 新的连连支付接口--update 2018-7-8
     */
    @POST("act/mine/bankCard/quickPay.htm")
    Call<HttpResult<BankRec>> lianlianQuickPay(@Body AuthRepaySub sub);

    /**
     * 用户主动还款
     */
    @POST("act/mine/bankCard/authSignPay.htm")
    Call<HttpResult<BankRec>> authSignPay(@Body AuthRepaySub sub);

    /**
     * 新增接口（支付宝预支付获取js请求参数）  /api/act/mine/juhePay/getWapPrePay.htm
     * 参数 borrowId
     * 参数 realRepayAmount
     * 参数 userId
     */
//    @POST("act/mine/juhePay/getWapPrePay.htm")
//    Call<HttpResult<JuheDataRec>> getWapPrePay(@Body AuthRepaySub sub);
//
//
//    @POST("act/mine/manQian/getWapPrePay.htm")
//    Call<HttpResult<JuheDataRec>> getNewWapPrePay(@Body AuthRepaySub sub);
//
//
//    /*** 续借 支付宝   废弃***/
//    @POST("act/mine/manQian/getRenewPrePay.htm")
//    Call<HttpResult<JuheDataRec>> getRenewPrePay(@Body AuthRepaySub sub);
//
//
//    /**** 统一方法名  支付宝支付 续借  还款  2018-8-8 ****/
//    @POST("act/mine/manQian/getWapPrePay.htm")
//    Call<HttpResult<JuheDataRec>> getUnionWapPrePay(@Body AuthRepaySub sub);

    /**** 统一方法名  银行卡支付（代扣） 续借  还款  2018-11-18 ****/
    @POST("act/mine/fuyouPay.htm")
    Call<HttpResult<Object>> getUnionBankCardPay(@Body AuthRepaySub sub);


    /**** 统一方法名  支付宝支付 续借  还款  2018-8-8 ****/
    @POST("act/mine/manQian/getWapPrePay.htm")
    Call<HttpResult<JuheDataRec>> getUnionWapPrePay(@Body AuthRepaySub sub);

    /**** 统一方法名  未绑定银行卡支付 续借  还款  2018-10-10  ****/
    @POST("act/mine/bankCard/createQuickOrder.htm")
    Call<HttpResult<NoBindCardQuickPayRec>> getNoBindCardPay(@Body AuthRepaySub sub);

    /**
     * 判断是否能还款
     */
    @POST("act/mine/payState.htm ")
    Call<HttpResult<BankRec>> payState();


    /**
     * 用户是否可以申请借款-验证交易密码是否正确
     */
    @POST("act/borrow/vevifyTradePwd.htm")
    Call<HttpResult> vevifyTradePwd(@Body LoanSub sub);


    /**
     * 去展期
     */
    @FormUrlEncoded
    @POST("act/borrow/extend.htm")
    Call<HttpResult<Object>> goExtend(@Field("borrowId") String borrowId);


    /**
     * 上传AppList
     *
     * @param appList
     * @return
     */
    @FormUrlEncoded
    @POST("act/saveUserAppList.htm")
    Call<HttpResult> upLoadAppList(@Field("userId") long userId, @Field("userAppLists") String appList);

    /**
     * 通话记录
     */
    @FormUrlEncoded
    @POST("act/saveUserCallRecord.htm")
    Call<HttpResult> userCallRecord(@Field("userId") long userId, @Field("userCallRecords") String userCallRecords);

    /**
     * 短信
     */
    @FormUrlEncoded
    @POST("act/saveUserSmsList.htm")
    Call<HttpResult> userSmsList(@Field("userId") long userId, @Field("userSmsList") String userCallRecords);

}
