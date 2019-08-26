package com.cashloan.jumidai.ui.homeMine.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.DicKey;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.network.api.UserService;
import com.cashloan.jumidai.ui.homeMine.bean.CreditBankVM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditBankRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditPersonRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.DicRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.KeyValueRec;
import com.cashloan.jumidai.ui.homeMine.bean.submit.CreditBankSub;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.ContextHolder;
import com.commom.utils.ToastUtil;
import com.commom.widget.NoDoubleClickButton;
import com.commom.widget.TimeButton;
import com.commom.widget.editText.ClearEditText;
import com.github.mzule.activityrouter.annotation.Router;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/23
 * 描述： 绑定银行卡
 */
@Router(value = RouterUrl.Mine_CreditBank, stringParams = BundleKeys.TYPE)
public class BindBankCardActivity extends BaseMvpActivity {


    @ViewInject(R.id.tv_bind_tips)
    private TextView tvTips;

    @ViewInject(R.id.tv_card_title)
    private TextView tvCardTitle;

    @ViewInject(R.id.tv_card_owner)
    private TextView            tvOwner;
    @ViewInject(R.id.tv_cardBind_bankName)
    private TextView            tvBankname;
    @ViewInject(R.id.cet_card_num)
    private ClearEditText       cetCardNum;
    @ViewInject(R.id.cet_card_code)
    private ClearEditText       cetCode;
    @ViewInject(R.id.tb_card_bind)
    private TimeButton          timeButton;
    @ViewInject(R.id.ndb_card_bindNow)
    private NoDoubleClickButton btnSubmit;

    private List<CreditBankRec> bankList;
    public  CreditBankVM        viewModel;
    //银行列表
    private OptionsPickerView   bankPicker;
    private ArrayList<String> bank = new ArrayList<>();
    private DicRec dic;

    private String type;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_bank_card;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);
        setPageTitleBack("绑定银行卡");
    }

    @Override
    protected void initFunc() {

        type = getIntent().getStringExtra(BundleKeys.TYPE);
        viewModel = new CreditBankVM();

        if (Constant.STATUS_0.equals(type)) {
            viewModel.setAgain(false);
        } else {
            viewModel.setAgain(true);
        }

        tvTips.setText(viewModel.isAgain() ? mContext.getString(R.string.credit_bank_tips1)
                : mContext.getString(R.string.credit_bank_tips));

        btnSubmit.setText(viewModel.isAgain() ? mContext.getString(R.string.bank_bind_again)
                : mContext.getString(R.string.save));

        tvCardTitle.setText(viewModel.isAgain() ? mContext.getString(R.string.bank_bank_card)
                : mContext.getString(R.string.bank_card_no));


        attachClickListener(tvBankname);
        attachClickListener(timeButton);
        attachClickListener(btnSubmit);

        reqDic();
        reqData();
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cardBind_bankName:
                bankShow(view);
                break;

            case R.id.tb_card_bind:
                getCodeClick(view);
                break;
            case R.id.ndb_card_bindNow:
                submit(view);
                break;
            default:
                break;
        }
    }

    /**
     * 个人信息
     */
    private void reqData() {
        Call<HttpResult<CreditPersonRec>> callInit = RDClient.getService(MineService.class).getUserInfo();
        callInit.enqueue(new RequestCallBack<HttpResult<CreditPersonRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<CreditPersonRec>> call, Response<HttpResult<CreditPersonRec>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    viewModel.setName(response.body().getData().getRealName());

                    tvOwner.setText(viewModel.getName());
                }
            }
        });
    }

    /*** 获取验证码 ***/
    public void getCodeClick(View view) {
        viewModel.setBankName(tvBankname.getText().toString());
        viewModel.setCardNo(cetCardNum.getText().toString());


        // update  2018-7-8  新的绑卡
        if (TextUtils.isEmpty(viewModel.getBankName())) {
//            DialogUtils.showToastDialog(Util.getActivity(view), ContextHolder.getContext().getString(R.string.bank_select_bank_hint));
            DialogUtils.showConfirmDialog(mContext, mContext.getString(R.string.bank_select_bank_hint), null);
            return;
        }
        if (TextUtils.isEmpty(viewModel.getCardNo())) {
//            DialogUtils.showToastDialog(Util.getActivity(view), ContextHolder.getContext().getString(R.string.bank_card_no_hint));
            DialogUtils.showConfirmDialog(mContext, mContext.getString(R.string.bank_card_no_hint), null);
            return;
        }
        if (viewModel.getCardNo().length() < 16) {
//            DialogUtils.showToastDialog(Util.getActivity(view), ContextHolder.getContext().getString(R.string.bank_card_no_error));
            DialogUtils.showConfirmDialog(mContext, mContext.getString(R.string.bank_card_no_error), null);
            return;
        }


        CreditBankSub sub = new CreditBankSub();
        sub.setBank(viewModel.getBankName());
        sub.setCardNo(viewModel.getCardNo());

        Call<HttpResult<Object>> callCode = RDClient.getService(UserService.class).getBindCode(sub);
        callCode.enqueue(new RequestCallBack<HttpResult<Object>>() {
            @Override
            public void onSuccess(Call<HttpResult<Object>> call, Response<HttpResult<Object>> response) {
                if (response.code() == 200) {
                    timeButton.runTimer();
                    ToastUtil.toast(response.body().getMsg());
                }
            }
        });

    }

    /**
     * 初始化弹出框
     */
    private void init() {
        if (dic != null) {//数据字典获取内容
            List<KeyValueRec> temp;
            if (dic.getBankTypeList() != null) {
                temp = dic.getBankTypeList();
                for (int i = 0; i < temp.size(); i++) {
                    bank.add(temp.get(i).getValue());
                }
            }
        }
        bankPicker = new OptionsPickerView(mContext);
        bankPicker.setPicker(bank);
        bankPicker.setCyclic(false);
    }

    /**
     * 数据字典请求
     */
    private void reqDic() {
        Call<HttpResult<DicRec>> callInit = RDClient.getService(MineService.class).getDicts(DicKey.BANKTYPE);
        NetworkUtil.showCutscenes(callInit);
        callInit.enqueue(new RequestCallBack<HttpResult<DicRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<DicRec>> call, Response<HttpResult<DicRec>> response) {
                dic = response.body().getData();
                init();
            }
        });


    }

    /**
     * 银行卡展现
     */
    public void bankShow(View view) {
        if (dic != null && dic.getBankTypeList() != null) {
            Util.hideKeyBoard(view);
            bankPicker.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    viewModel.setBankName(dic.getBankTypeList().get(options1).getValue());

                    tvBankname.setText(viewModel.getBankName());
                }
            });
            bankPicker.show();
        } else {
            ToastUtil.toast(R.string.credit_no_dic);
        }
    }

    //提交绑卡信息
    public void submit(final View view) {
        viewModel.setBankName(tvBankname.getText().toString());
        viewModel.setCardNo(cetCardNum.getText().toString());
        viewModel.setCode(cetCode.getText().toString());


        if (TextUtils.isEmpty(viewModel.getBankName())) {
            DialogUtils.showToastDialog(Util.getActivity(view), ContextHolder.getContext().getString(R.string.bank_select_bank_hint));
            return;
        }
        if (TextUtils.isEmpty(viewModel.getCardNo())) {
            DialogUtils.showToastDialog(Util.getActivity(view), ContextHolder.getContext().getString(R.string.bank_card_no_hint));
            return;
        }
        if (viewModel.getCardNo().length() < 16) {
            DialogUtils.showToastDialog(Util.getActivity(view), ContextHolder.getContext().getString(R.string.bank_card_no_error));
            return;
        }

        if (TextUtils.isEmpty(viewModel.getCode())) {
            DialogUtils.showToastDialog(Util.getActivity(view),
                    ContextHolder.getContext().getString(R.string.bank_verify_code_hint));
            return;
        }


        Call<HttpResult<Object>> call = RDClient.getService(MineService.class).creditCardBindConfirm(
                viewModel.getBankName(), viewModel.getCardNo(), viewModel.getCode());
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<Object>>() {
            @Override
            public void onSuccess(Call<HttpResult<Object>> call, Response<HttpResult<Object>> response) {
                if (response.body().getCode() == 200) {
                    System.out.println("成功：" + response.body().getMsg());

                    ToastUtil.toast("" + response.body().getMsg());
                    setResult(RequestResultCode.RES_AGAIN_BIND);
                    finish();
                }
            }
        });
    }

}
