package com.cashloan.jumidai.ui.homeMine.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.common.ViewClick;
import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.UrlUtils;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.adapter.WorkImagesAdapter;
import com.cashloan.jumidai.ui.homeMine.bean.CreditWorkPhotoVM;
import com.cashloan.jumidai.ui.homeMine.bean.submit.CreditWorkPhotoSub;
import com.cashloan.jumidai.ui.user.PhotographLogic;
import com.cashloan.jumidai.utils.ObjectDynamicCreator;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;
import com.commom.net.OkHttp.utils.FileUploadUtil;
import com.commom.utils.OnOnceClickListener;
import com.commom.utils.ToastUtil;
import com.commom.widget.NoDoubleClickButton;
import com.github.mzule.activityrouter.annotation.Router;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/27
 * 描述： 工作照片
 */
@Router(value = RouterUrl.Mine_CreditWorkPhoto)
public class WorkInfoImageActivity extends BaseMvpActivity {

    @ViewInject(R.id.ll_show_workImage)
    private LinearLayout        llShowImages;//
    @ViewInject(R.id.iv_workImage_show)
    private ImageView           ivImage;//
    @ViewInject(R.id.tv_workImage_tips)
    private TextView            tvTips;//
    @ViewInject(R.id.rc_work_image)
    private RecyclerView        mRecyclerView;
    @ViewInject(R.id.tv_workImage_tips)
    private TextView            tvTips2;//
    @ViewInject(R.id.ndb_upload_images)
    private NoDoubleClickButton btnUpload;//


    File file1, file2, file3;
    private int spacingInPixels = 8;

    private List<CreditWorkPhotoVM> dataList;
    //    private int                uploadEnable = View.GONE;
    private String[]           workPhotos = new String[]{"workFirst.jpg", "workSecond.jpg", "workThird.jpg"};
    private CreditWorkPhotoSub photoSub   = new CreditWorkPhotoSub();
    private List<String> currentList;
    private Drawable     showUrl;
    private Boolean canUpload = false; //上传按钮是否能点击
    private Boolean showImg   = false;

    private WorkImagesAdapter mAdapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_work_info_image;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);
        setPageTitleBack("工作照片");
    }

    @Override
    protected void initFunc() {
        attachClickListener(btnUpload);

        dataList = new ArrayList<>();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new WorkImagesAdapter(this, dataList,
                new OnOnceClickListener() {
                    @Override
                    public void onOnceClick(View v) {
                        Integer position = (Integer) v.getTag();
                        final CreditWorkPhotoVM photoVM = (CreditWorkPhotoVM) dataList.get(position);
                        doRcChildClick(v, position, photoVM);
                    }
                }
        );
        mRecyclerView.setAdapter(mAdapter);

        req_data();
    }

    //上传图片
    @Override
    protected void onViewClicked(View view) {
        upload(view);
    }

    /****item 图片的点击事件****/
    private void doRcChildClick(View view, int position, CreditWorkPhotoVM photoVM) {
        switch (view.getId()) {
            //点击 删除图片
            case R.id.iv_delete:

                DialogUtils.showDefaultDialog(mContext, "是否删除照片？",
                        new DialogUtils.btnConfirmClick() {
                            @Override
                            public void confirm() {
                                int size = mAdapter.getData().size();
                                dataList.remove(photoVM);
                                if (size == 3) {
                                    CreditWorkPhotoVM vm = (CreditWorkPhotoVM) mAdapter.getData().get(1);
                                    if (vm.getUploadEnable() != View.VISIBLE) {
                                        CreditWorkPhotoVM photoVM = new CreditWorkPhotoVM();
                                        photoVM.setUploadEnable(View.VISIBLE);
                                        photoVM.setIsUpload(View.VISIBLE);
                                        dataList.add(photoVM);
                                    }
                                }
                                if (size == 1) {
                                    if (currentList.size() == 0) {
                                        canUpload = false;

                                    }
                                } else if (size == 2) {
                                    if (currentList.size() == 0) {
                                        photoSub.setWorkImgFir(null);
                                        canUpload = false;
                                    } else if (currentList.size() == 1) {
                                        canUpload = false;
                                        photoSub.setWorkImgSec(null);
                                    }
                                } else if (size == 3) {
                                    if (currentList.size() == 0) {
                                        photoSub.setWorkImgFir(null);
                                        canUpload = true;
                                    } else if (currentList.size() == 1) {
                                        photoSub.setWorkImgSec(null);
                                        canUpload = true;
                                    } else if (currentList.size() == 2) {
                                        photoSub.setWorkImgThr(null);
                                        canUpload = false;
                                    }
                                }

                                btnUpload.setEnabled(canUpload);

                                mAdapter.notifyDataSetChanged();
                            }
                        });
                break;
            case R.id.iv_pic:
                if (photoVM.getUploadEnable() == View.GONE) {
                    //                        showUrl =currentList.get(getPosition()));
                    if (view instanceof ImageView) {
                        showUrl = null;
                        showImg = true;
                        ImageView iv = (ImageView) view;
                        showUrl = iv.getDrawable();
                    }
                }
                break;
            //添加 上传图片
            case R.id.iv_upload:

                if (AndPermission.hasPermissions(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)) {

                    PhotographLogic.obtain(view, System.currentTimeMillis() + workPhotos[position], true);

                } else {
                    AndPermission.with(mContext)
                            .runtime()
                            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    PhotographLogic.obtain(view, System.currentTimeMillis() + workPhotos[position], true);
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    DialogUtils.showPermisssionDialog(mContext);
                                }
                            }).start();
                }

//                PhotographLogic.obtain(view, System.currentTimeMillis() + workPhotos[position], true);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotographLogic.onActivityResult(WorkInfoImageActivity.this, requestCode,
                resultCode, data, new PhotographLogic.PhotographCallback() {

                    @Override
                    public void obtain(File file, String photoName) {
                        int size = dataList.size();
                        int adapterSize = mAdapter.getData().size();
                        if (adapterSize == 1) {
                            file1 = new File(file.getPath());
                            if (file1.exists()) {
                                photoSub.setWorkImgFir(file1);
                            }
                        } else if (adapterSize == 2) {
                            file2 = new File(file.getPath());
                            if (file2.exists()) {
                                photoSub.setWorkImgSec(file2);
                            }
                        } else if (adapterSize == 3) {
                            file3 = new File(file.getPath());
                            if (file3.exists()) {
                                photoSub.setWorkImgThr(file3);
                            }
                        }
                        if (size < 4) {
                            if (size != 3) {
                                CreditWorkPhotoVM photoVM = new CreditWorkPhotoVM();
                                photoVM.setUploadEnable(View.VISIBLE);
                                photoVM.setIsUpload(View.VISIBLE);
                                dataList.add(photoVM);
                            }

                            dataList.get(size - 1).setUrl(file.getPath());
                            dataList.get(size - 1).setUploadEnable(View.GONE);

                            mAdapter.notifyDataSetChanged();
                        }
                        canUpload = true;
                        btnUpload.setEnabled(canUpload);
                    }

                    @Override
                    public void obtain(Bitmap bitmap, String photoName) {

                    }
                });
    }


    private void req_data() {
        Call<HttpResult<ListData<String>>> call = RDClient.getService(MineService.class).getWorkImg();
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<ListData<String>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<String>>> call,
                                  Response<HttpResult<ListData<String>>> response) {
                currentList = response.body().getData().getList();
                convert(currentList);
            }
        });
    }

    private void convert(List<String> list) {

        if (list != null && list.size() > 0) {
            if (list.size() < 4)
                for (int i = 0; i < list.size(); i++) {
                    CreditWorkPhotoVM photoVM = new CreditWorkPhotoVM();
                    photoVM.setUrl(list.get(i));
                    photoVM.setUploadEnable(View.GONE);
                    photoVM.setIsUpload(View.GONE);
                    photoVM.setIsComplete(View.VISIBLE);
                    dataList.add(photoVM);
                }
            if (list.size() != 3) {
                CreditWorkPhotoVM photoVM2 = new CreditWorkPhotoVM();
                photoVM2.setUploadEnable(View.VISIBLE);
                photoVM2.setIsUpload(View.VISIBLE);
                dataList.add(photoVM2);
            }
        } else {
            CreditWorkPhotoVM photoVM = new CreditWorkPhotoVM();
            photoVM.setUploadEnable(View.VISIBLE);
            photoVM.setIsUpload(View.VISIBLE);
            dataList.add(photoVM);
        }
        if (list != null && list.size() < 3) {
            canUpload = true;
        } else {
            canUpload = false;
        }

        btnUpload.setEnabled(canUpload);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 上传照片
     */
    public void upload(final View view) {
        if (currentList.size() == 0) {

            if (photoSub.getWorkImgFir() == null) {
                ToastUtil.toast("请先选择照片");
                return;
            }
        } else if (currentList.size() == 1) {
            photoSub.setWorkImgFir(null);
            if (photoSub.getWorkImgSec() == null) {
                ToastUtil.toast("请先选择照片");
                return;
            }
        } else if (currentList.size() == 2) {
            photoSub.setWorkImgFir(null);
            photoSub.setWorkImgSec(null);
            if (photoSub.getWorkImgThr() == null) {
                ToastUtil.toast("请先选择照片");
                return;
            }
        }

        Map<String, Object> map = ObjectDynamicCreator.getFieldVlaue(photoSub);
        TreeMap treeMap = new TreeMap(map);
        treeMap = UrlUtils.getInstance().dynamicParams(treeMap);
        TreeMap temp = new TreeMap(treeMap);
        Map<String, String> head = new HashMap<>();
        head.put(Constant.TOKEN, UrlUtils.getInstance().getToken());
        head.put(Constant.SIGNA, UrlUtils.getInstance().signParams(temp));

        Call<HttpResult> call = RDClient.getService(MineService.class).workImgSave(head, FileUploadUtil.getRequestMap(treeMap));
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {

                DialogUtils.showConfirmDialog(mContext, response.body().getMsg(),
                        new DialogUtils.btnConfirmClick() {
                            @Override
                            public void confirm() {
                                canUpload = false;
                                btnUpload.setEnabled(canUpload);

                                Util.getActivity(view).setResult(1);
                                Util.getActivity(view).finish();
                            }
                        });
            }
        });

    }

    public void imgClick(View view) {
        showImg = false;
    }

    public class CreditWorkPhotoClick extends ViewClick {
        @Override
        public void onClick(final View v) {
            final CreditWorkPhotoVM photoVM = (CreditWorkPhotoVM) getObject();
            doRcChildClick(v, getPosition(), photoVM);
        }
    }
}
