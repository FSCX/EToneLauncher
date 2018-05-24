package com.topwise.etonelauncher.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topwise.etonelauncher.R;
import com.topwise.etonelauncher.bean.AppNameIcon;
import com.topwise.etonelauncher.model.AppNameAndIcon;
import com.topwise.etonelauncher.util.PromptAlertDialog;
import com.topwise.etonelauncher.view.PageIndicatorView;
import com.topwise.etonelauncher.view.PageRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {
    private PageRecyclerView mRecyclerView = null;
    private List<AppNameIcon> mAppNameIconList = null;
    private PageRecyclerView.PageAdapter myAdapter = null;
    private AppNameIcon mAppNameIcon;
    private AppNameAndIcon mAppNameAndIcon;
    private Toolbar mToolbar;//导航栏
    private TextView mTitle;//导航栏title

    private static LauncherActivity launcherActivity;

    private static UpadteAppReceive mUpadteAppReceive;
    private IntentFilter intentFilter;


    private ImageView mImageView;
    int[] icons = new int[]{R.mipmap.extendicon1, R.mipmap.extendicon2,
            R.mipmap.extendicon3, R.mipmap.extendicon4,
            R.mipmap.extendicon5};
    int[][] ram = new int[][]{{0, 2, 1}, {4, 3, 0}, {2, 1, 4}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

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

        launcherActivity = LauncherActivity.this;


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

                Intent intent = mAppNameIconList.get(position).getIntentPackage();
                PackageManager pm = LauncherActivity.this.getPackageManager();
                if (null != intent && pm.queryIntentActivities(intent, 0).size() > 0) {
                    startActivity(intent);
                } else {
                    if (position == 0) {
                        Drawable drawable = getResources().getDrawable(R.mipmap.extendicon4);
                        view.setBackground(drawable);
                        String appPackage = "com.topwise.etone";
                        String appClass = "com.topwise.etonepay.manager.LoginActivity";
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
                        PromptAlertDialog alertDialog = new PromptAlertDialog(LauncherActivity.this);
                        alertDialog.runAlertDialog();
                    }
                }
            }
        });
        mRecyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onBackPressed() {

    }

    /**
     * 去除本身app的显示
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
     *
     * @return
     */
    private List<AppNameIcon> fixListOrder() {
        List<AppNameIcon> list = new ArrayList();
        for (int i = 0; i < mAppNameIconList.size(); ) {
            AppNameIcon appNameIcon = mAppNameIconList.get(i);
            String packeName = appNameIcon.getAppPackage();
            if (packeName != null) {
                switch (packeName) {
                    case "com.topwise.etone.demo":
                    case "com.topwise.etonepay":
                /*case "topwise.com.toolbartraining":
                case "com.example.sch.myapplication":*/
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
    }

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

    public static class UpadteAppReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "android.intent.action.PACKAGE_ADDED":
                case "android.intent.action.PACKAGE_REMOVED":
                    if (LauncherActivity.launcherActivity != null) {
                        LauncherActivity.launcherActivity.mAppNameIconList.clear();
                        List<AppNameIcon> appInfos = new AppNameAndIcon().getAppInfos(LauncherActivity.launcherActivity);
                        appInfos=LauncherActivity.launcherActivity.deleteAppItself(appInfos);
                        LauncherActivity.launcherActivity.mAppNameIconList.addAll(appInfos);
                        LauncherActivity.launcherActivity.myAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
