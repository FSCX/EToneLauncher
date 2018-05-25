package com.topwise.etonelauncher.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by mt on 2018/5/19.
 */

public class AppNameIcon {
    public String appName;//应用名
    public Drawable appIcon;//应用图标
    public String appClass;//类名
    public String appPackage;//包名
    public Intent intentPackage;

    public AppNameIcon(String appName, Drawable appIcon, String appClass, String appPackage, Intent intentPackage) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.appClass = appClass;
        this.appPackage = appPackage;
        this.intentPackage = intentPackage;
    }

    public Intent getIntentPackage() {
        return intentPackage;

    }

    public void setIntentPackage(Intent intentPackage) {
        this.intentPackage = intentPackage;
    }

    public String getAppClass() {
        return appClass;
    }

    public void setAppClass(String appClass) {
        this.appClass = appClass;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
