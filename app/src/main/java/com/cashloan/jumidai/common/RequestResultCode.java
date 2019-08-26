package com.cashloan.jumidai.common;

/**
 * 作者： Ruby
 * 时间： 2018/8/2
 * 描述： 跳转回调code类
 */
public class RequestResultCode {
    /** 重新绑定银行卡 */
    public static int REQ_AGAIN_BIND        = 1002;
    public static int RES_AGAIN_BIND        = 1002;
    /** 高德地图 */
    public static int REQ_GD_MAP            = 1003;
    /** 手机运营商认证 */
    public static int REQ_PHONE             = 1004;
    public static int RES_PHONE             = 1004;
    /** 找回交易密码 */
    public static int REQ_FORGOT_PAY        = 1004;
    public static int RES_FORGOT_PAY        = 1004;
    /** 找回密码 */
    public static int REQ_FORGOT            = 1005;
    public static int RES_FORGOT            = 1005;
    /** 芝麻信用认证 */
    public static int REQ_ZMXY              = 1006;
    public static int RES_ZMXY              = 1006;
    /** 公积金认证 */
    public static int REQ_ACCUMULATION_FUND = 1008;
}
