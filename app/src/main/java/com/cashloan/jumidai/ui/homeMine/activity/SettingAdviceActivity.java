package com.cashloan.jumidai.ui.homeMine.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.net.OkHttp.entity.HttpResult;
import com.commom.utils.ToastUtil;
import com.commom.widget.NoDoubleClickTextView;
import com.github.mzule.activityrouter.annotation.Router;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.DialogUtils;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.network.RDClient;
import com.cashloan.jumidai.network.RequestCallBack;
import com.cashloan.jumidai.network.api.MineService;
import com.cashloan.jumidai.ui.homeMine.bean.SettingsIdeaVM;
import com.cashloan.jumidai.ui.homeMine.bean.submit.IdeaSub;
import com.cashloan.jumidai.utils.Util;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者： Ruby
 * 时间： 2018/8/28
 * 描述： 意见反馈
 */

@Router(value = RouterUrl.Mine_Settings_Idea)
public class SettingAdviceActivity extends BaseMvpActivity {

    @ViewInject(R.id.input_feedback)
    private EditText              etAdvice;
    @ViewInject(R.id.number)
    private TextView              tvNums;
    @ViewInject(R.id.ndt_advice_submit)
    private NoDoubleClickTextView tvSubmit;

    public SettingsIdeaVM ideaVM;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_advice;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("意见反馈");
    }

    @Override
    protected void initFunc() {
        ideaVM = new SettingsIdeaVM();
        tvSubmit.setEnabled(false);


        etAdvice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ideaVM.setIdea(etAdvice.getText().toString());

                tvNums.setText(ideaVM.getCount());
                tvSubmit.setEnabled(ideaVM.isEnable());
            }
        });


        attachClickListener(tvSubmit);
    }

    @Override
    protected void onViewClicked(View view) {
        submit(view);
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /** 意见提交 */
    public void submit(final View view) {
        if (containsEmoji(ideaVM.getIdea())) {
            DialogUtils.showToastDialog(view.getContext(), "输入内容含有表情，请重新输入");
            return;
        }
        Call<HttpResult> call = RDClient.getService(MineService.class).opinion(new IdeaSub(ideaVM.getIdea()));
        call.enqueue(new RequestCallBack<HttpResult>() {
            @Override
            public void onSuccess(Call<HttpResult> call, Response<HttpResult> response) {
                ToastUtil.toast(response.body().getMsg());
                Util.getActivity(view).finish();
            }
        });
    }
}
