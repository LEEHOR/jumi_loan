package com.cashloan.jumidai.network;

import android.content.Context;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.common.dialog.UpdateDialog;
import com.cashloan.jumidai.network.api.UserService;
import com.cashloan.jumidai.ui.user.UserLogic;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.utils.SharedInfo;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.ActivityManage;
import com.commom.utils.ToastUtil;
import com.github.mzule.activityrouter.router.Routers;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/5/30 11:53
 * <p/>
 * Description: 异常处理
 */
@SuppressWarnings("unchecked")
final class ExceptionHandling {
    static void operate(final HttpResult result) {
        if (result.getCode() == 400 && result.getAndroidDownloadUrl() != null
                && !result.getAndroidDownloadUrl().isEmpty()
                &&result.getAppUpdateWay()!=null) {
            UpdateDialog.getInstance(ActivityManage.peek(), result.getAndroidDownloadUrl(),
                    result.getMsg(),result.getUpdateCode(),result.getAppUpdateWay()).setCancelable(false).show();
            return;
        }

        switch (result.getCode()) {

            case AppResultCode.TOKEN_TIMEOUT:
                OauthTokenMo tokenMo = SharedInfo.getInstance().getEntity(OauthTokenMo.class);
                if (null != tokenMo) {
                    Call<HttpResult<OauthTokenMo>> call = RDClient.getService(UserService.class).refreshToken(tokenMo.getRefreshToken());
                    call.enqueue(new RequestCallBack<HttpResult<OauthTokenMo>>() {
                        @Override
                        public void onSuccess(Call<HttpResult<OauthTokenMo>> call, Response<HttpResult<OauthTokenMo>> response) {
                            SharedInfo.getInstance().saveEntity(response.body().getData());
                        }
                    });
                } else {
                    UserLogic.signOut();
                    Routers.openForResult(ActivityManage.peek(), RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3)), 0);
                }
                break;

            case AppResultCode.TOKEN_REFRESH_TIMEOUT:
                UserLogic.signOut();
                Routers.openForResult(ActivityManage.peek(), RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3)), 0);
                break;
            case AppResultCode.TOKEN_NOT_UNIQUE:
            case AppResultCode.TOKEN_NOT_EXIT:
                Context context = ActivityManage.peek();

                DialogUtils.showSureCancelDialog(context,
                        context.getString(R.string.user_login_two),
                        "取消",
                        context.getString(R.string.user_login_reset),
                        new DialogUtils.btnCancelClick() {
                            @Override
                            public void cancel() {
                                UserLogic.signOut();
                                Routers.openForResult(ActivityManage.peek(),
                                        RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3)), 0);
                            }
                        }, new DialogUtils.btnConfirmClick() {
                            @Override
                            public void confirm() {
                                UserLogic.signOut();
                                Routers.openForResult(ActivityManage.peek(), RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3))
                                        , 0);
                            }
                        });
                break;

            default:
                break;
        }
        if (result.getCode() != 410 && result.getCode() != 411
                && result.getCode() != 412 && result.getCode() != 413) {
            ToastUtil.toast(result.getMsg());
        }
    }
}
