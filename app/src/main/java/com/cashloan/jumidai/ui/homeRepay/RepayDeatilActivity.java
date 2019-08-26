package com.cashloan.jumidai.ui.homeRepay;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.RxBusEvent;
import com.cashloan.jumidai.network.api.LoanService;
import com.cashloan.jumidai.network.api.RepayService;
import com.cashloan.jumidai.ui.homeLoan.adapter.LoanProgressAdapter;
import com.cashloan.jumidai.ui.homeLoan.bean.AuthRepaySub;
import com.cashloan.jumidai.ui.homeLoan.bean.LoanProgressRec;
import com.cashloan.jumidai.ui.homeLoan.bean.LoanProgressVM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.BankRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CommonRec;
import com.cashloan.jumidai.ui.homeRepay.bean.JuheDataRec;
import com.cashloan.jumidai.ui.homeRepay.bean.NoBindCardQuickPayRec;
import com.cashloan.jumidai.ui.homeRepay.bean.RepayDetailsContentRec;
import com.cashloan.jumidai.ui.homeRepay.bean.RepayDetailsRec;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.cashloan.jumidai.views.RePayWaysPopupWindow;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.exception.ApiException;
import com.commom.net.RxBus.RxBus;
import com.commom.utils.ActivityManage;
import com.commom.utils.EmptyUtils;
import com.commom.utils.StringFormat;
import com.commom.utils.StringUtils;
import com.commom.utils.TextUtil;
import com.commom.utils.ToastUtil;
import com.commom.widget.NoDoubleClickButton;
import com.github.mzule.activityrouter.annotation.Router;

import org.apache.http.util.EncodingUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 作者： Ruby
 * 时间： 2018/8/23
 * 描述： 还款详情
 */
@Router(value = RouterUrl.Repay_Details, stringParams = {BundleKeys.ID, BundleKeys.TYPE})
public class RepayDeatilActivity extends BaseMvpActivity implements RePayWaysPopupWindow.OnItemClickListener {

    @ViewInject(R.id.rc_detail_progress)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_detail_amount)
    private TextView tvAmount;//借款金额
    @ViewInject(R.id.tv_detail_realAmount)
    private TextView tvRealAmount;//实际到账
    @ViewInject(R.id.tv_detail_fee)
    private TextView tvFee;//服务费
    @ViewInject(R.id.tv_detail_passFee)
    private TextView tvPassFee;//逾期费用
    @ViewInject(R.id.tv_detail_timeLimit)
    private TextView tvTimeLimit;//借款期限
    @ViewInject(R.id.tv_detail_applyTime)
    private TextView tvApplyTime;//申请日期
    @ViewInject(R.id.tv_detail_bankName)
    private TextView tvBankName;//收款银行

    @ViewInject(R.id.tv_detail_repayAmount)
    private TextView tvRepayAmount;//预计还款金额
    @ViewInject(R.id.tv_detail_repayTime)
    private TextView tvRepayTime;//预计还款日期

    @ViewInject(R.id.ndb_repay_extand)
    private NoDoubleClickButton btnExtand;//去续借
    @ViewInject(R.id.ndb_repay_go)
    private NoDoubleClickButton btnGoRepay;//去还款
    @ViewInject(R.id.webpay)
    private WebView webpay;
    @ViewInject(R.id.ll_repay_bottom)
    private LinearLayout llBottomBtns;

    private LoanProgressAdapter mProgressAdapter;
    private List<LoanProgressVM> mProgressVMList;

    private int repaywayflag = 1; //1、银行卡还款 2、支付宝
    private String userId = "";
    private String borrowId = "";
    private String bankName = "";
    private String bankNo = "";

    private String realapyamount = "0";
    private String extendAmount = "0";//续借手续费

    private RePayWaysPopupWindow popupWindow;
    java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");

    private String id;
    private String type;// 旧--判断按钮是否显示


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_repay_deatil;
    }

    @Override
    protected void initView() {
        setPageTitleBack("还款详情");
        AnnotateUtils.inject(this);
    }

    @Override
    protected void initFunc() {
        mProgressVMList = new ArrayList<>();
        mProgressAdapter = new LoanProgressAdapter(mContext, mProgressVMList);

        id = getIntent().getStringExtra(BundleKeys.ID);
        type = getIntent().getStringExtra(BundleKeys.TYPE);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mProgressAdapter);

        attachClickListener(btnExtand);
        attachClickListener(btnGoRepay);

        btnExtand.setVisibility(View.GONE);

        //还款记录-详情
        if (type.equals(Constant.STATUS_2)) {
            llBottomBtns.setVisibility(View.GONE);
        }

        req_data(id);
    }


    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ndb_repay_extand:
                applyToLoanClick(view);
                break;
            case R.id.ndb_repay_go:
                applyClick(view);
                break;
            default:
                break;
        }
    }

    private void req_data(String id) {
        Call<HttpResult<RepayDetailsRec>> detailsCall = RDClient.getService(RepayService.class).getRepayDetails(id);
        NetworkUtil.showCutscenes(detailsCall);
        detailsCall.enqueue(new RequestCallBack<HttpResult<RepayDetailsRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<RepayDetailsRec>> call, Response<HttpResult<RepayDetailsRec>> response) {
                RepayDetailsRec rec = response.body().getData();
                if (null != rec.getBorrow() && rec.getBorrow().size() > 0) {
                    convert(rec.getBorrow().get(0));
                }
                if (null != rec.getRepay() && rec.getRepay().size() > 0) {
                    userId = rec.getRepay().get(0).getUserId();
                    borrowId = rec.getRepay().get(0).getBorrowId();

                    realapyamount = rec.getRepay().get(0).getAmount();
//                    vm.setRepayAmount(StringFormat.doubleFormat(rec.getRepay().get(0).getAmount()) + "元");
//                    vm.setRepayTimeStr(rec.getRepay().get(0).getRepayTimeStr());

                    //预计还款金额
                    tvRepayAmount.setText(StringFormat.doubleFormat(rec.getRepay().get(0).getAmount()) + "元");
                    //预计还款时间
                    tvRepayTime.setText(rec.getRepay().get(0).getRepayTimeStr());

                    if (Constant.STATUS_10.equals(rec.getRepay().get(0).getState())) {
//                        vm.setRealRepayAmount(StringFormat.doubleFormat(rec.getRepay().get(0).getRealRepayAmount()) + "元");
//                        vm.setRealRepayTime(rec.getRepay().get(0).getRealRepayTime());

                        //预计还款金额
                        tvRepayAmount.setText(StringFormat.doubleFormat(rec.getRepay().get(0).getAmount()) + "元");
                        //预计还款时间
                        tvRepayTime.setText(rec.getRepay().get(0).getRepayTimeStr());
                    }
                }

                extendAmount = rec.getExtensionAmount();
                updateProgress(rec.getList());
            }
        });


//        Call<HttpResult<ListData<CommonRec>>> call = RDClient.getService(CommonService.class).h5List();
//        NetworkUtil.showCutscenes(call);
//        call.enqueue(new RequestCallBack<HttpResult<ListData<CommonRec>>>() {
//            @Override
//            public void onSuccess(Call<HttpResult<ListData<CommonRec>>> call, Response<HttpResult<ListData<CommonRec>>> response) {
//                convertH5List(response.body().getData().getList());
//            }
//        });
    }

    private void convert(RepayDetailsContentRec repayDetailsVM) {
        if (!TextUtil.isEmpty(repayDetailsVM.getCardNo()) && repayDetailsVM.getCardNo().length() > 15) {
            bankName = repayDetailsVM.getBank();
            bankNo = repayDetailsVM.getCardNo().substring(repayDetailsVM.getCardNo().length() - 4, repayDetailsVM
                    .getCardNo().length());
            String bank = repayDetailsVM.getBank() + "(" + repayDetailsVM.getCardNo().substring(repayDetailsVM.getCardNo().length() - 4, repayDetailsVM
                    .getCardNo().length()) + ")";
            //收款银行
            tvBankName.setText(StringUtils.nullToStr(bank));
        }
        //借款金额
        tvAmount.setText(StringUtils.nullToStr(StringFormat.doubleFormat(repayDetailsVM.getAmount()) + "元"));
        //实际到账
        tvRealAmount.setText(StringFormat.doubleFormat(repayDetailsVM.getRealAmount()) + "元");
        //服务费
        tvFee.setText(StringFormat.doubleFormat(repayDetailsVM.getFee()) + "元");
        //逾期费用
        tvPassFee.setText(StringFormat.doubleFormat(repayDetailsVM.getPenaltyAmount()) + "元");
        //借款期限
        tvTimeLimit.setText(repayDetailsVM.getTimeLimit() + "天");
        //申请日期
        tvApplyTime.setText(StringUtils.nullToStr(repayDetailsVM.getCreditTimeStr()));


//        vm.setAmount(StringFormat.doubleFormat(repayDetailsVM.getAmount()) + "元");
//        vm.setCardNo(repayDetailsVM.getCardNo());
//        vm.setCreateTime(repayDetailsVM.getCreateTime());
//        vm.setFee(StringFormat.doubleFormat(repayDetailsVM.getFee()) + "元");
//        vm.setRealAmount(StringFormat.doubleFormat(repayDetailsVM.getRealAmount()) + "元");
//        vm.setTimeLimit(repayDetailsVM.getTimeLimit() + "天");
//        vm.setCreditTimeStr(repayDetailsVM.getCreditTimeStr());
//        vm.setPassFeeStr(StringFormat.doubleFormat(repayDetailsVM.getPenaltyAmount()) + "元");
//        if (Constant.STATUS_10.equals(repayDetailsVM.getPenalty())) {
//            isPass.set(View.VISIBLE);
//        }

    }

    private void convertH5List(List<CommonRec> list) {
        if (list == null || list.size() == 0) {
            return;
        }
//        for (CommonRec rec : list) {
//            if (CommonType.REPAY_TYPE.equals(rec.getCode())) {
//                typeVm.set(rec);
//                helpVm.set(rec);
//            } else if (CommonType.REPAY_TYPE.equals(rec.getCode())) {
//                helpVm.set(rec);
//            }
//        }
    }

    private void updateProgress(List<LoanProgressRec> list) {
        if (list != null && list.size() > 0) {
            List<LoanProgressVM> vms = new ArrayList<>();
            if (list.size() == 1) {
                LoanProgressVM vm = new LoanProgressVM();
                LoanProgressRec rec = list.get(0);
                vm.setLoanTime(rec.getCreateTime());
                userId = rec.getUserId();
                borrowId = rec.getBorrowId();

                vm.setRemark(rec.getRemark());
                vm.setRepayTime(rec.getRepayTime());
                vm.setType(rec.getType());
                if (10 == vm.getType()) {
                    vm.setFirst(true);
                }
                vm.setState(rec.getState());
                vm.setEnd(true);
                vms.add(vm);
            } else {
                for (int i = 0; i < list.size(); i++) {

                    LoanProgressVM vm = new LoanProgressVM();
                    vm.setLoanTime(list.get(i).getCreateTime());

                    vm.setRemark(list.get(i).getRemark());
                    vm.setRepayTime(list.get(i).getRepayTime());
                    vm.setType(list.get(i).getType());
                    vm.setState(list.get(i).getState());
                    userId = list.get(0).getUserId();
                    borrowId = list.get(0).getBorrowId();
                    if (i == 0) {

                        vm.setFirst(true);
                        if (Constant.NUMBER_10 == vm.getType() || Constant.NUMBER_20 == vm.getType()) {
                            vm.setGrey_1(false);
                        }

                    }
                    if ((i == list.size() - 1) && i != 0) {
                        vm.setEnd(true);
                    }
                    vms.add(vm);
                }
            }
            mProgressVMList.addAll(vms);
            mProgressAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 去续借
     */
    public void applyToLoanClick(final View view) {
        orderPayType = PAY_TYPE_XU_JIE;

        popupWindow = new RePayWaysPopupWindow(ActivityManage.peek(), bankName + " 尾号" + bankNo, userId, borrowId, extendAmount);
        popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        BackgroudAlpha((float) 0.5);
        popupWindow.setOnItemClickListener(this);

    }

    private int orderPayType;//  1--续借   2--还款
    private final int PAY_TYPE_HUAN_KUAN = 100;
    private final int PAY_TYPE_XU_JIE = 200;

    /**
     * 马上申请--还款
     */
    public void applyClick(final View view) {
        orderPayType = PAY_TYPE_HUAN_KUAN;

        popupWindow = new RePayWaysPopupWindow(ActivityManage.peek(), bankName + " 尾号" + bankNo, userId, borrowId, realapyamount);
        popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        BackgroudAlpha((float) 0.5);
        popupWindow.setOnItemClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setOnItemClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                popupWindow.dismiss();
                BackgroudAlpha((float) 1.0);
                break;
            case R.id.ll_repay_bank:
                repaywayflag = 1;
                popupWindow.mImgBankcard.setBackground(ActivityManage.peek().getResources().getDrawable(R.drawable.shape_click));
                popupWindow.mImgAliPay.setBackground(ActivityManage.peek().getResources().getDrawable(R.drawable.shape));
                popupWindow.mImgCardBind.setBackground(ActivityManage.peek().getResources().getDrawable(R.drawable.shape));

                if (orderPayType == PAY_TYPE_XU_JIE) {
                    String str = "确认支付" + String.valueOf(df.format(Double.parseDouble(extendAmount)));
                    popupWindow.ndcRepay.setText(str);
                } else if (orderPayType == PAY_TYPE_HUAN_KUAN) {
                    String str = "确认支付" + String.valueOf(df.format(Double.parseDouble(realapyamount)));
                    popupWindow.ndcRepay.setText(str);
                }

                break;
            case R.id.ll_repay_aliPay:
                repaywayflag = 2;
                popupWindow.mImgBankcard.setBackground(ActivityManage.peek().getResources().getDrawable(R.drawable.shape));
                popupWindow.mImgAliPay.setBackground(ActivityManage.peek().getResources().getDrawable(R.drawable.shape_click));
                popupWindow.mImgCardBind.setBackground(ActivityManage.peek().getResources().getDrawable(R.drawable.shape));

                if (orderPayType == PAY_TYPE_XU_JIE) {
                    String str = "确认支付" + String.valueOf(df.format(Double.parseDouble(extendAmount)));
                    popupWindow.ndcRepay.setText(str);
                } else if (orderPayType == PAY_TYPE_HUAN_KUAN) {
                    String str = "确认支付" + String.valueOf(df.format(Double.parseDouble(realapyamount)));
                    popupWindow.ndcRepay.setText(str);
                }
                break;

            //绑定银行卡支付
            case R.id.ll_repay_bindBank:

                repaywayflag = 3;
                popupWindow.mImgAliPay.setBackground(ActivityManage.peek().getResources().getDrawable(R.drawable.shape));
                popupWindow.mImgBankcard.setBackground(ActivityManage.peek().getResources().getDrawable(R.drawable.shape));
                popupWindow.mImgCardBind.setBackground(ActivityManage.peek().getResources().getDrawable(R.drawable.shape_click));

                if (orderPayType == PAY_TYPE_XU_JIE) {
                    String str = "确认支付" + String.valueOf(df.format(Double.parseDouble(extendAmount)));
                    popupWindow.ndcRepay.setText(str);
                } else if (orderPayType == PAY_TYPE_HUAN_KUAN) {
                    String str = "确认支付" + String.valueOf(df.format(Double.parseDouble(realapyamount)));
                    popupWindow.ndcRepay.setText(str);
                }


                break;
            case R.id.ndc_repay:
                //连连代扣
                if (repaywayflag == 1) canPay();
                    //支付宝还款
                else if (repaywayflag == 2) canPay();
                    //绑定银行卡支付
                else if (repaywayflag == 3) canPay();

                popupWindow.dismiss();
                BackgroudAlpha(1.0f);
                break;
        }
    }


    /**
     * 判断是否能代扣还款
     */
    private void canPay() {
        Call<HttpResult<BankRec>> call = RDClient.getService(LoanService.class).payState();
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<BankRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<BankRec>> call, Response<HttpResult<BankRec>> response) {
                //判断是否能够还款

                String payStatus = response.body().getData().getState();
                System.out.println("代扣返回的结果：" + payStatus);

                if ("0".equals(payStatus)) {
                    showMessageTips();
                } else if ("1".equals(payStatus)) {
                    //  update 2018-7-11   暂时不能使用银行卡、支付宝   新的支付方式

                    if (repaywayflag == 1) {
                        //能够还款/续借，且支付方式选择为银行卡
                        newPay();

                    } else if (repaywayflag == 2) {

                        //能够还款/续借，且支付方式选择为支付宝
                        if (checkAliPayInstalled(ActivityManage.peek())) {
                            UseJuhePay();
                        } else {
                            ToastUtil.toast("请先安装支付宝应用!");
                        }

                    } else if (repaywayflag == 3) {
                        // 未绑定银行卡支付处理
                        useNoBindCardPay();
                    }
                }
            }
        });
    }

    //弹窗提示框
    private void showMessageTips() {
        DialogUtils.showConfirmDialog(mContext, "您有订单正在处理",
                new DialogUtils.btnConfirmClick() {
                    @Override
                    public void confirm() {
                        finish();
                    }
                });
    }

    //弹窗提示框
    private void showFailedMessage(String failMsg) {
        DialogUtils.showConfirmDialog(mContext, failMsg, () -> finish());
    }


    //调用接口执行代扣还款计划
    private void newPay() {
        AuthRepaySub sub = new AuthRepaySub();
        sub.setUserId(userId);
        sub.setBorrowId(borrowId);

        // 点击续借代扣
        if (orderPayType == PAY_TYPE_XU_JIE) {
            sub.setState("20");  //10--还款  20--展期

            // update  2018-8-8  统一银行卡支付的 展期(续借)  还款支付接口
            Call<HttpResult<Object>> call = RDClient.getService(LoanService.class).getUnionBankCardPay(sub);
            NetworkUtil.showCutscenes(call);
            call.enqueue(new RequestCallBack<HttpResult<Object>>() {
                @Override
                public void onSuccess(Call<HttpResult<Object>> call, Response<HttpResult<Object>> response) {
                    String reqData = response.body().toString();
                    System.out.println("续借代扣返回的结果：" + reqData);
                    RxBus.get().send(RxBusEvent.REPAY_SUCCESS);

                    finish();
                }

                @Override
                public void onFailure(Call<HttpResult<Object>> call, Throwable t) {
                    super.onFailure(call, t);

                    String failMsg = ((ApiException) t).getResult().getMsg();
                    if (EmptyUtils.isNotEmpty(failMsg)) {
                        showFailedMessage(failMsg);
                    }
                }
            });

            //点击还款代扣
        } else if (orderPayType == PAY_TYPE_HUAN_KUAN) {
            sub.setState("10");  //10--还款  20--展期

            // update  2018-8-8  统一银行卡支付的 展期(续借)  还款支付接口
            Call<HttpResult<Object>> call = RDClient.getService(LoanService.class).getUnionBankCardPay(sub);
            NetworkUtil.showCutscenes(call);
            call.enqueue(new RequestCallBack<HttpResult<Object>>() {

                @Override
                public void onSuccess(Call<HttpResult<Object>> call, Response<HttpResult<Object>> response) {

                    String reqData = response.body().toString();
                    System.out.println("还款代扣返回的结果：" + reqData);
                    RxBus.get().send(RxBusEvent.REPAY_SUCCESS);

                    finish();
                }

                @Override
                public void onFailure(Call<HttpResult<Object>> call, Throwable t) {
                    super.onFailure(call, t);

                    String failMsg = ((ApiException) t).getResult().getMsg();
                    if (EmptyUtils.isNotEmpty(failMsg)) {
                        showFailedMessage(failMsg);
                    }
                }
            });
        }
    }


    /**
     * 支付宝转账功能可用时，调用聚合支付功能
     */
    private void UseJuhePay() {
        // new 聚合支付
        AuthRepaySub sub = new AuthRepaySub();
        sub.setBorrowId(borrowId);

        // 点击续借--支付宝支付
        if (orderPayType == PAY_TYPE_XU_JIE) {
            sub.setState("20");  //10--还款  20--展期

            //  update  2018-7-13  修改了新的调用支付聚合接口
//            Call<HttpResult<JuheDataRec>> call = RDClient.getService(LoanService.class).getRenewPrePay(sub);

            // update  2018-8-8  统一支付宝支付的 展期(续借)  还款支付接口
            Call<HttpResult<JuheDataRec>> call = RDClient.getService(LoanService.class).getUnionWapPrePay(sub);
            NetworkUtil.showCutscenes(call);
            call.enqueue(new RequestCallBack<HttpResult<JuheDataRec>>() {
                @Override
                public void onSuccess(Call<HttpResult<JuheDataRec>> call,
                                      Response<HttpResult<JuheDataRec>> response) {

                    JuheDataRec rec = response.body().getData();

                    String aliPayLink = rec.getPicCode();
                    if (!"".equals(aliPayLink) && null != aliPayLink) {
                        toH5Pay("1", aliPayLink);
                    } else {
                        showDialog("支付失败，请稍后重试");
                    }
                }
            });
        }

        // 点击还款--支付宝支付
        else if (orderPayType == PAY_TYPE_HUAN_KUAN) {
            sub.setState("10"); //10--还款  20--展期

            // update  2018-8-8  统一支付宝支付的 展期(续借)  还款支付接口
            Call<HttpResult<JuheDataRec>> call = RDClient.getService(LoanService.class).getUnionWapPrePay(sub);
            NetworkUtil.showCutscenes(call);
            call.enqueue(new RequestCallBack<HttpResult<JuheDataRec>>() {
                @Override
                public void onSuccess(Call<HttpResult<JuheDataRec>> call,
                                      Response<HttpResult<JuheDataRec>> response) {

                    JuheDataRec rec = response.body().getData();
                    String aliPayLink = rec.getPicCode();
                    if (!"".equals(aliPayLink) && null != aliPayLink) {
                        toH5Pay("1", aliPayLink);
                    } else {
                        showDialog("支付失败，请稍后重试");
                    }
                }
            });
        }
    }

    //使用未绑定银行卡支付
    private void useNoBindCardPay() {
        // new 聚合支付
        AuthRepaySub sub = new AuthRepaySub();
        sub.setBorrowId(borrowId);

        // 点击续借--未绑定银行卡支付
        if (orderPayType == PAY_TYPE_XU_JIE) {
            sub.setState("20");  //10--还款  20--展期

            // update  2018-10-10   统一未绑定银行卡支付的 展期(续借)  支付接口
            Call<HttpResult<NoBindCardQuickPayRec>> call = RDClient.getService(LoanService.class).getNoBindCardPay(sub);
            NetworkUtil.showCutscenes(call);
            call.enqueue(new RequestCallBack<HttpResult<NoBindCardQuickPayRec>>() {
                @Override
                public void onSuccess(Call<HttpResult<NoBindCardQuickPayRec>> call,
                                      Response<HttpResult<NoBindCardQuickPayRec>> response) {

                    NoBindCardQuickPayRec rec = response.body().getData();

                    String params = rec.getParams();
                    String url = rec.getRequestUrl();

                    if (!"".equals(url) && null != url
                            && !"".equals(params) && null != params) {
                        toHuiChaoQuickPay(params, url);
                    } else {
//                        showDialog("支付失败，请稍后重试");
                    }
                }
            });
        }

        // 点击还款--未绑定银行卡支付
        else if (orderPayType == PAY_TYPE_HUAN_KUAN) {
            sub.setState("10"); //10--还款  20--展期

            // update  2018-10-10  统一未绑定银行卡支付的  还款支付接口
            Call<HttpResult<NoBindCardQuickPayRec>> call = RDClient.getService(LoanService.class).getNoBindCardPay(sub);
            NetworkUtil.showCutscenes(call);
            call.enqueue(new RequestCallBack<HttpResult<NoBindCardQuickPayRec>>() {
                @Override
                public void onSuccess(Call<HttpResult<NoBindCardQuickPayRec>> call,
                                      Response<HttpResult<NoBindCardQuickPayRec>> response) {

                    NoBindCardQuickPayRec rec = response.body().getData();

                    String params = rec.getParams();
                    String url = rec.getRequestUrl();

                    if (!"".equals(url) && null != url
                            && !"".equals(params) && null != params) {
                        toHuiChaoQuickPay(params, url);
                    } else {
//                        showDialog("支付失败，请稍后重试");
                    }
                }
            });
        }
    }


    public void showDialog(String hint) {
        DialogUtils.showToastDialog(mContext, hint);
    }


    private void toHuiChaoQuickPay(String postData, String jumpUrl) {
        webpay.setVisibility(View.VISIBLE);
        WebSettings webSettings = webpay.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        // 设置默认编码
        webSettings.setDefaultTextEncodingName("UTF-8"); //
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);

        final ProgressDialog progressDialog = new ProgressDialog(mContext);//1.创建一个ProgressDialog的实例
        progressDialog.setMessage("正在加载...");//3.设置显示内容
        progressDialog.setCancelable(true);//3.设置可否用back键关闭对话框

        webpay.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (null != progressDialog) {
                    progressDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (null != progressDialog) {
                    progressDialog.dismiss();
                }
            }
        });

//        System.out.println("---PostData=" + postData);
        webpay.postUrl(jumpUrl, EncodingUtils.getBytes(postData, "UTF-8"));
    }

    private void toH5Pay(String prepayId, String jumpUrl) {
        //  new 聚合支付  update 2018-7-8
        webpay.setVisibility(View.VISIBLE);
        WebSettings webSettings = webpay.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        // 设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8"); //
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);

        webpay.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //方法一
                if (url == null) return false;
                try {
                    if (url.startsWith("alipays://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        ActivityManage.peek().startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash
                    return false;
                }
                //处理http和https开头的url
                view.loadUrl(url);
                return true;

//               //方法二
//                Intent intent;
//                try {
//                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                    intent.setComponent(null);
//                    ActivityManage.peek().startActivity(intent);
//                    //finish();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                return true;
            }
        });
//        binding.webpay.setWebChromeClient(new WebChromeClient());
        webpay.loadUrl(jumpUrl);
    }


    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    //设置屏幕背景透明度
    private void BackgroudAlpha(float alpha) {
        WindowManager.LayoutParams l = ActivityManage.peek().getWindow().getAttributes();
        l.alpha = alpha;
        ActivityManage.peek().getWindow().setAttributes(l);
    }

}
