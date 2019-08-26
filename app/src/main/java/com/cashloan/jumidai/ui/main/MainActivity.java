package com.cashloan.jumidai.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.amap.api.location.AMapLocation;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.cashloan.jumidai.MyApplication;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BaseParams;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.LoanService;
import com.cashloan.jumidai.ui.homeLoan.HomeFrag;
import com.cashloan.jumidai.ui.homeLoan.LoanDetailActivity;
import com.cashloan.jumidai.ui.homeMine.MineFrag;
import com.cashloan.jumidai.ui.homeRepay.RepayRecordFrag;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.utils.DeviceUtil;
import com.cashloan.jumidai.utils.ExampleUtil;
import com.cashloan.jumidai.utils.GaodeMapLocationHelper;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.getImei;
import com.cashloan.jumidai.views.iconfont.IconFont;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.ScreenUtils;
import com.commom.utils.ToastUtil;
import com.commom.utils.getAppContentResolver;
import com.commom.utils.log.Logger;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;
import com.joanzapata.iconify.Iconify;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/29
 * 描述： 首页
 */
@Router(value = RouterUrl.AppCommon_Main, intParams = BundleKeys.TYPE)
public class MainActivity extends BaseMvpActivity {

    private static final String TAG = "MainActivity";
    private static final String TAG_HOME = "HomeFragOld";
    private static final String TAG_REPAY_RECORD = "RepayRecordFrag";
    private static final String TAG_MINE = "MineFrag";

    private HomeFrag mHomeFrag;
    //    private HomeFragOld     mHomeFragOld;
    private RepayRecordFrag recordFrag;
    private MineFrag mineFrag;

    public static int repayState = 1;
    FragmentTransaction transaction;

    private BottomNavigationBar mBottomTabs;
    private FrameLayout mContent;


    public static boolean isForeground = false;
    private String userId;
    private static final int REQUEST_LOGIN = 101;
    private GaodeMapLocationHelper.MyLocationListener myLocationListener;
    private ExecutorService singleThreadExecutor;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mBottomTabs = findViewById(R.id.tabs);
        mContent = findViewById(R.id.content);

        SharedInfo.init(BaseParams.SP_NAME);
    }

    @Override
    protected void initFunc() {
//        mHomeFragOld = HomeFragOld.newInstance();
        mHomeFrag = HomeFrag.newInstance();
        initBottomTabs();

        if (!hasToken()) {
            hideAll();
            showFragmentOne(false);
        } else {
//            reqUserData();
            hideAll();
            showFragmentOne(true);
        }

        //检查权限
        if (checkPermiss()) {
            initGpsStatus();

        } else { //获取权限
            initPermissions();
        }


//        //注册极光推送服务
//        registerMessageReceiver();
    }

    private void initBottomTabs() {
        mBottomTabs.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        mBottomTabs.setActiveColor(R.color.tab_bg)
                .setInActiveColor(R.color.tab_text_normal)
                .setBarBackgroundColor(R.color.app_color_principal);
        Iconify.with(new IconFont());
        mBottomTabs
                .addItem(new BottomNavigationItem(R.drawable.icon_home_loan_sel, R.string.home_index)
                        .setInactiveIconResource(R.drawable.icon_home_loan_def))
                .addItem(new BottomNavigationItem(R.drawable.icon_home_repay_sel, R.string.home_repay)
                        .setInactiveIconResource(R.drawable.icon_home_repay_def))
                .addItem(new BottomNavigationItem(R.drawable.icon_home_mine_sel, R.string.home_mine)
                        .setInactiveIconResource(R.drawable.icon_home_mine_def))
                .setTabSelectedListener(listener)
                .setFirstSelectedPosition(0)
                .initialise();
    }


    private void initGpsStatus() {
        if (getIntent().getIntExtra(BundleKeys.TYPE, -1) == Constant.NUMBER_6) {
            if ((!MyApplication.isOpen(getApplicationContext()))) {
                DialogUtils.showConfirmDialog(mContext, mContext.getResources().getString(R.string.gps_state),
                        new DialogUtils.btnConfirmClick() {
                            @Override
                            public void confirm() {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 0);
                            }
                        });
            } else {
                //打开定位
                MyApplication.openGps(new MyApplication.OnPosChanged() {
                    @Override
                    public void changed(AMapLocation location) {

                    }
                }, true);
                MyApplication.closeGps();
            }
        }
    }

    private boolean hasToken() {
        return SharedInfo.getInstance().getEntity(OauthTokenMo.class) != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasToken()) {
            mineFrag = null;
        }
        // 切入前台后关闭后台定位功能
        GaodeMapLocationHelper.ForegroundLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
      /*  boolean isBackground = ((MyApplication) getApplication()).isBackground();
        //如果app已经切入到后台，启动后台定位功能
        if (isBackground) {
            GaodeMapLocationHelper.BackgroundLocation(this);
        }*/
    }

    /**
     * 底部导航栏操作监听
     */
    public BottomNavigationBar.OnTabSelectedListener listener = new BottomNavigationBar.OnTabSelectedListener() {

        @Override
        public void onTabSelected(int position) {
            hideAll();
            switch (position) {
                case Constant.NUMBER_0:
                    showFragmentOne(hasToken());
                    break;
                case Constant.NUMBER_1:
                    showFragmentRecord();
                    break;
                case Constant.NUMBER_2:
                    showFragmentMine();
                    break;
            }
        }

        @Override
        public void onTabUnselected(int position) {
        }

        @Override
        public void onTabReselected(int position) {
        }
    };


    /**
     * 展示第一个页面
     */
    private void showFragmentOne(boolean hasToken) {
        FragmentManager manager = MainActivity.this.getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = manager.beginTransaction();
//        if (!hasToken) {
//            mHomeFragOld = (HomeFragOld) manager.findFragmentByTag(TAG_HOME);
//            if (null == mHomeFragOld) {
//                mHomeFragOld = HomeFragOld.newInstance();
//                transaction.add(R.id.content, mHomeFragOld, TAG_HOME);
//                transaction.show(mHomeFragOld);
//            } else {
//                transaction.show(mHomeFragOld);
//            }
//        } else {
//            mHomeFragOld = (HomeFragOld) manager.findFragmentByTag(TAG_HOME);
//            if (null == mHomeFragOld) {
//                mHomeFragOld = HomeFragOld.newInstance();
//                transaction.add(R.id.content, mHomeFragOld, TAG_HOME);
//                transaction.show(mHomeFragOld);
//            } else {
//                transaction.show(mHomeFragOld);
//            }
//        }

        if (!hasToken) {
            mHomeFrag = (HomeFrag) manager.findFragmentByTag(TAG_HOME);
            if (null == mHomeFrag) {
                mHomeFrag = HomeFrag.newInstance();
                transaction.add(R.id.content, mHomeFrag, TAG_HOME);
                transaction.show(mHomeFrag);
            } else {
                transaction.show(mHomeFrag);
            }
        } else {
            mHomeFrag = (HomeFrag) manager.findFragmentByTag(TAG_HOME);
            if (null == mHomeFrag) {
                mHomeFrag = HomeFrag.newInstance();
                transaction.add(R.id.content, mHomeFrag, TAG_HOME);
                transaction.show(mHomeFrag);
            } else {
                transaction.show(mHomeFrag);
            }
        }

        transaction.commitAllowingStateLoss();
        mStatusBar.setVisibility(View.VISIBLE);
        mStatusBar.setBackgroundResource(R.color.white);
        ScreenUtils.setTextColorStatusBar(this, true);
    }

    /**
     * 展示第二个页面
     */
    private void showFragmentRecord() {
        FragmentManager manager = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (hasToken()) {
            recordFrag = (RepayRecordFrag) manager.findFragmentByTag(TAG_REPAY_RECORD);
            if (null == recordFrag) {
                recordFrag = RepayRecordFrag.newInstance();
                transaction.add(R.id.content, recordFrag, TAG_REPAY_RECORD);
                transaction.show(recordFrag);
            } else {
                transaction.show(recordFrag);
            }
//            transaction.commitAllowingStateLoss();
//            mStatusBar.setVisibility(View.VISIBLE);
//            mStatusBar.setBackgroundResource(R.color.app_color_principal);
//            ScreenUtils.setTextColorStatusBar(this, false);
        } else {
            Routers.openForResult(MainActivity.this,
                    RouterUrl.getRouterUrl(RouterUrl.UserInfoManage_Login), REQUEST_LOGIN);

        }
        transaction.commitAllowingStateLoss();
        mStatusBar.setVisibility(View.VISIBLE);
        mStatusBar.setBackgroundResource(R.color.app_color_principal);
        ScreenUtils.setTextColorStatusBar(this, false);
    }


    /**
     * 展示第三个页面
     */
    private void showFragmentMine() {
        FragmentManager manager = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (hasToken()) {
            mineFrag = (MineFrag) manager.findFragmentByTag(TAG_MINE);
            if (null == mineFrag) {
                mineFrag = MineFrag.newInstance();
                transaction.add(R.id.content, mineFrag, TAG_MINE);
                transaction.show(mineFrag);
            } else {
                transaction.show(mineFrag);
            }
            transaction.commitAllowingStateLoss();
        } else {
            Routers.openForResult(MainActivity.this,
                    RouterUrl.getRouterUrl(RouterUrl.UserInfoManage_Login), REQUEST_LOGIN);
        }
        mStatusBar.setVisibility(View.GONE);

//        Routers.open(this, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditLinker,
//                "")));
    }


    private void hideAll() {
        FragmentManager manager = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//        if (mHomeFragOld != null) transaction.hide(mHomeFragOld);
        if (mHomeFrag != null) transaction.hide(mHomeFrag);
        if (recordFrag != null) transaction.hide(recordFrag);
        if (mineFrag != null) transaction.hide(mineFrag);
        transaction.commitAllowingStateLoss();
    }


    public static final String[] PERMISSION_ALL = new String[]{
            Permission.CAMERA,
            Permission.WRITE_EXTERNAL_STORAGE,
            Permission.READ_PHONE_STATE,
            Permission.READ_CONTACTS,
            Permission.READ_SMS,
            Permission.RECEIVE_SMS,
            Permission.ACCESS_COARSE_LOCATION,
            Permission.ACCESS_FINE_LOCATION,
            Permission.READ_CALL_LOG};

    private void initPermissions() {
        AndPermission.with(mContext)
                .runtime()
                .permission(PERMISSION_ALL)
//                .rationale(new RuntimeRationale())
                .onGranted(permissions -> {
                    initGpsStatus();

                })
                .onDenied(permissions -> {
                    //用户拒绝了开启权限,且选择了不再询问
                    if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                        // 弹出前往设置的提示

                    } else {//用户本次拒绝了开启权限

                    }
                    DialogUtils.showPermisssionDialog(mContext);
                })
                .start();
    }

    /**
     * 检查权限
     *
     * @return
     */
    private boolean checkPermiss() {
        boolean b = AndPermission.hasPermissions(mContext, PERMISSION_ALL);
        return b;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int position = getIntent().getIntExtra(BundleKeys.TYPE, -1);
        Logger.i(TAG, "onNewIntent() called with position = [ " + position + " ]");
        if (position != -1) {
            if (position == 1) {
                mBottomTabs.selectTab(1);
            } else {
                mBottomTabs.selectTab(0);
            }
        }
    }

    //注册成功--转到到首页
    @Router(RouterUrl.UserInfoManage_UserHomePage)
    public static void toMine(Context context, Bundle bundle) {
        System.out.println("main Success");
        Routers.open(context, RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_Main, Constant.STATUS_3)));
    }

    public void setTab(int position) {
        mBottomTabs.selectTab(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 登录结果回调
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                mBottomTabs.selectTab(0, false);
            } else {
                mBottomTabs.selectTab(0);
            }
        }
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                }
            } catch (Exception e) {

            }

        }

    }

    @Override
    protected void onPause() {
        isForeground = false;
        //  GaodeMapLocationHelper.unRegisterLocationCallback(myLocationListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    /**
     * 开启定位
     */

    public void openGps() {
        GaodeMapLocationHelper.startLocation();
        myLocationListener = new GaodeMapLocationHelper.MyLocationListener() {
            @Override
            public void onLocationSuccess(AMapLocation location) {
                Log.d("定位", location.getAddress());
                GaodeMapLocationHelper.closeLocation();
            }

            @Override
            public void onLocationFailure(int locType) {

            }
        };
        GaodeMapLocationHelper.registerLocationCallback(myLocationListener);

    }


}
