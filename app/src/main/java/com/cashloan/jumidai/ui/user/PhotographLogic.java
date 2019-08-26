package com.cashloan.jumidai.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.cashloan.jumidai.R;
import com.commom.net.Config;
import com.commom.utils.BitmapUtil;
import com.commom.utils.FileSizeUtil;
import com.commom.utils.FileUtil;
import com.commom.widget.popupWindow.PickPopupWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/5/20 17:41
 * <p/>
 * Description: 拍照逻辑处理类
 */
public class PhotographLogic {
    // 相册
    public static final int    REQUEST_CODE_PICK    = 0;
    // 拍照
    public static final int    REQUEST_CODE_TAKE    = 1;
    // 剪裁
    public static final int    REQUEST_CODE_CUTTING = 2;
    // 照片存储路径
    private static      String FILE_PATH            = Config.ROOT_PATH.get() + "/photo";
    // 本次操作需要保存的照片名
    private static String          IMAGE_FILE_NAME;
    private static PickPopupWindow popupWindow;
    private static boolean IS_CUT = true;

    private static Context mContext;

    /**
     * 获取照片
     *
     * @param view      view
     * @param photoName 照片名称
     */
    public static void obtain(View view, String photoName, boolean isCut) {
        if (TextUtils.isEmpty(photoName)) {
            return;
        }
        mContext = view.getContext();

        check();
        IMAGE_FILE_NAME = photoName;
        IS_CUT = isCut;
        popupWindow = new PickPopupWindow(view.getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                // 相册
                if (R.id.first == view.getId()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    (getActivity(view)).startActivityForResult(intent, REQUEST_CODE_PICK);
                } else if (R.id.second == view.getId()) {
                    // 拍照
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        //适配7.0 FileProvider
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //内部存储卡位---files_paths---external-path 对应获取方法Environment.getExternalStorageDirectory()
                            File iamgePath = new File(Environment.getExternalStorageDirectory(), "jmd/photo/");
                            File filePic = new File(iamgePath, IMAGE_FILE_NAME);
                            // TODO: 2018/9/11 包名修改后此处的3-4个授权信息一并修改
                            Uri photoOutputUri = FileProvider.getUriForFile(mContext, "com.cashloan.jumidai", filePic);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
                            //需要赋予Uri临时访问权限
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            (getActivity(view)).startActivityForResult(intent, REQUEST_CODE_TAKE);
                        } else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(FILE_PATH, IMAGE_FILE_NAME)));
                            (getActivity(view)).startActivityForResult(intent, REQUEST_CODE_TAKE);
                        }
                    }
                }
            }
        });
        popupWindow.showAtLocation(view.getRootView(), Gravity.BOTTOM, 0, 0);
    }

    /** onActivityResult */
    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, PhotographCallback callback) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 相册
                case REQUEST_CODE_PICK:
                    // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                    if (null != data) {
                        if (!IS_CUT) {
                            if (!TextUtils.isEmpty(IMAGE_FILE_NAME)) {
                                callback.obtain(decodeUriAsBitmap(activity, data.getData()), IMAGE_FILE_NAME);
                            }
                        } else {
                            startPhotoZoom(activity, data.getData());
                        }
                    }
                    break;

                // 拍照
                case REQUEST_CODE_TAKE:
                    File file = new File(FILE_PATH + File.separator + IMAGE_FILE_NAME);
                    if (!IS_CUT) {
                        if (!TextUtils.isEmpty(IMAGE_FILE_NAME)) {
                            //适配7.0
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //内部存储卡位置
                                File iamgePath = new File(Environment.getExternalStorageDirectory(), "jmd/photo/");
                                File filePic = new File(iamgePath, IMAGE_FILE_NAME);
                                Uri photoOutputUri = FileProvider.getUriForFile(mContext, "com.cashloan.jumidai", filePic);
                                callback.obtain(decodeUriAsBitmap(activity, photoOutputUri), IMAGE_FILE_NAME);
                            } else {
                                Uri uri = Uri.fromFile(file);
                                callback.obtain(decodeUriAsBitmap(activity, uri), IMAGE_FILE_NAME);
                            }
                        }
                    } else {
                        //适配7.0
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //内部存储卡位置
                            File iamgePath = new File(Environment.getExternalStorageDirectory(), "jmd/photo/");
                            File filePic = new File(iamgePath, IMAGE_FILE_NAME);
                            Uri photoOutputUri = FileProvider.getUriForFile(mContext, "com.cashloan.jumidai", filePic);
                            startPhotoZoom(activity, photoOutputUri);
                        } else {
                            startPhotoZoom(activity, Uri.fromFile(file));
                        }

                    }
                    break;

                // 剪裁
                case REQUEST_CODE_CUTTING:
                    if (!TextUtils.isEmpty(IMAGE_FILE_NAME)) {
                        //适配7.0
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            File iamgePath = new File(Environment.getExternalStorageDirectory(), "jmd/photo/");
                            File filePic = new File(iamgePath, IMAGE_FILE_NAME);
                            Uri photoOutputUri = FileProvider.getUriForFile(mContext, "com.cashloan.jumidai", filePic);
                            callback.obtain(decodeUriAsFile(activity, photoOutputUri), IMAGE_FILE_NAME);
                        } else {
                            Uri uri = Uri.fromFile(new File(FILE_PATH + File.separator + IMAGE_FILE_NAME));
                            callback.obtain(decodeUriAsFile(activity, uri), IMAGE_FILE_NAME);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 剪裁头像
     */
    private static void startPhotoZoom(Activity activity, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 512);
        intent.putExtra("aspectY", 512);
        // outputX outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 512);
        intent.putExtra("outputY", 512);
        // 文件输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 是否保留比例
        intent.putExtra("scale", true);
        // 是否返回数据
        intent.putExtra("return-data", false);
        // 关闭人脸检测
        intent.putExtra("noFaceDetection", true);
        // 保存为文件

        //适配7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //内部存储卡位置
            File iamgePath = new File(Environment.getExternalStorageDirectory(), "jmd/photo/");
            File filePic = new File(iamgePath, IMAGE_FILE_NAME);
            Uri photoOutputUri = FileProvider.getUriForFile(mContext, "com.cashloan.jumidai", filePic);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
            //需要赋予Uri临时访问权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.startActivityForResult(intent, REQUEST_CODE_CUTTING);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(FILE_PATH + File.separator + IMAGE_FILE_NAME)));
            activity.startActivityForResult(intent, REQUEST_CODE_CUTTING);
        }
    }

    /**
     * Uri 转 File
     */
    private static File decodeUriAsFile(Activity activity, Uri uri) {
        Bitmap bitmap = decodeUriAsBitmap(activity, uri);
        if (null == bitmap) {
            return null;
        }
        return FileUtil.saveFile(activity, FILE_PATH, IMAGE_FILE_NAME, bitmap, FileSizeUtil.SIZE_TYPE_MB, 2.0);
    }

    /**
     * Uri 转 Bitmap
     */
    private static Bitmap decodeUriAsBitmap(Activity activity, Uri uri) {
        try {
            return BitmapUtil.getBitmapFormUri(activity, uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检验目录是否存在
     */
    private static void check() {
        File dir = new File(FILE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 增加目录
     */
    public static void addDirectory(String directoryName) {
        if (!TextUtils.isEmpty(directoryName)) {
            FILE_PATH = Config.ROOT_PATH.get() + File.separator + directoryName;
        } else {
            FILE_PATH = Config.ROOT_PATH.get();
        }
    }

    public interface PhotographCallback {
        void obtain(File file, String photoName);

        void obtain(Bitmap bitmap, String photoName);
    }

    /**
     * 通过view暴力获取getContext()(Android不支持view.getContext()了)
     *
     * @param view
     *         要获取context的view
     *
     * @return 返回一个activity
     */
    /**
     * 通过 View 获取Activity
     */
    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (Activity) view.getRootView().getContext();
    }
}
