package com.cashloan.jumidai.ui.homeMine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
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
import com.cashloan.jumidai.ui.homeMine.bean.CreditWorkVM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditWorkInfoRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.DicRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.KeyValueRec;
import com.cashloan.jumidai.ui.homeMine.bean.submit.CreditWorkSub;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.ActivityManage;
import com.commom.utils.ContextHolder;
import com.commom.utils.OnOnceClickListener;
import com.commom.utils.StringUtils;
import com.commom.utils.ToastUtil;
import com.commom.widget.editText.ClearEditText;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/27
 * 描述： 工作信息
 */
@Router(value = RouterUrl.Mine_CreditWork, stringParams = BundleKeys.STATE)
public class WorkInfoActivity extends BaseMvpActivity {


    @ViewInject(R.id.cet_company_name)
    private ClearEditText tvCompanyName;
    @ViewInject(R.id.cet_company_phone)
    private ClearEditText tvCompanyPhone;
    @ViewInject(R.id.tv_work_companyAddress)
    private TextView      tvCompanyAddress;
    @ViewInject(R.id.cet_address_detail)
    private ClearEditText tvCompanyAddressDetail;

    @ViewInject(R.id.is_upload)
    private TextView tvWorkImage;
    @ViewInject(R.id.tv_work_time)
    private TextView tvWorkTime;

    String state;
    public  CreditWorkVM      viewModel;
    //工作时长
    private OptionsPickerView workTimePicker;
    private ArrayList<String> workTime;
    private DicRec            dic;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_work_info;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("个人信息");
        setTitleRight(Constant.NUMBER__1, "保存", new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                submit(v);
            }
        });
    }

    @Override
    protected void initFunc() {
        state = getIntent().getStringExtra(BundleKeys.STATE);
        viewModel = new CreditWorkVM();

        attachClickListener(tvCompanyAddress);
        attachClickListener(tvWorkImage);
        attachClickListener(tvWorkTime);

        reqDic();
        req_data();
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            //工作地址
            case R.id.tv_work_companyAddress:
                toMap(view);
                break;
            //工作照片
            case R.id.is_upload:
                photoSub(view);
                break;
            //工作时长
            case R.id.tv_work_time:
                workTimeShow(view);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestResultCode.REQ_GD_MAP) {
            if (data != null) {
                PoiItem poiItem = data.getParcelableExtra(BundleKeys.DATA);
                viewModel.setCompanyAddress(poiItem.getProvinceName()+ poiItem.getCityName()+poiItem.getAdName()+poiItem.getSnippet()+poiItem.getDirection()+poiItem.getTitle());
                viewModel.setAddressDetail(poiItem.getProvinceName()+ poiItem.getCityName()+poiItem.getAdName()+poiItem.getSnippet()+poiItem.getDirection()+poiItem.getTitle());
                viewModel.setCompanyCoordinate(poiItem.getLatLonPoint().getLongitude() + "," + poiItem.getLatLonPoint().getLatitude());

                tvCompanyAddress.setText(StringUtils.nullToStr(viewModel.getCompanyAddress()));
                tvCompanyAddressDetail.setText(StringUtils.nullToStr(viewModel.getAddressDetail()));
            }
        } else if (requestCode == 1 && resultCode == 1) {
            viewModel.setWorkPhoto(Constant.STATUS_10);

            tvWorkImage.setText(viewModel.getWorkPhoto());
        }
    }


    public void req_data() {
        Call<HttpResult<CreditWorkInfoRec>> call = RDClient.getService(MineService.class).getWorkInfo();
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<CreditWorkInfoRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<CreditWorkInfoRec>> call,
                                  Response<HttpResult<CreditWorkInfoRec>> response) {

                convert(response.body().getData());
            }
        });
    }

    private void convert(CreditWorkInfoRec data) {
        viewModel.setCompanyCoordinate(data.getCompanyCoordinate());
        viewModel.setWorkTimeStr(data.getWorkingYears());
        viewModel.setCompanyName(data.getCompanyName());
        viewModel.setCompanyPhone(data.getCompanyPhone());
        viewModel.setCompanyAddress(data.getCompanyAddr());
        viewModel.setAddressDetail(data.getCompanyDetailAddr());
        viewModel.setWorkPhoto(data.getWorkImgState());

        tvCompanyName.setText(StringUtils.nullToStr(viewModel.getCompanyName()));
        tvCompanyPhone.setText(StringUtils.nullToStr(viewModel.getCompanyPhone()));
        tvCompanyAddress.setText(StringUtils.nullToStr(viewModel.getCompanyAddress()));
        tvCompanyAddressDetail.setText(StringUtils.nullToStr(viewModel.getAddressDetail()));
        tvWorkTime.setText(StringUtils.nullToStr(viewModel.getWorkTimeStr()));
    }

    /**
     * 初始化弹出框
     */
    private void init() {
        if (dic != null) {//数据字典获取内容
            List<KeyValueRec> temp;
            if (dic.getWorkTimeList() != null) {
                workTime = new ArrayList<>();
                temp = dic.getWorkTimeList();
                for (KeyValueRec rec : temp) {
                    workTime.add(rec.getValue());
                }
            }
        }
        workTimePicker = new OptionsPickerView(mContext);
        workTimePicker.setPicker(workTime);
        workTimePicker.setCyclic(false);
    }

    /**
     * 数据字典请求
     */
    private void reqDic() {
        Call<HttpResult<DicRec>> callInit = RDClient.getService(MineService.class).getDicts(DicKey.WORKTIME);
        NetworkUtil.showCutscenes(callInit);
        callInit.enqueue(new RequestCallBack<HttpResult<DicRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<DicRec>> call, Response<HttpResult<DicRec>> response) {
                dic = response.body().getData();
                init();
            }
        });

    }

    /** 工作信息上传 */
    public void photoSub(View view) {
        Routers.openForResult(ActivityManage.peek(), RouterUrl.getRouterUrl(RouterUrl.Mine_CreditWorkPhoto), 1);
    }

    /** 工作时长 */
    public void workTimeShow(final View view) {
        if (dic != null && dic.getWorkTimeList() != null) {
            Util.hideKeyBoard(view);
            workTimePicker.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    viewModel.setWorkTime(workTime.get(options1));
                    viewModel.setWorkTimeStr(workTime.get(options1));

                    tvWorkTime.setText(viewModel.getWorkTimeStr());
                }
            });
            workTimePicker.show();
        } else {
            ToastUtil.toast(R.string.credit_no_dic);
        }
    }

    /** 获取位置信息 */
    public void toMap(View view) {
        Routers.openForResult(WorkInfoActivity.this, RouterUrl.getRouterUrl(RouterUrl.Mine_GdMap), RequestResultCode.REQ_GD_MAP);
    }

    public void submit(final View view) {
        String select = ContextHolder.getContext().getString(R.string.select);

        viewModel.setCompanyName(tvCompanyName.getText().toString());
        viewModel.setCompanyPhone(tvCompanyPhone.getText().toString());
        viewModel.setCompanyAddress(tvCompanyAddress.getText().toString());
        viewModel.setAddressDetail(tvCompanyAddressDetail.getText().toString());
        viewModel.setWorkTime(StringUtils.nullToStr(tvWorkTime.getText().toString()));

        if (TextUtils.isEmpty(viewModel.getCompanyName())) {
            DialogUtils.showToastDialog(mContext, mContext.getResources().getString(R.string.work_company_name_hint));
            return;
        }
        if (TextUtils.isEmpty(viewModel.getCompanyPhone())) {
            DialogUtils.showToastDialog(mContext, mContext.getResources().getString(R.string.work_company_phone_hint));
            return;
        }
        if (TextUtils.isEmpty(viewModel.getCompanyAddress())) {
            DialogUtils.showToastDialog(mContext, mContext.getResources().getString(R.string.work_company_address));
            return;
        }
        if (TextUtils.isEmpty(viewModel.getAddressDetail())) {
            DialogUtils.showToastDialog(mContext, mContext.getResources().getString(R.string.work_company_address_hint));
            return;
        }

        CreditWorkSub workSub = new CreditWorkSub(
                viewModel.getCompanyAddress(),
                viewModel.getCompanyCoordinate(),
                viewModel.getAddressDetail(),
                viewModel.getCompanyName(),
                viewModel.getCompanyPhone(),
                viewModel.getWorkTime());

        Call<HttpResult> call = RDClient.getService(MineService.class).workInfoSaveOrUpdate(workSub);
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                DialogUtils.showConfirmDialog(mContext, response.body().getMsg(),
                        new DialogUtils.btnConfirmClick() {
                            @Override
                            public void confirm() {
                                Util.getActivity(view).finish();
                            }
                        });
            }
        });
    }
}
