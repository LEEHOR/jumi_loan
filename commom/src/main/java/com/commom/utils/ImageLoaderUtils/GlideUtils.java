package com.commom.utils.ImageLoaderUtils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.commom.R;

/**
 * 作者： Ruby
 * 时间： 2018/8/2
 * 描述：
 */

public class GlideUtils {

    private static final int RES_ERROR     = R.mipmap.icon_loading_error;
    private static final int RES_HOLDER    = R.mipmap.icon_loading_empty;
    private static final int RES_FAILED    = R.mipmap.icon_loading_empty;
    private static final int RES_EMPTY_URL = R.mipmap.icon_loading_empty;


    public static RequestOptions mOptions = new RequestOptions()
            // 	禁用内存缓存功能
            .skipMemoryCache(true)
            /**
             * 硬盘缓存模式
             * DiskCacheStrategy.NONE 	    表示不缓存任何内容。
             * DiskCacheStrategy.DATA 	    表示只缓存原始图片。
             * DiskCacheStrategy.RESOURCE 	表示只缓存转换过后的图片。
             * DiskCacheStrategy.ALL 	    表示既缓存原始图片，也缓存转换过后的图片。
             * DiskCacheStrategy.AUTOMATIC 	表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）
             */
            .diskCacheStrategy(DiskCacheStrategy.ALL)

            .centerCrop()
            .centerInside()
            .circleCrop()
            .dontAnimate()
            .dontTransform()
            //请求失败是展示的图片
            .error(RES_ERROR)
            //当请求正在执行时被展示的图片
            .placeholder(RES_HOLDER)
            //在请求的url/model为 null 时展示的图片
            .fallback(RES_EMPTY_URL)

            .optionalCenterCrop()
            .optionalCenterInside()
            .optionalCircleCrop()
            .optionalFitCenter();

    public static RequestOptions mDefOptions = new RequestOptions()
            // 	禁用内存缓存功能
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .centerInside()
            .dontAnimate()
            .dontTransform()
            //请求失败是展示的图片
            .error(RES_ERROR)
            .optionalFitCenter();

    public static void disPlayImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).apply(mDefOptions).into(imageView);
    }

    public static void disPlayHolderImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

}
