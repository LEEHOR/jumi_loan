package com.cashloan.jumidai.ui.homeMine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.CommonType;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.CommonService;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CommonRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditBankRec;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.utils.StringFormat;
import com.commom.widget.NoDoubleClickButton;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/23
 * 描述： 已绑定的银行卡
 */
@Router(value = RouterUrl.Mine_CreditBindBank, stringParams = "status")
public class BindedCardActivity extends BaseMvpActivity {

    @ViewInject(R.id.tv_bind_bankName)
    private TextView            tvBankName;
    @ViewInject(R.id.tv_bind_cardNumber)
    private TextView            tvBindCardNum;
    @ViewInject(R.id.tv_getBindCode)
    private TextView            tvBindCode;
    @ViewInject(R.id.tv_bind_reminds)
    private TextView            tvBindReminds;
    @ViewInject(R.id.ndb_bind_card)
    private NoDoubleClickButton btnBindCard;

    @ViewInject(R.id.ll_bind_phone)
    private LinearLayout llBindPhone;
    @ViewInject(R.id.tv_bind_phoneNum)
    private TextView     tvBindPhoneNum;

    private String cardStatus = "";


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_binded_card;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);
        setPageTitleBack(" 已绑定银行卡");
    }

    @Override
    protected void initFunc() {
        llBindPhone.setVisibility(View.GONE);
        tvBindCode.setVisibility(View.GONE);

        attachClickListener(tvBindCode);
        attachClickListener(btnBindCard);

        reqData();
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getBindCode:

                break;

            case R.id.ndb_bind_card:
                toBind(view);
                break;
            default:
                break;

        }
    }

    /**
     * 请求银行卡信息
     */
    private void reqData() {
        Call<HttpResult<CreditBankRec>> callInit = RDClient.getService(MineService.class).getBankCardList();
        NetworkUtil.showCutscenes(callInit);
        callInit.enqueue(new RequestCallBack<HttpResult<CreditBankRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<CreditBankRec>> call, Response<HttpResult<CreditBankRec>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    convert(response.body().getData());
                    btnBindCard.setEnabled(true);
                }
            }
        });
        Call<HttpResult<ListData<CommonRec>>> call = RDClient.getService(CommonService.class).remarkList();
        call.enqueue(new RequestCallBack<HttpResult<ListData<CommonRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<CommonRec>>> call, Response<HttpResult<ListData<CommonRec>>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    List<CommonRec> list = response.body().getData().getList();
                    showTips(list);
                }
            }
        });


    }

    /**
     * dataModel to mRegisterVM
     */
    private void convert(CreditBankRec rec) {
        tvBankName.setText(rec.getBank());
        tvBindCardNum.setText(StringFormat.bankcardHideFormat(rec.getCardNo()));

    }

    /**
     * dataModel to mRegisterVM
     */
    private void showTips(List<CommonRec> list) {
        for (int i = 0; i < list.size(); i++) {
            if (CommonType.BANKREMARK.equals(list.get(i).getCode())) {
                tvBindReminds.setText(list.get(i).getValue());
            }
        }
    }

    /**
     * 重新绑定
     */
    public void toBind(final View view) {
        Routers.openForResult(this,
                RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditBank, Constant.STATUS_1)),
                RequestResultCode.REQ_AGAIN_BIND);
    }

    /**
     * 测试银行卡
     */
    public void testCard(final View view) {
//        Call<HttpResult<Object>> callInit = RDClient.getService(MineService.class).testBindCard();
//        NetworkUtil.showCutscenes(callInit);
//        callInit.enqueue(new RequestCallBack<HttpResult<Object>>() {
//            @Override
//            public void onSuccess(Call<HttpResult<Object>> call,
//                                  Response<HttpResult<Object>> response) {
//                ToastUtil.toast("提交验证成功");
//
//                Util.getActivity(binding.getRoot()).finish();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestResultCode.REQ_AGAIN_BIND && resultCode == RequestResultCode.RES_AGAIN_BIND) {
            finish();
        }
    }
}
