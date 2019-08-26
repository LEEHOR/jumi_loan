package com.cashloan.jumidai.common;

/**
 * 作者： Ruby
 * 时间： 2018/8/2
 * 描述： 类型判别标识
 */
public class FeatureConfig {
    /**
     * 手机运营商
     */
    public static final int enablemobileServerFeature = 1;
    /**
     * 芝麻信用
     */
    public static final int enablesesameFeature = 1;
    /**
     * 身份验证
     */
    public static final int enableidentityFeature = 1;
    /**
     * 公积金
     */
    public static final int enablefundFeature = 0;
    /**
     * 社保
     */
    public static final int enablesecurityFeature = 0;
    /**
     * 个人信息 - 商汤扫描
     */
    public static final int enablescanFeature = 1;
    /**
     * 支付宝认证
     */
    public static final int enablealipayIdentifyFeature = 0;
    /**
     * 还款模块
     */
    public static final int enablerepaymentFeature = 1;
    /**
     * 主动还款
     */
    public static final int enableactiveRepaymentFeature = 1;
    /**
     * 自动扣款
     */
    public static final int enableautomaticDeductionFeature = 1;
    /**
     * 支付宝扣款
     */
    public static final int enablealipayDeductionFeature = 1;
    /**
     * 同盾模块
     */
    public static final int enabletongdunModuleFeature = 1;
    /**
     * 邀请好友
     */
    public static final int enableinviteFeature = 1;


    public static boolean enableFeature(int feature) {
        return feature == 1;
    }
}
