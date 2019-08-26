package com.cashloan.jumidai.ui.homeMine.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
import com.authreal.component.AuthComponentFactory;
import com.authreal.component.OCRComponent;
import com.bigkoo.pickerview.OptionsPickerView;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.BaseParams;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.DicKey;
import com.cashloan.jumidai.common.RequestResultCode;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.UrlUtils;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.CreditPersonVM;
import com.cashloan.jumidai.ui.homeMine.bean.YouDunSessionDM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditPersonRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.DicRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.FaceOcrRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.IdCardTimeRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.KeyValueRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.RegardOcrMsgRec;
import com.cashloan.jumidai.ui.homeMine.bean.submit.CreditPersonSub;
import com.cashloan.jumidai.ui.homeMine.bean.submit.IdCardSyncSub;
import com.cashloan.jumidai.ui.homeMine.bean.submit.UpdatePersonSub;
import com.cashloan.jumidai.ui.user.PhotographLogic;
import com.cashloan.jumidai.utils.MD5Utils;
import com.cashloan.jumidai.utils.ObjectDynamicCreator;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.utils.FileUploadUtil;
import com.commom.utils.ActivityManage;
import com.commom.utils.BitmapUtil;
import com.commom.utils.ContextHolder;
import com.commom.utils.EmptyUtils;
import com.commom.utils.FileUtil;
import com.commom.utils.ImageLoaderUtils.GlideUtils;
import com.commom.utils.OnOnceClickListener;
import com.commom.utils.StringUtils;
import com.commom.utils.ToastUtil;
import com.commom.widget.editText.ClearEditText;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import dalvik.annotation.TestTargetClass;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/27
 * 描述： 个人信息认证
 */

@Router(value = RouterUrl.Mine_CreditPersonInfo, stringParams = BundleKeys.STATE)
public class PersonalInfoActivity extends BaseMvpActivity {


    @ViewInject(R.id.iv_idCard_front)
    private ImageView ivCardFront;
    @ViewInject(R.id.iv_idCard_back)
    private ImageView ivCardBack;
    @ViewInject(R.id.cet_person_name)
    private ClearEditText tvName;
    @ViewInject(R.id.cet_person_idCard)
    private ClearEditText tvIdNum;
    @ViewInject(R.id.tv_choose_education)
    private TextView tvChooseEdu;
    @ViewInject(R.id.tv_person_address)
    private TextView tvChooseAddress;
    @ViewInject(R.id.cet_person_addressDetail)
    private ClearEditText tvAddressDetail;


    public CreditPersonVM viewModel;
    // 学历
    private OptionsPickerView educationPicker;

    private ArrayList<String> education = new ArrayList<>();
    private ArrayList<String> marraige = new ArrayList<>();
    private ArrayList<String> liveTime = new ArrayList<>();
    private DicRec dic;
    private String state;
    private IdCardTimeRec timeRec;
    private FaceOcrRec faceOcrRec;
    private CreditPersonRec rec;
    private String OcrOrderId, IdentOrderId;
    private String result_auth = "T";
    private String riskTag = "";

    private String faceRecId, verifyId;
    private boolean isFaceRecSuccess = false;
    private boolean isRealNameVerifyRecSuccess = false;

    //根据类型判断启动的ocr页面
    private String openOCrType = "";


    private String idAddr = "";
    private String ocrSessionId = "";
    private String livingSessionId = "";
    private String getYouDunSessionId = "";
    private String getIdName = "";
    private String getIdCardNum = "";

    private int pageFlag = 1;  //2  活体识别  其它  个人信息OCR

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                //身份证正面
                case 1:
                    BaseParams.PHOTO_FRONT = (System.currentTimeMillis() / 1000) + "front.jpg";
                    Bitmap frontBitmap = (Bitmap) msg.obj;
                    String fileFrontFullPath = FileUtil.saveFile(ActivityManage.peek(),
                            BaseParams.PHOTO_PATH, BaseParams.PHOTO_FRONT, frontBitmap);

                    viewModel.setCardPositive(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_FRONT);

                    if (EmptyUtils.isNotEmpty(fileFrontFullPath)) {
                        GlideUtils.disPlayImage(mContext, fileFrontFullPath, ivCardFront);
                    }
                    break;
                //身份证反面
                case 2:
                    BaseParams.PHOTO_BACK = (System.currentTimeMillis() / 1000) + "back.jpg";
                    Bitmap backBitmap = (Bitmap) msg.obj;
                    String fileBackFullPath = FileUtil.saveFile(ActivityManage.peek(),
                            BaseParams.PHOTO_PATH, BaseParams.PHOTO_BACK, backBitmap);
                    viewModel.setCardOpposite(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_BACK);

                    if (EmptyUtils.isNotEmpty(fileBackFullPath)) {
                        GlideUtils.disPlayImage(mContext, fileBackFullPath, ivCardBack);
                    }
                    break;
            }
        }
    };


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("个人信息");
        setTitleRight(Constant.NUMBER__1, "保存", new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                doMsgSubmit();
            }
        });
    }

    private void doMsgSubmit() {
        if (state.equals("30")) {
            return;
        }
        submit();
    }

    @Override
    protected void initFunc() {
        viewModel = new CreditPersonVM();
        state = getIntent().getStringExtra(BundleKeys.STATE);

        attachClickListener(ivCardFront);
        attachClickListener(ivCardBack);
        attachClickListener(tvChooseEdu);
        attachClickListener(tvChooseAddress);

        //目录不存在，创建文件夹
        if (!new File(BaseParams.PHOTO_PATH).exists()) {
            FileUtil.createPaths(BaseParams.PHOTO_PATH);
        } else {
            FileUtil.deleteDir(new File(BaseParams.PHOTO_PATH));
        }


        reqDic();
        if (Constant.STATUS_20.equals(state) || Constant.STATUS_30.equals(state)) {
            tvName.setEnabled(false);
            reqData();
        } else {
            reqData();
            reqTimeData();
        }

        reqSessionId();
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_idCard_front:
                openCardFront();
                //testUd();
                break;

            case R.id.iv_idCard_back:
                //testUd();
                openCardFront();
                break;

            case R.id.tv_choose_education:
                educationShow(view);
                break;

            case R.id.tv_person_address:
                toMap(view);
                break;
            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        PermissionCheck.getInstance().askForPermissions(this, new String[]{
//                Manifest.permission.CAMERA}, PermissionCheck.REQUEST_CODE_ALL);

        AndPermission.with(this).runtime().permission(Manifest.permission.CAMERA).start();


        if (requestCode == PhotographLogic.REQUEST_CODE_TAKE || requestCode == PhotographLogic.REQUEST_CODE_PICK) {
            PhotographLogic.onActivityResult(this, requestCode, resultCode, data, new PhotographLogic.PhotographCallback() {
                @Override
                public void obtain(File file, String photoName) {

                }

                @Override
                public void obtain(Bitmap bitmap, String photoName) {
                    switch (photoName) {
                        case "front":
                            BaseParams.PHOTO_FRONT = (System.currentTimeMillis() / 1000) + "front.jpg";
                            FileUtil.saveFile(mContext, BaseParams.PHOTO_PATH, BaseParams.PHOTO_FRONT, bitmap);
                            viewModel.setCardPositive(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_FRONT);
                           // req_submit(new File(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_FRONT));
                            break;
                        case "back":
                            BaseParams.PHOTO_BACK = (System.currentTimeMillis() / 1000) + "back.jpg";
                            FileUtil.saveFile(mContext, BaseParams.PHOTO_PATH, BaseParams.PHOTO_BACK, bitmap);
                            viewModel.setCardOpposite(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_BACK);
                            break;
                    }
                }
            });
        } else if (requestCode == RequestResultCode.REQ_GD_MAP && resultCode == RESULT_OK) {
            if (data != null) {
                PoiItem poiItem = data.getParcelableExtra(BundleKeys.DATA);
                viewModel.setAddress(poiItem.getProvinceName()+ poiItem.getCityName()+poiItem.getAdName()+poiItem.getSnippet()+poiItem.getDirection()+poiItem.getTitle());
                viewModel.setAddressDetail(poiItem.getProvinceName()+ poiItem.getCityName()+poiItem.getAdName()+poiItem.getSnippet()+poiItem.getDirection()+poiItem.getTitle());
                viewModel.setLatitude(String.valueOf(poiItem.getLatLonPoint().getLatitude()));
                viewModel.setLongitude(String.valueOf(poiItem.getLatLonPoint().getLongitude()));

                tvChooseAddress.setText(viewModel.getAddress());
                tvAddressDetail.setText(viewModel.getAddressDetail());
            }
        }
    }


    /**
     * 小视科技(不在使用)
     * 联网获取ocr识别信息
     */
    @Deprecated //update 2019-7-15 此方法已过时。替换为友盾sdk
    public void req_submit(File forntImg) {
        if (faceOcrRec == null || faceOcrRec.getSecretKey() == null)
            return;
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String signStr = "{\"apiKey\":\"" + faceOcrRec.getApiKey()
                + "\",\"timestamp\":" + timestamp + "}";
        String sign = HMACSHA256(signStr.getBytes(), faceOcrRec.getSecretKey().getBytes());
        TreeMap map = new TreeMap();
        map.put("apiKey", faceOcrRec.getApiKey());
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        map.put("ocrFrontImg", forntImg);

        Call<HttpResult<RegardOcrMsgRec>> call = RDClient.getService(MineService.class)
                .subRegardOcr(faceOcrRec.getUrl(), FileUploadUtil.getRequestMap(map));
        call.enqueue(new RequestCallBack<HttpResult<RegardOcrMsgRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<RegardOcrMsgRec>> call, Response<HttpResult<RegardOcrMsgRec>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    //身份证号
                    viewModel.setCardNo(response.body().getData().getIdCard());
                    //姓名
                    viewModel.setName(response.body().getData().getName());
                    //扫描次数
                    viewModel.setOcrTimeInt(viewModel.getOcrTimeInt() - 1);
                    syncIdCard(response.body().getData().getName(), response.body().getData().getIdCard(), Constant.STATUS_20);
                }
            }
        });
    }

    @Nullable
    public static String HMACSHA256(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString();
    }

    /**
     * 个人信息
     */
    private void reqData() {
        Call<HttpResult<CreditPersonRec>> callInit = RDClient.getService(MineService.class).getUserInfo();
        NetworkUtil.showCutscenes(callInit);
        callInit.enqueue(new RequestCallBack<HttpResult<CreditPersonRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<CreditPersonRec>> call, Response<HttpResult<CreditPersonRec>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    convert(response.body().getData());
                    rec = response.body().getData();
                    SharedInfo.getInstance().saveEntity(rec);
                }
            }
        });
    }

    /**
     * 获取优盾的sessionid
     */
    private void reqSessionId() {
        Call<HttpResult<YouDunSessionDM>> callInit = RDClient.getService(MineService.class).getUserSessionId();
        NetworkUtil.showCutscenes(callInit);
        callInit.enqueue(new RequestCallBack<HttpResult<YouDunSessionDM>>() {
            @Override
            public void onSuccess(Call<HttpResult<YouDunSessionDM>> call,
                                  Response<HttpResult<YouDunSessionDM>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    getYouDunSessionId = response.body().getData().getOcrSessionId();
                    getIdName = response.body().getData().getRealName();
                    getIdCardNum = response.body().getData().getIdNo();

                }
            }
        });
    }


    /**
     * 获取可扫描次数
     */
    private void reqTimeData() {
        Call<HttpResult<IdCardTimeRec>> callInit = RDClient.getService(MineService.class).idCardCreditTime();
        NetworkUtil.showCutscenes(callInit);
        callInit.enqueue(new RequestCallBack<HttpResult<IdCardTimeRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<IdCardTimeRec>> call, Response<HttpResult<IdCardTimeRec>> response) {
                timeRec = response.body().getData();
                viewModel.setOcrTime(timeRec.getOcrTime());
                viewModel.setFaceTime(timeRec.getFaceTime());
                if ("idcard".equals(openOCrType)) {
                    useIdCardOCR(timeRec);

                }
            }
        });

    }

    /**
     * dataModel to mRegisterVM
     */
    private void convert(CreditPersonRec rec) {
        viewModel.setEnable(true);
        viewModel.setCardNo(rec.getIdNo());
        viewModel.setAddress(rec.getLiveAddr());
        viewModel.setAddressDetail(rec.getLiveDetailAddr());
        viewModel.setCardOpposite(rec.getBackImg());
        viewModel.setCardPositive(rec.getFrontImg());
        viewModel.setFaceImg(rec.getLivingImg());
        viewModel.setName(rec.getRealName());
        viewModel.setEducation(rec.getEducation());
        viewModel.setLatitude(rec.getLatitude());
        viewModel.setLongitude(rec.getLongitude());

        //update 2019-7-15 新增 年龄、性别、民族、签发机关、证件有效期
        viewModel.setAge(rec.getAge());
        viewModel.setGender(rec.getSex());
        viewModel.setNation(rec.getNational());
        viewModel.setIssuingAuthority(rec.getIssuingAuthority());
        viewModel.setValidityPeriod(rec.getValidityPeriod());

        if (EmptyUtils.isNotEmpty(rec.getFrontImg())) {
            GlideUtils.disPlayImage(this, rec.getFrontImg(), ivCardFront);
        }
        if (EmptyUtils.isNotEmpty(rec.getBackImg())) {
            GlideUtils.disPlayImage(this, rec.getBackImg(), ivCardBack);
        }

        tvName.setText(StringUtils.nullToStr(viewModel.getName()));
        tvIdNum.setText(StringUtils.nullToStr(viewModel.getCardNo()));
        tvChooseEdu.setText(StringUtils.nullToStr(viewModel.getEducation()));
        tvChooseAddress.setText(StringUtils.nullToStr(viewModel.getAddress()));
        tvAddressDetail.setText(StringUtils.nullToStr(viewModel.getAddressDetail()));
    }

    /**
     * 初始化弹出框
     */
    private void init() {
        if (dic != null) {//数据字典获取内容
            List<KeyValueRec> temp;
            if (dic.getEducationalStateList() != null) {
                temp = dic.getEducationalStateList();
                for (int i = 0; i < temp.size(); i++) {
                    education.add(temp.get(i).getValue());
                }
            }
            if (dic.getMaritalList() != null) {
                temp = dic.getMaritalList();
                for (int i = 0; i < temp.size(); i++) {
                    marraige.add(temp.get(i).getValue());
                }
            }
            if (dic.getLiveTimeList() != null) {
                temp = dic.getLiveTimeList();
                for (int i = 0; i < temp.size(); i++) {
                    liveTime.add(temp.get(i).getValue());
                }
            }
        }
        educationPicker = new OptionsPickerView(mContext);
        educationPicker.setPicker(education);
        educationPicker.setCyclic(false);
    }

    /**
     * 数据字典请求
     */
    private void reqDic() {
        Call<HttpResult<DicRec>> callInit = RDClient.getService(MineService.class).getDicts(DicKey.EDUCATION);
        callInit.enqueue(new RequestCallBack<HttpResult<DicRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<DicRec>> call,
                                  Response<HttpResult<DicRec>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    dic = response.body().getData();
                    init();
                }
            }
        });
    }


    public void getUrlBitmap(final String url, final int tag) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = BitmapUtil.getBitmap(url);
                if (bitmap != null) {
                    Message message = Message.obtain();
                    message.arg1 = tag;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 身份证识别
     * （友盾三方sdk获取的身份证信息的）
     */
    public void openCardFront() {
        openOCrType = "idcard";
        if (Constant.STATUS_20.equals(state) || Constant.STATUS_30.equals(state)) {

        } else {
            reqData();
            reqTimeData();
        }
    }

   private void testUd(){
      // reqDic();
      // reqData();
       OcrOrderId = Util.getYDOrderId();

       AuthBuilder authBuilder = getAuthBuilder(OcrOrderId);
       OCRComponent ocrComponenet = AuthComponentFactory.getOcrComponent();
       //开始调用
       authBuilder.addFollow(ocrComponenet).start(mContext);
   }

    /**
     * 有盾身份证识别
     *
     * @param timeRec
     */

    private void useIdCardOCR(IdCardTimeRec timeRec) {
        if (timeRec == null)
            return;
        if (viewModel.getOcrTimeInt() > 0) {

            if (timeRec == null)
                return;
            if (viewModel.getOcrTimeInt() > 0) {
                OcrOrderId = Util.getYDOrderId();

                AuthBuilder authBuilder = getAuthBuilder(OcrOrderId);
                OCRComponent ocrComponenet = AuthComponentFactory.getOcrComponent();
                //开始调用
                authBuilder.addFollow(ocrComponenet).start(mContext);
            } else {
                DialogUtils.showConfirmDialog(mContext, "今日操作过于频繁，请明日再试",
                        new DialogUtils.btnConfirmClick() {
                            @Override
                            public void confirm() {
                                ActivityManage.peek().finish();
                            }
                        });
            }
        } else {
            DialogUtils.showConfirmDialog(mContext, "今日操作过于频繁，请明日再试",
                    new DialogUtils.btnConfirmClick() {
                        @Override
                        public void confirm() {
                            ActivityManage.peek().finish();
                        }
                    });
        }
    }


    /**
     * 后台链接不可用时调试友盾ocr
     */
    private void StartOcr(){
        OcrOrderId = Util.getYDOrderId();
        AuthBuilder authBuilder = getAuthBuilder(OcrOrderId);
        OCRComponent ocrComponenet = AuthComponentFactory.getOcrComponent();
        //开始调用
        authBuilder.addFollow(ocrComponenet).start(mContext);
    }


    @NonNull
    private AuthBuilder getAuthBuilder(String orderId) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsss");
        String sign_time = sdf.format(date);
        //生成 签名
        String sign = MD5Utils.getMD5Sign(AppConfig.PUB_KEY, orderId, sign_time, AppConfig.SRCURITY_KEY);

        AuthBuilder authBuilder = new AuthBuilder(orderId, AppConfig.PUB_KEY, sign_time, sign,
                new OnResultListener() {
                    @Override
                    public void onResult(int op_type, String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);

                            /***
                             * 业务处理成功（不是认证成功）
                             */
                            if (jsonObject.has("success") && jsonObject.getString("success").equals("true")) {

                                System.out.println("AuthBuilder--success---");
                                switch (op_type) {
                                    case AuthBuilder.OPTION_ERROR:
                                        //// error
                                        System.out.println("AuthBuilder---Error：" + result);
                                        break;

                                    // 身份证OCR识别 回调
                                    case AuthBuilder.OPTION_OCR:
                                        parsonOcrResult(result);
                                        break;

                                }
                            } else {
                                /***
                                 * 业务处理失败
                                 */
                                String message = jsonObject.getString("message");
                                String errorcode = jsonObject.getString("errorcode");
                                /** 打印错误日志，可根据文档定位问题 */
                                System.out.println("AuthBuilder--Failed:" + errorcode + ":" + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            System.out.println("AuthBuilder--Exception:" + e.toString());
                        }
                    }
                });

        return authBuilder;
    }


    /**
     * 解析OCR结果
     *
     * @param result ocr识别返回的json
     */
    private void parsonOcrResult(String result) {
        System.out.println("ocr_result:" + result);
        try {
            JSONObject obj = new JSONObject(result);
            //身份证--正面照
            getUrlBitmap(obj.getString("idcard_front_photo"), 1);
            //身份证--反面照
            getUrlBitmap(obj.getString("idcard_back_photo"), 2);
            //身份证号码
            viewModel.setCardNo(obj.getString("id_number"));
            //身份证姓名
            viewModel.setName(obj.getString("id_name"));
            //扫描次数
            viewModel.setOcrTimeInt(viewModel.getOcrTimeInt() - 1);

            //  update 2019-7-15号添加:年龄、性别、民族、签发机关、身份证有效期
            viewModel.setAge(obj.getInt("age"));
            viewModel.setGender(obj.getString("gender"));
            viewModel.setNation(obj.getString("nation"));
            viewModel.setIssuingAuthority(obj.getString("issuing_authority"));
            viewModel.setValidityPeriod(obj.getString("validity_period"));


            tvName.setText(viewModel.getName());
            tvIdNum.setText(viewModel.getCardNo());

            ocrSessionId = obj.getString("session_id");
            idAddr = obj.getString("address");

            syncIdCard(obj.getString("id_name"), obj.getString("id_number"), Constant.STATUS_20);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 学历弹出框
     */
    public void educationShow(final View view) {
        if (dic != null && dic.getEducationalStateList() != null) {
            Util.hideKeyBoard(view);
            educationPicker.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    viewModel.setEducation(dic.getEducationalStateList().get(options1).getValue());
                    tvChooseEdu.setText(viewModel.getEducation());
                }
            });
            educationPicker.show();
        } else {
            ToastUtil.toast(R.string.credit_no_dic);
        }
    }

    /**
     * 同步扫描次数
     */
    public void syncIdCard(String name, String idCard, String type) {
        IdCardSyncSub sub = new IdCardSyncSub();
        sub.setIdCard(idCard);
        sub.setName(name);
        sub.setType(type);
        Call<HttpResult> callInit = RDClient.getService(MineService.class).ocrSynchron(sub);
        callInit.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {

            }
        });
    }

    /**
     * 获取位置信息
     */
    public void toMap(View view) {
        Routers.openForResult(this, RouterUrl.getRouterUrl(RouterUrl.Mine_GdMap), RequestResultCode.REQ_GD_MAP);
    }

    public void submitToast() {
        String tips = ContextHolder.getContext().getString(R.string.credit_confirm_toast, viewModel.getName(), viewModel.getCardNo());
        if (Util.isIDCard(viewModel.getCardNo())) {


            DialogUtils.showSureCancelDialog(mContext, tips, ContextHolder.getContext().getString(R.string.credit_back_update),
                    ContextHolder.getContext().getString(R.string.credit_confirm_submit),
                    null, new DialogUtils.btnConfirmClick() {
                        @Override
                        public void confirm() {
                            CreditPersonSub sub = new CreditPersonSub();
//                            sub.setYouDunIdCardFlag("1");
                            sub.setYouDunIdCardOrderNo(OcrOrderId);
                            sub.setBackImg(new File(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_BACK));
                            sub.setFrontImg(new File(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_FRONT));
                            File ocr = new File(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_AVATAR);
                            if (ocr.exists()) {
                                sub.setOcrImg(ocr);
                            } else {
                                sub.setOcrImg(null);
                            }
                            sub.setDetailAddr(viewModel.getAddressDetail());
                            sub.setLiveAddr(viewModel.getAddress());

                            //test测试
                            sub.setEducation(viewModel.getEducation());
                            //sub.setEducation("本科");
                            sub.setIdNo(viewModel.getCardNo());
                            sub.setRealName(viewModel.getName());
                            sub.setLiveCoordinate(viewModel.getLongitude() + "," + viewModel.getLatitude());

                            //update 2018-8-8 新增上传 ocrSessionId  ocr识别地址 idAddr
                            sub.setOcrSessionId(ocrSessionId);
                            sub.setIdAddr(idAddr);

                            //update 2019-7-15 新增上传 年龄、性别、民族、签发机关、身份证有效期
                            sub.setAge(String.valueOf(viewModel.getAge()));
                            sub.setSex(viewModel.getGender());
                            sub.setNational(viewModel.getNation());
                            sub.setIssuingAuthority(viewModel.getIssuingAuthority());
                            sub.setValidityPeriod(viewModel.getValidityPeriod());

                            Map map = ObjectDynamicCreator.getFieldVlaue(sub);
                            TreeMap tree = new TreeMap(map);
                            tree = UrlUtils.getInstance().dynamicParams(tree);
                            TreeMap temp = new TreeMap(tree);
                            Map<String, String> head = new HashMap<>();
                            head.put(Constant.TOKEN, UrlUtils.getInstance().getToken());
                            head.put(Constant.SIGNA, UrlUtils.getInstance().signParams(temp));
                            Call<HttpResult> callInit = RDClient.getService(MineService.class).idCardCreditTwo(head, FileUploadUtil.getRequestMap(tree));
                            NetworkUtil.showCutscenes(callInit);
                            callInit.enqueue(new RequestCallBack<HttpResult>() {
                                @Override
                                public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                                    DialogUtils.showConfirmDialog(mContext, response.body().getMsg(),
                                            new DialogUtils.btnConfirmClick() {
                                                @Override
                                                public void confirm() {
                                                    PersonalInfoActivity.this.finish();
                                                }
                                            });
                                }
                            });
                        }
                    });
        } else {
            DialogUtils.showToastDialog(mContext, ContextHolder.getContext().getString(R.string.credit_person_tips));
        }
    }

    public void submit() {
        String input = ContextHolder.getContext().getString(R.string.input);
        String select = ContextHolder.getContext().getString(R.string.select);

        viewModel.setName(tvName.getText().toString());

        if (TextUtils.isEmpty(viewModel.getCardOpposite())
                || TextUtils.isEmpty(viewModel.getCardPositive())) {
            DialogUtils.showToastDialog(mContext,
                    ContextHolder.getContext().getString(R.string.credit_id_card_toast));
            return;
        }

        if (TextUtils.isEmpty(viewModel.getName())) {
            DialogUtils.showToastDialog(mContext, ContextHolder.getContext().getString(R.string.person_edit_name));
            return;
        }

       /* if (TextUtils.isEmpty(viewModel.getEducation())) {
            DialogUtils.showToastDialog(mContext, select + ContextHolder.getContext().getString(R.string.person_education));
            return;
        }*/
        if (TextUtils.isEmpty(viewModel.getAddress())) {
            DialogUtils.showToastDialog(mContext, select + ContextHolder.getContext().getString(R.string.person_address_now));
            return;
        }
        if (TextUtils.isEmpty(viewModel.getAddressDetail())) {
            DialogUtils.showToastDialog(mContext, ContextHolder.getContext().getString(R.string.person_address_now_hint));
            return;
        }
        if (Constant.STATUS_20.equals(state) || Constant.STATUS_30.equals(state) || Constant.STATUS_25.equals(state)) {

            UpdatePersonSub sub = new UpdatePersonSub();
            sub.setDetailAddr(viewModel.getAddressDetail());
            sub.setLiveAddr(viewModel.getAddress());
            //test
            sub.setEducation(viewModel.getEducation());
          //  sub.setEducation("本科");
            sub.setIdNo(viewModel.getCardNo());
            sub.setRealName(viewModel.getName());
//            sub.setYouDunIdCardFlag("1");   //  update 2018-8-8  删除了这个键
            sub.setYouDunIdCardOrderNo(OcrOrderId);
            sub.setLiveCoordinate(viewModel.getLongitude() + "," + viewModel.getLatitude());
            // update 2018-8-8  新增上传ocr sessionId
            sub.setOcrSessionId(ocrSessionId);

            //update 2019-7-15 新增上传 年龄、性别、民族、签发机关、身份证有效期
            sub.setAge(String.valueOf(viewModel.getAge()));
            sub.setSex(viewModel.getGender());
            sub.setNational(viewModel.getNation());
            sub.setIssuingAuthority(viewModel.getIssuingAuthority());
            sub.setValidityPeriod(viewModel.getValidityPeriod());

            Map map = ObjectDynamicCreator.getFieldVlaue(sub);
            TreeMap tree = new TreeMap(map);
            tree = UrlUtils.getInstance().dynamicParams(tree);
            TreeMap temp = new TreeMap(tree);
            Map<String, String> head = new HashMap<>();
            head.put(Constant.TOKEN, UrlUtils.getInstance().getToken());
            head.put(Constant.SIGNA, UrlUtils.getInstance().signParams(temp));
            Call<HttpResult> callInit = RDClient.getService(MineService.class).updateIdCardCreditTwo(head, FileUploadUtil.getRequestMap(tree));
            NetworkUtil.showCutscenes(callInit);
            callInit.enqueue(new RequestCallBack<HttpResult>() {
                @Override
                public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                    DialogUtils.showConfirmDialog(mContext, response.body().getMsg(),
                            new DialogUtils.btnConfirmClick() {
                                @Override
                                public void confirm() {
                                    PersonalInfoActivity.this.finish();
                                }
                            });
                }
            });
        } else {
            submitToast();
        }
    }


}
