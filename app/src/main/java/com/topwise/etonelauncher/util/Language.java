package com.topwise.etonelauncher.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 配置多种语言
 */
public class Language {
    private Context mContext;

    public Language(Context mContext) {
        this.mContext = mContext;
    }

    //系统默认语言
    public static class Constants{
        //系统默认是zh-rCN的类型
        public static String language = "zh-rCN";
        public static List<Activity> activityList = new ArrayList<>();
    }
    //选择语言
    public void switchLanguage(String Language){
        //获取R资源对象
//        Resources mResources = mContext.getResources();
//        //获取设置对象
//        Configuration mConfiguration = mResources.getConfiguration();
//        //获得屏幕参数：主要是分辨率，像素等。
//        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
//        switch (Language){
//            case "zh-rCN":
//                mConfiguration.locale = Locale.CHINESE;
//                mResources.updateConfiguration(mConfiguration,mDisplayMetrics);
//                break;
//            case "en-rUS":
//                mConfiguration.locale = Locale.ENGLISH;
//                mResources.updateConfiguration(mConfiguration,mDisplayMetrics);
//                break;
//            default:
//                break;
//        }
    }
}
