package com.cashloan.jumidai.network.api;

import com.cashloan.jumidai.ui.user.bean.submit.Login_log;
import com.commom.net.OkHttp.entity.HttpResult;
import com.cashloan.jumidai.network.RequestParams;
import com.cashloan.jumidai.ui.homeMine.bean.submit.CreditBankSub;
import com.cashloan.jumidai.ui.user.bean.receive.IsExistsRec;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.ui.user.bean.receive.ProbeSmsRec;
import com.cashloan.jumidai.ui.user.bean.submit.ForgotSub;
import com.cashloan.jumidai.ui.user.bean.submit.LoginSub;
import com.cashloan.jumidai.ui.user.bean.submit.RegisterSub;
import com.cashloan.jumidai.ui.user.bean.submit.ResetPwdSub;
import com.cashloan.jumidai.ui.user.bean.submit.ValidateCodeSub;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/17 15:44
 * <p/>
 * Description: 用户接口
 * (@Url: 不要以 / 开头)
 */
public interface UserService {
    /**
     * 登录
     */
    @POST("user/login.htm")
    Call<HttpResult<OauthTokenMo>> doLogin(@Body LoginSub sub);

    /**
     * 验证码登录
     */
    @POST("user/noPwdLogin.htm")
    Call<HttpResult<OauthTokenMo>> doCodeLogin(@Body LoginSub sub);
//

    /**
     * 刷新令牌
     */
    @FormUrlEncoded
    @POST("user/autoLogin.htm")
    Call<HttpResult<OauthTokenMo>> refreshToken(@Field(RequestParams.REFRESH_TOKEN) String refreshToken);

    /**
     * 注册_验证手机号是否存在
     */
    @FormUrlEncoded
    @POST("user/isPhoneExists.htm")
    Call<HttpResult<IsExistsRec>> isPhoneExists(@Field(RequestParams.PHONE) String phone);


    /**
     * 注册_确认注册
     */
    @POST("user/register.htm")
    Call<HttpResult<OauthTokenMo>> doRegister(@Body RegisterSub sub);

    /**
     * 注册_获取验证码
     */
    @FormUrlEncoded
    @POST("user/sendSms.htm")
    Call<HttpResult<ProbeSmsRec>> getCode(@Field(RequestParams.PHONE) String phone,
                                          @Field(RequestParams.TYPE) String type,
                                          @Field(RequestParams.SIGN) String sign);

    /**
     * 绑卡--获取验证码
     */
    @POST("act/mine/bankCard/fuyouBindCard.htm")
    Call<HttpResult<Object>> getBindCode(@Body CreditBankSub sub);

    /**
     * 是否能获取验证码
     */
    @FormUrlEncoded
    @POST("user/probeSms.htm")
    Call<HttpResult<ProbeSmsRec>> probeSms(@Field(RequestParams.PHONE) String phone,
                                           @Field(RequestParams.TYPE) String type);

    /**
     * 验证忘记密码验证码 万能码0000
     */
    @POST("user/verifySms.htm")
    Call<HttpResult<ProbeSmsRec>> checkCode(@Body ValidateCodeSub sub);

    /**
     * 重置密码
     */
    @POST("user/login/forgetPwd.htm")
    Call<HttpResult> forgetPwd(@Body ForgotSub sub);

    /**
     * 修改密码_确认提交
     */
    @POST("CustomerModifyPwdAction/modifyPassword.htm")
    Call<HttpResult> resetPwd(@Body ResetPwdSub sub);

    /**
     * 登录日志
     */
    @POST("user/saveLocation.htm")
    Call<HttpResult> login_log(@Body Login_log login_log);


}
