package com.cashloan.jumidai.ui.homeMine.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.ActivityManage;
import com.commom.utils.ContextHolder;
import com.commom.utils.EmptyUtils;
import com.commom.utils.ToastUtil;
import com.commom.widget.NoDoubleClickTextView;
import com.commom.widget.editText.ClearEditText;
import com.github.mzule.activityrouter.annotation.Router;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.SettingsUpdatePwdVM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.InfoRec;
import com.cashloan.jumidai.ui.homeMine.bean.submit.UpdatePwdSub;
import com.cashloan.jumidai.utils.DisplayFormat;
import com.cashloan.jumidai.utils.InputCheck;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/28
 * 描述： 修改登录密码
 */
@Router(value = RouterUrl.Mine_Settings_Update)
public class UpdatePwdActivity extends BaseMvpActivity {

    @ViewInject(R.id.tv_update_phone)
    private TextView              tvPhone;
    @ViewInject(R.id.cet_old_pwd)
    private ClearEditText         cetOldPwd;
    @ViewInject(R.id.cet_new_pwd)
    private ClearEditText         cetNewPwd;
    @ViewInject(R.id.cet_confirm_newPwd)
    private ClearEditText         cetConfirmPwd;
    @ViewInject(R.id.ndt_submit)
    private NoDoubleClickTextView tvSubmit;

    private SettingsUpdatePwdVM pwdVM;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_pwd;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);
        setPageTitleBack("修改登录密码");
    }

    @Override
    protected void initFunc() {
        attachClickListener(tvSubmit);
        tvSubmit.setEnabled(false);

        cetOldPwd.addTextChangedListener(mTextWatcher);
        cetNewPwd.addTextChangedListener(mTextWatcher);
        cetConfirmPwd.addTextChangedListener(mTextWatcher);

        pwdVM = new SettingsUpdatePwdVM();
        reqinit();
    }

    @Override
    protected void onViewClicked(View view) {
        submit(view);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (EmptyUtils.isNotEmpty(cetOldPwd.getText().toString())
                    && EmptyUtils.isNotEmpty(cetNewPwd.getText().toString())
                    && EmptyUtils.isNotEmpty(cetConfirmPwd.getText().toString())) {
                tvSubmit.setEnabled(true);
            } else {
                tvSubmit.setEnabled(false);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestResultCode.REQ_FORGOT && resultCode == RequestResultCode.RES_FORGOT) {
            this.finish();
        }
    }


    /** 请求个人信息 */
    private void reqinit() {
        Call<HttpResult<InfoRec>> call = RDClient.getService(MineService.class).getInfo();
        call.enqueue(new RequestCallBack<HttpResult<InfoRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<InfoRec>> call, Response<HttpResult<InfoRec>> response) {
                if (null != response.body()) {
                    pwdVM.setPhone(DisplayFormat.accountHideFormat(response.body().getData().getPhone()));

                    tvPhone.setText(pwdVM.getPhone());
                }
            }
        });
    }

    /** 修改密码 */
    public void submit(View view) {
        pwdVM.setPwd(cetOldPwd.getText().toString().trim());
        pwdVM.setNewPwd(cetNewPwd.getText().toString().trim());
        pwdVM.setConfirmPwd(cetConfirmPwd.getText().toString().trim());


        if (!pwdVM.getConfirmPwd().equals(pwdVM.getNewPwd())) {
            ToastUtil.toast(R.string.seetings_pwd_tips);
            return;
        }
        if (!InputCheck.checkPwd(pwdVM.getPwd()) || !InputCheck.checkPwd(pwdVM.getNewPwd()) || !InputCheck.checkPwd(pwdVM.getConfirmPwd())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.settings_pwd_desc));
            return;
        }
        Call<HttpResult> call = RDClient.getService(MineService.class).updatePwd(new UpdatePwdSub(pwdVM.getNewPwd(), pwdVM.getPwd()));
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                ToastUtil.toast(response.body().getMsg());
                ActivityManage.pop();
            }
        });
    }

    public SettingsUpdatePwdVM getPwdVM() {
        return pwdVM;
    }

}
