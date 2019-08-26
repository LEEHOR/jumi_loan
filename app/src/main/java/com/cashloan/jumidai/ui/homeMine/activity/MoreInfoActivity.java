package com.cashloan.jumidai.ui.homeMine.activity;

import android.view.View;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.CreditMoreVM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditMoreRec;
import com.cashloan.jumidai.ui.homeMine.bean.submit.CreditMoreSub;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.OnOnceClickListener;
import com.commom.widget.editText.ClearEditText;
import com.github.mzule.activityrouter.annotation.Router;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/27
 * 描述： 更多信息
 */
@Router(value = RouterUrl.Mine_CreditMore, stringParams = BundleKeys.STATE)
public class MoreInfoActivity extends BaseMvpActivity {

    @ViewInject(R.id.cet_taobao)
    private ClearEditText cetTaobao;
    @ViewInject(R.id.cet_email)
    private ClearEditText cetEmail;
    @ViewInject(R.id.cet_qq)
    private ClearEditText cetQQ;
    @ViewInject(R.id.cet_wechat)
    private ClearEditText cetWechat;

    private CreditMoreVM moreVM;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more_info;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("我的更多信息");
        setTitleRight(Constant.NUMBER__1, "保存", new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                submit();
            }
        });
    }

    @Override
    protected void initFunc() {
        moreVM = new CreditMoreVM();
        req_data();
    }

    private void req_data() {
        Call<HttpResult<CreditMoreRec>> call = RDClient.getService(MineService.class).otherFindDetail();
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<CreditMoreRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<CreditMoreRec>> call,
                                  Response<HttpResult<CreditMoreRec>> response) {

                convert(response.body().getData());
            }
        });
    }

    private void convert(CreditMoreRec data) {
        if (data == null) return;
        moreVM.setEmail(data.getEmail());
        moreVM.setQq(data.getQq());
        moreVM.setTaobao(data.getTaobao());
        moreVM.setWechat(data.getWechat());
    }

    public CreditMoreVM getMoreVM() {
        return moreVM;
    }

    /** 更多信息 提交 */
    public void submit() {

        moreVM.setTaobao(cetTaobao.getText().toString());
        moreVM.setEmail(cetEmail.getText().toString());
        moreVM.setQq(cetQQ.getText().toString());
        moreVM.setWechat(cetWechat.getText().toString());

        CreditMoreSub moreSub = new CreditMoreSub(
                moreVM.getEmail(),
                moreVM.getQq(),
                moreVM.getTaobao(),
                moreVM.getWechat());

        Call<HttpResult> call = RDClient.getService(MineService.class).otherSaveOrUpdate(moreSub);
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                DialogUtils.showConfirmDialog(mContext, response.body().getMsg(),
                        new DialogUtils.btnConfirmClick() {
                            @Override
                            public void confirm() {
                                MoreInfoActivity.this.finish();
                            }
                        });
            }
        });

    }
}
