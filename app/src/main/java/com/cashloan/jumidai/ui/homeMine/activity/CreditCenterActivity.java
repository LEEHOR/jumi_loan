package com.cashloan.jumidai.ui.homeMine.activity;

import android.Manifest;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.adapter.VerifyCenterAdapter;
import com.cashloan.jumidai.ui.homeMine.bean.VerifyItemVM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditStatusRec;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.OnOnceClickListener;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;
import com.moxie.client.exception.ExceptionType;
import com.moxie.client.exception.MoxieException;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/28
 * 描述： 认证中心
 */
@Router(RouterUrl.Mine_CreditTwoCenter)
public class CreditCenterActivity extends BaseMvpActivity {
    private static final String TAG = "CreditCenterActivity";

    @ViewInject(R.id.swipe_verify)
    private SwipeRefreshLayout mRefreshLayout;

    @ViewInject(R.id.rc_must_verify)
    private RecyclerView mMustVerifyRc;
    @ViewInject(R.id.rc_choose_verify)
    private RecyclerView mChooseVerifyRc;

    private VerifyCenterAdapter mMustAdapter;
    private VerifyCenterAdapter mChooseAdapter;
    private List<VerifyItemVM> mMustItemLists;
    private List<VerifyItemVM> mChooseItemLists;

    private String[] mMustVerifyTitles = new String[]{"个人信息", "收款银行卡", "紧急联系人", "手机运营商", "人脸识别"};

    private String[] mChooseVerifyTitles = new String[]{"工作信息", "更多信息", "淘宝认证"};

    private int[] mMustVerifyImages = new int[]{R.drawable.icon_verify_msg, R.drawable.icon_verify_card,
            R.drawable.icon_verify_contact, R.drawable.icon_verify_phone, R.drawable.icon_verify_face};

    private int[] mChooseVerifyImages = new int[]{R.drawable.icon_verify_workinfo, R.drawable.icon_verify_more, R.drawable.icon_verify_taobao,};

    private CreditStatusRec statusRec;
    private String[] strs = new String[]{"请先完成个人信息认证", "请先完成银行卡绑定",
            "请先完成紧急联系人认证", "请先完成手机运营商认证"};

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_credit_center;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);
        setPageTitleBack("认证中心");
    }

    @Override
    protected void initFunc() {
        userId = SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId();

        mMustItemLists = new ArrayList<>();
        mChooseItemLists = new ArrayList<>();

        for (int i = 0; i < mMustVerifyImages.length; i++) {
            VerifyItemVM itemVM = new VerifyItemVM();
            itemVM.setImageRes(mMustVerifyImages[i]);
            itemVM.setTitle(mMustVerifyTitles[i]);
            itemVM.setStatus("10");
            mMustItemLists.add(itemVM);
        }

        for (int i = 0; i < mChooseVerifyImages.length - 1; i++) {
            VerifyItemVM itemVM = new VerifyItemVM();
            itemVM.setImageRes(mChooseVerifyImages[i]);
            itemVM.setTitle(mChooseVerifyTitles[i]);
            itemVM.setStatus("10");
            mChooseItemLists.add(itemVM);
        }

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mMustVerifyRc.setLayoutManager(manager);
        StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mChooseVerifyRc.setLayoutManager(manager2);

//        mMustVerifyRc.addItemDecoration(new DividerGridItemDecoration(this));
//        mChooseVerifyRc.addItemDecoration(new DividerGridItemDecoration(this));

        mMustAdapter = new VerifyCenterAdapter(mContext, mMustItemLists, new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                Integer position = (Integer) v.getTag();
                mustVerifyItemClick(position);
            }
        });

        mChooseAdapter = new VerifyCenterAdapter(mContext, mChooseItemLists, new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                Integer position = (Integer) v.getTag();
                chooseVerifyItemClick(position);
            }
        });

        mMustVerifyRc.setAdapter(mMustAdapter);
        mChooseVerifyRc.setAdapter(mChooseAdapter);


        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_color_principal));
        mRefreshLayout.setOnRefreshListener(() -> {

            mRefreshLayout.setRefreshing(false);
            reqVerifyData();
        });

        reqVerifyData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        reqVerifyData();
    }


    /***必选认证点击***/
    private void mustVerifyItemClick(Integer position) {
        switch (position) {
            //个人信息
            case 0:
                if (AndPermission.hasPermissions(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)) {

                    Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditPersonInfo,
                            10)));
                } else {
                    AndPermission.with(mContext)
                            .runtime()
                            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditPersonInfo,
                                            statusRec != null ? statusRec.getIdState() : "10")));
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    DialogUtils.showPermisssionDialog(mContext);
                                }
                            }).start();

//                    DialogUtils.showPermisssionDialog(mContext);
                }
                break;
            //收款银行卡
            case 1:
                if (statusRec != null && statusRec.getIdState().equals("30")) {
                    // NEW STATUS
                    if (Constant.STATUS_30.equals(statusRec.getBankCardState())
                            || Constant.STATUS_20.equals(statusRec.getBankCardState())) {
                        Routers.open(mContext, RouterUrl.getRouterUrl(
                                String.format(RouterUrl.Mine_CreditBindBank, statusRec.getBankCardState())));
                    } else {
                        Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditBank, Constant
                                .STATUS_0)));
                    }
                } else {
                    showDialog(strs[0]);
                }
                break;
            //紧急联系人
            case 2:
                if (AndPermission.hasPermissions(mContext,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_PHONE_STATE)) {

                    if (statusRec != null && statusRec.getBankCardState().equals("30")) {
                        Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditLinker,
                                statusRec.getContactState())));
                    } else {
                        showDialog(strs[1]);
//                        Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditLinker,
//                                "10")));
                    }
                } else {
                    AndPermission.with(mContext)
                            .runtime()
                            .permission(Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditLinker,
                                            statusRec != null ? statusRec.getContactState() : "10")));
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    DialogUtils.showPermisssionDialog(mContext);
                                }
                            }).start();
                    DialogUtils.showPermisssionDialog(mContext);
                }

                break;
            //手机运营商
            case 3:
                if (statusRec != null && statusRec.getContactState().equals("30")) {
                    Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditPhone, statusRec
                            .getPhoneState())));
                } else {
                    showDialog(strs[2]);
                }
                break;

         /*   //淘宝认证
            case 4:
                if (statusRec.getPhoneState().equals("30")) {
                    if (statusRec.getTaobaoState().equals("30")) {
                        showDialog("已认证");
                        return;
                    }
                    doTaoBaoVerify();
                } else {
                    showDialog(strs[3]);
                }

                break;*/

            //人脸识别
            case 4:
                if (AndPermission.hasPermissions(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)) {

                    if (statusRec != null && statusRec.getPhoneState().equals("30")) {
                        Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditPersonFace,
                                statusRec.getLivingIdentifyState())));
                    } else {
                        showDialog(strs[3]);
                    }
                } else {
                    AndPermission.with(mContext)
                            .runtime()
                            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditPersonFace,
                                            statusRec != null ? statusRec.getLivingIdentifyState() : "10")));
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    DialogUtils.showPermisssionDialog(mContext);
                                }
                            }).start();
                }
                break;
            default:
                break;
        }
    }

    /***可选认证点击***/
    private void chooseVerifyItemClick(Integer position) {
        switch (position) {
            //工作信息
            case 0:
                if (AndPermission.hasPermissions(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)) {

                    Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditWork,
                            statusRec.getWorkInfoState())));
                } else {
                    AndPermission.with(mContext)
                            .runtime()
                            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditWork,
                                            statusRec != null ? statusRec.getWorkInfoState() : "10")));
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    DialogUtils.showPermisssionDialog(mContext);
                                }
                            }).start();

                }
                break;
            //更多信息
            case 1:
                Routers.open(mContext, RouterUrl.getRouterUrl(String.format(RouterUrl.Mine_CreditMore,
                        statusRec != null ? statusRec.getOtherInfoState() : "10")));
                break;

            case 2:
                if (statusRec != null && statusRec.getTaobaoState().equals("30")) {
                    showDialog("已认证");

                } else {
                    doTaoBaoVerify();
                }

                break;
            default:
                break;
        }
    }

    //    //合作方系统中的客户ID
    private String userId;
    //     //获取任务状态时使用(合作方申请接入后由魔蝎数据提供)  
//    private String mApiKey     = "c1b0e8bbc6ef4853942c389989786d0f"; // sandBox
    private String mApiKey = "c1b0e8bbc6ef4853942c389989786d0f";// 生产
    //SDK里页面主色调
    private String mThemeColor = "";

    //执行淘宝认证
    private void doTaoBaoVerify() {
        try {

            MxParam mxParam = new MxParam();
            mxParam.setUserId(userId);
            mxParam.setApiKey(mApiKey);
            //传入产品类型--必传
            mxParam.setTaskType(MxParam.PARAM_TASK_TAOBAO);

            MoxieSDK.getInstance().start(this, mxParam, new MoxieCallBack() {

                @Override
                public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {

                    if (moxieCallBackData != null) {
                        Log.d("BigdataFragment", "MoxieSDK Callback Data : " + moxieCallBackData.toString());
                        switch (moxieCallBackData.getCode()) {
                            /**
                             * 账单导入中
                             */
                            case MxParam.ResultCode.IMPORTING:
                                if (moxieCallBackData.isLoginDone()) {
                                    //状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
                                    Log.d(TAG, "任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");

                                } else {
                                    //状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
                                    Log.d(TAG, "任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                                }
                                break;
                            /**
                             * 任务还未开始
                             * 假如有弹框需求，可以参考 {@link BigdataFragment#showDialog(MoxieContext)}
                             *
                             */
                            case MxParam.ResultCode.IMPORT_UNSTART:
                                Log.d(TAG, "任务未开始");
                                break;
                            case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
                                Toast.makeText(mContext, "导入失败(平台方服务问题)", Toast.LENGTH_SHORT).show();
                                break;
                            case MxParam.ResultCode.MOXIE_SERVER_ERROR:
                                Toast.makeText(mContext, "导入失败(魔蝎数据服务异常)", Toast.LENGTH_SHORT).show();
                                break;
                            case MxParam.ResultCode.USER_INPUT_ERROR:
                                Toast.makeText(mContext, "导入失败(" + moxieCallBackData.getMessage() + ")", Toast.LENGTH_SHORT).show();
                                break;
                            case MxParam.ResultCode.IMPORT_FAIL:
                                Toast.makeText(mContext, "导入失败", Toast.LENGTH_SHORT).show();
                                break;
                            case MxParam.ResultCode.IMPORT_SUCCESS:
                                Log.d(TAG, "任务采集成功，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");

                                //根据taskType进行对应的处理
                                switch (moxieCallBackData.getTaskType()) {
                                    case MxParam.PARAM_TASK_TAOBAO:
                                        Toast.makeText(mContext, "淘宝导入成功", Toast.LENGTH_SHORT).show();

                                        String taskId = moxieCallBackData.getTaskId();

                                        System.out.println("taskId:" + taskId);

                                        getResultCallBack(taskId);

                                        break;
                                    default:
                                        Toast.makeText(mContext, "导入成功", Toast.LENGTH_SHORT).show();
                                }
                                moxieContext.finish();
                                return true;
                        }
                    }
                    return false;
                }

                /**
                 * @param moxieContext    可能为null，说明还没有打开魔蝎页面，使用前要判断一下
                 * @param moxieException  通过exception.getExceptionType();来获取ExceptionType来判断目前是哪个错误
                 */
                @Override
                public void onError(MoxieContext moxieContext, MoxieException moxieException) {
                    super.onError(moxieContext, moxieException);
                    if (moxieException.getExceptionType() == ExceptionType.SDK_HAS_STARTED) {
                        Toast.makeText(mContext, moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (moxieException.getExceptionType() == ExceptionType.SDK_LACK_PARAMETERS) {
                        Toast.makeText(mContext, moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (moxieException.getExceptionType() == ExceptionType.WRONG_PARAMETERS) {
                        Toast.makeText(mContext, moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("BigdataFragment", "MoxieSDK onError : " + (moxieException != null ? moxieException.toString() : ""));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getResultCallBack(String taskId) {
        Call<HttpResult<Object>> call = RDClient.getService(MineService.class).moXieResultCallBack(taskId);
        call.enqueue(new RequestCallBack<HttpResult<Object>>() {
            @Override
            public void onSuccess(Call<HttpResult<Object>> call, Response<HttpResult<Object>> response) {
            }
        });
    }


    public void reqVerifyData() {
        Call<HttpResult<CreditStatusRec>> call = RDClient.getService(MineService.class).getUserAuth();
        call.enqueue(new RequestCallBack<HttpResult<CreditStatusRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<CreditStatusRec>> call, Response<HttpResult<CreditStatusRec>> response) {
                if (response.body().getData() != null) {
                    statusRec = response.body().getData();
                    chageBtnStatus(statusRec);
                }
            }
        });
    }

    private void chageBtnStatus(CreditStatusRec statusRec) {
        mMustItemLists.get(0).setStatus(statusRec.getIdState());
        mMustItemLists.get(1).setStatus(statusRec.getBankCardState());
        mMustItemLists.get(2).setStatus(statusRec.getContactState());
        mMustItemLists.get(3).setStatus(statusRec.getPhoneState());
        //mMustItemLists.get(4).setStatus(statusRec.getTaobaoState());
        mMustItemLists.get(4).setStatus(statusRec.getLivingIdentifyState());

        mChooseItemLists.get(0).setStatus(statusRec.getWorkInfoState());
        mChooseItemLists.get(1).setStatus(statusRec.getOtherInfoState());
        //mChooseItemLists.get(2).setStatus(statusRec.getTaobaoState());

        mMustAdapter.notifyDataSetChanged();
        mChooseAdapter.notifyDataSetChanged();
    }


    public void showDialog(String hint) {
        DialogUtils.showConfirmDialog(mContext, hint, null);
    }

}
