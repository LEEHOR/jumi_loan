package com.commom.utils;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者： zxb
 * 时间： 2018/4/11
 * 描述： View滑动、点击的事件判断
 */

public class TouchClickUtils {

    //判断滑动的距离
    private static float scrollTargetY = 50;
    //判断点击的距离
    private static float viewClickDistance = 10;
    //手指按下起始点的Y
    private static float startY = 0;

    private static View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startY = e.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    float endY = e.getY();
                    float distanceY = endY - startY;
                    //距离大于scrollTargetY，下滑
                    if (distanceY > scrollTargetY) {
                        if (EmptyUtils.isNotEmpty(mOnViewSlideListener)) {
                            mOnViewSlideListener.slideDown();
                        }
                        return true;
                    }
                    //距离小于scrollTargetY，上滑
                    if (distanceY < -scrollTargetY) {
                        if (EmptyUtils.isNotEmpty(mOnViewSlideListener)) {
                            mOnViewSlideListener.slideUp();
                        }
                        return true;
                    }
                    //距离在viewClickDistance之内，判断为点击事件
                    if (Math.abs(distanceY) < viewClickDistance) {
                        if (EmptyUtils.isNotEmpty(mOnViewSlideListener)) {
                            mOnViewSlideListener.viewClick();
                        }
                        return false;
                    }

                    if (-scrollTargetY < distanceY && distanceY < scrollTargetY) {
//                        LogUtils.e("判定距离之间:" + distanceY);
                        return true;
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    };

    public float getScrollTargetY() {
        return scrollTargetY;
    }

    public void setScrollTargetY(float scrollTargetY) {
        this.scrollTargetY = scrollTargetY;
    }


    private static onViewSlideListener mOnViewSlideListener;

    public interface onViewSlideListener {
        void slideUp();

        void slideDown();

        void viewClick();
    }

    public static void setTouchClickListener(@NonNull View view, onViewSlideListener onViewSlideListener) {
        view.setOnTouchListener(mOnTouchListener);
        mOnViewSlideListener = onViewSlideListener;
    }
}
