package com.topwise.etonelauncher.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by mt on 2018/5/19.
 */

public class AppNameAndIcon {
    private List<AppNameIcon> mAppNameIconList;
    private PackageManager mPackageManager;

    //Drawable压缩方法
    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
    //应用图标进行压缩
    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        //Log.d("msg", "width" + width + "/n" +"height" + height);
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    /**
     * 获取应用
     * @param context
     * @return
     */
    public List getAppInfos(Context context) {
        Drawable drawable;
        mPackageManager = context.getPackageManager();
        List<PackageInfo> packgeInfos = mPackageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        mAppNameIconList = new ArrayList<>();

        //调用固定的数据
        DataDesktop desktop = new DataDesktop();
        mAppNameIconList.clear();
        mAppNameIconList.addAll(desktop.appInfos(context));

        //调用固定的数据
        for (PackageInfo packgeInfo : packgeInfos) {
            //去除系统应用
            ApplicationInfo appInfo = packgeInfo.applicationInfo;
            if (!filterApp(appInfo)&&!appInfo.packageName.equals("com.topwise.etone.demo")) {
                continue;
            }
            /***
             * 固定背景图（把默认获取的应用图标覆盖）
             */

            String packageName = packgeInfo.packageName;
            String className = packgeInfo.applicationInfo.className;
            String appName = packgeInfo.applicationInfo.loadLabel(mPackageManager).toString();

            if(!packageName.equals("com.topwise.etone")) {
                Drawable drawable1 = packgeInfo.applicationInfo.loadIcon(mPackageManager);
                drawable = zoomDrawable(drawable1, 300, 300);
                int width1 = drawable.getIntrinsicWidth();
                int height1 = drawable.getIntrinsicHeight();
                Log.d("msg", "width" + width1 + "\n" + "height" + height1);
            }else{
                drawable = packgeInfo.applicationInfo.loadIcon(mPackageManager);
            }

            Intent intent = mPackageManager.getLaunchIntentForPackage(packageName);
            AppNameIcon appNameIcon = new AppNameIcon(appName, drawable, className, packageName, intent);
            mAppNameIconList.add(appNameIcon);
        }
        return mAppNameIconList;
    }

    //判断某一个应用程序是不是系统的应用程序，
    //如果是返回false，否则返回true。
    public boolean filterApp(ApplicationInfo info) {
    //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 ) {
            return true;
            //判断是不是系统应用
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
}
