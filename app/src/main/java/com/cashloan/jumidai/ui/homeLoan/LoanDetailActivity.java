package com.cashloan.jumidai.ui.homeLoan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.cashloan.jumidai.MyApplication;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.CommonType;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.RxBusEvent;
import com.cashloan.jumidai.network.api.CommonService;
import com.cashloan.jumidai.network.api.LoanService;
import com.cashloan.jumidai.ui.homeLoan.bean.LoanSub;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CommonRec;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.utils.DeviceUtil;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.getImei;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.cashloan.jumidai.views.InputPwdPopView;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.net.OkHttp.exception.ApiException;
import com.commom.net.RxBus.RxBus;
import com.commom.utils.ActivityManage;
import com.commom.utils.ConverterUtil;
import com.commom.utils.EmptyUtils;
import com.commom.utils.StringFormat;
import com.commom.utils.StringUtils;
import com.commom.utils.TextUtil;
import com.commom.utils.ToastUtil;
import com.commom.utils.getAppContentResolver;
import com.commom.widget.NoDoubleClickTextView;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/22
 * 描述： 借款详情
 */

@Router(value = RouterUrl.Loan_Details, stringParams = {BundleKeys.LOANMONEY, BundleKeys.LOANLIMIT,
        BundleKeys.REALMONEY, BundleKeys.FEE, BundleKeys.CARDNAME, BundleKeys.CARDNO, BundleKeys.CARDID})
public class LoanDetailActivity extends BaseMvpActivity {

    @ViewInject(R.id.tv_loan_amount)
    private TextView tvLoanMoney;//借款金额
    @ViewInject(R.id.tv_loan_period)
    private TextView tvDayLimit;//借款期限
    @ViewInject(R.id.tv_service_amount)
    private TextView tvServiceFee;//服务费用
    @ViewInject(R.id.tv_real_amount)
    private TextView tvRealAmount;//实际到账
    @ViewInject(R.id.tv_bank_card)
    private TextView tvBankName;//收款银行
    @ViewInject(R.id.tv_bank_card_num)
    private TextView tvCardNumber;//收款卡号

    @ViewInject(R.id.apply)
    private NoDoubleClickTextView tvApply;

    @ViewInject(R.id.ll_agreement)
    private LinearLayout llAgreement;

    @ViewInject(R.id.ck_agreement)
    private CheckBox mCheckBox;

    @ViewInject(R.id.tv_agreement_name)
    private TextView tvAgreement;// 协议名称

    private String loanMoney, loanLimit, realMoney, serviceFee, bankName, cardNumber, cardId;
    private String repayMoney;

    public InputPwdPopView popView;
    /*****交易密码*****/
    private EditText pwd;
    private ExecutorService singleThreadExecutor;
    private String passwordStr;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loan_detail;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("借款申请");
    }

    @Override
    protected void initFunc() {
        mCheckBox.setChecked(true);
//        attachClickListener(mCheckBox);
        attachClickListener(tvAgreement);
        attachClickListener(tvApply);

        loanMoney = getIntent().getStringExtra(BundleKeys.LOANMONEY);
        loanLimit = getIntent().getStringExtra(BundleKeys.LOANLIMIT);
        realMoney = getIntent().getStringExtra(BundleKeys.REALMONEY);
        serviceFee = getIntent().getStringExtra(BundleKeys.FEE);
        bankName = getIntent().getStringExtra(BundleKeys.CARDNAME);
        cardNumber = getIntent().getStringExtra(BundleKeys.CARDNO);
        cardId = getIntent().getStringExtra(BundleKeys.CARDID);


        tvLoanMoney.setText(StringUtils.nullToStr(loanMoney));
        tvDayLimit.setText(StringUtils.nullToStr(loanLimit));
        tvRealAmount.setText(StringUtils.nullToStr(realMoney));
      /*  if (loanMoney != null) {
            BigDecimal bigDecimal = new BigDecimal(loanMoney);
            BigDecimal bigDecimal2 = new BigDecimal(1.02);
            BigDecimal multiply = bigDecimal.multiply(bigDecimal2);
            BigDecimal bigDecimal_result = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
            tvServiceFee.setText(StringUtils.nullToStr(bigDecimal_result.toString()));
        } else {
            tvServiceFee.setText(StringUtils.nullToStr(realMoney));
        }*/
      //  Log.d("还钱", StringUtils.nullToStr(serviceFee) + "/" + loanMoney + "/" + realMoney);
         tvServiceFee.setText(StringUtils.nullToStr(serviceFee));
        tvBankName.setText(StringUtils.nullToStr(bankName));
        tvCardNumber.setText(StringFormat.bankcardFormat(cardNumber));


        double repay = (ConverterUtil.getDouble(realMoney) + ConverterUtil.getDouble(serviceFee)) / 1.0;
      //  System.out.println("repay" + repay);
       // System.out.println("repay" + (ConverterUtil.getDouble(realMoney) + ConverterUtil.getDouble(serviceFee)));
        this.repayMoney = String.valueOf(repay);


        initInputEdit();

        getAgreementName();
        getImeI();
    }

    /**
     * 获取设备指纹、唯一识别码、手机品牌
     */
    private void getImeI() {
        getImei getImei = new getImei(this);
        Constant.IMEI = getImei.getimei();
        Constant.PHONEBRAND = DeviceUtil.getPhoneBrand();
        Constant.PHONEMODEL = DeviceUtil.getPhoneModel();
      //  System.out.println("ImeI2" + Constant.IMEI + "/" + Constant.IMSI + "/" + Constant.ANDROID_ID + "/" + Constant.PHONEBRAND + Constant.PHONEMODEL);
    }


    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.ck_agreement:
//                break;
            case R.id.tv_agreement_name:
                protocolClick(view);
                break;
            case R.id.apply:
                if (mCheckBox.isChecked()) {
                    confirmApplyClick(view);
                } else {
                    ToastUtil.toast("您还没有同意借款协议");
                }
                break;
            default:
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    DialogUtils.showSureCancelDialog(mContext, mContext.getString(R.string.loan_pwd_error),
                            mContext.getString(R.string.dialog_confirm),
                            mContext.getString(R.string.loan_find_pwd),
                            new DialogUtils.btnCancelClick() {
                                @Override
                                public void cancel() {
                                    pwd.setText("");
                                    confirmApplyClick(tvApply);
                                }
                            }, new DialogUtils.btnConfirmClick() {
                                @Override
                                public void confirm() {
                                    pwd.setText("");
                                    Routers.open(mContext, RouterUrl.getRouterUrl(RouterUrl.UserInfoManage_ForgotPayPwd));
                                }
                            });
                    break;
                case 1:
                    applyBorrow(passwordStr);
                    break;
                case 2:
                    ToastUtil.toast("风控评估失败");
                    break;
            }

        }
    };


    /**
     * 密码输入框初始化信息点击事件
     */
    private void initInputEdit() {
        popView = new InputPwdPopView(mContext,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pwd.setText("");
                        Routers.open(mContext, RouterUrl.getRouterUrl(RouterUrl.UserInfoManage_ForgotPayPwd));
                        popView.dismiss();
                        ActivityManage.pop();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popView.dismiss();
                pwd.setText("");
            }
        }, mTextWatcher);
        pwd = (EditText) popView.getContentView().findViewById(R.id.pwd);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtil.isEmpty(s.toString()) && s.toString().length() == 6) {
                passwordStr = s.toString();
                popView.dismiss();
                pwd.setText("");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("password", passwordStr);
                MobclickAgent.onEvent(mContext, "pwdinfo", map);
//                TCAgent.onEvent(mContext, "借款交易密码校验", "vervityPass", map);
                isCanBorrow(passwordStr);
            }
        }
    };

    /***获取协议****/
    private void getAgreementName() {
        Call<HttpResult<ListData<CommonRec>>> call = RDClient.getService(CommonService.class).protocolList();
        call.enqueue(new RequestCallBack<HttpResult<ListData<CommonRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<CommonRec>>> call, Response<HttpResult<ListData<CommonRec>>> response) {
                convertData(response.body().getData().getList());
            }
        });

    }

    private CommonRec rec;
    private String protocol;

    private void convertData(List<CommonRec> list) {
        for (int i = 0; i < list.size(); i++) {
            if ("protocol_borrow".equals(list.get(i).getCode())) {
                rec = list.get(i);
                protocol = "《" + rec.getName() + "》";
            }
        }
        if (EmptyUtils.isNotEmpty(rec.getName())) {
            tvAgreement.setText(protocol);
        }
    }

    /***借款协议***/
    public void protocolClick(View view) {
        if (rec != null) {
            Routers.open(view.getContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_WebView,
                    rec.getName(), CommonType.getUrl(rec.getValue()), "")));
        }
    }


    private String address;
    private String coordinate;

    /**
     * 确认申请
     */
    public void confirmApplyClick(final View view) {
        // 判断定位信息
        if (TextUtil.isEmpty(address) || TextUtil.isEmpty(coordinate)) {
            NetworkUtil.showCutscenes("", "");
            MyApplication.openGps(new MyApplication.OnPosChanged() {
                @Override
                public void changed(AMapLocation location) {
                    address = location.getAddress();
                    coordinate = (location.getLongitude() + "," + location.getLatitude());
                    NetworkUtil.dismissCutscenes();
                }
            }, true);
            return;
        }

        if (!AndPermission.hasPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            AndPermission.with(mContext)
                    .runtime()
                    .permission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})
//                    .onGranted(permissions -> {
////                        Toast.makeText(SplashActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
//                    })
//                    .onDenied(permissions -> {
//                        //用户拒绝了开启权限,且选择了不再询问
//                        if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
//                            // 弹出前往设置的提示
//
//                        } else {//用户本次拒绝了开启权限
//
//                        }
//                        DialogUtils.showPermisssionDialog(mContext);
//                    })
                    .start();

            return;
        }


        // 展示支付密码弹窗
        popView.showAtLocation(view, Gravity.CENTER, 0, -100);
        Util.showKeyboard(view.getContext());
    }


    /**
     * 验证密码
     *
     * @param passwordStr
     */
    private void isCanBorrow(final String passwordStr) {
        NetworkUtil.showCutscenes("", "",false,false);
        Call<HttpResult> call = RDClient.getService(LoanService.class).vevifyTradePwd(new LoanSub(passwordStr));

        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("response", response.body().toString());
                map.put("userId", SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId());
                map.put("userName", SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUsername());

                MobclickAgent.onEvent(mContext, "passconfirm", map);

                if (response.body().getCode() == 401) {
                    handler.sendEmptyMessage(0);

                } else if (response.body().getCode() == 200) {

                    //执行多线程
                    FixedTreadPool();
                }

            }
        });

    }


    /**
     * 真正请求借款接口
     */
    private void applyBorrow(String passwordStr) {
        LoanSub reqLoanSub = new LoanSub(loanMoney,
                cardId,
                serviceFee,
                realMoney,
                loanLimit,
                passwordStr,
                address,
                coordinate,
                Constant.IMEI,
                Constant.PHONEBRAND + "-" + Constant.PHONEMODEL,
                "Android"
        );

        Call<HttpResult> call = RDClient.getService(LoanService.class).getLoanApply(reqLoanSub);
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                NetworkUtil.dismissCutscenes(false);
                HashMap<String, String> mapUmeng = new HashMap<String, String>();
                mapUmeng.put("userId", SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId());
                mapUmeng.put("userName", SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUsername());
                mapUmeng.put("response", response.body().toString());
                MobclickAgent.onEvent(mContext, "saveSuccess", mapUmeng);

                if (response.body().getCode() == 500 || response.body().getCode() == 400) {//  被拒绝的订单跳转到网页

                    Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_WebView,
                            "",
                            AppConfig.LOAN_FAILED_LINK_URL,//new 链接 2018-7-10
                            "")));
                } else {
                    //  200 --  成功
                    // 10-审核中 20-自动审核成功  21自动审核不通过  22自动审核未决待人工复审
                    DialogUtils.showConfirmDialog(mContext, response.body().getMsg(),
                            new DialogUtils.btnConfirmClick() {
                                @Override
                                public void confirm() {
                                    RxBus.get().send(RxBusEvent.APPLY_LOAN_SUCCESS);
                                    finish();
                                }
                            });
                }


            }

            @Override
            public void onFailure(Call<HttpResult> call, Throwable t) {
                super.onFailure(call, t);
                //  Log.i("HttpResult", String.valueOf(((ApiException) t).getResult()));
                NetworkUtil.dismissCutscenes(false);
                HashMap<String, String> mapUmeng = new HashMap<String, String>();
                mapUmeng.put("userId", SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId());
                mapUmeng.put("userName", SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUsername());
                mapUmeng.put("result", String.valueOf(((ApiException) t).getResult()));
//                TCAgent.onEvent(mContext, "借款失败", "saveError", mapUmeng);
                MobclickAgent.onEvent(mContext, "saveError", mapUmeng);
                if (((ApiException) t).getResult().getCode() == 400
                        || ((ApiException) t).getResult().getCode() == 500) {
                    Routers.open(mContext, RouterUrl.getRouterUrl(
                            String.format(
                                    RouterUrl.AppCommon_WebView,
                                    "",
                                    AppConfig.LOAN_FAILED_LINK_URL,
//                                    "http://h5.yongqianbei.com/other.html?userId=" + SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId(),
                                    "")));
                } else {
                    DialogUtils.showConfirmDialog(mContext, ((ApiException) t).getResult().getMsg() + "",
                            new DialogUtils.btnConfirmClick() {
                                @Override
                                public void confirm() {
                                    finish();
                                }
                            });
                }
            }
        });

    }

    /**
     * 、获取App列表
     */
    private Runnable getAppList() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String allApps = getAppContentResolver.getAllApps(LoanDetailActivity.this);
                String userId = SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId();

                if (userId != null) {
                    Call<HttpResult> call = RDClient.getService(LoanService.class).upLoadAppList(Long.parseLong(userId), allApps);
                    call.enqueue(new RequestCallBack<HttpResult>() {
                        @Override
                        public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("response", response.body().toString());
                            map.put("userId", SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId());
                            map.put("userName", SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUsername());
                            MobclickAgent.onEvent(mContext, "uploadAppList", map);
                            if (response.body().getCode() == 200) {
                                //applyBorrow(passwordStr);
                                handler.sendEmptyMessage(1);
                            } else if (response.body().getCode() == 400) {
                                //ToastUtil.toast("风控评估失败");
                                handler.sendEmptyMessage(2);
                            }
                        }
                    });
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        };
        return runnable;

    }

    /**
     * 通话记录
     */
    private Runnable getCallRecord() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String callLogs = getAppContentResolver.getCallLogs(LoanDetailActivity.this);
                String userId = SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId();
                if (userId != null) {
                    Call<HttpResult> call = RDClient.getService(LoanService.class).userCallRecord(Long.valueOf(userId), callLogs);
                    call.enqueue(new RequestCallBack<HttpResult>() {
                        @Override
                        public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {

                        }
                    });
                }

            }
        };
        return runnable;
    }

    /**
     * 短信
     */
    private Runnable getuserSmsList() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String smsInPhone = getAppContentResolver.getSmsInPhone(LoanDetailActivity.this);
                String userId = SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId();
                if (userId != null) {
                    Call<HttpResult> call = RDClient.getService(LoanService.class).userSmsList(Long.valueOf(userId), smsInPhone);
                    call.enqueue(new RequestCallBack<HttpResult>() {
                        @Override
                        public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {

                        }
                    });
                }
            }
        };

        return runnable;
    }

    /**
     * 初始化一个固定线程
     */
    private void FixedTreadPool() {
        if (singleThreadExecutor == null) {
            singleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        singleThreadExecutor.execute(getuserSmsList());
        singleThreadExecutor.execute(getCallRecord());
        singleThreadExecutor.execute(getAppList());

    }
}
