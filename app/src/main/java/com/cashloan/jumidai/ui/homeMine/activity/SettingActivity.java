package com.cashloan.jumidai.ui.homeMine.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.CommonType;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.CommonService;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CommonRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.TradeStateRec;
import com.cashloan.jumidai.ui.user.UserLogic;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.utils.ContextHolder;
import com.commom.utils.PackageUtils;
import com.commom.widget.NoDoubleClickButton;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 作者： Ruby
 * 时间： 2018/8/28
 * 描述： 设置
 */
@Router(value = RouterUrl.Mine_Settings)
public class SettingActivity extends BaseMvpActivity {

    @ViewInject(R.id.rl_set_aboutUs)
    private RelativeLayout rlAboutUs;
    @ViewInject(R.id.rl_update_login_pwd)
    private RelativeLayout rlUpdateLoginPwd;
    @ViewInject(R.id.rl_set_trade_pwd)
    private RelativeLayout rlUpdateTradePwd;
    @ViewInject(R.id.tv_set_trade_pwd)
    private TextView       tvTradePwdTitle;

    @ViewInject(R.id.ndb_exit)
    private NoDoubleClickButton btnExit;


    public String viersion = PackageUtils.getVersion(ContextHolder.getContext());
    private CommonRec vm;
    /***交易密码title*/
    public String pwdtitle = ContextHolder.getContext().getString(R.string.mine_settings_set_pwd);
    public int    payType  = 0; // 0: 设置交易密码 1:修改交易密码 2:修改交易密码(不可忘记密码)

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("设置");
    }

    @Override
    protected void initFunc() {
        attachClickListener(rlAboutUs);
        attachClickListener(rlUpdateLoginPwd);
        attachClickListener(rlUpdateTradePwd);
        attachClickListener(btnExit);

        reqAboutUsLink();
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            //关于我们
            case R.id.rl_set_aboutUs:
                aboutClick(view);
                break;
            //修改登录密码
            case R.id.rl_update_login_pwd:
                updatePwdClick(view);
                break;
            //设置--修改交易密码
            case R.id.rl_set_trade_pwd:
                setPwdClick(view);
                break;
            //退出登录
            case R.id.ndb_exit:
                exitClick(view);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) SharedInfo.getInstance().getValue(Constant.IS_LAND, false)) {
            reqTradePwdStatus();
        }
    }

    private void reqAboutUsLink() {
        Call<HttpResult<ListData<CommonRec>>> call = RDClient.getService(CommonService.class).h5List();
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<ListData<CommonRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<CommonRec>>> call,
                                  Response<HttpResult<ListData<CommonRec>>> response) {

                convert(response.body().getData().getList());
            }
        });
    }

    private void convert(List<CommonRec> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (CommonType.ABOUNT_US.equals(list.get(i).getCode())) {
                vm = list.get(i);
            }
        }
    }

    /**
     * 关于我们
     */
    public void aboutClick(View view) {
        if (vm != null) {
            Routers.open(view.getContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_WebView,
                    vm.getName(), CommonType.getUrl(vm.getValue()), "")));
        }
    }

    /**
     * 修改密码
     */
    public void updatePwdClick(View view) {

        if (vm != null) {
            Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.Mine_Settings_Update));
        }
    }

    /**
     * 设置交易密码
     */
    public void setPwdClick(View view) {
        if (vm != null) {
            Routers.open(view.getContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_Settings_Pay_Pwd, payType)));
        }
    }

    /**
     * 退出登录
     */
    public void exitClick(View view) {
        DialogUtils.showDefaultDialog(mContext, mContext.getString(R.string.user_login_out),
                new DialogUtils.btnConfirmClick() {
                    @Override
                    public void confirm() {
                        Call<HttpResult> call = RDClient.getService(MineService.class).logout();
                        call.enqueue(new RequestCallBack<HttpResult>() {
                            @Override
                            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                                if (response.body().getCode() == 200) {
                                    UserLogic.signOut();

                                    Routers.openForResult(SettingActivity.this,
                                            RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3)), 0);
                                    finish();
                                }
                            }
                        });
                    }
                });

    }

    /****交易密码的设置状态*****/
    public void reqTradePwdStatus() {
        Call<HttpResult<TradeStateRec>> call = RDClient.getService(MineService.class).getTradeState();
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<TradeStateRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<TradeStateRec>> call, Response<HttpResult<TradeStateRec>> response) {
                if (response.body() != null) {
                    TradeStateRec rec = response.body().getData();
                    if (rec.isSetable()) {
                        payType = Constant.NUMBER_0;
                        pwdtitle = ContextHolder.getContext().getString(R.string.mine_settings_set_pwd);
                    } else {
                        if (rec.isForgetable()) {
                            payType = Constant.NUMBER_1;
                        } else {
                            payType = Constant.NUMBER_2;
                        }
                        pwdtitle = ContextHolder.getContext().getString(R.string.mine_settings_update_paypaw);
                    }

                    tvTradePwdTitle.setText(pwdtitle);
                }
            }
        });
    }
}
