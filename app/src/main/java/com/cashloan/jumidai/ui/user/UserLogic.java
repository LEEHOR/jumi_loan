package com.cashloan.jumidai.ui.user;

import android.app.Activity;

import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.utils.SharedInfo;
import com.github.mzule.activityrouter.router.Routers;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/16 17:55
 * <p/>
 * Description:
 */
public class UserLogic {
    /**
     * 用户登录
     */
    public static void login(Activity activity, OauthTokenMo oauthTokenMo) {
        /** 登录逻辑处理 */
        SharedInfo.getInstance().saveValue(Constant.IS_LAND, true);
        SharedInfo.getInstance().saveEntity(oauthTokenMo);
        Routers.open(activity, RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_Main, Constant.NUMBER_0)));
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    /**
     * 登出必须执行的操作
     */
    public static void signOut() {

//        // 标记未启用手势密码
//        SharedInfo.getInstance().remove(Constant.IS_GESTURE_OPENED);
        // 标记未登录
        SharedInfo.getInstance().remove(Constant.IS_LAND);
        // 删除保存的OauthToken信息
        SharedInfo.getInstance().remove(OauthTokenMo.class);
    }

    /**
     * 用户被动登出
     */
    public static void signOutForcibly(Activity activity) {
        signOut();
        Routers.open(activity, RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_Main, Constant.NUMBER_0)));
    }

    /**
     * 用户主动登出(到主界面)
     */
    public static void signOutToMain(final Activity activity) {
//        DialogUtils.showDialog(activity, R.string.user_login_out, new OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog dialog) {
//                dialog.dismiss();
//                signOutForcibly(activity);
//            }
//        });

        DialogUtils.showDefaultDialog(activity, "确定要登出?",
                new DialogUtils.btnConfirmClick() {
                    @Override
                    public void confirm() {
                        signOutForcibly(activity);
                    }
                });

    }

    /**
     * 用户主动登出(到登录界面)
     */

    public static void signOutToLogin(final Activity activity) {
//        DialogUtils.showDialog(activity, R.string.user_login_out, new OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog dialog) {
//                signOut();
//                Routers.openForResult(activity, RouterUrl.getRouterUrl(
//                        String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3)), 0);
//                activity.finish();
//            }
//        });

        DialogUtils.showDefaultDialog(activity, "确定要登出?",
                new DialogUtils.btnConfirmClick() {
                    @Override
                    public void confirm() {
                        signOut();
                        Routers.openForResult(activity, RouterUrl.getRouterUrl(
                                String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3)), 0);
                        activity.finish();
                    }
                });
    }
}
