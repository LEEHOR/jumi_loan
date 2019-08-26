package com.cashloan.jumidai.ui.homeMine.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.authreal.api.AuthBuilder;
import com.authreal.api.FormatException;
import com.authreal.api.OnResultListener;
import com.authreal.component.AuthComponentFactory;
import com.authreal.component.CompareItemFactory;
import com.authreal.component.CompareItemSession;
import com.authreal.component.LivingComponent;
import com.authreal.component.VerifyCompareComponent;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.BaseParams;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.UrlUtils;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.CreditPersonVM;
import com.cashloan.jumidai.ui.homeMine.bean.YouDunSessionDM;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditPersonRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.IdCardTimeRec;
import com.cashloan.jumidai.ui.homeMine.bean.submit.IdCardSyncSub;
import com.cashloan.jumidai.ui.homeMine.bean.submit.LivenessSub;
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
import com.commom.widget.NoDoubleClickButton;
import com.github.mzule.activityrouter.annotation.Router;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/27
 * 描述： 人脸识别
 */

@Router(value = RouterUrl.Mine_CreditPersonFace, stringParams = BundleKeys.STATE)
public class FaceVerifyActivity extends BaseMvpActivity {

    @ViewInject(R.id.iv_face_img)
    private ImageView ivFace;
    @ViewInject(R.id.ndb_do_face)
    private NoDoubleClickButton btnStart;

    public CreditPersonVM viewModel;
    private IdCardTimeRec timeRec;
    private String faceRecId, verifyId;
    private boolean isFaceRecSuccess = false;//人脸比对是否成功
    private boolean isLivingRecSuccess = false;//活体认证是否成功
    private boolean isRealNameVerifyRecSuccess = false;//实名认证是否成功

    private String livingSessionId = "";
    private String getYouDunSessionId = "";
    private String getIdName = "";
    private String getIdCardNum = "";

    private String state;
    private CreditPersonRec rec;
    private String IdentOrderId;
    private String riskTag = "";

    private static final int REAL_NAME_FINISHED = 100;
    private static final int LIVING_FINISHED = 101;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_verify;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("人脸识别");
        setTitleRight(Constant.NUMBER__1, "保存", new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                //已经成功认证 不予处理操作
                if (state.equals("30")) {
                    return;
                }
                doMsgSubmit();

            }
        });
    }

    private void doMsgSubmit() {
        if (isLivingRecSuccess) {
            if (isFaceRecSuccess) {

                doRealNameVerify();
            } else {
                DialogUtils.showToastDialog(mContext, "人脸识别认证未通过，无法保存，请重新进行认证！");
            }
        } else {
            DialogUtils.showToastDialog(mContext, "活体认证未通过，无法保存，请重新进行认证！");
        }

    }

    //执行实名认证操作
    private void doRealNameVerify() {
        if (null != getIdName && !"".equals(getIdName)
                && null != getIdCardNum && !"".equals(getIdCardNum)) {

            userVerify(getIdName, getIdCardNum);
        } else {
            System.out.println("未获取到实名认证想关信息：idName=" + getIdName + "===idCardNum=" + getIdCardNum);
        }
    }

    @Override
    protected void initFunc() {
        viewModel = new CreditPersonVM();
        state = getIntent().getStringExtra(BundleKeys.STATE);

        attachClickListener(btnStart);

        //目录不存在，创建文件夹
        if (!new File(BaseParams.PHOTO_PATH).exists()) {
            FileUtil.createPaths(BaseParams.PHOTO_PATH);
        } else {
            FileUtil.deleteDir(new File(BaseParams.PHOTO_PATH));
        }


        if (Constant.STATUS_20.equals(state) || Constant.STATUS_30.equals(state)) {
            reqData();
            if (Constant.STATUS_30.equals(state)) {
                btnStart.setEnabled(false);
                btnStart.setText("已认证");
            }
        } else {
            reqData();
//            reqTimeData();
        }

        reqSessionId();
    }

    @Override
    protected void onViewClicked(View view) {
        reqTimeData();
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                //活体
                case 3:
                    BaseParams.PHOTO_ALIVE = (System.currentTimeMillis() / 1000) + "alive.jpg";
                    Bitmap identBitmap = (Bitmap) msg.obj;
                    FileUtil.saveFile(ActivityManage.peek(), BaseParams.PHOTO_PATH, BaseParams.PHOTO_ALIVE, identBitmap);
                    viewModel.setFaceImg(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_ALIVE);
                    syncIdCard("", "", Constant.STATUS_10);
                    viewModel.setFaceTimeInt(viewModel.getFaceTimeInt() - 1);

                    if (EmptyUtils.isNotEmpty(viewModel.getFaceImg())) {
                        GlideUtils.disPlayImage(mContext, viewModel.getFaceImg(), ivFace);
                    }
                    break;

                //活体认证结束
                case LIVING_FINISHED:
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            doFaceRecon();//执行人脸比对
                        }
                    }.start();
                    break;
            }
        }
    };

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

                useFaceAliveOcr(timeRec);
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

        if (EmptyUtils.isNotEmpty(viewModel.getFaceImg())) {
            GlideUtils.disPlayImage(mContext, viewModel.getFaceImg(), ivFace);
        }
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
     * 实名认证
     *
     * @param idName   姓名
     * @param idNumber 身份证号
     */
    private void userVerify(String idName, String idNumber) {
        verifyId = Util.getYDOrderId();

        AuthBuilder authBuilder = getAuthBuilder(verifyId);
        VerifyCompareComponent verifyComponent = AuthComponentFactory.getVerifyCompareComponent();

        try {
            //设置姓名和身份号码
            verifyComponent.setNameAndNumber(idName, idNumber);
            //实名验证方式
//            verifyComponent.needGridPhoto(false);
            verifyComponent.setCompareItem(CompareItemFactory.getCompareItemBySessionId(
                    livingSessionId,
                    CompareItemSession.SessionType.PHOTO_LIVING));

//            //结果异步通知地址
//            verifyComponent.setNotifyUrl("http://192.168.0.1/");
        } catch (FormatException e) {
            e.printStackTrace();
        }

        //开始调用
        authBuilder.addFollow(verifyComponent).start(mContext);
    }

    /**
     * 活体识别
     */
    private void useFaceAliveOcr(IdCardTimeRec timeRec) {
        if (timeRec == null)
            return;
        if (viewModel.getFaceTimeInt() > 0) {
            IdentOrderId = Util.getYDOrderId();

            LivingComponent livingComponent = AuthComponentFactory.getLivingComponent();
            Log.d("IdentOrderId", IdentOrderId);
            AuthBuilder authBuilder = getAuthBuilder(IdentOrderId);
            authBuilder.addFollow(livingComponent).start(mContext);
        } else {
            DialogUtils.showConfirmDialog(mContext, "今日操作过于频繁，请明日再试",
                    new DialogUtils.btnConfirmClick() {
                        @Override
                        public void confirm() {
                            FaceVerifyActivity.this.finish();
                        }
                    });
        }
    }


    //活体认证通过，进行人脸识别
    private void doFaceRecon() {
        faceRecId = Util.getYDOrderId();
        Log.e("人脸识别", faceRecId + "/" + getYouDunSessionId + "/" + livingSessionId);
        getAuthBuilder(faceRecId)
                .addFollow(//AuthComponentFactory.getCompareComponent()
                        /** 此示例比对项A为 活体动作过程截图
                         *  可以根据自己业务来更改，如有疑问请联系有盾对接人员
                         */
//                        .setCompareItemA(CompareItemFactory.getCompareItemBySessionId(
//                                livingSessionId,
//                                CompareItemSession.SessionType.PHOTO_LIVING))
//                        /** 此示例比对项B 为 实名验证留存网纹照
//                         *  可以根据自己业务来更改，如有疑问请联系有盾对接人员
//                         *  比对网格照 isGrid 必须设置为true
//                         */
//                        .setCompareItemB(CompareItemFactory.getCompareItemBySessionId(
//                                getYouDunSessionId,
//                                CompareItemSession.SessionType.PHOTO_IDENTIFICATION))

                        AuthComponentFactory.getCompareFaceComponent()
                                // 此示例对比项A为OCR人像图片
                                .setCompareItemA(CompareItemFactory.getCompareItemBySessionId(
                                        getYouDunSessionId,
                                        CompareItemSession.SessionType.PHOTO_IDENTIFICATION))
                                //此示例对比项B为活体过程中截图
                                .setCompareItemB(CompareItemFactory.getCompareItemBySessionId(
                                        livingSessionId,
                                        CompareItemSession.SessionType.PHOTO_LIVING))
                )
                /**
                 * 开始流程--传入 Activity 对象
                 **/
                .start(mContext);

        System.out.println("Face---start:"
                + "\nfaceRecId:" + faceRecId
                + "\nOCR_getYouDunSessionId:" + getYouDunSessionId
                + "\n活体livingSession:" + livingSessionId);

    }


    @NonNull
    private AuthBuilder getAuthBuilder(String orderId) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsss");
        String sign_time = sdf.format(date);
        //生成 签名
        String sign = MD5Utils.getMD5Sign(AppConfig.PUB_KEY, orderId, sign_time, AppConfig.SRCURITY_KEY);
        System.out.println("getAuthBuilder:"
                + "\nPARTNER_ORDER_NUMBER:" + orderId
                + "\nPUB_KEY:" + AppConfig.PUB_KEY
                + "\nsign_time:" + sign_time
                + "\nsign:" + sign);
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
                                    case AuthBuilder.OPTION_VIDEO:
                                        //// TODO:  视频存证 回调
                                        break;

                                    case AuthBuilder.OPTION_ERROR:
                                        ////
                                        System.out.println("AuthBuilder---Error：" + result);
                                        break;

                                    // 身份证OCR识别 回调
                                    case AuthBuilder.OPTION_OCR:
                                        break;

//                                    // 实名认证 回调
//                                    case AuthBuilder.OPTION_VERIFY:
//                                        parsonVerifyResult(result);
//                                        break;

                                    // 活体检测 回调
                                    case AuthBuilder.OPTION_LIVENESS:
                                        parsonLivingResult(result);
                                        break;

                                    // 人脸比对 回调
                                    case AuthBuilder.OPTION_COMPARE_FACE:
                                        parsonPeopleFaceResult(result);
                                        break;

                                    // 人像比对 回调
                                    case AuthBuilder.OPTION_VERIFY_COMPARE:
                                        parsonVerifyResult(result);
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
                                System.out.println("报文:" + jsonObject.toString());
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
     * 解析实名认证结果
     *
     * @param result result
     */
    private void parsonVerifyResult(String result) {
        Log.e("实名认证1", result);
        System.out.println("实名认证结果：" + result);
        try {
            JSONObject obj = new JSONObject(result);

            //验证状态
            String verifyStatus = obj.getString("verify_status");
            // 结果状态
            String resultStatus = obj.optString("result_status");

            switch (verifyStatus) {
//                //姓名与号码一致，无网格照
//                case "0":
//                    isRealNameVerifyRecSuccess = true;
//                    break;

                //姓名与号码一致，取得网格照
                case "1":
                    if (resultStatus.equals("01")) {
                        isRealNameVerifyRecSuccess = true;
                    } else {
                        isRealNameVerifyRecSuccess = false;
                    }
                    break;

                //姓名与号码不一致
                case "2":
                    isRealNameVerifyRecSuccess = false;
                    break;

                //查询无结果
                case "3":
                    isRealNameVerifyRecSuccess = false;
                    break;
            }

            if (isRealNameVerifyRecSuccess) {
                submitTLivness();
            } else {
                DialogUtils.showToastDialog(mContext, "实名认证失败，请联系客服");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析人脸比对结果
     *
     * @param result 人脸比对返回的json
     */
    private void parsonPeopleFaceResult(String result) {
        Log.e("实名认证2", result);
        System.out.println("人脸比对结果：" + result);
        try {
            JSONObject obj = new JSONObject(result);
            //相似度
            String similarity = obj.getString("similarity");
            double simil = Double.valueOf(similarity);

            if (simil > 0.7) {
                isFaceRecSuccess = true;
                btnStart.setEnabled(false);
            } else {
                isFaceRecSuccess = false;
                btnStart.setEnabled(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean isLivingSuccess = false;

    /**
     * 解析活体检测结果
     *
     * @param result 活体检测返回的json
     */
    private void parsonLivingResult(String result) {
        Log.e("实名认证3", result);
        System.out.println("活体检测返回:" + result);
        try {
            JSONObject obj = new JSONObject(result);
            //活体清晰照
            getUrlBitmap(obj.getString("living_photo"), 3);
            //风险标签
            JSONObject risk = obj.optJSONObject("risk_tag");
            // 0-未检测到活体攻击;  1-存在活体攻击风险
            riskTag = risk.getString("living_attack");
            livingSessionId = obj.getString("session_id");
            //请求是否成功
            isLivingSuccess = obj.getBoolean("success");
            // Log.d("活体检测", obj.toString());
            if ("0".equals(riskTag) && isLivingSuccess) {

                isLivingRecSuccess = true;

                Message message = Message.obtain();
                message.arg1 = LIVING_FINISHED;
                handler.sendMessage(message);

            } else {
                isLivingRecSuccess = false;

                Log.d("parsonLivingResult", "存在活体攻击风险");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //活体识别成功--进行人脸识别
    public void submitTLivness() {

        if (TextUtils.isEmpty(viewModel.getFaceImg())) {
            DialogUtils.showToastDialog(mContext, ContextHolder.getContext().getString(R.string.credit_face_toast));
            return;
        }
        if (Constant.STATUS_10.equals(state)) {

            LivenessSub sub = new LivenessSub();
            sub.setLivingImg(new File(BaseParams.PHOTO_PATH + "/" + BaseParams.PHOTO_ALIVE));
            sub.setLivingSessionId(livingSessionId);

            Map map = ObjectDynamicCreator.getFieldVlaue(sub);
            TreeMap tree = new TreeMap(map);
            tree = UrlUtils.getInstance().dynamicParams(tree);
            TreeMap temp = new TreeMap(tree);
            Map<String, String> head = new HashMap<>();
            head.put(Constant.TOKEN, UrlUtils.getInstance().getToken());
            head.put(Constant.SIGNA, UrlUtils.getInstance().signParams(temp));
            Call<HttpResult> callInit = RDClient.getService(MineService.class).livingIdentifyAuth(head, FileUploadUtil.getRequestMap(tree));
            final ProgressDialog progressDialog = new ProgressDialog(mContext);//1.创建一个ProgressDialog的实例
            progressDialog.setMessage("正在上传，请稍候...");//2.设置显示内容
            progressDialog.setCancelable(true);//3.设置可否用back键关闭对话框
            progressDialog.show();
            callInit.enqueue(new RequestCallBack<HttpResult>() {
                @Override
                public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {

                    DialogUtils.showConfirmDialog(mContext, response.body().getMsg(),
                            new DialogUtils.btnConfirmClick() {
                                @Override
                                public void confirm() {
                                    FaceVerifyActivity.this.finish();
                                }
                            });
                }

                @Override
                public void onFailure(Call<HttpResult> call, Throwable t) {
                    super.onFailure(call, t);
                    if (null != progressDialog) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailed(Call<HttpResult> call, Response<HttpResult> response) {
                    super.onFailed(call, response);
                    if (null != progressDialog) {
                        progressDialog.dismiss();
                    }
                }
            });

        }
    }


}
