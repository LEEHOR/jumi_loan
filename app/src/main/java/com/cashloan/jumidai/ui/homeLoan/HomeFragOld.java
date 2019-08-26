package com.cashloan.jumidai.ui.homeLoan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
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
import com.cashloan.jumidai.ui.main.MainActivity;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.cashloan.jumidai.views.spinnerwheel.WheelVerticalView;
import com.cashloan.jumidai.views.spinnerwheel.adapters.ListWheelAdapter;
import com.commom.base.BaseMvpFragment;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.net.RxBus.RxBus;
import com.commom.utils.ContextHolder;
import com.commom.utils.StringFormat;
import com.commom.utils.StringUtils;
import com.commom.utils.TextUtil;
import com.commom.widget.NoDoubleClickButton;
import com.github.mzule.activityrouter.router.Routers;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述： 借款首页
 */
public class HomeFragOld extends BaseMvpFragment {
    private static final String TAG = "HomeFragOld";

    @ViewInject(R.id.swipe_home)
    private SwipeRefreshLayout mRefreshLayout;

    @ViewInject(R.id.home_banner)
    private BGABanner         mBanner;
    @ViewInject(R.id.wheel_view)
    private WheelVerticalView mVerticalView;//垂直广告
    //    @ViewInject(R.id.tv_home_verify)
//    private TextView           tvVerifyNums;//认证个数
    @ViewInject(R.id.tv_home_can_loan_limit)
    private TextView          tvCanLoanLimit;//头部信用额度---旧可借额度
    @ViewInject(R.id.tv_loan_limit_money)
    private TextView          tvLoanLimitMoney;//借款金额
    @ViewInject(R.id.tv_loan_limit_day)
    private TextView          tvLoanLimit;//借款期限
    //    @ViewInject(R.id.tv_loan_money)
//    private TextView           tvLoanMoney;//到账金额
    @ViewInject(R.id.tv_loan_service_fee)
    private TextView          tvServiceFee;//服务费

    @ViewInject(R.id.iv_home_costDaialog)
    private ImageView ivCostDialog;

    /***能够借款页面 默认***/
    @ViewInject(R.id.ll_home_can_borrow)
    private LinearLayout llStatusCanLoan;


    @ViewInject(R.id.ndb_loan_now)
    private NoDoubleClickButton btnLoanNow;//马上申请

    /***已经申请借款***/
    @ViewInject(R.id.rc_loan_progress)
    private RecyclerView mRecyclerView;

    /***借款成功 待还款***/
    @ViewInject(R.id.ll_home_go_repay)
    private LinearLayout        llToRepay;
    @ViewInject(R.id.tv_repay_money)
    private TextView            tvRepayMoney;
    @ViewInject(R.id.tv_repay_dayLeft)
    private TextView            tvRepayDayLeft;
    @ViewInject(R.id.tv_apply_time)
    private TextView            tvApply;
    @ViewInject(R.id.tv_repay_time)
    private TextView            tvRepay;
    @ViewInject(R.id.tv_apply_timeShow)
    private TextView            tvApplyTime;
    @ViewInject(R.id.tv_repay_timeShow)
    private TextView            tvRepayTime;
    @ViewInject(R.id.ndb_go_repay)
    private NoDoubleClickButton btnGoRepay;//去还款
    @ViewInject(R.id.ndb_go_extend)
    private NoDoubleClickButton btnGoExtend;//去展期


    private LoanProgressAdapter  mProgressAdapter;
    private List<LoanProgressVM> mProgressVMList;

    //借款浮动天数
    public String loanDay;
    //借款浮动额度
    public String loanMoney    = "0.00";
    //实际借款金额
    public String realMoney    = "0.00";
    //服务费用
    public String serviceMoney = "0.00";

    /** 计算金额 */
    private      String calculateMoney = "";
    /** 计算金额 */
    private      String calculateDay   = "7";
    /** 计算费率 */
    private      String calculateRate  = "";
    /** 借款标识 */
    public final int    LOAN           = 0x0111;
    private HomeFeeDetailRec rec;


    public HomeFragOld() {
        // Required empty public constructor
    }

    public static HomeFragOld newInstance() {
        HomeFragOld fragment = new HomeFragOld();
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
        return R.layout.fragment_home_old;
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

        /***顶部轮播图片 ****/  // TODO: 2018/9/17  model类型转换为string
        mBanner.setAdapter(new BGABanner.Adapter<ImageView, Integer>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, Integer model, int position) {
                itemView.setTag(null);
                Glide.with(mContext).load(model).into(itemView);
                itemView.setTag(position);
            }
        });

        List<Integer> bannerImgRes = new ArrayList<>();
        bannerImgRes.add(R.drawable.banner);
//        bannerImgRes.add(R.drawable.banner);
//        bannerImgRes.add(R.drawable.banner);

        mBanner.setData(bannerImgRes, null);

        mBanner.setDelegate(new BGABanner.Delegate<ImageView, Integer>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, Integer model, int position) {
                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });

        if (bannerImgRes.size() <= 1) {
            mBanner.setAutoPlayAble(false);
        }
        /***顶部轮播图片 ****/

        homeVM = new HomeVM();
        mProgressVMList = new ArrayList<>();
        mProgressAdapter = new LoanProgressAdapter(mContext, mProgressVMList);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mProgressAdapter);

//        attachClickListener(tvVerifyNums);
        attachClickListener(btnLoanNow);
        attachClickListener(btnGoRepay);
        attachClickListener(btnGoExtend);
        attachClickListener(ivCostDialog);

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_color_principal));
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(false);
            reqHomeData();
            reqNotice();
        });


        llStatusCanLoan.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        llToRepay.setVisibility(View.GONE);

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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mVerticalView.setCurrentItem(mVerticalView.getCurrentItem() + 1, true);
            }
        }
    };

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
//            //认证中心
//            case R.id.tv_home_verify:
//                auth(view);
//                break;
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

    private int     isFirstTimeCreat;
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

//    @RxBusSubscribe(code = RxBusEvent.REGISTER_SUCCESS)
//    private void updateRegisterHome() {
//        Log.d(TAG, "注册成功，刷新数据");
//        reqHomeData();
//        reqNotice();
//    }
//
//    @RxBusSubscribe(code = RxBusEvent.LOGIN_SUCCESS)
//    private void updateLoginHome() {
//        Log.d(TAG, "登录成功，刷新数据");
//        reqHomeData();
//        reqNotice();
//    }

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

    public  HomeVM  homeVM;
    private boolean isClick;

    private void convertHomeData(HomeRec data) {

        calculateMoney = data.getCreditList().get(0);
        calculateDay = data.getDayList().get(0);

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
////                认证0/6  >
//                tvVerifyNums.setText("认证" + data.getAuth().getResult() + "/" + data.getAuth().getTotal() + "  >");
            }
        } else {
//            tvVerifyNums.setText("认证0/0  >");
        }
//        //更新头部
//        updateTop(data);

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


    private void updateTop(HomeRec data) {

    }

    //更新状态1
    private void updateStatusOne(HomeRec data) {

        if (isClick) {
            tvCanLoanLimit.setText("1000~5000");//可借额度----信用额度
        } else {
            tvCanLoanLimit.setText(data.getCreditList().get(0));//可借额度----信用额度
        }

//        判断费用计算从服务端获取
        reqHomeChoiceData(calculateMoney, calculateDay);

        if (isClick) {//未认证完
            btnLoanNow.setText("激活额度");
        } else {//已认证所有必选项
            btnLoanNow.setText("马上借钱");
        }
    }

    //更新状态3
    private void updateStatusThree(HomeRec data) {
        //  StringFormat.subZeroAndDot
        tvRepayMoney.setText(StringFormat.subZeroAndDot(data.getPenaltyAmout() + data.getAmount()));

        int leftDays = Integer.parseInt(data.getRemainderDay());

        if (leftDays <= 0) {
            tvRepayDayLeft.setText("剩余还款" + Math.abs(leftDays) + "天");
            tvRepayDayLeft.setSelected(false);
        } else {
            tvRepayDayLeft.setText("已逾期" + leftDays + "天");
            tvRepayDayLeft.setSelected(true);
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
            tvLoanLimitMoney.setText("0.00");//借款金额
            tvLoanLimit.setText("0");//借款期限
//            tvLoanMoney.setText("0.00");//到账金额
            tvServiceFee.setText("0.00");//综合费用
        } else {
            tvLoanLimitMoney.setText(StringUtils.nullToStr(data.getAmount()));//借款金额
            tvLoanLimit.setText(data.getTimeLimit());//借款期限
//            tvLoanMoney.setText(StringUtils.nullToStr(data.getRealAmount()));//到账金额
            tvServiceFee.setText(StringUtils.nullToStr(data.getFee()));//综合费用
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
}
