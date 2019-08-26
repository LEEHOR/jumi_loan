package com.cashloan.jumidai.ui.homeMine.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.RxBusEvent;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.recive.PassRec;
import com.cashloan.jumidai.ui.homeMine.bean.submit.UpdatePwdSub;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.RxBus.RxBus;
import com.commom.utils.ActivityManage;
import com.commom.utils.TextUtil;
import com.github.mzule.activityrouter.annotation.Router;

import cn.pedant.SweetAlert.PasswordInputView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/28
 * 描述： 设置/修改支付密码
 */
@Router(value = RouterUrl.Mine_Settings_Pay_Pwd, intParams = BundleKeys.TYPE)
public class SetPayPwdActivity extends BaseMvpActivity {

    @ViewInject(R.id.tv_set_payPwd_tips)
    private TextView          tvTips;
    @ViewInject(R.id.pwd)
    private PasswordInputView piVPwd;

    public  String title;
    public  String tips;
    private int    payType; // 0: 设置交易密码 1:修改交易密码 2:修改交易密码(不可忘记密码) 3:重置交易密码
    private int step = Constant.NUMBER_0; //0: 输入旧密码 1: 输入新密码 : 2: 确认新密码
    private String oldpwd;
    private String newpwd;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pay_pwd;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        payType = getIntent().getIntExtra(BundleKeys.TYPE, Constant.NUMBER_0);
        initTitle();
    }

    @Override
    protected void initFunc() {
//        payType = getIntent().getIntExtra(BundleKeys.TYPE, Constant.NUMBER_0);

        piVPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                input(editable);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            setPayType(Constant.NUMBER_3);
        }
        if (requestCode == RequestResultCode.REQ_FORGOT_PAY && resultCode == RequestResultCode.RES_FORGOT_PAY) {
            setPayType(Constant.NUMBER_3);
        }
    }


    public void setPayType(int payType) {
        this.payType = payType;
        initTitle();
    }

    public void input(Editable e) {
        if (!TextUtil.isEmpty(e.toString()) && e.toString().length() == 6) {
            switch (step) {
                case Constant.NUMBER_0:
                    reqCheckPwd(e.toString());
                    break;
                case Constant.NUMBER_1:
                    newpwd = e.toString();
                    step = Constant.NUMBER_2;
                    title = mContext.getString(R.string.settints_pay_update_confirm_title);
                    tips = mContext.getString(R.string.settints_pay_update_confirm_tips);
                    e.clear();
                    break;
                case Constant.NUMBER_2:
                    if (newpwd.equals(e.toString())) {
                        if (payType == Constant.NUMBER_0) {
                            reqSetPwd(e.toString());
                        } else if (payType == Constant.NUMBER_3) {
                            resetTradePwd(e.toString());
                        } else {
                            reqUpdatePwd();
                        }
                    } else {

                        DialogUtils.showSureCancelDialog(mContext,
                                mContext.getResources().getString(R.string.seetings_pwd_tips),
                                "重新设置",
                                "再试一次",
                                new DialogUtils.btnCancelClick() {
                                    @Override
                                    public void cancel() {
                                        piVPwd.setText("");
                                    }
                                }, new DialogUtils.btnConfirmClick() {
                                    @Override
                                    public void confirm() {
                                        piVPwd.setText("");
                                        title = mContext.getString(R.string.mine_settings_set_pwd);
                                        tips = mContext.getString(R.string.settints_pay_set_tips);
                                        step = Constant.NUMBER_1;
                                    }
                                });
                    }
                    break;
            }

            tvTips.setText(tips);
            setPageTitle(title);
        }
    }

    //设置交易密码
    private void reqSetPwd(String pwd) {
        UpdatePwdSub sub = new UpdatePwdSub();
        sub.setPwd(pwd);
        Call<HttpResult> call = RDClient.getService(MineService.class).setTradePwd(sub);
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                RxBus.get().send(RxBusEvent.SET_PAY_PWD_SUCCESS);
                showTipsDialog(mContext.getString(R.string.settints_pay_success));
            }
        });
    }

    //修改交易密码
    private void reqUpdatePwd() {
        Call<HttpResult> call = RDClient.getService(MineService.class).updatePayPwd(new UpdatePwdSub(newpwd, oldpwd));
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                showTipsDialog(mContext.getString(R.string.settints_pay_success));
            }
        });
    }

    //校验交易密码
    private void reqCheckPwd(final String pwd) {
        Call<HttpResult<PassRec>> call = RDClient.getService(MineService.class).validateTradePwd(new UpdatePwdSub(pwd));
        call.enqueue(new RequestCallBack<HttpResult<PassRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<PassRec>> call, Response<HttpResult<PassRec>> response) {
                if (response.body().getData().isPass()) {
                    oldpwd = pwd;
                    step = Constant.NUMBER_1;
                    title = mContext.getString(R.string.settints_pay_update_new_title);
                    tips = mContext.getString(R.string.settints_pay_update_new_tips);
                    piVPwd.setText("");
                } else {
                    DialogUtils.showConfirmDialog(mContext, "原交易密码错误",
                            new DialogUtils.btnConfirmClick() {
                                @Override
                                public void confirm() {
                                    piVPwd.setText("");
                                }
                            });
                }

                tvTips.setText(tips);
                setPageTitle(title);
            }
        });
    }

    //重置交易密码
    private void resetTradePwd(String pwd) {
        UpdatePwdSub sub = new UpdatePwdSub();
        sub.setNewPwd(pwd);
        Call<HttpResult> call = RDClient.getService(MineService.class).resetTradePwd(sub);
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                showTipsDialog(mContext.getString(R.string.settints_pay_success));
            }
        });
    }

    private void showTipsDialog(String string) {
        DialogUtils.showConfirmDialog(mContext, string, new DialogUtils.btnConfirmClick() {
            @Override
            public void confirm() {
                ActivityManage.pop();
            }
        });
    }

    private void initTitle() {
        piVPwd.setText("");
        if (payType == Constant.NUMBER_0 || payType == Constant.NUMBER_3) {
            title = mContext.getString(R.string.mine_settings_set_pwd);
            tips = mContext.getString(R.string.settints_pay_set_tips);
            step = Constant.NUMBER_1;
//            if (this.binding.toolbar.getTitleBar().getActionCount() > 0) {
//                this.binding.toolbar.getTitleBar().removeActionAt(0);
//            }
        } else {
            title = mContext.getString(R.string.mine_settings_update_paypaw);
            tips = mContext.getString(R.string.settints_pay_update_tips);
            step = Constant.NUMBER_0;
        }

        setPageTitleBack(title);
        tvTips.setText(tips);
    }
}
