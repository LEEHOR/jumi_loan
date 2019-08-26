package com.cashloan.jumidai.ui.user.bean;

import android.view.View;

import com.commom.base.BaseBean;
import com.commom.utils.ContextHolder;
import com.commom.utils.RegularUtil;
import com.commom.utils.TextUtil;
import com.cashloan.jumidai.R;
import com.cashloan.jumidai.utils.InputCheck;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/17 17:25
 * <p/>
 * Description: 忘记密码页面模型
 */
public class ForgotVM extends BaseBean {
    /* 忘记修改面第一步 */
    /** 手机号 */
    private String  phone;
    /** 验证码 */
    private String  code;
    /** 获取验证码按钮是否可用 */
    private boolean codeEnable;
    /** 下一步按钮是否可用 */
    private boolean enable;
    /* 忘记修改面第二步 */
    /** 修改按钮是否可用 */
    private boolean updateEnable;
    /** 新密码 */
    private String  pwd;
    /** 确认新密码 */
    private String  confirmPwd;
    /** 是否可见第一步 */
    private int    isOne = View.VISIBLE;
    /** 是否可见第二步 */
    private int    isTwo = View.GONE;
    private String title = ContextHolder.getContext().getResources().getString(R.string.forgot_pwd_title_step_1);   //忘记密码标题


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        checkInput();
        codeEnableCheck();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        checkInput();
    }


    public boolean isCodeEnable() {
        return codeEnable;
    }

    public void setCodeEnable(boolean codeEnable) {
        this.codeEnable = codeEnable;
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    public int getIsOne() {
        return isOne;
    }

    public void setIsOne(int isOne) {
        this.isOne = isOne;
    }


    public int getIsTwo() {
        return isTwo;
    }

    public void setIsTwo(int isTwo) {
        this.isTwo = isTwo;
    }

    /* 忘记修改密码第二步 */

    public boolean isUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(boolean updateEnable) {
        this.updateEnable = updateEnable;
    }


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        checkInputUpdate();
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
        checkInputUpdate();
    }

    /**
     * TimeButton是否可用
     */
    private void codeEnableCheck() {
        if (RegularUtil.isPhone(phone)) {
            setCodeEnable(true);
        } else {
            setCodeEnable(false);
        }
    }

    /**
     * 输入校验
     */
    private void checkInput() {
        if (!TextUtil.isEmpty(phone) && InputCheck.checkCode(code)) {
            setEnable(true);
        } else {
            setEnable(false);
        }
    }

    /**
     * 修改密码输入校验
     */
    private void checkInputUpdate() {
        if (TextUtil.isEmpty(pwd) || TextUtil.isEmpty(confirmPwd)
                || pwd.length() < 6 || confirmPwd.length() < 6
                || confirmPwd.length() > 16 || pwd.length() > 16) {
            setUpdateEnable(false);
        } else {
            setUpdateEnable(true);
        }
    }
}
