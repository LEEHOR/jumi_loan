package com.cashloan.jumidai.ui.homeRepay;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.RepayService;
import com.cashloan.jumidai.ui.homeRepay.adapter.RepayRecordAdapter;
import com.cashloan.jumidai.ui.homeRepay.bean.RepayRecordItemRec;
import com.cashloan.jumidai.ui.main.MainActivity;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpFragment;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.net.OkHttp.entity.PageMo;
import com.commom.utils.ContextHolder;
import com.commom.widget.NoDoubleClickButton;
import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.xRecycler.XRecyclerView;
import com.github.mzule.activityrouter.router.Routers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/21
 * 描述： 借款首页
 */
public class RepayRecordFrag extends BaseMvpFragment {

    private static final String TAG = "RepayRecordFrag";

    @ViewInject(R.id.swipe_record)
    private SwipeRefreshLayout mRefreshLayout;

    @ViewInject(R.id.ll_no_record)
    private LinearLayout llNoRecord;
    @ViewInject(R.id.iv_no_record)
    private ImageView ivNoRecord;
    @ViewInject(R.id.tv_no_record)
    private TextView tvNoRecord;
    @ViewInject(R.id.ndb_no_reocrd)
    private NoDoubleClickButton btnApply;


    @ViewInject(R.id.ll_repay_record)
    private LinearLayout llRepayRecord;
    @ViewInject(R.id.tv_record_nums)
    private TextView tvRecordNums;
    @ViewInject(R.id.rc_repay_record)
    private XRecyclerView mXRecyclerView;

    private RepayRecordAdapter mRecordAdapter;
    private List<RepayRecordItemRec> mRecList;
    private PageMo pageMo;

    private MainActivity mainAct;


    public RepayRecordFrag() {
        // Required empty public constructor
    }

    public static RepayRecordFrag newInstance() {
        RepayRecordFrag fragment = new RepayRecordFrag();
        return fragment;
    }

    @Override
    protected void setStatusBar() {
        // 不处理状态栏
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_repay_record;
    }

    @Override
    protected void initView(View view) {
        setPageTitle("还款");

        AnnotateUtils.inject(this, view);
    }

    @Override
    protected void initFunc() {
        mainAct = (MainActivity) getActivity();
        ivNoRecord.setImageResource(R.drawable.icon_no_loan_record);

        pageMo = new PageMo();
        mRecList = new ArrayList<>();
        mRecordAdapter = new RepayRecordAdapter(mContext, mRecList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mXRecyclerView.setLayoutManager(manager);
        mXRecyclerView.setAdapter(mRecordAdapter);

        mXRecyclerView.setRefreshEnabled(false);
        mXRecyclerView.setLoadMoreEnabled(false);

        attachClickListener(btnApply);

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_color_principal));
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(false);
            reqRecordData();
        });

        mRecordAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                RepayRecordItemRec rec = mRecList.get(position);
                Routers.open(mContext, RouterUrl.getRouterUrl(
                        String.format(RouterUrl.Repay_Details, rec.getBorrowId(), Constant.STATUS_1)));
            }
        });

        if (MainActivity.repayState == 1) {
            llNoRecord.setVisibility(View.VISIBLE);
            llRepayRecord.setVisibility(View.GONE);
        } else {
            llNoRecord.setVisibility(View.GONE);
            llRepayRecord.setVisibility(View.VISIBLE);
        }

        reqRecordData();
    }

    @Override
    protected void onViewClicked(View view) {
        /**测试认证页面*/
        //Routers.openForResult(getActivity(),RouterUrl.getRouterUrl(RouterUrl.Mine_GdMap), RequestResultCode.REQ_GD_MAP);
        //Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.Mine_CreditTwoCenter));
        if ((boolean) SharedInfo.getInstance().getValue(Constant.IS_LAND, false)) {
            mainAct.setTab(Constant.NUMBER_0);
        } else {
            Routers.open(view.getContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_1)));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isFragmentVisible = !hidden;
        //页面可见
        if (!hidden) {
            reqRecordData();
        }
    }

    private int isFirstTimeCreat;
    private boolean isFragmentVisible;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTimeCreat > 0 && isFragmentVisible) {
            reqRecordData();
        }
        isFirstTimeCreat++;
    }


    /****请求还款记录数据****/
    public void reqRecordData() {
        Call<HttpResult<ListData<RepayRecordItemRec>>> call = RDClient.getService(RepayService.class).getRecordList(pageMo);
        call.enqueue(new RequestCallBack<HttpResult<ListData<RepayRecordItemRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<RepayRecordItemRec>>> call,
                                  Response<HttpResult<ListData<RepayRecordItemRec>>> response) {
                convertData(response.body().getData().getList());
            }

        });
    }

    private void convertData(List<RepayRecordItemRec> list) {
        String str;
        if (null == list || list.size() == 0) {
            MainActivity.repayState = 1;

            str = ContextHolder.getContext().getString(R.string.repay_record_no, Constant.STATUS_0);
            tvRecordNums.setText(getRepayNumBuilder(str));
            return;
        }

        str = ContextHolder.getContext().getString(R.string.repay_record_no, String.valueOf(list.size()));
        tvRecordNums.setText(getRepayNumBuilder(str));
        mRecList.clear();
        mRecList.addAll(list);
        mRecordAdapter.notifyDataSetChanged();

        if (MainActivity.repayState == 1) {
            llNoRecord.setVisibility(View.VISIBLE);
            llRepayRecord.setVisibility(View.GONE);
        } else {
            llNoRecord.setVisibility(View.GONE);
            llRepayRecord.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取还款数量字符串
     */
    private SpannableStringBuilder getRepayNumBuilder(String str) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ContextHolder.getContext(),
                R.color.app_color_principal)), 4, str.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }
}
