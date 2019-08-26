package com.cashloan.jumidai;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.BaseParams;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.CrashUtil;
import com.cashloan.jumidai.common.LifecycleApplication;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.utils.GaodeMapLocationHelper;
import com.cashloan.jumidai.utils.SharedInfo;
import com.commom.net.Config;
import com.commom.net.OkHttp.utils.SerializedUtil;
import com.commom.utils.ActivityManage;
import com.commom.utils.ContextHolder;
import com.commom.utils.EmptyUtils;
import com.commom.utils.encryption.RSA;
import com.commom.utils.log.CrashHandler;
import com.commom.utils.log.Logger;
import com.github.mzule.activityrouter.router.RouterCallback;
import com.github.mzule.activityrouter.router.RouterCallbackProvider;
import com.github.mzule.activityrouter.router.SimpleRouterCallback;
import com.moxie.client.manager.MoxieSDK;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

/**
 * 作者： Ruby
 * 时间： 2018/8/21$
 * 描述：
 */

public class MyApplication extends LifecycleApplication implements RouterCallbackProvider, AMapLocationListener {

    private static final String TAG = "MyApplication";
    private int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i(TAG, "---onCreate()---");
        basicInit();
        dataInit();
        CrashUtil crashUtil = CrashUtil.getInstance();
        crashUtil.init(this);
        registerGaoDeLocation();
        //GaodeMapLocationHelper.init(this);
        ActivityLife();
    }

    private static MyApplication instance;

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    //字体图标
    private Typeface iconTypeFace;

    public Typeface getIconTypeFace() {
        return iconTypeFace;
    }

    /**
     * 初始化 Application 运行所需的配置信息  一些三方的集成初始化等操作
     */
    private void basicInit() {
        //魔蝎SDK初始化
        MoxieSDK.init(this);

//        //极光SDK初始化
//        JPushInterface.setDebugMode(false);// 设置开启日志,发布时请关闭日志
//        JPushInterface.getRegistrationID(this);
//        JPushInterface.init(this);

        MobclickAgent.setDebugMode(false);
        UMShareAPI.get(this);
//        CrashReport.initCrashReport(getApplicationContext(), AppConfig.BUGLYID, true);
        ContextHolder.init(this);
        com.umeng.socialize.Config.DEBUG = true;


        Config.DEBUG.set(AppConfig.IS_DEBUG);
        Config.ROOT_PATH.set(BaseParams.ROOT_PATH);
        if (AppConfig.IS_DEBUG) {
            // 崩溃日志
            CrashHandler.getInstance().init();
        }

        // 内存共享对象初始化
        SharedInfo.init(BaseParams.SP_NAME);
//        // 分享初始化
//        shareInit();


//        // iconfont 初始化
//        iconFontInit();

        try {
            // RSA加密 key 初始化
            SerializedUtil.init(RSA.getKey(RSA.MODE.MODULUS_EXPONENT, "密钥类型: 0 - 私钥, 1 - 公钥", "模数", "指数"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 退出动作初始化
        ActivityManage.setOperations(new ActivityManage.ExtraOperations() {
            @Override
            public void onExit() {
                // TODO APP退出时需要额外处理的事情，例如广播的反注册，服务的解绑
            }

            @Override
            public void onActivityFinish(Activity activity) {
                // TODO activity 销毁时需要额外处理的事情，例如finish动画等
            }
        });

    }

    /**
     * iconfont 初始化
     */
    private void iconFontInit() {
        instance = this;
        iconTypeFace = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
    }

    /**
     * 分享初始化
     */
    private void shareInit() {
//        //微信
//        PlatformConfig.setWeixin(AppConfig.WX_APP_ID, AppConfig.WX_APP_SECRET);
//        PlatformConfig.setSinaWeibo(AppConfig.SINA_APP_ID, AppConfig.SINA_APP_SECRET, AppConfig.SINA_APP_CALLBACKURL);
//        //QQ_ID
//        PlatformConfig.setQQZone(AppConfig.QQ_ID, AppConfig.QQ_APP_KEY);
    }


    /**
     * 输出初始化
     */
    private void dataInit() {
        // 根据保存的 OauthToken ，判断用户是否已登录
        if (null != SharedInfo.getInstance().getEntity(OauthTokenMo.class)) {
            SharedInfo.getInstance().saveValue(Constant.IS_LAND, true);
        } else {
            SharedInfo.getInstance().saveValue(Constant.IS_LAND, false);
        }
    }

    @Override
    public RouterCallback provideRouterCallback() {
        return new SimpleRouterCallback() {
            @Override
            public void beforeOpen(Context context, Uri uri) {
            }

            @Override
            public void afterOpen(Context context, Uri uri) {
            }

            @Override
            public void notFound(Context context, Uri uri) {
            }

            @Override
            public void error(Context context, Uri uri, Throwable e) {
            }
        };
    }

    /**
     * app的生命周期
     */
    public void ActivityLife() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (count > 0) {
                    count--;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }


    /**
     * 判断app是否在后台
     *
     * @return
     */
    public boolean isBackground() {
        if (count <= 0) {
            return true;
        } else {
            return false;
        }
    }


//==========================================================高德定位=========================================
    /***********************
     * 高德地图定位
     *****************************/
    public static AMapLocationClient mLocationClient;
    private static String locCity = "";
    private static String district = "";
    public static String address = "";
    public static double lat = 0;
    public static double lon = 0;
    private static boolean openGps = false;
    private static int locCount = 0;

    public void registerGaoDeLocation() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            //设置定位监听
            mLocationClient.setLocationListener(this);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            setLocOption(10000);
            mLocationClient.startLocation();
        }
    }

    public static void setLocOption(int time) {

        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);// 设置定位模式
        option.setInterval(time);// 设置发起定位请求的间隔时间为5000ms
        option.setNeedAddress(true);// 返回的定位结果包含地址信息
        //		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        //设置为高精度定位模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(option);
    }


    /***********
     * 打开GPS
     *
     * @param lisenter
     * @param status   表示是否进行GPS跟踪
     */
    public static void openGps(OnPosChanged lisenter, boolean status) {
        locCount = 0;
        onPosChanged = lisenter;
        openGps = status;
        setLocOption(5000);
        mLocationClient.startLocation();
    }

    /**
     * 判断GPS是否开启
     */
    public static boolean isOpen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        //        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {//|| network) {
            return true;
        }

        return false;
    }

    /**********
     * 位置回调
     */
    static OnPosChanged onPosChanged;

    public static void setOnPosChanged(OnPosChanged callback) {
        locCount = 0;
        onPosChanged = callback;
    }

    public interface OnPosChanged {
        void changed(AMapLocation location);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                district = amapLocation.getDistrict();
                //address = amapLocation.getProvince() + "," + amapLocation.getCity() + "," + district + "," + amapLocation.getStreet();
                address = amapLocation.getAddress();
                locAddress = amapLocation.getCity() + district + amapLocation.getStreet() + amapLocation.getStreetNum();
                locCity = amapLocation.getCity();
                lon = amapLocation.getLongitude();
                lat = amapLocation.getLatitude();

                if (locCity != null && !"".equals(locCity)) {
                    if (!openGps) {
                        mLocationClient.stopLocation();
                    }

                    if (onPosChanged != null) {
                        onPosChanged.changed(amapLocation);
                    }

                } else {
                    locCount++;
                    if (locCount >= 4) {
                        if (!openGps) {
                            mLocationClient.stopLocation();
                        }

                        if (onPosChanged != null) {
                            onPosChanged.changed(amapLocation);
                        }

                    }
                }
                Log.d(TAG, "app4: " + amapLocation.getAddress() + "/" + amapLocation.getCity() + "/" + amapLocation.getStreet() + "/" + amapLocation.getErrorCode() + "/" + amapLocation.getErrorInfo());
                // System.out.println("locCity:" + locCity);
            } else {
                System.out.println("amapLocation.getErrorCode()" + amapLocation.getErrorCode());
                System.out.println("amapLocation.getErrorInfo()" + amapLocation.getErrorInfo());
                if (onPosChanged != null)
                    onPosChanged.changed(amapLocation);
            }
        }
    }

    private static String locAddress = "";

    public static String getLocAddress() {
        return locAddress;
    }

    public static void closeGps() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }

    }

}
