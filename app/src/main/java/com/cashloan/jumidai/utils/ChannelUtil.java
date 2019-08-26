package com.cashloan.jumidai.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Description : 渠道读取工具类  <br/>
 * author : WangGanxin <br/>
 * date : 2016/12/20 <br/>
 * email : mail@wangganxin.me <br/>
 */
public class ChannelUtil {
	
	private static final String CHANNEL_KEY     = "channel";
	private static final String CHANNEL_DEFAULT = "app";
	
	private static final String PREF_KEY_CHANNEL         = "pref_key_channel";
	private static final String PREF_KEY_CHANNEL_VERSION = "pref_key_channel_version";
	
	private static String mChannel;

	private static final String FILE_NAME = "channelFile";
	
	/**
	 * 返回市场。  如果获取失败返回""
	 * @param context
	 * @return
	 */
	public static String getChannel(Context context){
		return getChannel(context, CHANNEL_DEFAULT);
	}
	
	/**
	 * 返回市场。  如果获取失败返回defaultChannel
	 * @param context
	 * @param defaultChannel
	 * @return
	 */
	public static String getChannel(Context context, String defaultChannel) {
		//内存中获取
		if(!TextUtils.isEmpty(mChannel)){
			return mChannel;
		}
		//sp中获取
//		mChannel = getChannelFromSP(mContext);
//		if(!TextUtils.isEmpty(mChannel)){
//			return mChannel;
//		}
		//从apk中获取
		mChannel = getChannelFromApk(context, CHANNEL_KEY);
		if(!TextUtils.isEmpty(mChannel)){
			//保存sp中备用
//			saveChannelInSP(mContext, mChannel);
			return mChannel;
		}
		//全部获取失败
		return defaultChannel;
    }
	
	/**
	 * 从apk中获取版本信息
	 * @param context
	 * @param channelKey
	 * @return
	 */
	private static String getChannelFromApk(Context context, String channelKey) {
		//从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                Log.d("APK entry name" ,entryName);
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            Log.e("APK entry name",e.getMessage());
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    Log.e("APK entry name",e.getMessage());
                }
            }
        }
        String[] split = ret.split("_");
        String channel = "";
        if (split != null && split.length >= 2) {
        		channel = ret.substring(split[0].length() + 1);
        }
        return channel;
	}
	
	/**
	 * 本地保存channel & 对应版本号
	 * @param context
	 * @param channel
	 */
	private static void saveChannelInSP(Context context, String channel){
		writeString(context,PREF_KEY_CHANNEL, channel);
		writeInt(context,PREF_KEY_CHANNEL_VERSION, getVersionCode(context));
	}
	
	/**
	 * 从sp中获取channel
	 * @param context
	 * @return 为空表示获取异常、sp中的值已经失效、sp中没有此值
	 */
	private static String getChannelFromSP(Context context){
		int currentVersionCode = getVersionCode(context);
		if(currentVersionCode == -1){
			//获取错误
			return "";
		}
		int versionCodeSaved = readInt(context,PREF_KEY_CHANNEL_VERSION, -1);
		if(versionCodeSaved == -1){
			//本地没有存储的channel对应的版本号
			//第一次使用  或者 原先存储版本号异常
			return "";
		}
		if(currentVersionCode != versionCodeSaved){
			return "";
		}
		return readString(context,PREF_KEY_CHANNEL, "");
	}
	
	public static int getVersionCode(Context context) {
		try{
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		}catch(NameNotFoundException e) {
            Log.e("APK entry name",e.getMessage());
		}
		return -1;
	}

	private static int readInt(Context context, String value, int defaultValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(value,defaultValue);
	}

	private static String readString(Context context, String value, String defaultValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(value,defaultValue);
	}

	private static void writeInt(Context context, String key, int value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key,value);
		editor.apply();
	}

	private static void writeString(Context context, String key, String value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key,value);
		editor.apply();
	}
}