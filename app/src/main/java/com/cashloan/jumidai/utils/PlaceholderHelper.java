package com.cashloan.jumidai.utils;

import android.content.Context;
import com.commom.utils.ContextHolder;

/**
 * 作者： Ruby
 * 时间： 2018/8/2
 * 描述： placeholder 工具类
 */
public class PlaceholderHelper {
    /** 空页 - 暂无消息 */
    public final static int EMPTY_MESSAGE       = 0x103;
    /** 空页 - 暂无公告 */
    public final static int EMPTY_NOTICE        = 0x104;
    /** 空页 - 暂无记录 */
    public final static int EMPTY_RECORD        = 0x106;
    /** 空页 - 暂无银行卡 */
    public final static int EMPTY_CARD          = 0x107;
    /** 空页 - 暂无邀请记录 */
    public final static int EMPTY_INVITE_RECORD = 0x108;
    /** 上下文 */
    private Context context;

    private PlaceholderHelper() {
        context = ContextHolder.getContext();
    }

    public static PlaceholderHelper getInstance() {
        return PlaceholderHelperInstance.instance;
    }

    private static class PlaceholderHelperInstance {
        static PlaceholderHelper instance = new PlaceholderHelper();
    }

//    public void setStatus(PlaceholderLayout layout, int status) {
//        switch (status) {
//            case PlaceholderLayout.SUCCESS:
//            case PlaceholderLayout.ERROR:
//            case PlaceholderLayout.NO_NETWORK:
//            case PlaceholderLayout.LOADING:
//                layout.setStatus(status);
//                return;
//
//            case EMPTY_MESSAGE:
//                layout.setEmptyText(mContext.getString(R.string.placeholder_empty_message));
//                layout.setEmptyImage(R.drawable.placeholder_empty_message);
//                break;
//
//            case EMPTY_NOTICE:
//                layout.setEmptyText(mContext.getString(R.string.placeholder_empty_notice));
//                layout.setEmptyImage(R.drawable.placeholder_empty_notice);
//                layout.setStatus(PlaceholderLayout.EMPTY);
//                break;
//
//            case EMPTY_RECORD:
//                layout.setEmptyText(mContext.getString(R.string.placeholder_empty_record));
//                layout.setEmptyImage(R.drawable.placeholder_empty_record);
//                break;
//
//            case EMPTY_CARD:
//                layout.setEmptyText(mContext.getString(R.string.placeholder_empty_card));
//                layout.setEmptyImage(R.drawable.placeholder_empty_card);
//                break;
//            case EMPTY_INVITE_RECORD:
//                layout.setEmptyText(mContext.getString(R.string.placeholder_empty_invite_record));
//                layout.setEmptyImage(R.drawable.placeholder_empty_record);
//                break;
//            case PlaceholderLayout.EMPTY:
//            default:
//                layout.setEmptyText(mContext.getString(R.string.placeholder_empty));
//                layout.setEmptyImage(R.drawable.placeholder_empty);
//                break;
//        }
//        layout.setStatus(PlaceholderLayout.EMPTY);
//    }
}
