package com.cashloan.jumidai.ui.user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.cashloan.jumidai.network.api.UserService;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CommonRec;
import com.cashloan.jumidai.ui.user.bean.RegisterVM;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.ui.user.bean.receive.ProbeSmsRec;
import com.cashloan.jumidai.ui.user.bean.submit.RegisterSub;
import com.cashloan.jumidai.utils.DisplayFormat;
import com.cashloan.jumidai.utils.InputCheck;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.net.RxBus.RxBus;
import com.commom.utils.ActivityManage;
import com.commom.utils.ContextHolder;
import com.commom.utils.EmptyUtils;
import com.commom.utils.ScreenUtils;
import com.commom.utils.TextUtil;
import com.commom.utils.ToastUtil;
import com.commom.utils.encryption.MDUtil;
import com.commom.utils.log.Logger;
import com.commom.widget.NoDoubleClickButton;
import com.commom.widget.TimeButton;
import com.commom.widget.editText.ClearEditText;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;
import com.umeng.analytics.AnalyticsConfig;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/28
 * 描述： 注册页面
 */
@Router(value = RouterUrl.UserInfoManage_Register, stringParams = {BundleKeys.ID, BundleKeys.CODE})
public class RegisterActivity extends BaseMvpActivity {


    @ViewInject(R.id.iv_register_back)
    private ImageView ivBack;
    @ViewInject(R.id.tv_register_phone)
    private TextView  tvPhone;
    @ViewInject(R.id.code)
    private ClearEditText cetCode;
    @ViewInject(R.id.newPwd)
    private ClearEditText cetNewPwd;

    @ViewInject(R.id.timeButton)
    private TimeButton          timeButton;
    @ViewInject(R.id.agree)
    private CheckBox            cbAgree;
    @ViewInject(R.id.tv_register_agreement)
    private TextView            tvAgreement;
    @ViewInject(R.id.ndb_register)
    private NoDoubleClickButton btnRegister;

    private String       phone;
    public  RegisterVM   viewModel;
    /**
     * 图形验证码校验时需要的sid
     */
    private String       sid;
    private LinearLayout protocolLayout;
    public  String       address;
    public  String       coordinate;

    public static Bitmap bitmap;
    public static String codeStr;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        mStatusBar.setBackgroundResource(R.color.white);
        ScreenUtils.setTextColorStatusBar(this, true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initFunc() {
        phone = getIntent().getStringExtra(BundleKeys.ID);
        codeStr = getIntent().getStringExtra(BundleKeys.CODE);

        viewModel = new RegisterVM();
        viewModel.setPhone(phone);
        if (EmptyUtils.isNotEmpty(codeStr)) {
            viewModel.setBitmapCode(codeStr);
        }
        if (EmptyUtils.isNotEmpty(phone)) {
            tvPhone.setText("您好，" + DisplayFormat.accountHideFormat(phone));
        }

        attachClickListener(ivBack);
        attachClickListener(timeButton);
        attachClickListener(btnRegister);
        attachClickListener(tvAgreement);
        cbAgree.setChecked(true);


        reqData();
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_register_back:
                finish();
                break;

            case R.id.timeButton:
                getCodeClick(view);
                break;
            case R.id.ndb_register:
                if (cbAgree.isChecked()) {
                    submitClick(view);
                } else {
                    ToastUtil.toast("您还没有同意注册协议");
                }
                break;
            case R.id.tv_register_agreement:
                Routers.open(view.getContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_WebView,
                        agreementItem.getName(), CommonType.getUrl(agreementItem.getValue()), "")));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndPermission.with(mContext).runtime().permission(Manifest.permission.ACCESS_FINE_LOCATION).start();
        if (!MyApplication.isOpen(getApplicationContext())) {
            DialogUtils.showSureCancelDialog(mContext, getResources().getString(R.string.register_gps_state),
                    "取消", "确定",
                    new DialogUtils.btnCancelClick() {
                        @Override
                        public void cancel() {
                            ActivityManage.pop();
                        }
                    }, new DialogUtils.btnConfirmClick() {
                        @Override
                        public void confirm() {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0);
                        }
                    });
        } else {
            if (TextUtil.isEmpty(MyApplication.address) || 0 == MyApplication.lat || 0 == MyApplication.lon) {
                MyApplication.openGps(new MyApplication.OnPosChanged() {
                    @Override
                    public void changed(AMapLocation location) {
                        address = location.getAddress();
                        coordinate = location.getLongitude() + "," + location.getLatitude();
                    }
                }, true);
            } else {
                address = MyApplication.address;
                coordinate = MyApplication.lon + "," + MyApplication.lat;
            }
        }
    }


//    private void getBitmapCodeAndInfo() {
//        BitmapCodeUtil codeUtil = BitmapCodeUtil.getInstance();
//        bitmap = codeUtil.createBitmap();
//        codeStr = codeUtil.getCode().toLowerCase();
//
//        ivYanZheng.setImageBitmap(bitmap);
//    }
//
//
//    public void changeImage(View view) {
//        getBitmapCodeAndInfo();
//    }

    /**
     * 获取验证码
     */
    public void getCodeClick(final View view) {
//        mRegisterVM.setBitmapCode(cetBitmapCode.getText().toString().trim());
        viewModel.setBitmapCode(codeStr);

//        if (TextUtils.isEmpty(mRegisterVM.getBitmapCode())) {
//            ToastUtil.toast(R.string.register_bitmapcode_hint);
//            return;
//        } else if (!codeStr.equalsIgnoreCase(mRegisterVM.getBitmapCode())) {
//            ToastUtil.toast("图形验证码输入不正确");
//            return;
//        } else {

        String sign = MDUtil.encode(MDUtil.TYPE.MD5, AppConfig.APP_KEY + viewModel.getPhone() + CommonType.REGISTER_CODE);

        Call<HttpResult<ProbeSmsRec>> callCode = RDClient.getService(UserService.class)
                .getCode(viewModel.getPhone(), CommonType.REGISTER_CODE, sign);
        callCode.enqueue(new RequestCallBack<HttpResult<ProbeSmsRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<ProbeSmsRec>> call, Response<HttpResult<ProbeSmsRec>> response) {
                if (!Constant.STATUS_10.equals(response.body().getData().getState())) {
                    ToastUtil.toast(response.body().getData().getMessage());

                } else {
                    timeButton.runTimer();
                    ToastUtil.toast(response.body().getMsg());
                }
            }
        });
//        }
    }

    /**
     * 初始化协议
     */
    private void reqData() {
        Call<HttpResult<ListData<CommonRec>>> call = RDClient.getService(CommonService.class).protocolList();
        call.enqueue(new RequestCallBack<HttpResult<ListData<CommonRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<CommonRec>>> call,
                                  Response<HttpResult<ListData<CommonRec>>> response) {
                protocolInit(response.body().getData().getList());
            }
        });
    }

    private CommonRec agreementItem;

    private void protocolInit(List<CommonRec> list) {
        List<CommonRec> temp = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (CommonType.PROTOCOL_REGISTER.equals(list.get(i).getCode())) {
                    temp.add(list.get(i));
                    agreementItem = list.get(i);
                    tvAgreement.setText("《" + list.get(i).getName() + "》");
                }
            }
        }
        viewModel.setProtocolList(temp);
    }

    private View.OnFocusChangeListener MyFoucsChange = (view, b) -> editFocusChange(view, b);


    // TODO: 2018/8/28  更改高亮图片
    public void editFocusChange(View view, boolean focus) {
        switch (view.getId()) {
//            case R.id.bitmapcode:
//                if (focus) {
//                    mRegisterVM.setBitmapCodeSel(true);
//                } else {
//                    mRegisterVM.setBitmapCodeSel(false);
//                }
//                break;

            case R.id.code:
                if (focus) {
                    viewModel.setCodeSel(true);
                } else {
                    viewModel.setCodeSel(false);
                }
                break;
            case R.id.newPwd:
                if (focus) {
                    viewModel.setPwdSel(true);
                } else {
                    viewModel.setPwdSel(false);
                }
                break;
//            case R.id.invite:
//                if (focus) {
//                    mRegisterVM.setInviteSel(true);
//                } else {
//                    mRegisterVM.setInviteSel(false);
//                }
//                break;
        }
    }

    /**
     * 注册协议点击
     */
    public void protocolClick(View view) {
        ToastUtil.toast("注册协议");
    }

    /**
     * 注册_信息提交
     */
    public void submitClick(final View view) {
        viewModel.setBitmapCode(codeStr);
        viewModel.setCode(cetCode.getText().toString().trim());
        viewModel.setPwd(cetNewPwd.getText().toString().trim());
//        mRegisterVM.setInvite(cetInvite.getText().toString().trim());


        boolean hasDeniePermission = AndPermission.hasPermissions(this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (!hasDeniePermission) {
            AndPermission.with(this)
                    .runtime()
                    .permission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE})
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            Toast.makeText(mContext, "需要开启定位权限", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .start();
            return;
        }

//        if (TextUtils.isEmpty(mRegisterVM.getBitmapCode())) {
//            ToastUtil.toast(R.string.register_bitmapcode_hint);
//            return;
//        }
        if (TextUtils.isEmpty(viewModel.getCode())) {
            ToastUtil.toast(R.string.register_code_hint);
            return;
        }
        if (TextUtils.isEmpty(viewModel.getPwd())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.login_pwd_hint));
            return;
        }
        if (!InputCheck.checkPwd(viewModel.getPwd())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.forgot_pwd_tips));
            return;
        }

        if (MyApplication.isOpen(mContext)) {

        }
        System.out.println("login action1");
        if (TextUtil.isEmpty(address) || TextUtil.isEmpty(coordinate)) {
            NetworkUtil.showCutscenes("", "");
            MyApplication.openGps(new MyApplication.OnPosChanged() {
                @Override
                public void changed(AMapLocation location) {
                    address = location.getAddress();
                    coordinate = location.getLongitude() + "," + location.getLatitude();
                    NetworkUtil.dismissCutscenes();
                }
            }, true);
            return;
        }

        RegisterSub sub = new RegisterSub();
        sub.setPhone(viewModel.getPhone());
        sub.setPwd(viewModel.getPwd());
        sub.setCode(viewModel.getCode());
        sub.setInviter(viewModel.getInvite());
        sub.setRegisterAddr(address);
        sub.setRegisterCoordinate(coordinate);
        sub.setClient("android");
        sub.setChannelCode(Util.changeChannelCodeToNumberId(AnalyticsConfig.getChannel(ActivityManage.peek())));
        sub.setBox("");

        Call<HttpResult<OauthTokenMo>> call = RDClient.getService(UserService.class).doRegister(sub);
        call.enqueue(new RequestCallBack<HttpResult<OauthTokenMo>>() {
            @Override
            public void onSuccess(Call<HttpResult<OauthTokenMo>> call, Response<HttpResult<OauthTokenMo>> response) {

                SharedInfo.getInstance().saveValue(Constant.IS_LAND, true);
                OauthTokenMo mo = response.body().getData();
                mo.setUsername(viewModel.getPhone());
                SharedInfo.getInstance().saveEntity(mo);
                System.out.println("login Success");
                onTagAliasAction();

                RxBus.get().send(RxBusEvent.REGISTER_SUCCESS);

                Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.UserInfoManage_UserHomePage));
                Util.getActivity(view).finish();
            }
        });
    }


    /**
     * 处理tag/alias相关操作的点击
     */
    public void onTagAliasAction() {
//        Set<String> tags = null;
//        String alias = null;
//        int action = -1;
//        boolean isAliasAction = false;
//
//        //设置tag
//
//        tags = getInPutTags();
//        if (tags == null) {
//            return;
//        }
//        action = ACTION_SET;
//
//
//        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
//        tagAliasBean.action = action;
//        sequence++;
//        if (isAliasAction) {
//            tagAliasBean.alias = alias;
//        } else {
//            tagAliasBean.tags = tags;
//        }
//        tagAliasBean.isAliasAction = isAliasAction;
//        TagAliasOperatorHelper.getInstance().handleAction(ActivityManage.peek(), sequence, tagAliasBean);
    }

    /**
     * 获取输入的tags
     */
    private Set<String> getInPutTags() {
        String tag = SharedInfo.getInstance().getEntity(OauthTokenMo.class).getUserId();
        Set<String> tagSet = new LinkedHashSet<String>();

        Logger.i("Jpush-tag", tag);
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            return null;
        } else {
            tagSet.add(tag);
        }
        return tagSet;
    }
}
