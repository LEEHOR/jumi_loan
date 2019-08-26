package com.cashloan.jumidai.ui.homeMine.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.RepayService;
import com.cashloan.jumidai.ui.homeMine.adapter.LoanRecordAdapter;
import com.cashloan.jumidai.ui.homeMine.bean.LendRecordItemVM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.BorrowRec;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.net.OkHttp.entity.PageMo;
import com.commom.utils.EmptyUtils;
import com.commom.utils.OnOnceClickListener;
import com.commom.widget.LoadingLayout;
import com.commom.widget.recycler.xRecycler.XRecyclerView;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/23
 * 描述： 借款详情
 */
@Router(RouterUrl.Mine_LendRecord)
public class LoanRecordActivity extends BaseMvpActivity {

    private static final String TAG = "LoanRecordActivity";

    @ViewInject(R.id.swipe_loan_record)
    private SwipeRefreshLayout mRefreshLayout;
    @ViewInject(R.id.rc_loan_record)
    private XRecyclerView      mXRecyclerView;

    private PageMo pageMo = new PageMo();
    private LoanRecordAdapter      mLoanRecordAdapter;
    private List<LendRecordItemVM> mRecordList;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loan_record;
    }

    @Override
    protected void initView() {
        setPageTitleBack("借款记录");
        AnnotateUtils.inject(this);
    }

    @Override
    protected void initFunc() {
        mRecordList = new ArrayList<>();
        mLoanRecordAdapter = new LoanRecordAdapter(mContext, mRecordList, new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                Integer position = (Integer) v.getTag();
                Log.d(TAG, "onItemClick: position=" + position + "\nlist-size=" + mRecordList.size());
                LendRecordItemVM rec = mRecordList.get(position);
                Routers.open(mContext, RouterUrl.getRouterUrl(
                        String.format(RouterUrl.Repay_Details, rec.getId(), Constant.STATUS_2)));
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mXRecyclerView.setLayoutManager(manager);
        mXRecyclerView.setAdapter(mLoanRecordAdapter);

        mXRecyclerView.setRefreshEnabled(false);
        mXRecyclerView.setLoadMoreEnabled(false);

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_color_principal));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
                pageMo.setCurrent(1);
                req_data();
            }
        });

        req_data();
    }


    /**
     * 请求数据
     */
    public void req_data() {
        Call<HttpResult<ListData<BorrowRec>>> call = RDClient.getService(RepayService.class).getBorrow(pageMo);
        call.enqueue(new RequestCallBack<HttpResult<ListData<BorrowRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<BorrowRec>>> call,
                                  Response<HttpResult<ListData<BorrowRec>>> response) {

                pageMo = response.body().getPage();
                convertData(response.body().getData().getList());
            }
        });

    }

    private void convertData(List<BorrowRec> list) {

        if (null != list && list.size() != 0) {
            if (pageMo.getCurrent() == 1) {
                mRecordList.clear();
            }

            for (BorrowRec rec : list) {
                if (null != rec) {
                    LendRecordItemVM recordItemVM = new LendRecordItemVM();
                    recordItemVM.setMoney(rec.getAmount());
                    recordItemVM.setTime(rec.getCreateTime());
                    recordItemVM.setStatuStr(rec.getStateStr());
                    recordItemVM.setStatus(rec.getState());
                    recordItemVM.setId(String.valueOf(rec.getId()));
                    mRecordList.add(recordItemVM);

                }
            }
            mLoanRecordAdapter.notifyDataSetChanged();
//            mXRecyclerView.setLoadMoreEnabled(!pageMo.isOver());
        } else {

            if (EmptyUtils.isEmpty(list)) {
                mXRecyclerView.setVisibility(View.GONE);
                mLoadingLayout.setStatus(LoadingLayout.Status.Empty);
                mLoadingLayout.setEmptyImage(R.drawable.icon_no_loan_record);
                mLoadingLayout.setEmptyText(mContext.getString(R.string.placeholder_empty_record));
            }
        }

    }

}
