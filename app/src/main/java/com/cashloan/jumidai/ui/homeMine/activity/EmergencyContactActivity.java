package com.cashloan.jumidai.ui.homeMine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.DicKey;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.CreditLinkerVM;
import com.cashloan.jumidai.ui.homeMine.bean.PhoneUtil;
import com.cashloan.jumidai.ui.homeMine.bean.recive.CreditLinkerRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.DicRec;
import com.cashloan.jumidai.ui.homeMine.bean.recive.KeyValueRec;
import com.cashloan.jumidai.ui.homeMine.bean.submit.CreditLinkerSub;
import com.cashloan.jumidai.ui.homeMine.bean.submit.PhoneInfoSub;
import com.cashloan.jumidai.utils.DeviceUtil;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.cashloan.jumidai.views.CutscenesProgress;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.utils.OnOnceClickListener;
import com.commom.utils.ToastUtil;
import com.commom.utils.encryption.Base64;
import com.github.mzule.activityrouter.annotation.Router;
import com.google.gson.Gson;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/27
 * 描述： 紧急联系人
 */
@Router(value = RouterUrl.Mine_CreditLinker, stringParams = BundleKeys.STATE)
public class EmergencyContactActivity extends BaseMvpActivity {

    @ViewInject(R.id.relation1)
    private TextView tvRelation1;
    @ViewInject(R.id.tv_contact_name1)
    private TextView tvName1;

    @ViewInject(R.id.relation2)
    private TextView tvRelation2;
    @ViewInject(R.id.tv_contact_name2)
    private TextView tvName2;


    String state;
    CutscenesProgress mDialog;

    public CreditLinkerVM viewModel;
    //联系人关系
    private OptionsPickerView relationPicker;
    //直系联系人关系
    private OptionsPickerView kinsfolkrelationPicker;
    private ArrayList<String> relation = new ArrayList<>();
    private ArrayList<String> kinsfolkrelation = new ArrayList<>();
    //联系人多个号码
    private OptionsPickerView phonePicker;
    private ArrayList<String> phones = new ArrayList<>();
    private DicRec dic;
    private List<CreditLinkerRec> linkerList;
    private static boolean isFirst = true;
    List contacts;

    String strContacts;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_emergency_contact;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("紧急联系人");
        setTitleRight(Constant.NUMBER__1, "保存", new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                //已经成功认证 不予处理操作
                if (state.equals("30")) {
                    doMsgSubmit();
                } else {
                    doMsgSubmit();
                }
              //  doMsgSubmit();
            }
        });
    }

    private void doMsgSubmit() {
        String select = mContext.getString(R.string.select);
        if (TextUtils.isEmpty(viewModel.getRelation1())) {
            showMessageDialogTips(select + mContext.getString(R.string.credit_lineal_kin) + mContext.getString(R.string.linker_relation));
            return;
        }
        if (TextUtils.isEmpty(viewModel.getName1())) {
            showMessageDialogTips(select + mContext.getString(R.string.credit_lineal_kin) + mContext.getString(R.string.linker_name));
            return;
        }

        if (TextUtils.isEmpty(viewModel.getPhone1())) {
            showMessageDialogTips(select + mContext.getString(R.string.credit_lineal_kin) + mContext.getString(R.string.linker_name));
            return;
        }
        if (TextUtils.isEmpty(viewModel.getRelation2())) {
            showMessageDialogTips(select + mContext.getString(R.string.credit_other_kin) + mContext.getString(R.string.linker_relation));
            return;
        }
        if (TextUtils.isEmpty(viewModel.getName2())) {
            showMessageDialogTips(select + mContext.getString(R.string.other) + mContext.getString(R.string.linker_name));
            return;
        }
        if (TextUtils.isEmpty(viewModel.getPhone2())) {
            showMessageDialogTips(select + mContext.getString(R.string.credit_other_kin) + mContext.getString(R.string.linker_name));
            return;
        }

        if (viewModel.getName1().equals(viewModel.getName2())) {
            showMessageDialogTips(mContext.getString(R.string.linker_the_same));
            return;
        }

        if (!Constant.STATUS_30.equals(state)) {
            subPhone(contacts);
        } else {
            showMessageDialogTips("请勿重复认证");
        }


    }

    private void showMessageDialogTips(String text) {
        DialogUtils.showConfirmDialog(mContext, text, null);
    }

    @Override
    protected void initFunc() {
        state = getIntent().getStringExtra(BundleKeys.STATE);
        viewModel = new CreditLinkerVM();

        attachClickListener(tvRelation1);
        attachClickListener(tvRelation2);
        attachClickListener(tvName1);
        attachClickListener(tvName2);

        reqDic();
        if (Constant.STATUS_20.equals(state) || Constant.STATUS_30.equals(state)) {
            reqData();
        }

//        mDialog = CutscenesProgress.createDialog(mContext);
//        mDialog.setMessage("请稍后");
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.setCancelable(false);
//
//        mDialog.show();
//        getContacts(mContext).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List>() {
//                    @Override
//                    public void accept(List s) throws Exception {
//                        contacts = s;
//                        if (mDialog.isShowing()) {
//                            mDialog.dismiss();
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        if (mDialog.isShowing()) {
//                            mDialog.dismiss();
//                        }
//                    }
//                });
//
//        getStringContacts(mContext).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        strContacts = s;
//                        if (mDialog.isShowing()) {
//                            mDialog.dismiss();
//                        }
//                    }
//                });
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            //直系亲属--与本人关系
            case R.id.relation1:
                kinsfolkrelationShow(view);
                break;
            //直系亲属--联系人名称
            case R.id.tv_contact_name1:
                openContactClick(view, 0);
                break;

            //其他联系人--与本人关系
            case R.id.relation2:
                relationShow(view);
                break;
            //其他联系人--联系人名称
            case R.id.tv_contact_name2:
                openContactClick(view, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    //    getContacts(this);
    //    getStringContacts(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        PermissionCheck.getInstance().askForPermissions(this, new String[]{
//                Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS,
//                Manifest.permission.READ_PHONE_STATE}, PermissionCheck.REQUEST_CODE_ALL);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            String[] phoneContactsData = PhoneUtil.getPhoneContactsData(data.getData());
            if (phoneContactsData == null) {
                return;
            }

            if (phoneContactsData.length > 1) {
                viewModel.setName1(phoneContactsData[0]);
                tvName1.setText(viewModel.getName1());
                if (!TextUtils.isEmpty(phoneContactsData[1])) {
                    //设置电话1
                    viewModel.setPhone1(phoneContactsData[1]);
                } else {
                    viewModel.setName1("");
                    viewModel.setPhone1("");
                }

            } else {
                viewModel.setName1("");
                viewModel.setPhone1("");
            }
        } else if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String[] phoneContactsData = PhoneUtil.getPhoneContactsData(data.getData());
            if (phoneContactsData == null) {
                return;
            }
            if (phoneContactsData.length > 1) {
                viewModel.setName2(phoneContactsData[0]);

                tvName2.setText(viewModel.getName2());

                if (!TextUtils.isEmpty(phoneContactsData[1])) {
                    //设置电话2
                    viewModel.setPhone2(phoneContactsData[1]);
                } else {
                    viewModel.setName2("");
                    viewModel.setPhone2("");
                }
            } else {
                viewModel.setName2("");
                viewModel.setPhone2("");
            }

        }

//            String[] contacts = PhoneUtil.getPhoneContacts(data.getData());
//            if (contacts == null) {
//                return;
//            }
//            if (contacts.length > 1) {
//                viewModel.setName1(contacts[0]);
//
//                tvName1.setText(viewModel.getName1());
//
//                if (!TextUtils.isEmpty(contacts[1]) && contacts[1].length() > 0) {
//                    String[] phone = contacts[1].split(",");
//                    if (phone.length > 1) {
//                        showPhones(contacts[0], phone, requestCode);
//                    } else {
//                        //设置电话1
//                        viewModel.setPhone1(contacts[1]);
//
//                        if (TextUtils.isEmpty(contacts[0])) {
//                            viewModel.setName1(contacts[1]);
//
//                            tvName1.setText(viewModel.getName1());
//
//                        }
//                    }
//                } else {
//                    viewModel.setName1("");
//                    viewModel.setPhone1("");
//                }
//            }
//        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
//            String[] contacts = PhoneUtil.getPhoneContacts(data.getData());
//            if (contacts == null) {
//                return;
//            }
//            if (contacts.length > 1) {
//                viewModel.setName2(contacts[0]);
//
//                tvName2.setText(viewModel.getName2());
//
//                if (!TextUtils.isEmpty(contacts[1]) && contacts[1].length() > 0) {
//                    String[] phone = contacts[1].split(",");
//                    if (phone.length > 1) {
//                        showPhones(contacts[0], phone, requestCode);
//                    } else {
//                        //设置电话2
//                        viewModel.setPhone2(contacts[1]);
//
//                        if (TextUtils.isEmpty(contacts[0])) {
//                            viewModel.setName2(contacts[1]);
//
//                            tvName2.setText(viewModel.getName2());
//                        }
//                    }
//                } else {
//                    viewModel.setName2("");
//                    viewModel.setPhone2("");
//                }
//            }
//        }
    }


    /**
     * 获取手机联系人-根据规则过滤通讯录
     */
    public Flowable<List> getContacts(final Context context) {
        return Flowable.create(new FlowableOnSubscribe<List>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<List> e) throws Exception {
                e.onNext(PhoneUtil.getContacts(context));
                e.onComplete();
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io());
    }


    /**
     * 获取手机联系人-不过滤任何信息(只去除空格)
     */
    public Flowable<String> getStringContacts(final Context context) {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext(Util.getContacts(context));
                e.onComplete();
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io());
    }


    /**
     * 初始化弹出框
     */
    private void init() {
        if (dic != null) {//数据字典获取内容
            List<KeyValueRec> temp;
            if (dic.getContactRelationList() != null) {
                temp = dic.getContactRelationList();
                for (int i = 0; i < temp.size(); i++) {
                    relation.add(temp.get(i).getValue());
                }
            }
            if (dic.getKinsfolkRelationList() != null) {
                temp = dic.getKinsfolkRelationList();
                for (int i = 0; i < temp.size(); i++) {
                    kinsfolkrelation.add(temp.get(i).getValue());
                }
            }
        }
        relationPicker = new OptionsPickerView(mContext);
        relationPicker.setPicker(relation);
        relationPicker.setCyclic(false);
        kinsfolkrelationPicker = new OptionsPickerView(mContext);
        kinsfolkrelationPicker.setPicker(kinsfolkrelation);
        kinsfolkrelationPicker.setCyclic(false);
        phonePicker = new OptionsPickerView(mContext);
    }

    /**
     * 数据字典请求
     */
    private void reqDic() {
        Call<HttpResult<DicRec>> callInit = RDClient.getService(MineService.class).getDicts(DicKey.CONTACT + "," + DicKey.KINSFOLK);
        NetworkUtil.showCutscenes(callInit);
        callInit.enqueue(new RequestCallBack<HttpResult<DicRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<DicRec>> call, Response<HttpResult<DicRec>> response) {
                dic = response.body().getData();
                init();
            }
        });
    }

    /**
     * 请求联系人信息
     */
    private void reqData() {
        Call<HttpResult<ListData<CreditLinkerRec>>> callInit = RDClient.getService(MineService.class).getContactInfoList();
        callInit.enqueue(new RequestCallBack<HttpResult<ListData<CreditLinkerRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<CreditLinkerRec>>> call, Response<HttpResult<ListData<CreditLinkerRec>>> response) {
                if (response.body().getData() != null) {
                    linkerList = response.body().getData().getList();
                    convert(linkerList);
                }
            }
        });
    }

    /**
     * dataModel to mRegisterVM
     */
    private void convert(List<CreditLinkerRec> list) {
        if (list != null && list.size() > 0) {
            if (Constant.STATUS_10.equals(list.get(0).getType())) {
                viewModel.setName1(list.get(0).getName());
                viewModel.setPhone1(list.get(0).getPhone());
                viewModel.setRelation1(list.get(0).getRelation());
                viewModel.setName2(list.get(1).getName());
                viewModel.setPhone2(list.get(1).getPhone());
                viewModel.setRelation2(list.get(1).getRelation());
            } else {
                viewModel.setName1(list.get(1).getName());
                viewModel.setPhone1(list.get(1).getPhone());
                viewModel.setRelation1(list.get(1).getRelation());
                viewModel.setName2(list.get(0).getName());
                viewModel.setPhone2(list.get(0).getPhone());
                viewModel.setRelation2(list.get(0).getRelation());
            }

            tvRelation1.setText(viewModel.getRelation1());
            tvRelation2.setText(viewModel.getRelation2());
            tvName1.setText(viewModel.getName1());
            tvName2.setText(viewModel.getName2());
        }
    }

    /**
     * 其他联系人的pickerView展现
     *
     * @param view
     */
    public void relationShow(final View view) {
        if (dic != null && dic.getContactRelationList() != null) {
            Util.hideKeyBoard(view);
            relationPicker.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    switch (view.getId()) {
                        case R.id.relation1:
                            viewModel.setRelation1(dic.getContactRelationList().get(options1).getValue());

                            tvRelation1.setText(viewModel.getRelation1());
                            break;
                        case R.id.relation2:
                            viewModel.setRelation2(dic.getContactRelationList().get(options1).getValue());

                            tvRelation2.setText(viewModel.getRelation2());
                            break;
                    }
                }
            });
            relationPicker.show();
        } else {
            ToastUtil.toast(R.string.credit_no_dic);
        }
    }

    /**
     * 直系亲属的pickerView展现
     *
     * @param view
     */
    public void kinsfolkrelationShow(final View view) {
        if (dic != null && dic.getKinsfolkRelationList() != null) {
            Util.hideKeyBoard(view);
            kinsfolkrelationPicker.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    switch (view.getId()) {
                        case R.id.relation1:
                            viewModel.setRelation1(dic.getKinsfolkRelationList().get(options1).getValue());

                            tvRelation1.setText(viewModel.getRelation1());
                            break;
                        case R.id.relation2:
                            viewModel.setRelation2(dic.getContactRelationList().get(options1).getValue());

                            tvRelation2.setText(viewModel.getRelation2());
                            break;
                    }
                }
            });
            kinsfolkrelationPicker.show();
        } else {
            ToastUtil.toast(R.string.credit_no_dic);
        }
    }

    /**
     * 打开通讯录
     *
     * @param view
     * @param index 选择第一个通讯录电话
     */

    public void openContactClick( View view, final int index) {
                if (!AndPermission.hasPermissions(mContext,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_PHONE_STATE))
                {
                    AndPermission.with(mContext)
                            .runtime()
                            .permission(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS,
                                    Manifest.permission.READ_PHONE_STATE})
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_PICK);
                                    //  ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                                    intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                                    //  intent.setData(ContactsContract.Contacts.CONTENT_URI);
                                    startActivityForResult(intent, index);
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    ToastUtil.toast("请开启读取通讯录的权限");
                                }
                            })
                            .start();
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    //  ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                    intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    //  intent.setData(ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, index);
                }
    }

    public void showPhones(String name, String[] phone, final int code) {
        phones.clear();
        for (int i = 0; i < phone.length; i++) {
            phones.add(phone[i]);
        }
        phonePicker.setPicker(phones);
        phonePicker.setCyclic(false);
        phonePicker.setTitle(mContext.getString(R.string.linker_phone_title, ""));
        phonePicker.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if (code == 0) {
                    viewModel.setPhone1(phones.get(options1));

                    if (TextUtils.isEmpty(viewModel.getName1())) {
                        viewModel.setName1(phones.get(options1));
                    }
                } else if (code == 1) {
                    viewModel.setPhone2(phones.get(options1));

                    if (TextUtils.isEmpty(viewModel.getName2())) {
                        viewModel.setName2(phones.get(options1));
                    }
                }
            }
        });
        phonePicker.show();
    }

    private void subPhone(List contactsInfo) {
        Gson gson = new Gson();
        String contactsInfoGson = gson.toJson(contactsInfo);

        PhoneInfoSub sub = new PhoneInfoSub();
        sub.setInfo(Base64.encode(contactsInfoGson.getBytes()));
        Call<HttpResult> contactCall = RDClient.getService(MineService.class).contacts(sub);
        NetworkUtil.showCutscenes(contactCall);
        contactCall.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                if (response.body().getCode() == 200) {
                    submit();
                }
            }

            @Override
            public void onFailed(Call<HttpResult> call, Response<HttpResult> response) {
            }
        });
    }

    private void submit() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        viewModel.setRelation1(tvRelation1.getText().toString());
        viewModel.setRelation2(tvRelation2.getText().toString());
        viewModel.setName1(tvName1.getText().toString());
        viewModel.setName2(tvName2.getText().toString());


        CreditLinkerSub sub = new CreditLinkerSub();
        StringBuffer name = new StringBuffer();
        StringBuffer relation = new StringBuffer();
        StringBuffer phone = new StringBuffer();
        StringBuffer id = new StringBuffer();
        if (linkerList != null) {
            if (Constant.STATUS_10.equals(linkerList.get(0).getType())) {
                id.append(linkerList.get(0).getId()).append(",").append(linkerList.get(1).getId());
            } else {
                id.append(linkerList.get(1).getId()).append(",").append(linkerList.get(0).getId());
            }
        } else {
            id.append("0,0");
        }
        if (strContacts != null && strContacts.contains(viewModel.getName1())
                && strContacts.contains(viewModel.getName2())) {
            name.append(viewModel.getName1()).append(",").append(viewModel.getName2());
        } else {
            DialogUtils.showConfirmDialog(mContext, "请选择有效联系人信息", null);
            return;
        }

        relation.append(viewModel.getRelation1()).append(",").append(viewModel.getRelation2());
        phone.append(Util.filterUnNumber(viewModel.getPhone1())).append(",").append(Util.filterUnNumber(viewModel.getPhone2()));
        sub.setId(id.toString());
        sub.setName(name.toString());
        sub.setPhone(phone.toString());
        sub.setRelation(relation.toString());
        sub.setMac(DeviceUtil.macAddress());
        sub.setOperatingSystem(DeviceUtil.getBuildVersion());
        sub.setPhoneBrand(DeviceUtil.getPhoneBrand());
        sub.setPhoneType(DeviceUtil.getPhoneModel());
        sub.setPhoneMark(DeviceUtil.getDeviceId(tm));
        sub.setSystemVersions(DeviceUtil.getBuildVersion());
        sub.setVersionCode(DeviceUtil.getVersionCode(mContext));
        sub.setVersionName(DeviceUtil.getVersionName(mContext));
        sub.setType(Constant.STATUS_10 + "," + Constant.STATUS_20);

        Call<HttpResult> callInit = RDClient.getService(MineService.class).contactSaveOrUpdate(sub);
        NetworkUtil.showCutscenes(callInit);
        callInit.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {

                DialogUtils.showConfirmDialog(mContext, response.body().getMsg(),
                        new DialogUtils.btnConfirmClick() {
                            @Override
                            public void confirm() {
                                EmergencyContactActivity.this.finish();
                            }
                        });
            }
        });
    }
}
