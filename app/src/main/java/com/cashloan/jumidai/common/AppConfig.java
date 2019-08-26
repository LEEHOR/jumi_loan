package com.cashloan.jumidai.common;

/**
 * 作者： Ruby
 * 时间： 2018/8/2
 * 描述： 配置信息
 */
public class AppConfig {
    /**
     * 是否是debug模式
     */
    public static final boolean IS_DEBUG = true;

    /**
     * 测试服务器地址
     */
    public static String IP_PORT="http://192.168.1.45:8080/";
    public static  String URI_AUTHORITY_BETA = IP_PORT+"api/"; //李浩
//    public static final String URI_AUTHORITY_BETA = "http://192.168.0.254:8080/";//小平
   //public static  String URI_AUTHORITY_BETA = "http://z19m599274.51mypc.cn/";//炜凯
//    public static final String URI_AUTHORITY_BETA = "http://192.168.0.150:8087/";//贤军     jmd456789
    /**
     * 生产地址 http://103.219.31.61：8080/
     */
    public static  String URI_AUTHORITY_RELEASE = "http://103.219.31.61:8080/";

    /**
     * 正式地址
     */
    public static  String URL_AUTHORITY_PRO="http://z19m599274.51mypc.cn/api/";



    /**
     * app_key
     */
    public static final String APP_KEY = "FEA198F41528E70213E44F7782BDE856";


    /*有盾(异步回调)生产回调地址*/
    public static final String YD_CLIENTURL = URI_AUTHORITY_RELEASE + "api/actzm/mine/youdun/livingCallBack.htm";

    /**
     * 有盾key
     * //8cef0c9e-879c-4a2b-bd9a-77d844baa28e
     * //d015082b-50fb-4ca0-ae95-25efdc3633b6
     **/
    public static final String PUB_KEY      = "d015082b-50fb-4ca0-ae95-25efdc3633b6";// 新的公钥

    //fe3f6e16-c285-4882-b32d-eb7f2a671237
    //73138020-064c-4020-89ff-88f0b941f57d
    public static final String SRCURITY_KEY = "73138020-064c-4020-89ff-88f0b941f57d";

    /**
     * 引导页数量
     */
    public static final int GUIDE_COUNT             = 3;
    /**
     * 首页下标
     */
    public static final int HOME_INDEX_NUM          = 1;
    /**
     * 认证中心下标
     */
    public static final int CREDIT_CENTER_INDEX_NUM = 2;
    /**
     * 人像识别方案 face++
     */
    public static final int IDENTIFICATION_TYPE     = 3;

    /**
     * bugly----appId
     */
    public static final String BUGLYID = "8519fc69d7";


	//借款失败跳转推广的url   old   http://m.51jjhao.com/h5/#/?titleType=0
    public static final String LOAN_FAILED_LINK_URL = "http://www.9dainiyouqian.com/#/";

}
