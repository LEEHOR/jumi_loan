# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# ================================================================================================
# =========================================== 混淆模板 ===========================================
# ================================================================================================

# ---------------------------------------- 输入/输出 选项 ----------------------------------------
# 指定的jar将不被混淆
# -libraryjars libs/fastjson-1.2.4.jar
# 跳过(不混淆) jars中的 非public classes
-dontskipnonpubliclibraryclasses
# 不跳过(混淆) jars中的 非public classes   默认选项
# -dontskipnonpubliclibraryclassmembers
# ------------------------------------------- 优化选项 -------------------------------------------
# 不优化(当使用该选项时，下面的选项均无效)
-dontoptimize
# 默认启用优化,根据 optimization_filter 指定要优化的文件
# -optimizations optimization_filter
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 迭代优化的次数默认99次，一般迭代10次左右，代码已经不能再次优化了
-optimizationpasses 5

# ------------------------------------------- 压缩选项 -------------------------------------------
# 不压缩(全局性的,即便使用了-keep 开启shrink，也无效)
-dontshrink

# ------------------------------------------ 预校验选项 ------------------------------------------
# 不预校验
-dontpreverify

# ------------------------------------------- 通用选项 -------------------------------------------
# 打印详细
-verbose
# 不打印某些错误
# -dontnote android.support.v4.**
# 不打印警告信息
# -dontwarn android.support.v4.**
# 忽略警告，继续执行
-ignorewarnings

# ------------------------------------------- 混淆选项 -------------------------------------------
# 不混淆
# -dontobfuscate
# 不使用大小写混合类名
-dontusemixedcaseclassnames
# 指定重新打包,所有包重命名,这个选项会进一步模糊包名,将包里的类混淆成n个再重新打包到一个个的package中
-flattenpackagehierarchy ''
# 将包里的类混淆成n个再重新打包到一个统一的package中  会覆盖 flattenpackagehierarchy 选项
-repackageclasses ''
# 混淆时可能被移除下面这些东西，如果想保留，需要用该选项。对于一般注解处理如 -keepattributes *Annotation*
# attribute_filter : Exceptions, Signature, Deprecated, SourceFile, SourceDir, LineNumberTable,
# LocalVariableTable, LocalVariableTypeTable, Synthetic,
# EnclosingMethod, RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, RuntimeVisibleParameterAnnotations,
# RuntimeInvisibleParameterAnnotations, and AnnotationDefault.
# -keepattributes *Annotation*
# ---------------------------------------- 保持不变的选项 ----------------------------------------
# 保持class_specification规则；若有[,modifier,...]，则先启用它的规则
# -keep [,modifier,...] class_specification
# 保持类的成员：属性(可以是成员属性、类属性)、方法(可以是成员方法、类方法)
# -keepclassmembers [,modifier,...]class_specification
# 与-keep功能基本一致(经测试)
# -keepclasseswithmembers [,modifier,...] class_specification
# Short for -keep,allowshrinking class_specification
# -keepnames class_specification
# Short for -keepclassmembers,allowshrinking class_specification
# -keepclassmembernames class_specification
# Short for -keepclasseswithmembers,allowshrinking class_specification
# -keepclasseswithmembernames class_specification
# 打印匹配的-keep家族处理的 类和类成员列表，到标准输出。
# -printseeds [filename]

# ******************************************* Android相关组件不混淆 *******************************************
# ************************************************************************************************

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# 所有native的方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 继承自View的构造方法不混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
    public *** get*();
}

# 枚举类不混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# AIDL 文件不能去混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 保护 谷歌第三方 jar 包，界面特效
-keep class android.support.v4.**
-dontwarn android.support.v4.**
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# 保持源文件和行号的信息,用于混淆后定位错误位置
-keepattributes SourceFile,LineNumberTable
# 保持签名
-keepattributes Signature
# 保持任意包名.R类的类成员属性。即保护R文件中的属性名不变（资源类不混淆）
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclassmembers class * {
    public <methods>;
    public static <fields>;
    private static <fields>;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 保护所有实体中的字段名称
#-keepclassmembers class * implements java.io.Serializable {
#    <fields>;
#    <methods>;
#}

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
 static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    !static !transient <fields>;
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();

}
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    !static !transient <fields>;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
# ******************************************* Android相关组件不混淆*******************************************



#------------------------------------------------------------添加自己的规则------------------------------------------
#--------------------------------------------------------------------------------------------------------------------
#添加自己的混淆规则:
#1. 代码中使用了反射，如一些ORM框架的使用，需要保证类名 方法不变，不然混淆后，就反射不了
#2. 使用GSON、fastjson等JSON解析框架所生成的对象类，生成的bean实体对象，内部大多是通过反射来生成,不能混淆
#3. 引用了第三方开源框架或继承第三方SDK，如开源的okhttp网络访问框架，百度定位SDK等，在这些第三库的文档中 一般会给出"相应的"混淆规则，复制过来即可
#4. 有用到WEBView的JS调用接口，真没用过这块, 不是很熟, 网上那个看到的
#5. 继承了Serializable接口的类，在反序列画的时候，需要正确的类名等，在Android 中大多是实现 Parcelable 来序列化的

#对于引用第三方包的情况，可以采用下面方式避免打包出错：
#-libraryjars libs/aaa.jar
#-dontwarn com.xx.yy.**
#-keep class com.xx.yy.** { *;}

# 指定无需混淆的jar包和so库
#-libraryjars libs/aaa.jar

## GSON 2.2.4 specific rules ##
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes EnclosingMethod

#==========================================Gson ========================================================
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
#==========================================Gson  end========================================================

#==========================================bean ========================================================
# 实体类 混淆keep规则
-keep class com.google.common.base.**{ *; }
-keep class com.cashloan.jumidai.ui.homeLoan.bean.**{ *; }
-keep class com.cashloan.jumidai.ui.homeMine.bean.**{ *; }
-keep class com.cashloan.jumidai.ui.homeMine.bean.recive.**{ *; }
-keep class com.cashloan.jumidai.ui.homeMine.bean.submit.**{ *; }
-keep class com.cashloan.jumidai.ui.homeRepay.bean.**{ *; }
-keep class com.cashloan.jumidai.ui.main.bean.**{ *; }
-keep class com.cashloan.jumidai.ui.user.bean.**{ *; }
-keep class com.cashloan.jumidai.ui.user.bean.receive.**{ *; }
-keep class com.cashloan.jumidai.ui.user.bean.submit.**{ *; }
-keep class com.cashloan.jumidai.views.**{ *; }
-keep class com.common.widget.**{ *; }
-keep class com.commom.net.OkHttp.entity.**{ *; }
#==========================================bean  end========================================================

#==========================================Retrofit ========================================================
# Retrofit 2.X 混淆keep规则
#-dontwarn retrofit2.**
#-keep class retrofit2.** { *; }
#-keepattributes Exceptions
#-keepclasseswithmembers class * {
#    @retrofit2.http.* <methods>;
#}
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep public class * extends retrofit2.Converter {*;}
#==========================================Retrofit  end========================================================

#==========================================RxJava========================================================
# RxJava 混淆keep规则
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#==========================================RxJava end========================================================

#==========================================OkHttp3========================================================
# OkHttp3 混淆keep规则
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
#==========================================OkHttp3 end========================================================

#==========================================Okio============================================================
# Okio 混淆keep规则
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
#-keep class okio.**{*;}
#-keep interface okio.**{*;}
#==========================================Okio end============================================================


# SweetAlert 混淆keep规则
-keep class cn.pedant.SweetAlert.Rotate3dAnimation {
    public <init>(...);
 }

# ActivityRouter 混淆keep规则
-keep class com.github.mzule.activityrouter.router.** { *; }


# 连连Demo混淆keep规则，如果使用了Demo中工具包com.yintong.pay.utils下面的类，请对应添加keep规则，否则混下会包签名错误
-keep public class com.cashloan.jumidai.utils.** {
    <fields>;
    <methods>;
}
# 连连混淆keep规则，请添加
-keep class com.yintong.secure.activityproxy.PayIntro$LLJavascriptInterface{*;}

# databinding混淆keep规则，请添加
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

#-keep class * extends java.lang.annotation.Annotation { *; }
#-keep interface * extends java.lang.annotation.Annotation { *; }
-keepattributes *Annotation*
-keepattributes javax.xml.bind.annotation.*
-keepattributes javax.annotation.processing.*
-keepclassmembers class * extends java.lang.Enum { *; }
-keepclasseswithmembernames class android.**
-keepclasseswithmembernames interface android.**

#-dontobfuscate
-libraryjars  <java.home>/lib/rt.jar
-libraryjars  <java.home>/lib/jce.jar
-dontwarn
# 友盟分享  混淆keep规则
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature

#webView js调用混淆
#-keepclassmembers class com.erongdu.wireless.views.RdWebView {
#    public *;
#}
#保留annotation， 例如 @JavascriptInterface 等 annotation
-keepattributes *Annotation*
#保留跟 javascript相关的属性
-keepattributes JavascriptInterface
#保留JavascriptInterface中的方法
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
#这个根据自己的project来设置，这个类用来与js交互，所以这个类中的 字段 ，方法， 等尽量保持
-keepclassmembers public class com.commom.widget.RDWebViewClient{
   <fields>;
   <methods>;
   public *;
   private *;
}
#这个根据自己的project来设置，这个类用来与js交互，所以这个类中的 字段 ，方法， 等尽量保持
#-keepclassmembers public class com.commom.widget.WebViewCtrl{
 #  <fields>;
 #  <methods>;
 #  public *;
 #  private *;
#}

#==========================================================高德地图======================================
#高德地图定位功能
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep class com.amap.api.services.**{*;}
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#==========================================================高德地图 end======================================

#权限管理
-keep class com.yanzhenjie.permission.**{ *; }
#同盾
-dontwarn android.os.**
-dontwarn com.android.internal.**
-keep class cn.tongdun.android.**{*;}

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#begin  有盾
-keepparameternames
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-dontwarn cn.fraudmetrix.android.**
-dontwarn cn.com.bsfit.volley.toolbox.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient

#-keep class com.udcredit.** {*;}
-keep class cn.com.bsfit.** {*;}
-keep class io.card.** {*;}
-keep class com.android.snetjob** {*;}
-keep class com.face.** {*;}
-keep class com.hotvision.** {*;}
-keep class com.android.volley.**{*;}
-keep class com.authreal.api.** {*;}
-keep class com.authreal.component.** {*;}
-keep class com.authreal.module.** {*;}
-keep class com.authreal.util.ErrorCode {*;}
-keep class com.authreal.util.FVSdk {*;}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
#-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.lianlian.** { *; }
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------

-optimizationpasses 5
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

## UD
-keep public class com.udcredit.android.fingerprint.UDCREDIT {*;}
-keep public class com.udcredit.android.fingerprint.FingerCallBack {*;}
-keep public class com.udcredit.android.function.FingerPrintData {*;}
-keep public class com.udcredit.android.entity.FingerprintException {*;}
-keep public class com.udcredit.android.controller.FingerprintUpdateReceiver {*;}
-keep public class com.udcredit.android.tool.UDHttpsTrustManager {*; }
-keep public class com.udcredit.android.collection.EnvValidate {
   private boolean isEmulator1();
}
#end  有盾


#友盟统计分析
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn android.support.**

-dontwarn com.markupartist.**

-keep public class com.umeng.example.R$*{
    public static final int *;
}

#-keepclassmembers class * {
#   public <init>(org.json.JSONObject);
#}

-keep public class com.umeng.fb.ui.ThreadView {
}

#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}


#jpush混淆部分
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontnote
-verbose

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.IntentService
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#talkingdata
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}
-keep class dice.** {*; }
-dontwarn dice.**

# gilde 混淆keep规则
-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#  MoXieSdk---start---

#------------------  下方是共性的排除项目         ----------------
# 方法名中含有“JNI”字符、或带有 native 关键词的，认定是Java Native Interface方法，自动排除
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

-keepclasseswithmembers class * { native <methods>; }
-keepclasseswithmembers class * {
    ... *JNI*(...);
}
-keepclasseswithmembernames class * {
	... *JRI*(...);
}
-keep class **JNI* {*;}

# keep curllib
-keep class com.moxie.mxcurllib.** { *; }

# keep annotated by NotProguard
-keep @com.proguard.annotation.NotProguard class * {*;}
-keep class * {
    @com.proguard.annotation <fields>;
    @android.webkit.JavascriptInterface <fields>;
}
-keepclassmembers class * {
    @com.proguard.annotation <fields>;
}
# @android.webkit.JavascriptInterface <fields>;
-keepclassmembers class **.R$* {
    public static <fields>;
}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
#-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontwarn dalvik.**

#------------------  下方是android平台自带的排除项，这里不要动         ----------------
# 保留使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class com.android.vending.licensing.ILicensingService
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference

# 保护注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation {*;}
# 避免混淆泛型
-keepattributes Signature
# 避免混淆反射
-keepattributes EnclosingMethod
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
# 不混淆内部类
-keepattributes InnerClasses

# 不混淆枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 不混淆Bean对象
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#  MoXieSdk---end---

