package com.cashloan.jumidai.ui.homeMine;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.CommonType;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.CommonService;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.MineItemBean;
import com.cashloan.jumidai.ui.homeMine.bean.MineVM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CommonRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.InfoRec;
import com.cashloan.jumidai.ui.main.adapter.ItemMineAdapter;
import com.cashloan.jumidai.utils.DisplayFormat;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpFragment;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
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
public class MineFrag extends BaseMvpFragment {

    private static final String TAG = "MineFrag";

    @ViewInject(R.id.iv_mine_head)
    private ImageView     ivHead;
    @ViewInject(R.id.tv_mine_phone)
    private TextView      tvPhone;
    @ViewInject(R.id.rc_mine_items)
    private XRecyclerView mRecyclerView;

    private ItemMineAdapter mAdapter;
    private List<MineItemBean> mItemBeans = new ArrayList<>();

    private final int[]    iconRes    = {R.drawable.icon_loanrecord_mine, R.drawable.icon_user_msg,
            R.drawable.icon_bank_card, R.drawable.icon_help_mine, R.drawable.icon_set};
    private final String[] itemTitles = {"借款记录", "完善资料", "收款银行卡", "帮助中心", "设置"};


    public  MineVM    mineVM;
    private InfoRec   infoRec;
    private CommonRec rec;

    public MineFrag() {
        // Required empty public constructor
    }

    public static MineFrag newInstance() {
        MineFrag fragment = new MineFrag();

        return fragment;
    }

    @Override
    protected void setStatusBar() {
        // 不处理状态栏
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isFragmentVisible = !hidden;
        //页面可见
        if (!hidden) {
            requestHelpurl();
            requestUserInfo();
        }
    }

    private int     isFirstTimeCreat;
    private boolean isFragmentVisible;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTimeCreat > 0 && isFragmentVisible) {
            requestHelpurl();
            requestUserInfo();
        }
        isFirstTimeCreat++;
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {
//        setPageTitle("我的");
        AnnotateUtils.inject(this, view);
    }

    @Override
    protected void initFunc() {


        for (int i = 0; i < iconRes.length; i++) {
            MineItemBean bean = new MineItemBean();
            bean.setItemTag(i);
            bean.setImageRes(iconRes[i]);
            bean.setItemText(itemTitles[i]);
            mItemBeans.add(bean);
        }

        mineVM = new MineVM();

        mAdapter = new ItemMineAdapter(mContext, mItemBeans);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setRefreshEnabled(false);
        mRecyclerView.setLoadMoreEnabled(false);

        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                MineItemBean bean = (MineItemBean) item;
                doItemClick(view, position);
            }
        });

        requestHelpurl();
        requestUserInfo();
    }

    private void doItemClick(View view, int itemTag) {
        switch (itemTag) {
            case 0:
                lendRecordClick(view);
                break;
            case 1:
                perfectClick(view);
                break;
            case 2:
                bankClick(view);
                break;
            case 3:
                helpClick(view);
                break;
            case 4:
                settingClick(view);
                break;
            default:
                break;
        }
    }


    private void requestHelpurl() {

        Call<HttpResult<ListData<CommonRec>>> call = RDClient.getService(CommonService.class).h5List();
        call.enqueue(new RequestCallBack<HttpResult<ListData<CommonRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<CommonRec>>> call, Response<HttpResult<ListData<CommonRec>>> response) {
                List<CommonRec> list = response.body().getData().getList();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (CommonType.HELP.equals(list.get(i).getCode())) {
                            rec = list.get(i);
                            return;
                        }
                    }
                }
            }
        });
    }


    /**
     * 请求个人信息
     */
    public void requestUserInfo() {
        Call<HttpResult<InfoRec>> call = RDClient.getService(MineService.class).getInfo();
        call.enqueue(new RequestCallBack<HttpResult<InfoRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<InfoRec>> call,
                                  Response<HttpResult<InfoRec>> response) {
                if (null != response.body()) {
                    convert(response.body().getData());
                }
            }
        });
    }

    /**
     * 数据转换
     */
    private void convert(InfoRec infoRec) {
        this.infoRec = infoRec;
        mineVM.setPhone(DisplayFormat.accountHideFormat(infoRec.getPhone()));

        tvPhone.setText(mineVM.getPhone());
    }


    /**
     * 借款记录
     */
    public void lendRecordClick(View view) {
        Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.Mine_LendRecord));
    }

    /**
     * 完善资料
     */
    public void perfectClick(View view) {
        Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.Mine_CreditTwoCenter));
    }

    /**
     * 收款银行卡
     */
    public void bankClick(final View view) {
        if (infoRec == null)
            return;
        if (Constant.STATUS_10.equals(infoRec.getIdState())) {
            DialogUtils.showDefaultDialog(mContext, mContext.getString(R.string.person_first),
                    new DialogUtils.btnConfirmClick() {
                        @Override
                        public void confirm() {
                            Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.Mine_CreditTwoCenter));
                        }
                    });
        } else {
            if (Constant.STATUS_30.equals(infoRec.getBankCardState())
                    || Constant.STATUS_20.equals(infoRec.getBankCardState())) {
                Routers.open(Util.getActivity(view), RouterUrl.getRouterUrl(
                        String.format(RouterUrl.Mine_CreditBindBank, infoRec.getBankCardState())));
            } else {
                Routers.open(Util.getActivity(view), RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditBank, Constant.STATUS_0)));
            }
        }
    }

    /**
     * 帮助中心
     */
    public void helpClick(View view) {
        if (null != rec) {
            Routers.open(view.getContext(), RouterUrl.getRouterUrl(
                    String.format(RouterUrl.Mine_HelpCenter, CommonType.getUrl(rec.getValue()))));
        }
    }

    /**
     * 设置
     */
    public void settingClick(View view) {
        Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.Mine_Settings));
    }


}
