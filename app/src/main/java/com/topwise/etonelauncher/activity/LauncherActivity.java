package com.topwise.etonelauncher.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topwise.etonelauncher.R;
import com.topwise.etonelauncher.model.AppNameAndIcon;
import com.topwise.etonelauncher.model.AppNameIcon;
import com.topwise.etonelauncher.util.PromptAlertDialog;
import com.topwise.etonelauncher.view.PageIndicatorView;
import com.topwise.etonelauncher.view.PageRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LauncherActivity extends AppCompatActivity {
    private PageRecyclerView mRecyclerView = null;
    private List<AppNameIcon> mAppNameIconList = null;
    private PageRecyclerView.PageAdapter myAdapter = null;
    private AppNameIcon mAppNameIcon;
    private AppNameAndIcon mAppNameAndIcon;
    private Toolbar mToolbar;//导航栏
    private TextView mTitle;//导航栏title



    /*private static UpadteAppReceive mUpadteAppReceive;
    private IntentFilter intentFilter;
    private ImageView mImageView;*/

    int[] icons = new int[]{R.mipmap.extendicon1, R.mipmap.extendicon2,
            R.mipmap.extendicon3, R.mipmap.extendicon4,
            R.mipmap.extendicon5};
    //定义一个二位数组，背景随机
    int[][] ram = new int[][]{{0, 2, 1}, {4, 3, 0}, {2, 1, 4}};
private UpadteAppReceive upadteAppReceive;

    @Override
    protected void onDestroy() {
        unregisterReceiver(upadteAppReceive);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.setPriority(100);
        intentFilter.addDataScheme("package");
        upadteAppReceive = new UpadteAppReceive();
        registerReceiver(upadteAppReceive,intentFilter);

        setContentView(R.layout.activity_main);

        initData();
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(R.string.ETonePay);

        mRecyclerView = (PageRecyclerView) findViewById(R.id.cusom_swipe_view);
        // 设置指示器
        mRecyclerView.setIndicator((PageIndicatorView) findViewById(R.id.indicator));
        // 设置行数和列数
        mRecyclerView.setPageSize(3, 3);
        // 设置页间距
        //mRecyclerView.setPageMargin(30);
        mRecyclerView.setPageMargin(0);

        //去除本身app应用
        mAppNameIconList=deleteAppItself(mAppNameIconList);



        //mAppNameIconList=fixListOrder();
        // 设置数据
        myAdapter = mRecyclerView.new PageAdapter(mAppNameIconList, new PageRecyclerView.CallBack() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(LauncherActivity.this).inflate(R.layout.desktop_app_item, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                mAppNameIcon = mAppNameIconList.get(position);
                ((MyHolder) holder).appName.setText(mAppNameIcon.getAppName());
                //((MyHolder) holder).appIcon.setImageDrawable(mAppNameIcon.getAppIcon());

                ((MyHolder) holder).appIcon.setImageDrawable(mAppNameIcon.getAppIcon());
                Drawable drawable = getResources().getDrawable(icons[ram[((position % 9) / 3)][position % 3]]);
                ((MyHolder) holder).appIcon.setBackground(drawable);

            }

            @Override
            public void onItemClickListener(View view, int position) {
                //intent = new Intent();
                PromptAlertDialog alertDialog = new PromptAlertDialog(LauncherActivity.this);

                Intent intent = mAppNameIconList.get(position).getIntentPackage();
                PackageManager pm = LauncherActivity.this.getPackageManager();
                if (null != intent && pm.queryIntentActivities(intent, 0).size() > 0) {
                    startActivity(intent);
                } else {
                    if (position == 0) {
                            //Drawable drawable = getResources().getDrawable(R.mipmap.extendicon4);
                            //view.setBackground(drawable);
                            String appPackage = "com.topwise.etone";
                            String appClass = "com.topwise.etonepay.manager.ConsumeAmountInputActivity";
                            ComponentName componentName = new ComponentName(appPackage, appClass);
                            Intent consume = new Intent();
                            consume.setComponent(componentName);
                            startActivity(consume);
                            return;

                    } else if (position == 2) {
                        String settingPackage = "com.android.settings";
                        String settingClass = "com.android.settings.Settings";
                        ComponentName componentName = new ComponentName(settingPackage, settingClass);
                        Intent setting = new Intent();
                        setting.setComponent(componentName);
                        startActivity(setting);
                        return;
                    } else {
                        //PromptAlertDialog alertDialog = new PromptAlertDialog(LauncherActivity.this);
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this);
                        builder.setIcon(mAppNameIconList.get(position).appIcon);
                        builder.setMessage(mAppNameIconList.get(position).getAppName() + R.string.alertdialog_message+"");
                        builder.setPositiveButton(R.string.alertdialog_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();*/
                        alertDialog.runAlertDialog();
                    }
                }
            }
        });
        mRecyclerView.setAdapter(myAdapter);

        //配置系统多种语言
        Constants.activityList.add(this);
        switchLanguage(Constants.langae);
    }

    //核心设置的代码
    private void switchLanguage(String Language) {
        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。

        switch (Language) {
            case "zh-rCN":
                config.locale = Locale.CHINESE;
                resources.updateConfiguration(config, dm);
                break;
            case "en-rUS":
                config.locale = Locale.ENGLISH;
                resources.updateConfiguration(config, dm);
                break;
            default:
                break;
        }
    }
    //配置系统多种语言
    public static class Constants {
        //系统默认是zh的类型
        public static String langae = "zh-rCN";
        public static List<Activity> activityList = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        //Message message = Message.obtain();
    }

    /**
     * 去除本身app的显示q
     */
    private  List<AppNameIcon> deleteAppItself(List<AppNameIcon> mAppNameIconList) {
        Log.d("mes", "deleteAppItself: ");
        for (int i = 0; i < mAppNameIconList.size(); ) {
            AppNameIcon appNameIcon = mAppNameIconList.get(i);
            if (appNameIcon.getAppPackage() != null && appNameIcon.getAppPackage().equals("com.topwise.etonelauncher")) {
                mAppNameIconList.remove(appNameIcon);
            } else if (appNameIcon.getAppPackage() != null && appNameIcon.getAppPackage().equals("com.topwise.etone")) {
                mAppNameIconList.remove(appNameIcon);
            } else {
                i++;
            }
        }
        return  mAppNameIconList;
    }

    /**
     * 固定制定app的位置
     * @return
     */
    /*private List<AppNameIcon> fixListOrder() {
        List<AppNameIcon> list = new ArrayList();
        for (int i = 0; i < mAppNameIconList.size(); ) {
            AppNameIcon appNameIcon = mAppNameIconList.get(i);
            String packeName = appNameIcon.getAppPackage();
            if (packeName != null) {
                switch (packeName) {
                    case "com.topwise.etone.demo":
                    case "com.topwise.etonepay":
                *//*case "topwise.com.toolbartraining":
                case "com.example.sch.myapplication":*//*
                        list.add(appNameIcon);
                        mAppNameIconList.remove(appNameIcon);
                        break;
                    default:
                        i++;
                        break;
                }
            }
        }
        list.addAll(mAppNameIconList);
        return list;
    }*/

    private void initData() {
        mAppNameAndIcon = new AppNameAndIcon();
        mAppNameIconList = mAppNameAndIcon.getAppInfos(LauncherActivity.this);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public ImageView appIcon;
        public TextView appName;
        public LinearLayout clickApp;

        public MyHolder(View itemView) {
            super(itemView);
            this.appIcon = (ImageView) itemView.findViewById(R.id.app_icon);

            this.appName = (TextView) itemView.findViewById(R.id.app_name);
            this.clickApp = (LinearLayout) itemView.findViewById(R.id.clickApp);

            clickApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(LauncherActivity.this, getAdapterPosition() + "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 是一个系统广播监听recyclerview的数据更新
     */


    public  class UpadteAppReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LauncherActivity","UpadteAppReceive onReceive");
            switch (intent.getAction()) {
                case "android.intent.action.PACKAGE_ADDED":
                case "android.intent.action.PACKAGE_REMOVED":
                case "android.intent.action.PACKAGE_CHANGED":
                        LauncherActivity.this.mAppNameIconList.clear();
                        List<AppNameIcon> appInfos = new AppNameAndIcon().getAppInfos(LauncherActivity.this);
                        appInfos=LauncherActivity.this.deleteAppItself(appInfos);
                        LauncherActivity.this.mAppNameIconList.addAll(appInfos);
                        LauncherActivity.this.myAdapter.notifyDataSetChanged();

                        //更新总页数
                        LauncherActivity.this.mRecyclerView.update();
                    break;
                default:
                    break;
            }
        }
    }
}
