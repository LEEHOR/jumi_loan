package com.commom.net;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/8/23 15:49
 * <p/>
 * Description: 配置信息
 */
@SuppressWarnings("unused")
public class Config {
    /** 项目根目录路径 */
    public static ObservableValue<String>  ROOT_PATH = new ObservableValue<>(null);
    /** 是否是debug模式 */
    public static ObservableValue<Boolean> DEBUG     = new ObservableValue<>(true);
}
