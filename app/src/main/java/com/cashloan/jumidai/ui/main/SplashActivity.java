package com.cashloan.jumidai.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BaseParams;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.RouterUrl;
import com.commom.utils.ActivityManage;
import com.commom.utils.BitmapUtil;
import com.commom.utils.ContextHolder;
import com.commom.utils.SPUtil;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;

import java.lang.ref.WeakReference;

/**
 * 作者： Ruby
 * 时间： 2018/8/29
 * 描述： 启动页
 */
@Router(RouterUrl.AppCommon_Splash)
public class SplashActivity extends AppCompatActivity {

    //    // 跳转引导页
    private static final int GO_GUIDE = 0x01;
    // 跳转首页
    private static final int GO_MAIN = 0x02;
    // 页面跳转逻辑
    private static final int DO_HANDLER = 0x99;
    // 最小显示时间
    private static final int SHOW_TIME_MIN = 700;
    // 开始时间
    private static long mStartTime;
    private SplashHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        imageView.setImageBitmap(BitmapUtil.readBitmap(this, R.drawable.splash));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_guide_in));
        setContentView(imageView);

        mHandler = new SplashHandler(this);
        // 记录开始时间
        mStartTime = System.currentTimeMillis();
        // 初始化一些数据
        somethingToDo();

    }


    private void somethingToDo() {

        mHandler.removeMessages(DO_HANDLER);
        mHandler.sendEmptyMessage(DO_HANDLER);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Routers.open(this, RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_Main, Constant.NUMBER_6)));
            finish();
        } else {
            finish();
        }
    }

    /**
     * Handler:跳转到不同界面
     */
    private static class SplashHandler extends Handler {
        WeakReference<SplashActivity> act;

        SplashHandler(SplashActivity act) {
            this.act = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 计算一下总共花费的时间
            long loadingTime = System.currentTimeMillis() - mStartTime;
            switch (msg.what) {
                case DO_HANDLER:
                    // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
                    boolean isFirstIn = (boolean) SPUtil.getValue(SPUtil.getSp(ContextHolder.getContext(), BaseParams.SP_NAME), Constant.IS_FIRST_IN, true);
                    // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
                    if (!isFirstIn) {
                        sendEmptyMessage(GO_MAIN);
                    } else {
                        sendEmptyMessage(GO_GUIDE);
                    }
                    break;

                case GO_GUIDE:
                    // 如果比最小显示时间还短，就延时进入GuideActivity，否则直接进入
                    if (loadingTime < SHOW_TIME_MIN) {
                        postDelayed(goToGuideActivity, SHOW_TIME_MIN - loadingTime);
                    } else {
                        post(goToGuideActivity);
                    }
                    break;

                case GO_MAIN:
                    // 如果比最小显示时间还短，就延时进入HomeActivity，否则直接进入
                    if (loadingTime < SHOW_TIME_MIN) {
                        postDelayed(goToMainActivity, SHOW_TIME_MIN - loadingTime);
                    } else {
                        post(goToMainActivity);
                    }
                    break;
            }
        }

        // 进入 GuideAct
        Runnable goToGuideActivity = new Runnable() {
            @Override
            public void run() {
                Routers.open(act.get(), RouterUrl.getRouterUrl(RouterUrl.AppCommon_Guide));
                act.get().finish();
            }
        };
        // 进入 MainAct
        Runnable goToMainActivity = new Runnable() {
            @Override
            public void run() {
                Routers.open(act.get(), RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_Main, Constant.NUMBER_6)));
                act.get().finish();
            }
        };
    }

    @Override
    public void onBackPressed() {
        ActivityManage.onExit();
    }
}
