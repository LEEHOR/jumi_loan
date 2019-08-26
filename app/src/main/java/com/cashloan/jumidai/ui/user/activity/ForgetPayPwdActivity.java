package com.cashloan.jumidai.ui.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.ChineseUtil;
import com.commom.utils.ContextHolder;
import com.commom.utils.EmptyUtils;
import com.commom.utils.ToastUtil;
import com.commom.utils.encryption.MDUtil;
import com.commom.widget.NoDoubleClickButton;
import com.commom.widget.TimeButton;
import com.commom.widget.editText.ClearEditText;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.CommonType;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.network.api.UserService;
import com.cashloan.jumidai.ui.homeMine.bean.recive.PassRec;
import com.cashloan.jumidai.ui.user.bean.ForgotPayVM;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.ui.user.bean.receive.ProbeSmsRec;
import com.cashloan.jumidai.ui.user.bean.submit.ForgotPaySub;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/29
 * 描述： 忘记支付密码
 */

@Router(RouterUrl.UserInfoManage_ForgotPayPwd)
public class ForgetPayPwdActivity extends BaseMvpActivity {

    @ViewInject(R.id.tv_payPwd_phone)
    private TextView            tvPhone;
    @ViewInject(R.id.cet_payPwd_name)
    private ClearEditText       cetName;
    @ViewInject(R.id.cet_payPwd_idCardNo)
    private ClearEditText       cetIdNo;
    @ViewInject(R.id.cet_payPwd_code)
    private ClearEditText       cetCode;
    @ViewInject(R.id.tb_payPwd_code)
    private TimeButton          timeButton;
    @ViewInject(R.id.ndb_payPwd_next)
    private NoDoubleClickButton btnNext;


    public ForgotPayVM forgotPayVM;
    public int payType = 0;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pay_pwd;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("");
    }

    @Override
    protected void initFunc() {
        forgotPayVM = new ForgotPayVM();
        String phone = SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUsername();
        forgotPayVM.setPhone(phone);

        tvPhone.setText(forgotPayVM.getPhone());

        attachClickListener(timeButton);
        attachClickListener(btnNext);

        cetName.addTextChangedListener(mTextWatcher);
        cetIdNo.addTextChangedListener(mTextWatcher);
        cetCode.addTextChangedListener(mTextWatcher);
    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (EmptyUtils.isNotEmpty(cetName.getText().toString())
                    && EmptyUtils.isNotEmpty(cetIdNo.getText().toString())
                    && EmptyUtils.isNotEmpty(cetCode.getText().toString())) {
                btnNext.setEnabled(true);
            } else {
                btnNext.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            this.finish();
        }
    }


    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tb_payPwd_code:
                getCodeClick(view);
                break;
            case R.id.ndb_payPwd_next:
                nextClick(view);
                break;
            default:
                break;
        }
    }

    public void nextClick(final View v) {
        forgotPayVM.setName(cetName.getText().toString());
        forgotPayVM.setNo(cetIdNo.getText().toString());
        forgotPayVM.setCode(cetCode.getText().toString());


        if (TextUtils.isEmpty(forgotPayVM.getName())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.settings_forgot_pay_name_tip));
            return;
        }
        if (!ChineseUtil.isChineseName(forgotPayVM.getName())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.settings_forgot_pay_name_not_chinese));
            return;
        }
        if (TextUtils.isEmpty(forgotPayVM.getNo())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.settings_forgot_pay_no_tip));
            return;
        }
        if (TextUtils.isEmpty(forgotPayVM.getCode())) {
            ToastUtil.toast(R.string.settings_forgot_pay_code_tip);
            return;
        }
        ForgotPaySub sub = new ForgotPaySub();
        sub.setIdNo(forgotPayVM.getNo());
        sub.setRealName(forgotPayVM.getName());
        sub.setVcode(forgotPayVM.getCode());
        Call<HttpResult<PassRec>> call = RDClient.getService(MineService.class).validateUser(sub);
        call.enqueue(new RequestCallBack<HttpResult<PassRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<PassRec>> call, Response<HttpResult<PassRec>> response) {
                if (response.body().getData().isPass()) {

                    Routers.open(v.getContext(), RouterUrl.getRouterUrl
                            (String.format(RouterUrl.Mine_Settings_Pay_Pwd, Constant.NUMBER_3)));
                    Activity activity = Util.getActivity(v);
                    activity.setResult(RequestResultCode.RES_FORGOT_PAY);
                    activity.finish();
                } else {
                    ToastUtil.toast(response.body().getMsg());
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    public void getCodeClick(View view) {
        String sign = MDUtil.encode(MDUtil.TYPE.MD5, AppConfig.APP_KEY + forgotPayVM.getPhone() + CommonType.PAY_CODE);
        Call<HttpResult<ProbeSmsRec>> callCode = RDClient.getService(UserService.class).getCode(forgotPayVM.getPhone(), CommonType.PAY_CODE, sign);
        callCode.enqueue(new RequestCallBack<HttpResult<ProbeSmsRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<ProbeSmsRec>> call, Response<HttpResult<ProbeSmsRec>> response) {
                if (!Constant.STATUS_10.equals(response.body().getData().getState())) {
                    ToastUtil.toast(response.body().getData().getMessage());
                } else {
                    timeButton.runTimer();
                    ToastUtil.toast(response.body().getMsg());
                }
            }
        });
    }
}
