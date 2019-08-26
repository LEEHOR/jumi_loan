package com.cashloan.jumidai.ui.homeMine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.CreditPhoneVM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditUrlRec;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.ContextHolder;
import com.commom.widget.NoDoubleClickButton;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/27
 * 描述： 手机运营商
 */
@Router(value = RouterUrl.Mine_CreditPhone, stringParams = BundleKeys.STATE)
public class PhoneOperatorActivity extends BaseMvpActivity {

    @ViewInject(R.id.iv_phoneVerify_status)
    private ImageView           ivStatus;
    @ViewInject(R.id.tv_phoneVerify_tips)
    private TextView            tvTips;
    @ViewInject(R.id.ndb_verify_phone)
    private NoDoubleClickButton btnSubmit;

    private String        state;
    private CreditPhoneVM viewModel;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_operator;
    }

    @Override
    protected void initView() {

        AnnotateUtils.inject(this);
        setPageTitleBack("手机运营商");
    }

    @Override
    protected void initFunc() {
        state = getIntent().getStringExtra(BundleKeys.STATE);

        attachClickListener(btnSubmit);

        viewModel = new CreditPhoneVM();
        viewModel.setState(state);

        tvTips.setText(viewModel.getTips());
        btnSubmit.setText(viewModel.getBtnStr());
        btnSubmit.setEnabled(viewModel.isEnable());

        ivStatus.setImageResource(Constant.STATUS_30.equals(state) ? R.drawable.icon_phone_verify_ok : R.drawable.icon_phone_credit);
    }

    @Override
    protected void onViewClicked(View view) {
        submit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestResultCode.REQ_PHONE && resultCode == RESULT_OK) {
            DialogUtils.showConfirmDialog(mContext, mContext.getString(R.string.phone_crediting_tips),
                    new DialogUtils.btnConfirmClick() {
                        @Override
                        public void confirm() {
                            finish();
                        }
                    });
        }
    }

    /** 请求认证接口 */
    public void submit() {
        if (Constant.STATUS_30.equals(viewModel.getState()) || Constant.STATUS_20.equals(viewModel.getState())) {
            return;
        } else {
            Call<HttpResult<CreditUrlRec>> call = RDClient.getService(MineService.class).operatorCredit();
            NetworkUtil.showCutscenes(call);
            call.enqueue(new RequestCallBack<HttpResult<CreditUrlRec>>() {
                @Override
                public void onSuccess(Call<HttpResult<CreditUrlRec>> call, Response<HttpResult<CreditUrlRec>> response) {
                    if (response.body() != null && response.body().getData() != null) {
                        SharedInfo.getInstance().saveValue(BundleKeys.URL, response.body().getData().getUrl());
                        Routers.openForResult(PhoneOperatorActivity.this,
                                RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_WebView,
                                        ContextHolder.getContext().getString(R.string.credit_phone_title), "", "")),
                                RequestResultCode.REQ_PHONE);
                    }
                }
            });
        }
    }
}
