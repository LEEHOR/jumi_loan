package com.cashloan.jumidai.ui.user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.cashloan.jumidai.BuildConfig;
import com.cashloan.jumidai.MyApplication;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.CommonType;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.RxBusEvent;
import com.cashloan.jumidai.network.api.UserService;
import com.cashloan.jumidai.ui.user.UserLogic;
import com.cashloan.jumidai.ui.user.bean.LoginVM;
import com.cashloan.jumidai.ui.user.bean.RegisterVM;
import com.cashloan.jumidai.ui.user.bean.TestUrlBean;
import com.cashloan.jumidai.ui.user.bean.receive.IsExistsRec;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.ui.user.bean.receive.ProbeSmsRec;
import com.cashloan.jumidai.ui.user.bean.submit.LoginSub;
import com.cashloan.jumidai.ui.user.bean.submit.Login_log;
import com.cashloan.jumidai.ui.user.bean.submit.RegisterSub;
import com.cashloan.jumidai.utils.DeviceUtil;
import com.cashloan.jumidai.utils.GaodeMapLocationHelper;
import com.cashloan.jumidai.utils.InputCheck;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.getImei;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.interceptor.HttpLoggingInterceptor;
import com.commom.net.RxBus.RxBus;
import com.commom.utils.ActivityManage;
import com.commom.utils.ContextHolder;
import com.commom.utils.EmptyUtils;
import com.commom.utils.RegularUtil;
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
import com.google.gson.Gson;
import com.umeng.analytics.AnalyticsConfig;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.IOException;
import java.net.Proxy;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/27
 * 描述： 登陆页面
 */
@Router(value = RouterUrl.UserInfoManage_Login, stringParams = BundleKeys.TYPE)
public class LoginActivity extends BaseMvpActivity {


    @ViewInject(R.id.iv_login_back)
    private ImageView ivBack;

    @ViewInject(R.id.ll_login_stepOne)
    private LinearLayout llLoginPageCode;//验证码登录页

    @ViewInject(R.id.til_phone)
    private TextInputLayout tilPhone;
    @ViewInject(R.id.til_pwd)
    private TextInputLayout tilPwd;

    @ViewInject(R.id.cet_login_phone)
    private TextInputEditText etPhone;//验证码登录--手机号
    @ViewInject(R.id.ndb_login_next)
    private NoDoubleClickButton btnCodeLogin;

    @ViewInject(R.id.ll_login_stepTwo)
    private LinearLayout llLoginPagePwd;//密码登录页

    @ViewInject(R.id.tv_login_phoneNum)
    private TextView tvPhoneShow;
    @ViewInject(R.id.cet_login_pwd)
    private TextInputEditText etPwdLoginPwd;//密码登录--密码
    @ViewInject(R.id.ndb_login_now)
    private NoDoubleClickButton btnPwdLogin;

    @ViewInject(R.id.tv_login_forget)
    private TextView tvForget;

    @ViewInject(R.id.tv_login_title)
    private TextView tvChangeService;

    private LoginVM loginVM;

    /**
     * update  start
     */
    @ViewInject(R.id.tv_change_pwdLogin)
    private TextView tvChangePwdLogin;//切换为密码登录
    @ViewInject(R.id.tv_change_codeLogin)
    private TextView tvChangeCodeLogin;//切换为验证码登录

    @ViewInject(R.id.til_pwdLogin_phone)
    private TextInputLayout tilPwdLoginPhone;
    @ViewInject(R.id.cet_pwdLogin_phone)
    private TextInputEditText etPwdLoginPhone;//密码登录--手机号


    @ViewInject(R.id.timeButton)
    private TimeButton timeButton;// 获取验证码按钮

    @ViewInject(R.id.code)
    private ClearEditText cetCode;//验证码登录--验证码

    private boolean isPhoneRegister = true;
    private GaodeMapLocationHelper.MyLocationListener myLocationListener;

    /**
     * update  end
     */

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);
        getPermissionLocation();
        mStatusBar.setBackgroundResource(R.color.white);
        ScreenUtils.setTextColorStatusBar(this, true);
    }

    @Override
    protected void initFunc() {
        loginVM = new LoginVM();

        etPhone.addTextChangedListener(PhoneTextWatcher);
        cetCode.addTextChangedListener(PhoneTextWatcher);

        etPwdLoginPwd.addTextChangedListener(PhoneTextWatcher);

        tvChangePwdLogin.setVisibility(View.GONE);

        attachClickListener(btnCodeLogin);
//        attachClickListener(btnPwdLogin);
//        attachClickListener(tvForget);
        attachClickListener(ivBack);

        attachClickListener(tvChangeCodeLogin);
//        attachClickListener(tvChangePwdLogin);
        attachClickListener(timeButton);


        checkTest();
        getImeI();
    }


    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            //验证码登录
            case R.id.ndb_login_next:
                if (!isPhoneRegister) {//手机号未注册
                    submitRegisterClick(view);
                } else {//手机号已注册--调用新的登录接口
                    submitCodeLoginClick(view);
                }
                break;
//            //密码登录
//            case R.id.ndb_login_now:
//                submitClick(view);
//                break;

//            case R.id.tv_login_forget:
//                forgotClick(view);
//                break;
            case R.id.iv_login_back:
                onBackPressed();
                break;

            //获取验证码
            case R.id.timeButton:
                checkIsPhoneNumExist(view);
                break;
//            case R.id.tv_change_pwdLogin:
//                llLoginPageCode.setVisibility(View.GONE);
//                llLoginPagePwd.setVisibility(View.VISIBLE);
//                break;
//            case R.id.tv_change_codeLogin:
//                llLoginPageCode.setVisibility(View.VISIBLE);
//                llLoginPagePwd.setVisibility(View.GONE);
//                break;
            default:
                break;

        }
    }

    private TextWatcher PhoneTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (llLoginPageCode.getVisibility() == View.VISIBLE) {
                if (EmptyUtils.isNotEmpty(etPhone.getText().toString())
                        && EmptyUtils.isNotEmpty(cetCode.getText().toString())) {
                    btnCodeLogin.setEnabled(true);
                } else {
                    btnCodeLogin.setEnabled(false);
                }
            }
//            if (llLoginPagePwd.getVisibility() == View.VISIBLE) {
//                if (EmptyUtils.isNotEmpty(etPwdLoginPhone.getText().toString())
//                        && EmptyUtils.isNotEmpty(etPwdLoginPwd.getText().toString())) {
//                    btnPwdLogin.setEnabled(true);
//                } else {
//                    btnPwdLogin.setEnabled(false);
//                }
//            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    public void onBackPressed() {
        String type = getIntent().getStringExtra(BundleKeys.TYPE);
        if ("1".equals(type)) {
            Routers.open(getApplicationContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_Main, Constant.NUMBER_1)));
        } else {
            Routers.open(getApplicationContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_Main, Constant.NUMBER_0)));
        }
        super.onBackPressed();
    }


    /**
     * 登录按钮
     */
    public void submitClick(final View view) {
        loginVM.setPhone(etPwdLoginPhone.getText().toString());
        loginVM.setPwd(etPwdLoginPwd.getText().toString());

        if (!RegularUtil.isPhone(loginVM.getPhone())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.forgot_phone_hint_step_1_error));
            return;
        }

        if (!InputCheck.checkPwd(loginVM.getPwd())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.settings_pwd_desc));
            return;
        }
        LoginSub sub = new LoginSub(loginVM.getPhone(), loginVM.getPwd());
        sub.setBox("");

        Call<HttpResult<OauthTokenMo>> call = RDClient.getService(UserService.class).doLogin(sub);
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<OauthTokenMo>>() {
            @Override
            public void onSuccess(Call<HttpResult<OauthTokenMo>> call,
                                  Response<HttpResult<OauthTokenMo>> response) {
                OauthTokenMo mo = response.body().getData();
                mo.setUsername(loginVM.getPhone());
                RxBus.get().send(RxBusEvent.LOGIN_SUCCESS);

                UserLogic.login(Util.getActivity(view), mo);
                onTagAliasAction();
            }
        });
    }

    /**
     * 短信验证码登录--提交数据
     */
    public void submitCodeLoginClick(final View view) {
        loginVM.setPhone(etPhone.getText().toString());
        loginVM.setLoginCode(cetCode.getText().toString());
        if (!RegularUtil.isPhone(loginVM.getPhone())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.forgot_phone_hint_step_1_error));
            return;
        }

        if (EmptyUtils.isEmpty(loginVM.getLoginCode())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.register_code_hint));
            return;
        }
        //登录
        LoginMethods(view);

    }

    /**
     * 点击获取验证码，判断手机号是否注册
     */
    public void checkIsPhoneNumExist(final View view) {
        loginVM.setPhone(etPhone.getText().toString());

        if (!RegularUtil.isPhone(loginVM.getPhone())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.forgot_phone_hint_step_1_error));
            return;
        }
        Call<HttpResult<IsExistsRec>> phoneCall = RDClient.getService(UserService.class).isPhoneExists(loginVM.getPhone());
        NetworkUtil.showCutscenes(phoneCall);
        phoneCall.enqueue(new RequestCallBack<HttpResult<IsExistsRec>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Call<HttpResult<IsExistsRec>> call, Response<HttpResult<IsExistsRec>> response) {
                if (Constant.STATUS_10.equals(response.body().getData().getIsExists())) {

                    // 手机号未注册--获取注册验证码
                    isPhoneRegister = false;
                    getCodeClick(view, CommonType.REGISTER_CODE);
                } else {
                    loginVM.setStep(false);

                    // 手机号已经注册--获取登录验证码
                    isPhoneRegister = true;
                    getCodeClick(view, CommonType.LOGIN_CODE);
                }

            }
        });
    }


    /**
     * 忘记密码按钮
     */
    public void forgotClick(View view) {
        Routers.open(view.getContext(), RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_ForgotPwd,
                loginVM.getPhone(), Constant.STATUS_2)));
    }

    //update 2018-8-8
    private AlertDialog mDialog;
    private ImageView ivYanZhengMa;

//    public void showBitmapCodeDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_get_image_code, null, false);
//        ClearEditText etImageCode = view.findViewById(R.id.cet_dialog_bitmapcode);
//        ivYanZhengMa = view.findViewById(R.id.iv_imgYanzheng);
//        NoDoubleClickButton btnCodeLogin = view.findViewById(R.id.btn_dialog_next_reigster);
//
//        ivYanZhengMa.setOnClickListener(new OnOnceClickListener() {
//            @Override
//            public void onOnceClick(View v) {
//                getBitmapCodeAndInfo();
//            }
//        });
//
//        btnCodeLogin.setOnClickListener(new OnOnceClickListener() {
//            @Override
//            public void onOnceClick(View v) {
//                if (TextUtils.isEmpty(etImageCode.getText().toString())) {
//                    ToastUtil.toast(R.string.register_bitmapcode_hint);
//                    return;
//                } else if (!codeStr.equalsIgnoreCase(etImageCode.getText().toString())) {
//                    ToastUtil.toast("图形验证码输入不正确");
//                    return;
//                }
//                Routers.open(view.getContext(), RouterUrl.getRouterUrl(
//                        String.format(RouterUrl.UserInfoManage_Register, loginVM.getPhone(), codeStr)));
//
//                if (EmptyUtils.isNotEmpty(mDialog)) {
//                    mDialog.dismiss();
//                    mDialog = null;
//                }
//            }
//        });
//
//        getBitmapCodeAndInfo();
//
//        builder.setView(view);
//        mDialog = builder.create();
//        mDialog.show();
//    }

    public Bitmap bitmap;
    public String codeStr = "";

//    private void getBitmapCodeAndInfo() {
//        BitmapCodeUtil codeUtil = BitmapCodeUtil.getInstance();
//        bitmap = codeUtil.createBitmap();
//        codeStr = codeUtil.getCode().toLowerCase();
//
//        if (EmptyUtils.isNotEmpty(ivYanZhengMa)) {
//            ivYanZhengMa.setImageBitmap(bitmap);
//        }
//    }


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


    /***
     * update new  注册逻辑
     */

    public RegisterVM mRegisterVM;
    public String address;
    public String coordinate;
    public long location_time;

    /**
     * 注册获取验证码   TODO  注册获取验证码接口 登录获取验证码接口是否一致  SIGN类型
     */
    public void getCodeClick(final View view, String codeType) {

        String sign = MDUtil.encode(MDUtil.TYPE.MD5, AppConfig.APP_KEY + loginVM.getPhone() + codeType);

        Call<HttpResult<ProbeSmsRec>> callCode = RDClient.getService(UserService.class)
                .getCode(loginVM.getPhone(), codeType, sign);
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

//        Toast.makeText(mContext, "点击了获取验证码", Toast.LENGTH_SHORT).show();

    }


    /**
     * 注册_信息提交
     */
    public void submitRegisterClick(final View view) {
        mRegisterVM = new RegisterVM();
        mRegisterVM.setPhone(etPhone.getText().toString());
        mRegisterVM.setCode(cetCode.getText().toString().trim());

        mRegisterVM.setBitmapCode(codeStr);
//        mRegisterVM.setPwd("");
//        mRegisterVM.setInvite(cetInvite.getText().toString().trim());

        if (!RegularUtil.isPhone(mRegisterVM.getPhone())) {
            ToastUtil.toast(ContextHolder.getContext().getString(R.string.forgot_phone_hint_step_1_error));
            return;
        }

        if (TextUtils.isEmpty(mRegisterVM.getCode())) {
            ToastUtil.toast(R.string.register_code_hint);
            return;
        }

        //注册
        RegisterMethods(view);
    }


    //----------------------------------------------------------------------------------------------
    //-----------------------------------FOR DEBUG-------------------------------------------------------

    private void checkTest() {
        if (!BuildConfig.DEBUG) {
//            ToastUtil.toast("并非debug模式");
            return;
        } else {
            tvChangeService.setOnClickListener(new View.OnClickListener() {

                //需要监听几次点击事件数组的长度就为几
                //如果要监听双击事件则数组长度为2，如果要监听3次连续点击事件则数组长度为3...
                long[] mHints = new long[3];//初始全部为0

                @Override
                public void onClick(View v) {
                    //将mHints数组内的所有元素左移一个位置
                    System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
                    //获得当前系统已经启动的时间
                    mHints[mHints.length - 1] = SystemClock.uptimeMillis();
                    if (SystemClock.uptimeMillis() - mHints[0] <= 500) {
                        doSth();
                        Toast.makeText(getApplicationContext(), "点击三次之后才会出现", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void doSth() {
        String testUrl = (String) SharedInfo.getInstance().getValue("test_url", "");

        if (EmptyUtils.isEmpty(testUrl)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("当前测试为空，是否获取测试地址？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getLatestTest();
                                }
                            })
                    .setNegativeButton("取消", null);

            builder.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("当前测试地址为\n" + testUrl + "\n是否需要更新？")
                    .setPositiveButton("更新",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getLatestTest();
                                }
                            }).setNegativeButton("不用", null);
            builder.show();
        }
    }


    private void getLatestTest() {

        // 创建一个OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.proxy(Proxy.NO_PROXY);
        // 设置网络请求超时时间
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);

        // 打印参数
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
//                if (AppConfig.IS_DEBUG) {
                Log.i("RetrofitLog", "retrofitBack = " + message);
//                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        // 失败后尝试重新请求
        builder.retryOnConnectionFailure(true);

        Request request = new Request.Builder().url("https://9quzu-app.oss-cn-hangzhou.aliyuncs.com/Android.json").get().build();
        okhttp3.Call call = builder.build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                String testUrlMessage = response.body().string();
                Log.d("splash--", "onResponse: " + testUrlMessage);

                if (EmptyUtils.isNotEmpty(testUrlMessage)) {
                    Gson gson = new Gson();
                    TestUrlBean urlBean = gson.fromJson(testUrlMessage, TestUrlBean.class);

                    SharedInfo.getInstance().saveValue("test_url", urlBean.getRul());
                }
//                Toast.makeText(getApplicationContext(), "获取成功，请重启应用测试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取设备指纹、唯一识别码、手机品牌
     */
    private void getImeI() {
        getImei getImei = new getImei(this);
        Constant.IMEI = getImei.getimei();
        Constant.PHONEBRAND = DeviceUtil.getPhoneBrand();
        Constant.PHONEMODEL = DeviceUtil.getPhoneModel();
        System.out.println("ImeI2" + Constant.IMEI + "/" + Constant.IMSI + "/" + Constant.ANDROID_ID + "/" + Constant.PHONEBRAND + Constant.PHONEMODEL);
    }

    /**
     * @param userId
     * @param type
     * @param view
     * @param mo
     * @param loginType 登陆还是注册
     */
    private void upload_login_log(long userId, String type, View view, OauthTokenMo mo, int loginType) {
        Login_log login_log = new Login_log();
        login_log.setBox(Constant.IMSI);
        login_log.setClient("Android");
        login_log.setDeviceId(Constant.IMEI);
        login_log.setAddress(address);
        login_log.setCoordinate(coordinate);
        login_log.setLocationTime(location_time);
        login_log.setTermModel(Constant.PHONEBRAND + Constant.PHONEMODEL);
        login_log.setType(type);
        login_log.setUpdateTime(System.currentTimeMillis());
        login_log.setUserId(userId);
        Call<HttpResult> httpResultCall = RDClient.getService(UserService.class).login_log(login_log);
        httpResultCall.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
//                onTagAliasAction();
//
//                UserLogic.login(Util.getActivity(view), mo);
//                if (loginType == 1) {
//                    RxBus.get().send(RxBusEvent.LOGIN_SUCCESS);
//                    UserLogic.login(Util.getActivity(view), mo);
//                } else {
//                    RxBus.get().send(RxBusEvent.REGISTER_SUCCESS);
//                    UserLogic.login(Util.getActivity(view), mo);
//                    Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.UserInfoManage_UserHomePage));
//                    Util.getActivity(view).finish();
//                }
            }
        });
    }

    /**
     * 手机号登录方法
     */
    private void LoginMethods(View view) {
        LoginSub sub = new LoginSub();
        sub.setId(loginVM.getPhone());
        sub.setCode(loginVM.getLoginCode());
        sub.setBox(Constant.IMEI);
        // sub.setClient("Android");
        //Up_login_loadImeI(sub);
        //sub.setRegisterAddr(address);
        // sub.setRegisterCoordinate(coordinate);
        /*sub.setDevice_id(Constant.IMEI);
        sub.setBox(Constant.IMEI);
        sub.setLocation_time(location_time);
        sub.setTerm_model(Constant.PHONEBRAND+Constant.PHONEMODEL);
        sub.setUpdate_time(System.currentTimeMillis());*/
        Call<HttpResult<OauthTokenMo>> call = RDClient.getService(UserService.class).doCodeLogin(sub);
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<OauthTokenMo>>() {
            @Override
            public void onSuccess(Call<HttpResult<OauthTokenMo>> call,
                                  Response<HttpResult<OauthTokenMo>> response) {
                OauthTokenMo mo = response.body().getData();
                mo.setUsername(loginVM.getPhone());
                //MyApplication.closeGps();
                //上传登录日志
              //  NetworkUtil.dismissCutscenes();
                RxBus.get().send(RxBusEvent.LOGIN_SUCCESS);
                upload_login_log(Long.valueOf(mo.getUserId()), Constant.TYPE_20, view, mo, 1);
                UserLogic.login(Util.getActivity(view), mo);
                onTagAliasAction();
            }
        });
    }

    /**
     * 手机号注册的方法
     */
    private void RegisterMethods(View view) {
        RegisterSub sub = new RegisterSub();
        sub.setPhone(mRegisterVM.getPhone());
//        sub.setPwd(mRegisterVM.getPwd());
        sub.setCode(mRegisterVM.getCode());
        sub.setInviter(mRegisterVM.getInvite());
        sub.setRegisterAddr(address);
        sub.setRegisterCoordinate(coordinate);
        sub.setClient("Android");
        sub.setBox(Constant.IMEI);
        sub.setBrowser("30");
        /*update 2019-7-19*/
        //  sub.setDevice_id(Constant.IMEI);
        //
        // sub.setLocation_time(location_time);
        // sub.setTerm_model(Constant.PHONEBRAND+Constant.PHONEMODEL);
        // sub.setUpdate_time(System.currentTimeMillis());
        sub.setChannelCode(Util.changeChannelCodeToNumberId(AnalyticsConfig.getChannel(ActivityManage.peek())));
        Call<HttpResult<OauthTokenMo>> call = RDClient.getService(UserService.class).doRegister(sub);
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<OauthTokenMo>>() {
            @Override
            public void onSuccess(Call<HttpResult<OauthTokenMo>> call, Response<HttpResult<OauthTokenMo>> response) {
                //MyApplication.closeGps();
                SharedInfo.getInstance().saveValue(Constant.IS_LAND, true);
                OauthTokenMo mo = response.body().getData();
                mo.setUsername(mRegisterVM.getPhone());
                SharedInfo.getInstance().saveEntity(mo);
               //   UserLogic.login(Util.getActivity(view), mo);
                RxBus.get().send(RxBusEvent.REGISTER_SUCCESS);
                //上传登录日志
                upload_login_log(Long.valueOf(mo.getUserId()), Constant.TYPE_10, view, mo, 2);

                UserLogic.login(Util.getActivity(view), mo);
//                onTagAliasAction();
//                RxBus.get().send(RxBusEvent.REGISTER_SUCCESS);
                Routers.open(view.getContext(), RouterUrl.getRouterUrl(RouterUrl.UserInfoManage_UserHomePage));
                Util.getActivity(view).finish();
            }
        });
    }


    /**
     * 获取定位权限 并打开定位
     */
    private void getPermissionLocation() {

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
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            openLocation();
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            Toast.makeText(mContext, "需要开启定位权限", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .start();
        } else {
            openLocation();
        }
    }

    /**
     * 开启长定位
     */
    private void openLocation() {
        MyApplication.closeGps();
        MyApplication.openGps(new MyApplication.OnPosChanged() {
            @Override
            public void changed(AMapLocation location) {
                address = location.getAddress();
                coordinate = location.getLongitude() + "," + location.getLatitude();
                location_time = location.getTime();
                Log.e("定位", address + "/" + coordinate + "/" + location_time);
            }
        }, true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.closeGps();
    }
}
