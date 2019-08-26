package com.cashloan.jumidai.ui.homeLoan.Impl;

import android.support.annotation.NonNull;

import com.cashloan.jumidai.network.NetworkUtil;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.LoanService;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeChoiceRec;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeRec;
import com.cashloan.jumidai.ui.homeLoan.bean.NoticeRec;
import com.cashloan.jumidai.ui.homeLoan.contact.HomeFragContact;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.net.OkHttp.entity.ListData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/9/1
 * 描述：
 */

public class HomeFragPresenterImp extends HomeFragContact.HomePresenter {

    public String calculateMoney;
    public String calculateDay;

    @NonNull
    public static HomeFragPresenterImp newInstance() {
        return new HomeFragPresenterImp();
    }

    @Override
    public void getHomeData() {
        Call<HttpResult<HomeRec>> call = RDClient.getService(LoanService.class).getHomeIndex();
        NetworkUtil.showCutscenes(call);
        call.enqueue(new RequestCallBack<HttpResult<HomeRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<HomeRec>> call,
                                  Response<HttpResult<HomeRec>> response) {

                HomeRec homeRec = response.body().getData();
                mView.updateHome(homeRec);

                calculateMoney = homeRec.getCreditList().get(0);
                calculateDay = homeRec.getDayList().get(0);

                //判断费用计算从服务端获取
                reqHomeChoiceData(calculateMoney, calculateDay);
            }
        });
    }

    @Override
    public void getNotice() {
        Call<HttpResult<ListData<NoticeRec>>> noticeCall = RDClient.getService(LoanService.class).getNoticeList();
        noticeCall.enqueue(new RequestCallBack<HttpResult<ListData<NoticeRec>>>() {
            @Override
            public void onSuccess(Call<HttpResult<ListData<NoticeRec>>> call, Response<HttpResult<ListData<NoticeRec>>> response) {
                List<NoticeRec> noticeRecList = response.body().getData().getList();
                if (noticeRecList != null && noticeRecList.size() > 0) {
                    List<String> wheelList = new ArrayList<>();
                    for (int i = 0; i < noticeRecList.size(); i++) {
                        wheelList.add(noticeRecList.get(i).getValue());
                    }
//                    ListWheelAdapter adapter = new ListWheelAdapter<>(ContextHolder.getContext(),
//                            R.layout.list_item_home_text, wheelList);
//
//                    adapter.setTextSize(13);
//                    mVerticalView.setViewAdapter(adapter);

                    mView.updateNotice(wheelList);
                }
            }
        });
    }

    /**
     * 获取服务费率和手续费
     *
     * @param amount
     * @param timeLimit
     */
    public void reqHomeChoiceData(String amount, String timeLimit) {
        Call<HttpResult<HomeChoiceRec>> call = RDClient.getService(LoanService.class).getHomeChoice(amount, timeLimit);
        call.enqueue(new RequestCallBack<HttpResult<HomeChoiceRec>>() {
            @Override
            public void onSuccess(Call<HttpResult<HomeChoiceRec>> call, Response<HttpResult<HomeChoiceRec>> response) {
                final HomeChoiceRec homeChoiceRec = response.body().getData();
//                convertHomeChoiceData(homeChoiceRec);

                mView.updateHomeChoiceData(homeChoiceRec);
            }
        });
    }
}
