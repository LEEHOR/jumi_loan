package com.cashloan.jumidai.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.commom.utils.ActivityManage;
import com.cashloan.jumidai.ui.user.bean.receive.OauthTokenMo;
import com.commom.utils.log.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者： Ruby
 * 时间： 2018/8/20
 * 描述： 一些杂项工具
 */
public class Util {


    public static String filterUnNumber(String str) {
        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        //替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();
    }

    public static final String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";


    /**
     * 过滤表情符号
     *
     * @param str
     * @return str(去掉表情符号的字符串)
     * @create by ldw on 2016-10-25
     * @version 1.0
     */
    public static String filterEmoji(String str) {
        if (str.trim().isEmpty()) {
            return str;
        }
        String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
        String reStr = "";
        Pattern emoji = Pattern.compile(pattern);
        Matcher emojiMatcher = emoji.matcher(str);
        str = emojiMatcher.replaceAll(reStr);
        return str;
    }


    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public static void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
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


    public static void showKeyboard(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static String getYDOrderId() {
        OauthTokenMo entity = SharedInfo.getInstance().getEntity(OauthTokenMo.class);
        if (entity != null) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsss");
            return "A" + entity.getUserId() + sdf.format(date);
        }
        return "";
    }


    /**
     * 通过Cursor获取联系人电话
     */
    public static String getPhoneName(Activity activity, Cursor cursor) {
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult = "";
        System.out.print(phoneNum);
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = activity.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);

            if (phones.moveToFirst()) {
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phoneNumber = phones.getString(index).replace("-", "").replace(" ", "").trim();
                    if (phoneNumber.startsWith("+86")) {
                        phoneNumber = phoneNumber.replace("+86", "");
                    }
                    if (phoneNumber.startsWith("86")) {
                        phoneNumber = phoneNumber.replace("86", "");
                    }
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    phoneResult = phoneNumber + "," + name;
                    break;
                }
            }
            if (!phones.isClosed()) {
                phones.close();
            }
        }
        return phoneResult;
    }

    private static String paramsString;//获取所有联系人的信息

    public static String getContacts(Context context) {
        StringBuffer sb = new StringBuffer();
        int count = 0;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (name1 != null) {
                    Logger.d("联系人2", name1);
                    String name = name1.replaceAll("\\s", "");
                    //第一条不用换行
                    sb.append(name).append(":");
                    Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        String phoneNumber1 = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        // 添加Phone的信息
                        if (phoneNumber1 != null) {
                            Logger.d("联系人3", phoneNumber1);
                            String phoneNumber = phoneNumber1.replaceAll("\\s", "");
                            Logger.d("联系人3", phoneNumber);
                            sb.append(phoneNumber).append(",");
                        } else {
                            sb.append("").append(",");
                        }

                    }
                    phones.close();
                    count++;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        paramsString = sb.toString();
        Logger.d("联系人", paramsString);
        if (paramsString != null && paramsString.length() > 1) {
            paramsString = paramsString.substring(0, paramsString.length() - 1);
            return paramsString;
        } else {
            return null;
        }

    }

    public static String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(ActivityManage.peek().getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 强制将渠道中的字母标识改为编码方式
     *
     * @param channelcode
     * @return
     */
    public static String changeChannelCodeToNumberId(String channelcode) {
        String numberChannel = channelcode;
        if ("app".equals(channelcode)) {
            numberChannel = "100001";
        } else if ("yingyongbao".equals(channelcode)) {
            numberChannel = "100002";
        } else if ("_360".equals(channelcode)) {
            numberChannel = "100003";
        } else if ("xiaomi".equals(channelcode)) {
            numberChannel = "100004";
        } else if ("ppzhushoou".equals(channelcode)) {
            numberChannel = "100005";
        } else if ("anzhi".equals(channelcode)) {
            numberChannel = "100006";
        } else if ("vivo".equals(channelcode)) {
            numberChannel = "100007";
        } else if ("sougou".equals(channelcode)) {
            numberChannel = "100009";
        } else if ("meizu".equals(channelcode)) {
            numberChannel = "100010";
        } else if ("androidyuan".equals(channelcode)) {
            numberChannel = "100011";
        } else if ("anfen".equals(channelcode)) {
            numberChannel = "100012";
        } else if ("anbei".equals(channelcode)) {
            numberChannel = "100013";
        } else if ("samsung".equals(channelcode)) {
            numberChannel = "100015";
        } else if ("yingyonghui".equals(channelcode)) {
            numberChannel = "100016";
        } else if ("chuizi".equals(channelcode)) {
            numberChannel = "100017";
        } else if ("Nduo".equals(channelcode)) {
            numberChannel = "100018";
        } else if ("mumayi".equals(channelcode)) {
            numberChannel = "100019";
        } else if ("letv".equals(channelcode)) {
            numberChannel = "100020";
        } else if ("luan".equals(channelcode)) {
            numberChannel = "100022";
        } else if ("jifeng".equals(channelcode)) {
            numberChannel = "100023";
        } else if ("kuan".equals(channelcode)) {
            numberChannel = "100024";
        } else if ("oppo".equals(channelcode)) {
            numberChannel = "100025";
        } else if ("huawei".equals(channelcode)) {
            numberChannel = "100026";
        } else if ("youyi".equals(channelcode)) {
            numberChannel = "100027";
        } else if ("maopaotang".equals(channelcode)) {
            numberChannel = "100028";
        } else if ("baiduzhushou".equals(channelcode)) {
            numberChannel = "100031";
        } else if ("_91zhushou".equals(channelcode)) {
            numberChannel = "100032";
        } else if ("androidmarket".equals(channelcode)) {
            numberChannel = "100033";
        } else if ("liqu".equals(channelcode)) {
            numberChannel = "100034";
        } else if ("haixin".equals(channelcode)) {
            numberChannel = "100038";
        }
        return numberChannel;

    }


    /**
     * 获取app的名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            appName = context.getResources().getString(labelRes);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return appName;
    }

}
