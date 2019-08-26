package com.cashloan.jumidai.ui.homeLoan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cashloan.jumidai.MyApplication;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.RxBusEvent;
import com.cashloan.jumidai.network.api.ContentResolverService;
import com.cashloan.jumidai.network.api.LoanService;
import com.cashloan.jumidai.ui.homeLoan.Impl.HomeFragPresenterImp;
import com.cashloan.jumidai.ui.homeLoan.adapter.LoanProgressAdapter;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeChoiceRec;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeFeeDetailRec;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeRec;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeVM;
import com.cashloan.jumidai.ui.homeLoan.bean.LoanProgressRec;
import com.cashloan.jumidai.ui.homeLoan.bean.LoanProgressVM;
import com.cashloan.jumidai.ui.homeLoan.bean.NoticeRec;
import com.cashloan.jumidai.ui.homeMine.bean.PhoneUtil;
import com.cashloan.jumidai.ui.main.MainActivity;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.utils.ConvertUtil;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.cashloan.jumidai.views.spinnerwheel.WheelVerticalView;
import com.cashloan.jumidai.views.spinnerwheel.adapters.ListWheelAdapter;
import com.commom.base.BaseMvpFragment;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.net.OkHttp.entity.ResultAppList;
import com.commom.net.RxBus.RxBus;
import com.commom.net.RxBus.RxBusSubscribe;
import com.commom.utils.ContextHolder;
import com.commom.utils.ConverterUtil;
import com.commom.utils.EmptyUtils;
import com.commom.utils.ScreenUtils;
import com.commom.utils.StringFormat;
import com.commom.utils.StringUtils;
import com.commom.utils.TextUtil;
import com.commom.utils.getAppContentResolver;
import com.commom.widget.HomeSeekBar;
import com.commom.widget.NoDoubleClickButton;
import com.github.mzule.activityrouter.router.Routers;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述： 借款首页   15005810784
 */
public class HomeFrag extends BaseMvpFragment {
    private static final String TAG = "HomeFrag";

    @ViewInject(R.id.swipe_home)
    private SwipeRefreshLayout mRefreshLayout;
    @ViewInject(R.id.wheel_view)
    private WheelVerticalView mVerticalView;//垂直广告
    @ViewInject(R.id.tv_home_verify)
    private TextView tvVerifyNums;//认证个数
    @ViewInject(R.id.tv_home_can_loan_limit)
    private TextView tvCanLoanLimit;//头部信用额度---旧可借额度
    @ViewInject(R.id.tv_loan_limit_money)
    private TextView tvLoanLimitMoney;//借款金额
    @ViewInject(R.id.tv_loan_limit_day)
    private TextView tvLoanLimit;//借款期限
    @ViewInject(R.id.tv_loan_money)
    private TextView tvLoanMoney;//到账金额
    @ViewInject(R.id.tv_loan_service_fee)
    private TextView tvServiceFee;//服务费

    @ViewInject(R.id.iv_home_costDaialog)
    private ImageView ivCostDialog;

    /***能够借款页面 默认***/
    @ViewInject(R.id.ll_home_can_borrow)
    private LinearLayout llStatusCanLoan;


    @ViewInject(R.id.hsb_selected_money)
    private HomeSeekBar hsbSelectedMoney;
    @ViewInject(R.id.hsb_selected_day)
    private HomeSeekBar hsbSelectedDay;
    @ViewInject(R.id.tv_money)
    private TextView tvMoney;//顶部滑动金额文字
    @ViewInject(R.id.tv_day)
    private TextView tvDay;//滑动天数文字
    @ViewInject(R.id.tv_start_money)
    private TextView tvMoneyMin;//最小金额    yuan(StringFormat.subZeroAndDot(viewCtrl.homeVM.minCredit))
    @ViewInject(R.id.tv_end_money)
    private TextView tvMoneyMax;//最大金额    android:text="@{@string/yuan(StringFormat.subZeroAndDot(viewCtrl.homeVM.maxCredit))}"
    @ViewInject(R.id.tv_start_day)
    private TextView tvDayMin;//最小天数      android:text="@{@string/days(viewCtrl.homeVM.minDays)}"
    @ViewInject(R.id.tv_end_day)
    private TextView tvDayMax;//最大天数      android:text="@{@string/days(viewCtrl.homeVM.maxDays)}"

    private int width = 0;

    @ViewInject(R.id.ndb_loan_now)
    private NoDoubleClickButton btnLoanNow;//马上申请

    /***已经申请借款***/
    @ViewInject(R.id.rc_loan_progress)
    private RecyclerView mRecyclerView;

    /***借款成功 待还款***/
    @ViewInject(R.id.ll_home_go_repay)
    private LinearLayout llToRepay;
    @ViewInject(R.id.tv_repay_money)
    private TextView tvRepayMoney;
    @ViewInject(R.id.tv_repay_dayLeft)
    private TextView tvRepayDayLeft;
    @ViewInject(R.id.tv_apply_time)
    private TextView tvApply;
    @ViewInject(R.id.tv_repay_time)
    private TextView tvRepay;
    @ViewInject(R.id.tv_apply_timeShow)
    private TextView tvApplyTime;
    @ViewInject(R.id.tv_repay_timeShow)
    private TextView tvRepayTime;
    @ViewInject(R.id.ndb_go_repay)
    private NoDoubleClickButton btnGoRepay;//去还款
    @ViewInject(R.id.ndb_go_extend)
    private NoDoubleClickButton btnGoExtend;//去展期


    private LoanProgressAdapter mProgressAdapter;
    private List<LoanProgressVM> mProgressVMList;

    //借款浮动天数
    public String loanDay;
    //借款浮动额度
    public String loanMoney = "0.00";
    //实际借款金额
    public String realMoney = "0.00";
    //服务费用
    public String serviceMoney = "0.00";

    /**
     * 计算金额
     */
    private String calculateMoney = "";
    /**
     * 计算金额
     */
    private String calculateDay = "7";
    /**
     * 计算费率
     */
    private String calculateRate = "";
    /**
     * 借款标识
     */
    public final int LOAN = 0x0111;
    private HomeFeeDetailRec rec;
    private ExecutorService singleThreadExecutor;


    public HomeFrag() {
        // Required empty public constructor
    }

    public static HomeFrag newInstance() {
        HomeFrag fragment = new HomeFrag();
        return fragment;
    }

    @Override
    protected void setStatusBar() {
        // 不处理状态栏
    }

    @Override
    protected HomeFragPresenterImp createPresenter() {
        return null;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        //添加注解
        AnnotateUtils.inject(this, view);

        RxBus.get().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
    }

    @Override
    protected void initFunc() {
        width = ScreenUtils.getScreenWidth(mContext);
        homeVM = new HomeVM();
        mProgressVMList = new ArrayList<>();
        mProgressAdapter = new LoanProgressAdapter(mContext, mProgressVMList);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mProgressAdapter);

        attachClickListener(tvVerifyNums);
        attachClickListener(btnLoanNow);
        attachClickListener(btnGoRepay);
        attachClickListener(btnGoExtend);
        attachClickListener(ivCostDialog);

        btnGoExtend.setVisibility(View.GONE);

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_color_principal));
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(false);
            reqHomeData();
            reqNotice();
            //getuserSmsList();

        });


        llStatusCanLoan.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        llToRepay.setVisibility(View.GONE);

        initListener();

        reqHomeData();
        reqNotice();


        mVerticalView.setCyclic(true);
        mVerticalView.setOnTouchListener((v, event) -> true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


    private void initListener() {
        hsbSelectedMoney.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        if (homeRec == null && homeRec.getCreditList() == null && homeRec.getCreditList().size() == 0)
                            return;

                        int stopProgress = progress;

                        String money = "";

                        if (homeRec.getCreditList().size() == 1) {
                            //下面到账金额的改变
                            money = homeRec.getCreditList().get(0);
                            tvMoney.setText(money + "元");

                        } else {
                            int size = homeRec.getCreditList().size();
                            //等分判断的单位
                            float divideUnit = 100 / ((size - 1) * 2);

                            for (int i = 0; i < size; i++) {
                                if ((i * 2 - 1) * divideUnit <= progress && progress < (i * 2 + 1) * divideUnit) {
                                    if (i == 0) {
                                        money = homeRec.getCreditList().get(0);
                                        tvMoney.setText(money + "元");
                                    } else if (i == size - 1) {
                                        money = homeRec.getCreditList().get(size - 1);
                                        tvMoney.setText(money + "元");
                                    } else {
                                        money = homeRec.getCreditList().get(i);
                                        tvMoney.setText(money + "元");
                                    }
                                }
                            }
                        }
                        //下面到账金额的改变
                        calculateMoney = money;
                        loanMoney = money + "元";

                        //设置文字左padding
                        TextPaint paint = tvMoney.getPaint();
                        float textLength = paint.measureText(money + "元");
                        //文字所在行的左右边距
                        int lineMargin = ConvertUtil.dip2px(ContextHolder.getContext(), 70);
                        // 显示文字的最大左padding
                        int textMaxPaddingLeft = (int) (width - textLength - lineMargin);

                        int paddingLeft = (int) (textMaxPaddingLeft * (stopProgress * 0.01));
                        if (paddingLeft > textMaxPaddingLeft) {
                            paddingLeft = textMaxPaddingLeft;
                        }
                        tvMoney.setPadding(paddingLeft, 0, 0, 0);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // 停止滑动时获取费率
                        calculateRate();
                    }
                }

        );

        hsbSelectedDay.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        if (homeRec == null && homeRec.getDayList() == null && homeRec.getDayList().size() == 0)
                            return;

                        int stopProgress = progress;
                        String day = "";

                        if (homeRec.getDayList().size() == 1) {
                            //下面到账天数的改变
                            day = homeRec.getDayList().get(0);
                            tvDay.setText(day + "天");
                            Log.d(TAG, "onProgressChanged1: " + day);

                        } else {
                            int size = homeRec.getDayList().size();
                            //等分判断的单位
                            float divideUnit = 100 / ((size - 1) * 2);

                            for (int i = 0; i < size; i++) {
                                if ((i * 2 - 1) * divideUnit <= progress && progress < (i * 2 + 1) * divideUnit) {
                                    if (i == 0) {
                                        day = homeRec.getDayList().get(0);
                                        Log.d(TAG, "onProgressChanged3: " + day);
                                        tvDay.setText(day + "天");
                                    } else if (i == size - 1) {
                                        day = homeRec.getDayList().get(size - 1);
                                        tvDay.setText(day + "天");
                                        Log.d(TAG, "onProgressChanged4: " + day);
                                    } else {
                                        day = homeRec.getDayList().get(i);
                                        tvDay.setText(day + "天");
                                        Log.d(TAG, "onProgressChanged5: " + day);
                                    }
                                }
                            }
                        }

                        calculateDay = day;
                        loanDay = day + "天";
                        Log.d(TAG, "onProgressChanged2: " + loanDay + "/" + calculateDay);
                        //设置文字左padding
                        TextPaint paint = tvDay.getPaint();
                        float textLength = paint.measureText(day + "天");
                        //文字所在行的左右边距
                        int lineMargin = ConvertUtil.dip2px(ContextHolder.getContext(), 70);
                        // 显示文字的最大左padding
                        int textMaxPaddingLeft = (int) (width - textLength - lineMargin);

                        int paddingLeft = (int) (textMaxPaddingLeft * (stopProgress * 0.01));
                        if (paddingLeft > textMaxPaddingLeft) {
                            paddingLeft = textMaxPaddingLeft;
                        }
                        tvDay.setPadding(paddingLeft, 0, 0, 0);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // 停止滑动时获取费率
                        calculateRate();
                    }
                }

        );
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mVerticalView.setCurrentItem(mVerticalView.getCurrentItem() + 1, true);
            } else if (msg.what == 2) {
                HomeRec homeRec = (HomeRec) msg.obj;
                loanDay = homeRec.getMaxDays() + "天";
                loanMoney = homeRec.getMaxCredit() + "元";
                if (homeRec.getCreditList().size() > 0) {
                    calculateMoney = homeRec.getCreditList().get(homeRec.getCreditList().size() - 1);
                } else {
                    calculateMoney = "0";
                }
                if (homeRec.getDayList().size() > 0) {
                    calculateDay = homeRec.getDayList().get(homeRec.getDayList().size() - 1);
                } else {
                    calculateDay = "7";
                }
                if (homeRec.getInterests().size() > 0) {
                    calculateRate = homeRec.getInterests().get(homeRec.getInterests().size() - 1);
                }
                hsbSelectedDay.setProgress(100);
                hsbSelectedMoney.setProgress(100);
            } else if (msg.what == 3) {
                NetworkUtil.dismissCutscenes();
            }
        }
    };

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            //认证中心
            case R.id.tv_home_verify:
                auth(view);
                break;
            //马上申请
            case R.id.ndb_loan_now:
                applyClick(view);
                break;
            //去还款
            case R.id.ndb_go_repay:
                goRepayMoney(view);
                break;
            //去展期
            case R.id.ndb_go_extend:
                goExtension(view);
                break;
            //服务费用
            case R.id.iv_home_costDaialog:
                showCostDialog(view);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isFragmentVisible = !hidden;
        //页面重新可见
        if (!hidden) {
            reqHomeData();
            reqNotice();
            System.out.println("--------onHiddenChanged  可见------");
        }
    }

    private int isFirstTimeCreat;
    private boolean isFragmentVisible;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTimeCreat > 0 && isFragmentVisible) {
            System.out.println("--------onResume------");
            reqHomeData();
            reqNotice();
        }
        isFirstTimeCreat++;
    }

    @RxBusSubscribe(code = RxBusEvent.REGISTER_SUCCESS)
    public void updateRegisterHome() {
        reqHomeData();
        reqNotice();
    }

    @RxBusSubscribe(code = RxBusEvent.LOGIN_SUCCESS)
    public void updateLoginHome() {
        reqHomeData();
        reqNotice();
    }

    @RxBusSubscribe(code = RxBusEvent.SET_PAY_PWD_SUCCESS)
    public void onSetPayPwdSuccessCallBack() {
        reqHomeData();
        reqNotice();
    }

    @RxBusSubscribe(code = RxBusEvent.APPLY_LOAN_SUCCESS)
    public void applyLoanSuccessCallBack() {
        reqHomeData();
        reqNotice();
    }

    @RxBusSubscribe(code = RxBusEvent.REPAY_SUCCESS)
    public void repaySuccessCallBack() {
        reqHomeData();
        reqNotice();
    }


    HomeRec homeRec = null;

    /****获取首页数据****/
    public void reqHomeData() {
        Call<HttpResult<HomeRec>> call = RDClient.getService(LoanService.class).getHomeIndex();
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<HomeRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<HomeRec>> call, Response<HttpResult<HomeRec>> response) {
                homeRec = response.body().getData();
                convertHomeData(homeRec);
            }
        });
    }

    /****获取公告****/
    public void reqNotice() {
        Call<HttpResult<ListData<NoticeRec>>> noticeCall = RDClient.getService(LoanService.class).getNoticeList();
        noticeCall.enqueue(new RequestCallBack<HttpResult<ListData<NoticeRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<NoticeRec>>> call, Response<HttpResult<ListData<NoticeRec>>> response) {
                List<NoticeRec> noticeRecList = response.body().getData().getList();
                if (noticeRecList != null && noticeRecList.size() > 0) {
                    List<String> wheelList = new ArrayList<>();
                    for (int i = 0; i < noticeRecList.size(); i++) {
                        wheelList.add(noticeRecList.get(i).getValue());
                    }
                    ListWheelAdapter adapter = new ListWheelAdapter<>(ContextHolder.getContext(),
                            R.layout.list_item_home_text, wheelList);

                    adapter.setTextSize(13);
                    mVerticalView.setViewAdapter(adapter);
                }
            }
        });
    }

    public HomeVM homeVM;
    private boolean isClick;

    private void convertHomeData(HomeRec data) {

//        calculateMoney = data.getCreditList().get(0);
//        calculateDay = data.getDayList().get(0);

        //认证个数
        if (null != data.getAuth()) {
            if (!TextUtil.isEmpty(data.getAuth().getQualified())
                    || !TextUtil.isEmpty(data.getAuth().getResult())
                    || !TextUtil.isEmpty(data.getAuth().getTotal())) {
                if ("0".equals(data.getAuth().getQualified())) {
                    isClick = true;
                } else {
                    isClick = false;
                }
//                认证0/6  >
                tvVerifyNums.setText("认证" + data.getAuth().getResult() + "/" + data.getAuth().getTotal() + "  >");
            }
        } else {
            tvVerifyNums.setText("认证0/0  >");
        }
        //更新头部
        updateTop(data);

        //已经借款 --状态 2,3
        if (data.isBorrow()) {

            //放款中--展示放款进度   展示状态--2
            if (!data.isRepay()) {
                llStatusCanLoan.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                llToRepay.setVisibility(View.GONE);

                getProgressData(data.getList());
            } else {//放款成功 待还款 展示状态--3

                llStatusCanLoan.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                llToRepay.setVisibility(View.VISIBLE);

                updateStatusThree(data);
            }


            MainActivity.repayState = 2;
        } else {//未借款  展示状态--1
            llStatusCanLoan.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            llToRepay.setVisibility(View.GONE);
            MainActivity.repayState = 1;

            updateStatusOne(data);
        }

        if (isDialogSHow == 0) {
            //判断弹窗是否显示  true  弹窗  false  不弹
            if (data.isShowPopup()) {
                if (null != data.getFailDate() && !"".equals(data.getFailDate())) {
                    showRePayDialog(data.getFailDate());
                }
            }
        }
    }

    //更新顶部状态
    private void updateTop(HomeRec data) {
//        if (isClick) {
//            tvCanLoanLimit.setText("暂无额度");//可借额度----信用额度
//        } else {
//            tvCanLoanLimit.setText(data.getCreditList().get(0));//可借额度----信用额度
//        }

        if (isClick) {//未认证完
//            tvMineLimit.setText("0");//我的额度
//            int size = data.getCreditList().size();
            String maxStr = StringFormat.subZeroAndDot(data.getMaxCredit());
//            String maxStr = data.getCreditList().get(size - 1);
            double max = Double.parseDouble(maxStr);
            if (max == 0) {
                tvCanLoanLimit.setText("0");//
            } else {
                tvCanLoanLimit.setText("0~" + maxStr);//我的额度
            }

        } else {//已认证所有必选项
//            int size = data.getCreditList().size();
            String maxStr = StringFormat.subZeroAndDot(data.getMaxCredit());
//            String maxStr = data.getCreditList().get(size - 1);
            tvCanLoanLimit.setText(maxStr);//我的额度
        }
    }

    //更新状态1
    private void updateStatusOne(HomeRec data) {


        if (isClick) {//未认证完
            btnLoanNow.setText("激活额度");
        } else {//已认证所有必选项
            btnLoanNow.setText("马上借钱");
        }

        if (EmptyUtils.isEmpty(data.getCreditList())) {
            tvMoneyMin.setText("0");
            tvMoneyMax.setText("0");
        } else {
            tvMoneyMin.setText(StringFormat.subZeroAndDot(data.getMinCredit()));
            tvMoneyMax.setText(StringFormat.subZeroAndDot(data.getMaxCredit()));
        }

        tvDayMin.setText(data.getMinDays());
        tvDayMax.setText(data.getMaxDays());

        Message message = new Message();
        message.obj = data;
        message.what = 2;
        handler.sendMessage(message);


        if (EmptyUtils.isNotEmpty(data.getCreditList())
                && EmptyUtils.isNotEmpty(data.getDayList())) {

            int moneySize = data.getCreditList().size();
            int daySize = data.getDayList().size();

            if (moneySize > 0) {
                calculateMoney = data.getCreditList().get(moneySize - 1);
            }
            if (daySize > 0) {
                calculateDay = data.getDayList().get(daySize - 1);
            }

//        判断费用计算从服务端获取
            reqHomeChoiceData(calculateMoney, calculateDay);
        }
    }

    //更新状态3
    private void updateStatusThree(HomeRec data) {
        //  StringFormat.subZeroAndDot
        tvRepayMoney.setText(StringFormat.subZeroAndDot(data.getPenaltyAmout() + data.getAmount()));

        int leftDays = Integer.parseInt(data.getRemainderDay());

        if (leftDays <= 0) {
            tvRepayDayLeft.setText("剩余还款" + Math.abs(leftDays) + "天");
        } else {
            tvRepayDayLeft.setText("已逾期" + leftDays + "天");
        }
        tvApplyTime.setText(data.getCreateTime());
        tvRepayTime.setText(data.getRepayTime());
    }

    /****更新状态2--获取借款进度****/
    private void getProgressData(List<LoanProgressRec> list) {

        if (list != null && list.size() > 0) {
            mProgressVMList.clear();

            List<LoanProgressVM> vms = new ArrayList<>();
            if (list.size() == 1) {
                LoanProgressVM vm = new LoanProgressVM();
                LoanProgressRec rec = list.get(0);
                vm.setLoanTime(rec.getCreateTime());
                vm.setRemark(rec.getRemark());
//                vm.setRepayTime(rec.getRepayTime());
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
//                    vm.setRepayTime(list.get(i).getRepayTime());
                    vm.setType(list.get(i).getType());
                    vm.setState(list.get(i).getState());
                    if (i == 0) {
                        //if (10 == vm.getType()) {
                        vm.setFirst(true);
                        if (Constant.NUMBER_10 == vm.getType() || Constant.NUMBER_20 == vm.getType()) {
                            vm.setGrey_1(false);
                        }
                        //}
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
     * 计算费率
     */
    private void calculateRate() {
        double real = ConverterUtil.getDouble(calculateMoney);
        if (real != 0) {
            //判断费用计算从服务端获取
            if (EmptyUtils.isNotEmpty(calculateMoney)
                    && EmptyUtils.isNotEmpty(calculateDay)) {
                reqHomeChoiceData(calculateMoney, calculateDay);
            }
        }
    }


    /**
     * 获取服务费率和手续费
     *
     * @param amount
     * @param timeLimit
     */
    public void reqHomeChoiceData(String amount, String timeLimit) {
        Call<HttpResult<HomeChoiceRec>> call = RDClient.getService(LoanService.class).getHomeChoice(amount, timeLimit);
        call.enqueue(new RequestCallBack<HttpResult<HomeChoiceRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<HomeChoiceRec>> call, Response<HttpResult<HomeChoiceRec>> response) {
                final HomeChoiceRec homeChoiceRec = response.body().getData();
                convertHomeChoiceData(homeChoiceRec);
            }
        });
    }

    /**
     * dataModel to mRegisterVM
     */
    private void convertHomeChoiceData(HomeChoiceRec data) {
        serviceMoney = (StringFormat.twoDecimalFormat(Double.parseDouble(data.getFee())));
        String money = data.getRealAmount();
        realMoney = StringFormat.twoDecimalFormat(money);
        rec = data.getFeeDetail();

        if (isClick) {
//            tvLoanLimitMoney.setText("¥0.00");//借款金额
//            tvLoanLimit.setText("0天");//借款期限
            tvLoanMoney.setText("¥0.00");//到账金额
            tvServiceFee.setText("¥0.00");//综合费用
        } else {
//            tvLoanLimitMoney.setText(StringUtils.nullToStr(data.getAmount()));//借款金额
//            tvLoanLimit.setText(data.getTimeLimit());//借款期限
            tvLoanMoney.setText(StringUtils.nullToStr(data.getRealAmount()));//到账金额
           /* if (data.getRealAmount() != null) {
                BigDecimal bigDecimal = new BigDecimal(data.getRealAmount());
                BigDecimal bigDecimal2 = new BigDecimal(1.02);
                BigDecimal multiply = bigDecimal.multiply(bigDecimal2);
                BigDecimal bigDecimal_result = multiply.setScale(2,BigDecimal.ROUND_HALF_UP);
                tvServiceFee.setText(StringUtils.nullToStr(bigDecimal_result.toString()));//综合费用(到期应还)
            } else {
                tvServiceFee.setText(StringUtils.nullToStr("0.00"));
            }*/
            tvServiceFee.setText(StringUtils.nullToStr(data.getFee()));
        }

    }

    /**
     * 立即申请
     */
    public void applyClick(View view) {

        if ((boolean) SharedInfo.getInstance().getValue(Constant.IS_LAND, false)) {

            if (!AndPermission.hasPermissions(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AndPermission.with(mContext)
                        .runtime()
                        .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        .onDenied(permissions -> {
                            //用户拒绝了开启权限,且选择了不再询问
                            if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                                // 弹出前往设置的提示
                                DialogUtils.showPermisssionDialog(mContext);
                            } else {//用户本次拒绝了开启权限

                            }
//                            DialogUtils.showPermisssionDialog(mContext);
                        })
                        .start();
            } else {

           /*     Routers.openForResult(Util.getActivity(view), RouterUrl.getRouterUrl(String.format(RouterUrl.Loan_Details,
                        2000, 7, 1500, 500,
                        "李浩", "1234567890123456789", "341226199306151536"
                )), LOAN);*/
                if (!isClick) {
                    if (homeRec.isPwd()) {
                        Routers.openForResult(Util.getActivity(view), RouterUrl.getRouterUrl(String.format(RouterUrl.Loan_Details,
                                calculateMoney, calculateDay, realMoney, serviceMoney,
                                homeRec.getCardName(), homeRec.getCardNo(), homeRec.getCardId()
                        )), LOAN);
                    } else {
                        Routers.open(view.getContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_Settings_Pay_Pwd, Constant.NUMBER_0)));
                    }
                } else {
                    Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.Mine_CreditTwoCenter));

                }
            }
        } else {
            Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.UserInfoManage_Login));
        }
    }


    /**
     * 点击进入还款详情页面
     */
    public void goRepayMoney(final View view) {
        Routers.open(view.getContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.Repay_Details,
                homeRec.getBorrowId(), Constant.STATUS_1)));
    }

    /**
     * 点击展期页面
     */
    public void goExtension(final View view) {
        Routers.open(view.getContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.Repay_Details,
                homeRec.getBorrowId(), Constant.STATUS_1)));
    }

    /**
     * 跳转认证中心
     */
    public void auth(View view) {

        if ((boolean) SharedInfo.getInstance().getValue(Constant.IS_LAND, false)) {
            Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.Mine_CreditTwoCenter));
        } else {
            Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.UserInfoManage_Login));
        }
    }

    public void showCostDialog(View view) {
        if (isClick) {
            return;
        }
        if (rec != null) {
            DialogUtils.showCostDialog(mContext, rec, null);
        }
    }

    private int isDialogSHow = 0;

    //显示去还款弹窗
    public void showRePayDialog(String tipsText) {

        DialogUtils.showGoRepay(mContext, tipsText, new DialogUtils.goRepayConfirm() {
            @Override
            public void goRepay() {
                Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Repay_Details,
                        homeRec.getBorrowId(), Constant.STATUS_1)));
            }
        });

        isDialogSHow++;
    }

    /**
     * 短信
     */
    private void getuserSmsList() {
        String smsInPhone = getAppContentResolver.getSmsInPhone(getActivity());
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
}
