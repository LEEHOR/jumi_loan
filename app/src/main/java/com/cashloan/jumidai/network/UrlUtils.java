package com.cashloan.jumidai.network;

import android.text.TextUtils;
import android.util.Log;

import com.commom.utils.ActivityManage;
import com.commom.utils.ContextHolder;
import com.commom.utils.encryption.MDUtil;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.BaseParams;
import com.cashloan.jumidai.common.Constant;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.cashloan.jumidai.utils.SharedInfo;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.statistics.DeviceInfoUtils;
import com.umeng.analytics.AnalyticsConfig;

import java.io.File;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/12/20 15:04
 * <p/>
 * Description:
 */
public class UrlUtils {
    /**
     * 通用参数
     */
    private TreeMap<String, String> commonParamsTreeMap;

    private UrlUtils() {
        commonParamsTreeMap = new TreeMap<>();
        commonParamsTreeMap.put(Constant.MOBILE_TYPE, BaseParams.MOBILE_TYPE);
        commonParamsTreeMap.put(Constant.UPDATE_CHANNEL, Util.changeChannelCodeToNumberId(AnalyticsConfig.getChannel(ActivityManage.peek())));
        commonParamsTreeMap.put(Constant.VERSION_NUMBER, DeviceInfoUtils.getVersionName(ContextHolder.getContext()));
    }

    public static UrlUtils getInstance() {
        return NetworkUtilsInstance.instance;
    }

    private static class NetworkUtilsInstance {
        static UrlUtils instance = new UrlUtils();
    }

    /**
     * 对字符串进行签名
     */
    public String dynamicParams(String postBodyString) {
        TreeMap<String, String> treeMap = splitPostString(postBodyString);
        treeMap = dynamicParams(treeMap);
        return getPostParamsStr(treeMap);
    }

    /**
     * 动态拼接请求参数
     */
    public TreeMap<String, String> dynamicParams(TreeMap<String, String> map) {
        String ts = String.valueOf(System.currentTimeMillis() / 1000);
        //map.put(Constant.TS, ts);
        String token = getToken();
        String userId = getUserId();
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(userId)) {
            map.put(Constant.TOKEN, token);
            map.put(Constant.USER_ID, userId);
        }
        return map;
    }

    /**
     * 提交文件 对Map数据进行签名
     */
    public String signParams(TreeMap<String, String> treeMap) {
        treeMap.putAll(commonParamsTreeMap);
        String sign = getSign(treeMap);
        return sign;
    }

    /**
     * 对Map数据进行签名
     */
    public Map signParams(Map<String, String> treeMap) {
        TreeMap map = new TreeMap();
        map.putAll(commonParamsTreeMap);
        map.putAll(treeMap);
        map = dynamicParams(map);
        String sign = getSign(map);
        TreeMap headMap = new TreeMap();
        headMap.put(Constant.SIGNA, sign);
        headMap.put(Constant.TOKEN, getToken());
        return headMap;
    }

    /**
     * 分割请求参数，放入treeMap中,拼接动态参数
     *
     * @param postBodyString 请求参数
     */
    private TreeMap<String, String> splitPostString(String postBodyString) {
        TreeMap<String, String> map = new TreeMap<>();
        for (String s : postBodyString.split("&")) {
            String[] keyValue = s.split("=");
            map.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
        }
        return map;
    }


    /**
     * 一般接口调用-signa签名生成规则
     *
     * @param map 有序请求参数map
     */
    private String getSign(TreeMap map) {
        String signa = "";
        try {
            Iterator it = map.entrySet().iterator();
            StringBuilder sb = new StringBuilder();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (entry.getValue() instanceof File)
                    continue;//URLEncoder.encode(, "UTF-8")
                sb.append(entry.getKey()).append("=").append(URLDecoder.decode(entry.getValue().toString(), "UTF-8")).append("|");
            }
            // 所有请求参数排序后的字符串后进行MD5（32）
            //signa = MDUtil.encode(MDUtil.TYPE.MD5, sb.toString());
            // 得到的MD5串拼接appsecret再次MD5，所得结果转大写
            String sign = "";
            if (sb.toString().length() > 1) {
                sign = sb.toString().substring(0, sb.length() - 1);
            } else {
                sign = sb.toString();
            }

            if (AppConfig.IS_DEBUG) {
               // Log.e("网络请求公共参数", sign);
                //System.out.println("sb.toString()" + AppConfig.APP_KEY + getToken() + sign);
            }

            signa = MDUtil.encode(MDUtil.TYPE.MD5, AppConfig.APP_KEY + getToken() + sign).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signa;
    }

    /**
     * 将map拼装成请求字符串
     *
     * @return 返回请求参数
     */
    public String getPostParamsStr(TreeMap map) {
        Iterator it = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        try {
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                sb.append(entry.getKey()).append("=").append(entry.getValue().toString()).append("&");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sb.toString().length() > 1) {
            return sb.toString().substring(0, sb.length() - 1);
        } else {
            return sb.toString();
        }
    }

    /**
     * 获取oauthToken
     */
    public String getToken() {
        OauthTokenMo mo = SharedInfo.getInstance().getEntity(OauthTokenMo.class);
        if (mo != null) {
            return mo.getToken();
        }
        return "";
    }

    /**
     * 获取userId
     */
    private String getUserId() {
        OauthTokenMo mo = SharedInfo.getInstance().getEntity(OauthTokenMo.class);
        if (mo != null) {
            return mo.getUserId();
        }
        return "";
    }
}