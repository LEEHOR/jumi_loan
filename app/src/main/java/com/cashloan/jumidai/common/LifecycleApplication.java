package com.cashloan.jumidai.common;

//import android.app.Activity;
//import android.app.Application;
//import android.os.Bundle;
//import android.support.multidex.MultiDexApplication;
//
//import com.erongdu.wireless.tools.log.Logger;
//import com.erongdu.wireless.tools.utils.ActivityManage;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.commom.utils.ActivityManage;
import com.commom.utils.log.Logger;

/**
 * 作者： Ruby
 * 时间： 2018/8/2
 * 描述： 程序中所有Activity的管理类
 */
public class LifecycleApplication
        extends MultiDexApplication
{
    protected static final String TAG   = "LifecycleApplication";
    /** 当前活动的Activity数量 */
    private                int    count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                ActivityManage.push(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                ActivityManage.setTopActivity(activity);
                if (count++ == 0) {
                    Logger.i(TAG, ">>>>>>>>>>>>>>>>>>> 切到前台 <<<<<<<<<<<<<<<<<<<");

                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    Logger.i(TAG, ">>>>>>>>>>>>>>>>>>> 切到后台 <<<<<<<<<<<<<<<<<<<");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityManage.remove(activity);
            }
        });
    }

}
