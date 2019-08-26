package com.commom.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 作者： zxb
 * 时间： 2017/8/18
 * 描述：
 */

public class DoubleUtils {

    /**
     * double相加
     */
    public static double add(double a1, double b1) {
        BigDecimal a2 = new BigDecimal(Double.toString(a1));
        BigDecimal b2 = new BigDecimal(Double.toString(b1));
        return a2.add(b2).doubleValue();
    }

    /**
     * double相减
     */
    public static double sub(double a1, double b1) {
        BigDecimal a2 = new BigDecimal(Double.toString(a1));
        BigDecimal b2 = new BigDecimal(Double.toString(b1));
        return a2.subtract(b2).doubleValue();
    }

    /**
     * double相乘
     */
    public static double mul(double a1, double b1) {
        BigDecimal a2 = new BigDecimal(Double.toString(a1));
        BigDecimal b2 = new BigDecimal(Double.toString(b1));
        return a2.multiply(b2).doubleValue();
    }

    /**
     * double相除
     * scale参数为除不尽时，指定精度
     */
    public static double div(double a1, double b1, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("error");
        }
        BigDecimal a2 = new BigDecimal(Double.toString(a1));
        BigDecimal b2 = new BigDecimal(Double.toString(b1));
        return a2.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * double数值太大时，不以科学计数法形式先生，保留小数点后两位
     */
    public static String saveToPoint2(double a1) {
        DecimalFormat df = new DecimalFormat("#,##0.00");// 格式化设置
        return df.format(a1);
    }

//    public static String saveToPoint2WithNo(double a1) {
//        DecimalFormat df = new DecimalFormat("#,##0.00");// 格式化设置
//        return df.format(a1);
//    }
//
//    public static String saveToPoint2(float a1) {
//        DecimalFormat df = new DecimalFormat("#,##0.00");// 格式化设置
//        return df.format(a1);
//    }
}
