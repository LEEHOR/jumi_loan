package com.cashloan.jumidai.utils;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.commom.utils.ContextHolder;
import com.commom.utils.ConverterUtil;
import com.commom.utils.DensityUtil;
import com.commom.utils.StringFormat;
import com.commom.utils.TextUtil;


/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/10/21 17:08
 * <p/>
 * Description:
 */
public class DisplayFormat {
    /**
     * 获取String
     */
    public static String getResourcesString(int id) {
        return ContextHolder.getContext().getString(id);
    }

    /**
     * 对年化利率的小数点之后的格式化
     */
    public static Spannable XLIFFAprFormat(String params, int size) {
        params = StringFormat.twoDecimalFormat(params) + "%";
        if (params.contains(".")) {
            int       start = 0;
            int       end   = params.length() - 1;
            Spannable span  = new SpannableString(params);
            // 设置字体大小
            span.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(ContextHolder.getContext(), size)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else {
            return new SpannableString(params);
        }
    }

    /**
     * 对年化利率百分号的之后的格式化
     */
    public static Spannable XLIFFAprFormat(String apr, String addApr, int size) {
        String params = StringFormat.twoDecimalFormat(apr) + "%";
        if (addApr != null && ConverterUtil.getDouble(addApr) != 0 && !addApr.equals("")) {
            params = params + "+" + StringFormat.twoDecimalFormat(addApr) + "%";
        }
        if (params.contains(".")) {
            int       start = 0;
            int       end   = params.indexOf("%");
            Spannable span  = new SpannableString(params);
            // 设置字体大小
            span.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(ContextHolder.getContext(), size)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else {
            return new SpannableString(params);
        }
    }

    /**
     * 对指定的字符串大小设置的格式化
     */
    public static Spannable XLIFFStrFormat(String params, String replace, int size) {
        if (replace != null && params != null && params.contains(replace)) {
            int       start = params.indexOf(replace);
            int       end   = start + replace.length();
            Spannable span  = new SpannableString(params);
            // 设置字体大小
            span.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(ContextHolder.getContext(), size)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else {
            return new SpannableString(params);
        }
    }

    /**
     * 对中间部分字符串的颜色修改
     */
    public static Spannable XLIFFPremiumFormat(String params, String replace, int color) {
        if (replace != null && params != null && params.contains(replace)) {
            int       start = params.indexOf(replace);
            int       end   = start + replace.length();
            Spannable span  = new SpannableString(params);
            // 设置字体颜色
            span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else {
            return new SpannableString(params);
        }
    }

    /**
     * 对中间部分字符串的颜色修改
     */
    public static Spannable XLIFFPremiumFormat(Spannable params, String replace, int color) {
        String str = params.toString();
        if (!TextUtil.isEmpty(replace) && !TextUtil.isEmpty(str) && str.contains(replace)) {
            int start = str.indexOf(replace);
            int end   = start + replace.length();
            // 设置字体颜色
            params.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return params;
        } else {
            return params;
        }
    }

    /**
     * 获得隐私保护托管账户
     */
    public static String accountHideFormat(String args) {
        if (TextUtils.isEmpty(args) || args.length() < 4) {
            return args;
        }
        return args.substring(0, 3) + "****" + args.substring(args.length() - 4, args.length());
    }
    /**
     * 添加背景图片 setColorFilter
     * @param drawable
     * @return
     */
    public static Drawable getBackground(Drawable drawable){
//        drawable.setColorFilter(ContextCompat.getColor(ContextHolder.getContext(), R.color.app_color_principal), PorterDuff.Mode.MULTIPLY);
        return drawable;
    }
}
