package com.cashloan.jumidai.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/17 10:30
 * <p/>
 * Description:
 */
public class InputCheck {
    /**
     * 验证密码规则，是否由8-16为数字+字母组成
     *
     * @param pwd
     *         待检测密码
     *
     * @return true - 符合密码规则;
     * false - 不符合密码规则
     */
    public static boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }
        //                if(pwd.length() >=6 && pwd.length() <=16){
        //                    return true;
        //                } else {
        //                    return false;
        //                }
        Pattern regex = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");
        //                Pattern regex   = Pattern.compile("^{6,16}$");//^(?![^a-zA-Z]+$)(?!\D+$).{6,16}$
        Matcher matcher = regex.matcher(pwd);
        return matcher.matches();
    }

    /**
     * 检验验证码是否符合规则
     *
     * @param code
     *         待检测验证码
     *
     * @return true - 符合验证码规则；
     * false - 不符合验证码规则
     */
    public static boolean checkCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return false;
        }
        Pattern regex   = Pattern.compile("^\\d{4,6}$");
        Matcher matcher = regex.matcher(code);
        return matcher.matches();
    }

    /**
     * 校验银行卡号是否符合规则
     *
     * @param bankcard
     *         待检测银行卡号
     *
     * @return true - 符合验证码规则；
     * false - 不符合验证码规则
     */
    public static boolean checkBankcard(String bankcard) {
        return !TextUtils.isEmpty(bankcard) && bankcard.length() >= 15 && bankcard.length() <= 19;
    }
}
