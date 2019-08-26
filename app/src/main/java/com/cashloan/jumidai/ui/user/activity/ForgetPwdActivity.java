package com.cashloan.jumidai.ui.user.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.CommonType;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.UserService;
import com.cashloan.jumidai.ui.user.bean.ForgotVM;
import com.cashloan.jumidai.ui.user.bean.receive.ProbeSmsRec;
import com.cashloan.jumidai.ui.user.bean.submit.ForgotSub;
import com.cashloan.jumidai.ui.user.bean.submit.ValidateCodeSub;
import com.cashloan.jumidai.utils.InputCheck;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.EmptyUtils;
import com.commom.utils.RegularUtil;
import com.commom.utils.ToastUtil;
import com.commom.utils.encryption.MDUtil;
import com.commom.widget.NoDoubleClickButton;
import com.commom.widget.TimeButton;
import com.commom.widget.editText.ClearEditText;
import com.github.mzule.activityrouter.annotation.Router;

import retrofit2.Call;
import retrofit2.Response;

import static com.commom.utils.encryption.MDUtil.encode;


/**
 * 作者： Ruby
 * 时间： 2018/8/29
 * 描述： 忘记密码
 */
@Router(value = RouterUrl.UserInfoManage_ForgotPwd, stringParams = {BundleKeys.ID, BundleKeys.TYPE})
public class ForgetPwdActivity extends BaseMvpActivity {


    @ViewInject(R.id.phone_edit)
    private ClearEditText cetPhone;
    @ViewInject(R.id.cet_forget_code)
    private ClearEditText cetCode;
    @ViewInject(R.id.cet_forget_new)
    private ClearEditText cetPwdNew;
    @ViewInject(R.id.cet_forget_new_confirm)
    private ClearEditText cetPwdConfrim;

    @ViewInject(R.id.ll_forget_step1)
    private LinearLayout        llStepOne;
    @ViewInject(R.id.ll_forget_step2)
    private LinearLayout        llStepTwo;
    @ViewInject(R.id.timeButton)
    private TimeButton          mTimeButton;
    @ViewInject(R.id.noDoubleClickButton)
    private NoDoubleClickButton btnNext;
    @ViewInject(R.id.ndb_forget_submit)
    private NoDoubleClickButton btnSubmit;

    public ForgotVM forgotVM;
    public boolean isExit = true;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);
        setPageTitleBack(mContext.getString(R.string.forgot_pwd_title_step_1));
    }

    @Override
    protected void initFunc() {
        llStepTwo.setVisibility(View.GONE);

        String phone = getIntent().getStringExtra(BundleKeys.ID);
        String type = getIntent().getStringExtra(BundleKeys.TYPE);
        if (Constant.STATUS_1.equals(type)) {
            cetPhone.setKeyListener(null);
        }

        forgotVM = new ForgotVM();
        forgotVM.setPhone(phone);
        forgotVM.setTitle(mContext.getString(R.string.forgot_pwd_title_step_1));

        if (EmptyUtils.isNotEmpty(phone) && !"null".equals(phone)) {
            cetPhone.setText(phone);
        } else {
            cetPhone.setText("");
            cetPhone.setEnabled(true);
        }

        setPageTitle(forgotVM.getTitle());

        attachClickListener(mTimeButton);
        attachClickListener(btnNext);
        attachClickListener(btnSubmit);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.timeButton:
                getCodeClick(view);
                break;
            case R.id.noDoubleClickButton:
                nextClick(view);
                break;
            case R.id.ndb_forget_submit:
                updateClick(view);
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    public void getCodeClick(View view) {

        forgotVM.setPhone(cetPhone.getText().toString());

        if (TextUtils.isEmpty(forgotVM.getPhone())) {
            ToastUtil.toast(mContext.getString(R.string.input) + mContext.getString(R.string.forgot_phone_hint_step_1));
            return;
        }
        if (!RegularUtil.isPhone(forgotVM.getPhone())) {
            ToastUtil.toast(mContext.getString(R.string.forgot_phone_hint_step_1_error));
            return;
        }
        String sign = encode(MDUtil.TYPE.MD5, AppConfig.APP_KEY + forgotVM.getPhone() + CommonType.FORGOT_CODE);

        Call<HttpResult<ProbeSmsRec>> callCode = RDClient.getService(UserService.class).getCode(forgotVM.getPhone(), CommonType.FORGOT_CODE, sign);
        NetworkUtil.showCutscenes(callCode);
        callCode.enqueue(new RequestCallBack<HttpResult<ProbeSmsRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<ProbeSmsRec>> call, Response<HttpResult<ProbeSmsRec>> response) {
                if (!Constant.STATUS_10.equals(response.body().getData().getState())) {
                    ToastUtil.toast(response.body().getData().getMessage());
                } else {
                    mTimeButton.runTimer();
                    ToastUtil.toast(response.body().getMsg());
                }
            }
        });
    }

    /**
     * 下一步点击
     */
    public void nextClick(final View view) {
        String input = mContext.getString(R.string.input);

        forgotVM.setPhone(cetPhone.getText().toString());
        forgotVM.setCode(cetCode.getText().toString());


        if (TextUtils.isEmpty(forgotVM.getPhone())) {
            ToastUtil.toast(input + mContext.getString(R.string.forgot_phone_hint_step_1));
            return;
        }
        if (!RegularUtil.isPhone(forgotVM.getPhone())) {
            ToastUtil.toast(mContext.getString(R.string.forgot_phone_hint_step_1_error));
            return;
        }
        if (TextUtils.isEmpty(forgotVM.getCode())) {
            ToastUtil.toast(input + mContext.getString(R.string.forgot_pwd_code_step_1));
            return;
        }
        ValidateCodeSub sub = new ValidateCodeSub();
        sub.setPhone(forgotVM.getPhone());
        sub.setVcode(forgotVM.getCode());
        sub.setType(CommonType.FORGOT_CODE);
        sub.setSignMsg(encode(MDUtil.TYPE.MD5, AppConfig.APP_KEY + forgotVM.getPhone() + CommonType.FORGOT_CODE + forgotVM.getCode()));
        Call<HttpResult<ProbeSmsRec>> call = RDClient.getService(UserService.class).checkCode(sub);
        call.enqueue(new RequestCallBack<HttpResult<ProbeSmsRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<ProbeSmsRec>> call, Response<HttpResult<ProbeSmsRec>> response) {
                if (!Constant.STATUS_10.equals(response.body().getData().getState())) {
                    ToastUtil.toast(response.body().getData().getMessage());
                } else {
                    forgotVM.setIsOne(View.GONE);
                    forgotVM.setIsTwo(View.VISIBLE);
                    forgotVM.setTitle(view.getContext().getResources().getString(R.string.forgot_pwd_title_step_2));
                    isExit = false;

                    llStepOne.setVisibility(View.GONE);
                    llStepTwo.setVisibility(View.VISIBLE);
                    setPageTitle(mContext.getResources().getString(R.string.forgot_pwd_title_step_2));
                }
            }
        });
    }

    /**
     * 确认修改
     */
    public void updateClick(final View view) {
        String input = mContext.getString(R.string.input);

        forgotVM.setPwd(cetPwdNew.getText().toString());
        forgotVM.setConfirmPwd(cetPwdConfrim.getText().toString());

        if (TextUtils.isEmpty(forgotVM.getPwd())) {
            ToastUtil.toast(mContext.getString(R.string.forgot_pwd_new_hint_step_2));
            return;
        }
        if (TextUtils.isEmpty(forgotVM.getConfirmPwd())) {
            ToastUtil.toast(input + mContext.getString(R.string.forgot_pwd_confirm_hint_step_2));
            return;
        }
        if (!forgotVM.getConfirmPwd().equals(forgotVM.getPwd())) {
            ToastUtil.toast(R.string.pwd_no_confirm);
            return;
        }
        if (!InputCheck.checkPwd(forgotVM.getPwd()) || !InputCheck.checkPwd(forgotVM.getConfirmPwd())) {
            ToastUtil.toast(mContext.getString(R.string.settings_pwd_desc));
            return;
        }
        System.out.println("AppConfig.APP_KEY + forgotVM.getPwd() + forgotVM.getPhone() + forgotVM.getCode()"
                + AppConfig.APP_KEY + encode(MDUtil.TYPE.MD5, forgotVM.getPwd()).toUpperCase() + forgotVM.getPhone() + forgotVM.getCode());

        String signMsg = encode(MDUtil.TYPE.MD5, AppConfig.APP_KEY + encode(MDUtil.TYPE.MD5, forgotVM.getPwd()).toUpperCase() + forgotVM
                .getPhone() + forgotVM.getCode());
        ForgotSub sub = new ForgotSub();
        sub.setSignMsg(signMsg);
        sub.setMobile(forgotVM.getPhone());
        sub.setPassword(forgotVM.getPwd());
        sub.setVerifyCode(forgotVM.getCode());
        sub.setConfirmPassword(forgotVM.getConfirmPwd());
        Call<HttpResult> call = RDClient.getService(UserService.class).forgetPwd(sub);
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {

                DialogUtils.showConfirmDialog(mContext, "密码修改成功!",
                        new DialogUtils.btnConfirmClick() {
                            @Override
                            public void confirm() {
                                Util.getActivity(view).setResult(RequestResultCode.RES_FORGOT);
                                Util.getActivity(view).finish();
                            }
                        });
            }
        });
    }
}
