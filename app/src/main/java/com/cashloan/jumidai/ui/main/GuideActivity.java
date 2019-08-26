package com.cashloan.jumidai.ui.main;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commom.utils.ActivityManage;
import com.github.mzule.activityrouter.annotation.Router;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.AppConfig;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.ui.main.adapter.ViewPagerAdapter;

import java.util.ArrayList;

/**
 * 作者： Ruby
 * 时间： 2018/8/29
 * 描述： 引导页
 */
@Router(RouterUrl.AppCommon_Guide)
public class GuideActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    // image容器
    private ViewPager            mViewPager;
    // point容器
    private LinearLayout         mSpace;
    // ViewPager适配器
    private ViewPagerAdapter     mAdapter;
    // image存放View
    private ArrayList<View>      views;
    // 引导图片资源
    private TypedArray           pics;
    // 底部小点的图片
    private ArrayList<ImageView> points;
    // 记录当前选中位置
    private int                  currentIndex;
    // 引导页数量
    private int COUNT = AppConfig.GUIDE_COUNT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        pics = getResources().obtainTypedArray(R.array.guideImages);
        mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        mSpace = (LinearLayout) findViewById(R.id.guide_bottom_point);
        initView();
        initData();
    }


    protected void initView() {
        pics = getResources().obtainTypedArray(R.array.guideImages);
        mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        mSpace = (LinearLayout) findViewById(R.id.guide_bottom_point);

        // 实例化ArrayList对象
        views = new ArrayList<>();
        points = new ArrayList<>();
        // 实例化ViewPager适配器
        mAdapter = new ViewPagerAdapter(views);
    }


    protected void initData() {
        // 定义一个布局并设置参数
        ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 初始化引导图片列表
        for (int i = 0; i < COUNT; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            // 防止图片不能填满屏幕
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            // 加载图片资源
            iv.setImageDrawable(pics.getDrawable(i));
            views.add(iv);
        }
        // 设置数据
        mViewPager.setAdapter(mAdapter);
        // 设置监听
        mViewPager.addOnPageChangeListener(this);
        // 初始化底部小点
//        initPoint();
    }


    /**
     * 初始化底部小点
     */
    private void initPoint() {
//        ImageView view;
//        for (int i = 0; i < COUNT; i++) {
//            view = new ImageView(this);
//            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(36, 36);
//            view.setPadding(5, 5, 5, 5);
//            view.setLayoutParams(layoutParams);
//            if (i == 0) {
//                view.setImageResource(R.drawable.guide_point_cur);
//            } else {
//                view.setImageResource(R.drawable.guide_point);
//            }
//            mSpace.addView(view);
//            points.add(view);
//        }
    }

    /**
     * 滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    /**
     * 当前页面滑动时调用
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * 新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态
        setCurDot(position);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
//        setCurDot(position);
    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= COUNT) {
            return;
        }
        mViewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int position) {
//        if (position < 0 || position > COUNT - 1 || currentIndex == position) {
//            return;
//        }
//        points.get(currentIndex).setImageResource(R.drawable.guide_point);
//        points.get(position).setImageResource(R.drawable.guide_point_cur);
//        currentIndex = position;
    }

    @Override
    public void onBackPressed() {
        ActivityManage.onExit();
    }
}
