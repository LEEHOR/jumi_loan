package com.cashloan.jumidai.common;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/12/6 18:06
 * <p>
 * Description: 路由表
 */
@SuppressWarnings("unused")
public class RouterUrl {
    private static final String URI_SCHEME = "wolverine:/";

    /**
     * 获得请求URI
     */
    public static String getRouterUrl(String host) {
        return URI_SCHEME + host;
    }

    /****************************************************************************************************************/
    /******************************************* appCommon router url ***********************************************/
    /****************************************************************************************************************/

    // 主界面 - 声明
    public static final String AppCommon_IMain = "appCommon/main";
    // 主界面 - 调用(参数：type：0 - 首页，1 - 理财，2 - 资产，3 - 我)
    public static final String AppCommon_Main = AppCommon_IMain + "?type=%1$s";
    // 启动页
    public static final String AppCommon_Splash = "appCommon/splash";
    // 引导页
    public static final String AppCommon_Guide = "appCommon/guide";
    // WebView 页面 - 声明
    public static final String AppCommon_IWebView = "appCommon/webView";
    // WebView 页面 - 调用(参数：title - 标题，url - 请求地址，)
    public static final String AppCommon_WebView = AppCommon_IWebView + "?title=%1$s&url=%2$s&postData=%3$s";


    /****************************************************************************************************************/
    /******************************************* userInfoManage router url ******************************************/
    /****************************************************************************************************************/
    // 登录
    public static final String UserInfoManage_ILogin = "userInfoManage/login";
    // 登录
    public static final String UserInfoManage_Login = UserInfoManage_ILogin + "?type=%1$s";
    // 注册
    public static final String UserInfoManage_IRegister = "userInfoManage/register";
    // 注册
    public static final String UserInfoManage_Register = UserInfoManage_IRegister + "?id=%1$s&code=%2$s";
    // 注册成功
    public static final String UserInfoManage_RegisterSucceed = "userInfoManage/registerSucceed";
    // 忘记密码 - 声明
    public static final String UserInfoManage_IForgotPwd = "userInfoManage/forgotPwd";
    // 忘记密码- 调用(参数：id - 手机号)
    public static final String UserInfoManage_ForgotPwd = UserInfoManage_IForgotPwd + "?id=%1$s&type=%2$s";
    // 忘记交易密码
    public static final String UserInfoManage_ForgotPayPwd = "userInfoManage/forgotPayPwd";
    // 重置密码页面 - 声明
    public static final String UserInfoManage_IResetPwd = "userInfoManage/resetPwd";
    // 重置密码页面 - 调用(参数：id - 手机号)
    public static final String UserInfoManage_ResetPwd = UserInfoManage_IResetPwd + "?id=%1$s&sid=%2$s";
    // 主界面 “我”
    public static final String UserInfoManage_UserHomePage = "userInfoManage/userHomePage";

    /****************************************************************************************************************/
    /******************************************* Loan router url ******************************************/
    /****************************************************************************************************************/
    //借款详情 - 声明
    public static final String Loan_IDetails = "loan/details";
    //借款详情 - 调用
    public static final String Loan_Details = Loan_IDetails +
            "?loanMoney=%1$s&loanLimit=%2$s&realMoney=%3$s&fee=%4$s&cardName=%5$s&cardNo=%6$s&cardId=%7$s";


    /****************************************************************************************************************/
    /******************************************* Repay router url ******************************************/
    /****************************************************************************************************************/
    // 还款详情--声明
    public static final String Repay_IDetails = "repay/details";
    // 还款详情--调用
    public static final String Repay_Details = Repay_IDetails + "?id=%1$s&type=%2$s";
//    // 还款方式
//    public static final String Repay_Type     = "repay/type";
//    // 还款方式 - 自动还款
//    public static final String Repay_Auto     = "repay/auto";
//    // 还款方式 - 声明
//    public static final String Repay_IAccount = "repay/account";
//    // 还款方式 - 调用
//    public static final String Repay_Account  = Repay_IAccount + "?type=%1$s";

    /****************************************************************************************************************/
    /************************************************ Mine router url ***********************************************/
    /****************************************************************************************************************/
    // 账户与安全
    public static final String Mine_AccountSecurity = "mine/accountSecurity";
    // 设置
    public static final String Mine_Settings = "mine/settings";
    // 意见反馈
    public static final String Mine_Settings_Idea = "mine/settings/idea";
    // 修改密码
    public static final String Mine_Settings_Update = "mine/settings/update";
    // 设置/修改支付密码
    public static final String Mine_Settings_IPay_Pwd = "mine/settings/payPwd";
    // 设置/修改支付密码
    public static final String Mine_Settings_Pay_Pwd = Mine_Settings_IPay_Pwd + "?type=%1$s";
    //    // 认证中心
//    public static final String Mine_CreditCenter          = "mine/creditCenter";
    // 认证中心
    public static final String Mine_CreditTwoCenter = "mine/creditTwoCenter";
    // 借款记录
    public static final String Mine_LendRecord = "mine/lendRecord";
    // 个人信息
    public static final String Mine_CreditPersonInfo = "mine/creditPersonInfo" + "?state=%1$s";
    // 人脸识别
    public static final String Mine_CreditPersonFace = "mine/creditPersonFace" + "?state=%1$s";

    // 工作信息 - 申明
    public static final String Mine_ICreditWork = "mine/creditWork";
    // 工作信息 - 调用
    public static final String Mine_CreditWork = Mine_ICreditWork + "?state=%1$s";
    // 工作照片
    public static final String Mine_CreditWorkPhoto = "mine/creditWorkPhoto";
    // 银行卡信息 - 申明
    public static final String Mine_ICreditBank = "mine/creditBank";
    // 银行卡信息 - 调用
    public static final String Mine_CreditBank = Mine_ICreditBank + "?type=%1$s";
    // 已绑定银行卡信息
    public static final String Mine_ICreditBindBank = "mine/creditBindBank";

    public static final String Mine_CreditBindBank = Mine_ICreditBindBank + "?status=%1$s";
    // 联系人信息 - 申明
    public static final String Mine_ICreditLinker = "mine/creditLinker";
    // 联系人信息 - 调用
    public static final String Mine_CreditLinker = Mine_ICreditLinker + "?state=%1$s";

    // 运营商信息 - 申明
    public static final String Mine_ICreditPhone = "mine/creditPhone";
    // 运营商信息 - 调用
    public static final String Mine_CreditPhone = Mine_ICreditPhone + "?state=%1$s";
    //    // 公积金信息 - 申明
//    public static final String Mine_ICreditAccumulation = "mine/creditAccumulation";
//    // 公积金信息 - 调用
//    public static final String Mine_CreditAccumulation  = Mine_ICreditAccumulation + "?state=%1$s";
    // 运营商信息 - 申明
    public static final String Mine_ICreditMore = "mine/creditMore";
    // 运营商信息 - 调用
    public static final String Mine_CreditMore = Mine_ICreditMore + "?state=%1$s";
    // 高德地图
    public static final String Mine_GdMap = "mine/GdMap";
//    //身份识别
//    public static final String Mine_CreditIdentity      = "mine/creditIndentity" + "?state=%1$s";

    //帮助中心
    public static final String Mine_HelpCenter = "mine/HelpCenter" + "?url=%1$s";

}
