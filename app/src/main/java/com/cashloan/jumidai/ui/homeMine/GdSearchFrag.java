package com.cashloan.jumidai.ui.homeMine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.ui.homeMine.adapter.MapAddressItemAdapter;
import com.cashloan.jumidai.ui.homeMine.bean.PioSearchItemVM;
import com.commom.widget.recycler.CommonAdapter;
import com.commom.widget.recycler.xRecycler.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 作者： Ruby
 * 时间： 2018/8/30$
 * 描述：
 */

public class GdSearchFrag extends Fragment {


    private XRecyclerView         mXRecyclerView;
    private MapAddressItemAdapter mItemAdapter;
    private List<PioSearchItemVM> mItemVMList;


    private static GdSearchFrag fragment;
    private int    page      = 0;
    private int    pageCount = 20;
    private int    pageSize  = 15;
    private String cityCode  = "";
    private String keyword   = "";
    private Activity activity;
    private List<PoiItem> searchList = new ArrayList<>();

    public static GdSearchFrag getInstance(String cityCode) {
        if (fragment == null) {
            fragment = new GdSearchFrag();
            fragment.cityCode = cityCode;
        }
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_gd_map, container, false);

        mXRecyclerView = view.findViewById(R.id.xrc_map);

        mItemVMList = new ArrayList<>();
        mItemAdapter = new MapAddressItemAdapter(getActivity(), mItemVMList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mXRecyclerView.setLayoutManager(layoutManager);
        mXRecyclerView.setAdapter(mItemAdapter);

        mXRecyclerView.setRefreshEnabled(false);
        mXRecyclerView.setLoadMoreEnabled(true);
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 0;
                mItemVMList.clear();
                poiSearch(page, keyword);
            }

            @Override
            public void onLoadMore() {
                page++;
                poiSearch(page, keyword);
            }
        });

        mItemAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                PioSearchItemVM itemVM = mItemVMList.get(position);

                Intent intent = new Intent();
                intent.putExtra(BundleKeys.DATA, searchList.get(position));
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragment = null;
    }


    public void poiSearch(int pageNum, String key) {
        //POI搜索类型共分为以下20种：汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //餐饮服务|商务住宅|生活服务
        if (TextUtils.isEmpty(key)) {
            return;
        }
        page = pageNum;
        keyword = key;
        PoiSearch.Query query = new PoiSearch.Query(key, "商务住宅|餐饮服务|生活服务", cityCode);
        query.setPageSize(pageSize);
        query.setPageNum(pageNum);
        PoiSearch poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
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
        searchList.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            PioSearchItemVM item = new PioSearchItemVM();
            item.setTitle(list.get(i).getProvinceName()+ list.get(i).getCityName()+list.get(i).getAdName()+list.get(i).getSnippet()+list.get(i).getDirection()+list.get(i).getTitle());
            item.setSnippet(list.get(i).getProvinceName()+ list.get(i).getCityName()+list.get(i).getAdName()+list.get(i).getSnippet()+list.get(i).getDirection()+list.get(i).getTitle());
            mItemVMList.add(item);
        }
        mItemAdapter.notifyDataSetChanged();
    }
}
