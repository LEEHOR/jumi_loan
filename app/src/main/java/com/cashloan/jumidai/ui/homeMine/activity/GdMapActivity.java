package com.cashloan.jumidai.ui.homeMine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.ui.homeMine.GdSearchFrag;
import com.cashloan.jumidai.ui.homeMine.adapter.MapAddressItemAdapter;
import com.cashloan.jumidai.ui.homeMine.bean.PioSearchItemVM;
import com.cashloan.jumidai.utils.SystemBarTintManager;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.utils.EmptyUtils;
import com.commom.widget.editText.ClearEditText;
import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.xRecycler.XRecyclerView;
import com.github.mzule.activityrouter.annotation.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/29
 * 描述： 高德地图
 */

@Router(RouterUrl.Mine_GdMap)
public class GdMapActivity extends AppCompatActivity
        implements LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener {

    @ViewInject(R.id.iv_back)
    private ImageView     ivBack;
    @ViewInject(R.id.tv_search)
    private TextView      tvSearch;
    @ViewInject(R.id.tv_search_btn)
    private TextView      tvSearchBtn;
    @ViewInject(R.id.et_search)
    private ClearEditText etSearch;
    @ViewInject(R.id.container)
    private FrameLayout   container;
    @ViewInject(R.id.iv_location)
    private ImageView     ivLocation;
    @ViewInject(R.id.map)
    private MapView       mMapView;
    @ViewInject(R.id.rc_map_list)
    private XRecyclerView mXRecyclerView;

    private MapAddressItemAdapter mItemAdapter;
    private List<PioSearchItemVM> mSearchItemVMS;

    private AMap                                     aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient                       mlocationClient;
    private AMapLocationClientOption                 mLocationOption;
    private PoiSearch.SearchBound                    searchBound;
    private int page      = 0;
    private int pageCount = 20;
    private int pageSize  = 10;
    private Activity activity;
    private String cityCode = "";
    GdSearchFrag        gdSearchFrag;
    FragmentTransaction transaction;
    private List<PoiItem> searchList = new ArrayList<>();
    private boolean       firstList  = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EmptyUtils.isNotEmpty(getSupportActionBar())) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_gd_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_color_principal);
        }

        AnnotateUtils.inject(this);

        activity = this;
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
            aMap.setOnCameraChangeListener(this);
        }

        tvSearch.setOnClickListener(view -> toSearch(view));
        tvSearchBtn.setOnClickListener(view -> toSearch(view));
        ivBack.setOnClickListener(view -> onBackPressed(view));

        initData();

        mMapView.onCreate(savedInstanceState);
    }

    private void initData() {
        mSearchItemVMS = new ArrayList<>();
        mItemAdapter = new MapAddressItemAdapter(this, mSearchItemVMS);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mXRecyclerView.setLayoutManager(layoutManager);
        mXRecyclerView.setAdapter(mItemAdapter);

        mXRecyclerView.setRefreshEnabled(false);
        mXRecyclerView.setLoadMoreEnabled(true);
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                page++;
                poiSearch(page);
            }
        });

        mItemAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                PioSearchItemVM itemVM = mSearchItemVMS.get(position);

                Intent intent = new Intent();
                intent.putExtra(BundleKeys.DATA, searchList.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    private void setTitleVisibility() {
        if (tvSearch.getVisibility() == View.VISIBLE) {
            tvSearch.setVisibility(View.GONE);
            tvSearchBtn.setVisibility(View.VISIBLE);
            etSearch.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);
            etSearch.requestFocus();
            gdSearchFrag = GdSearchFrag.getInstance(cityCode);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, gdSearchFrag);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        } else {
            tvSearch.setVisibility(View.VISIBLE);
            tvSearchBtn.setVisibility(View.GONE);
            etSearch.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(gdSearchFrag);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 返回事件
     */
    public void onBackPressed(View view) {
        if (tvSearch.getVisibility() == View.VISIBLE) {
            activity.finish();
        } else {
            tvSearch.setVisibility(View.VISIBLE);
            tvSearchBtn.setVisibility(View.GONE);
            etSearch.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
            transaction = ((FragmentActivity) activity).getSupportFragmentManager().beginTransaction();
            transaction.remove(gdSearchFrag);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 搜索按钮
     */
    public void toSearch(View view) {
        setTitleVisibility();
    }

    /**
     * 搜索
     */
    public void search(View view) {
        page = 1;
        poiSearch(page);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.icon_location_marker));// 设置小蓝点的图标
        myLocationStyle.radiusFillColor(activity.getResources().getColor(R.color.amap_transparent_theme_color));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeColor(activity.getResources().getColor(R.color.amap_transparent_theme_color));
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听

        //设置地图拖动和缩放
        aMap.getUiSettings().setScrollGesturesEnabled(false);
        aMap.getUiSettings().setZoomGesturesEnabled(false);

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()


        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mlocationClient != null) {
                    AMapLocation aMapLocation = mlocationClient.getLastKnownLocation();
                    aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                gdSearchFrag.poiSearch(0, editable.toString());
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                ivLocation.setVisibility(View.VISIBLE);
                cityCode = aMapLocation.getCityCode();
                /*if(firstList){
                    firstList = false;
                    searchBound = new PoiSearch.SearchBound(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 1000, true);
                    page = 0;
                    pageCount = 20;
                    poiSearch(page);
                }*/
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        searchBound = new PoiSearch.SearchBound(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude), 1000, true);
        page = 0;
        pageCount = 20;
        poiSearch(page);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(activity);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private void poiSearch(final int pageNum) {
        //		POI搜索类型共分为以下20种：汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //		地名地址信息|餐饮服务|商务住宅|生活服务
        PoiSearch.Query query = new PoiSearch.Query("", "地名地址信息|商务住宅|餐饮服务|生活服务");
        query.setPageSize(pageSize);
        query.setPageNum(pageNum);
        PoiSearch poiSearch = new PoiSearch(activity, query);
        poiSearch.setBound(searchBound);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
////                getSwipeLayout().setRefreshing(false);
////                getSwipeLayout().setLoadingMore(false);
//                pageCount = poiResult.getPageCount();
//                convert(poiResult.getPois());
////                if (page < pageCount - 1) {
////                    getSwipeLayout().setLoadMoreEnabled(true);
////                } else {
////                    getSwipeLayout().setLoadMoreEnabled(false);
////                }

                mXRecyclerView.loadMoreComplete();
                pageCount = poiResult.getPageCount();
                convert(poiResult.getPois());
                if (page < pageCount - 1) {
                    mXRecyclerView.setLoadMoreEnabled(true);
                } else {
                    mXRecyclerView.setLoadMoreEnabled(false);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        });
        poiSearch.searchPOIAsyn();
    }

    private void convert(List<PoiItem> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (page == 0) {
            searchList.clear();
            mSearchItemVMS.clear();
//  //          mRegisterVM.get().items.clear();
        }
        searchList.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            PioSearchItemVM item = new PioSearchItemVM();
            item.setTitle(list.get(i).getProvinceName()+ list.get(i).getCityName()+list.get(i).getAdName()+list.get(i).getSnippet()+list.get(i).getDirection()+list.get(i).getTitle());
            item.setSnippet(list.get(i).getProvinceName()+ list.get(i).getCityName()+list.get(i).getAdName()+list.get(i).getSnippet()+list.get(i).getDirection()+list.get(i).getTitle());
            Log.d("地址",list.get(i).getProvinceName()+"/\n"+
                    list.get(i).getCityName()+"/\n"
                    +list.get(i).getAdName()+"/\n"
                    + list.get(i).getSnippet()+"/\n"
                    +list.get(i).getDirection()+"/\n"
                    +list.get(i).getTitle());
            ////          mRegisterVM.get().items.add(item);
            mSearchItemVMS.add(item);
        }
        mItemAdapter.notifyDataSetChanged();
    }


}
