package com.cashloan.jumidai.ui.homeLoan.contact;

import com.cashloan.jumidai.ui.homeLoan.bean.HomeChoiceRec;
import com.cashloan.jumidai.ui.homeLoan.bean.HomeRec;
import com.commom.base.BasePresenter;
import com.commom.base.BaseView;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/9/6$
 * 描述：
 */

public interface HomeFragContact {

    interface view extends BaseView {

        void updateHome(HomeRec homeRec);

        void updateNotice(List<String> wheelList);

        void updateHomeChoiceData(HomeChoiceRec homeChoiceRec);

    }

    abstract class HomePresenter extends BasePresenter<view> {

        public abstract void getHomeData();

        public abstract void getNotice();

        public abstract void reqHomeChoiceData(String amount, String timeLimit);

    }
}
