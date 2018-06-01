package com.topwise.etonelauncher.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import com.topwise.etonelauncher.custom.CustomDialog;
import com.topwise.etonelauncher.model.AppNameAndIcon;
import com.topwise.etonelauncher.model.AppNameIcon;
import com.topwise.etonelauncher.util.Language;
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
    private CustomDialog dialog;//提示
    //设置语言
    private Language mLanguage;
    private Language.Constants mConstants;

    int[] icons = new int[]{R.mipmap.extendicon1, R.mipmap.extendicon2,
            R.mipmap.extendicon3, R.mipmap.extendicon4,
            R.mipmap.extendicon5};
    //定义一个二位数组，背景随机
    int[][] ram = new int[][]{{0, 2, 1}, {4, 3, 0}, {2, 1, 4}};

    private UpadteAppReceive upadteAppReceive;//广播

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

        //固定前七个应用
        //mAppNameIconList=fixListOrder();

        myAdapter = mRecyclerView.new PageAdapter(mAppNameIconList, new PageRecyclerView.CallBack() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(LauncherActivity.this).inflate(R.layout.desktop_app_item, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Log.i("msg","position:" + position);
                mAppNameIcon = mAppNameIconList.get(position);
                ((MyHolder) holder).appName.setText(mAppNameIcon.getAppName());
                //((MyHolder) holder).appIcon.setImageDrawable(mAppNameIcon.getAppIcon());
                Log.i("msg","position:" + position + ",name:" + mAppNameIcon.getAppName());
                ((MyHolder) holder).appIcon.setImageDrawable(mAppNameIcon.getAppIcon());
                Drawable drawable = getResources().getDrawable(icons[ram[((position % 9) / 3)][position % 3]]);
                ((MyHolder) holder).appIcon.setBackground(drawable);

            }

            @Override
            public void onItemClickListener(View view, int position) {

                Intent intent = mAppNameIconList.get(position).getIntentPackage();
                PackageManager pm = LauncherActivity.this.getPackageManager();
                if (null != intent && pm.queryIntentActivities(intent, 0).size() > 0) {
                    startActivity(intent);
                } else {
                    fixListOrder(position);
                }
            }
        });
        mRecyclerView.setAdapter(myAdapter);

        //配置系统多种语言
        mLanguage = new Language(LauncherActivity.this);
        mConstants = new Language.Constants();
        mConstants.activityList.add(this);
        mLanguage.switchLanguage(mConstants.language);
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
     * 点击apk图标执行跳转
     * @return
     */
    private void fixListOrder(int position) {

        String appPackage;
        String appClass;
        ComponentName componentName;
        Intent intent = new Intent();

        switch(position){
            case 0:
                appPackage = "com.topwise.etone";
                appClass = "com.topwise.etonepay.activity.ConsumeAmountInputActivity";
                componentName = new ComponentName(appPackage, appClass);
                intent.setComponent(componentName);
                startActivity(intent);
                break;
            case 2:
                appPackage = "com.android.settings";
                appClass = "com.android.settings.Settings";
                componentName = new ComponentName(appPackage, appClass);
                intent.setComponent(componentName);
                startActivity(intent);
                break;
           default:
               String alert_message = LauncherActivity.this.getString(R.string.alertdialog_message);
               CustomDialog.Builder customBuilder = new
                       CustomDialog.Builder(LauncherActivity.this);
               customBuilder.setTitle(R.string.alertdialog_title)
                       .setIcon(mAppNameIconList.get(position).getAppIcon())
                       .setMessage(mAppNameIconList.get(position).getAppName() + "  " + alert_message)
                       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       })
                       .setPositiveButton(R.string.alertdialog_confirm,
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                   }
                               });
               dialog = customBuilder.create();
               dialog.show();
               break;
        }
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

    /**
     * 是一个系统广播监听recyclerview的数据更新
     */
    public  class UpadteAppReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LauncherActivity","UpadteAppReceive onReceive");
            //Context context = LauncherActivity.this;
            //String pkgName;
            List<AppNameIcon> appInfos;
            switch (intent.getAction()) {
                case "android.intent.action.PACKAGE_ADDED":
                    //Log.i("msg","data:" + intent.getDataString());
                    //pkgName = intent.getDataString().substring(8);
                    //LauncherActivity.this.mAppNameIconList.clear();
                    // List<AppNameIcon> appInfos = new AppNameAndIcon().getAppInfos(LauncherActivity.this);
                    //appInfos = /*new AppNameAndIcon()*/mAppNameAndIcon.addApp(LauncherActivity.this,pkgName);
                    //appInfos=LauncherActivity.this.deleteAppItself(appInfos);
                    //LauncherActivity.this.mAppNameIconList.addAll(appInfos);
                    //mAppNameAndIcon.addApp(LauncherActivity.this,pkgName);
                    //LauncherActivity.this.myAdapter.notifyDataSetChanged();

                    //更新总页数
                    //LauncherActivity.this.mRecyclerView.update();
                    //break;

                case "android.intent.action.PACKAGE_REMOVED":
                case "android.intent.action.PACKAGE_CHANGED":
                case "android.intent.action.PACKAGE_REPLACED":
                        Log.i("msg","data:" + intent.getDataString());
                        //pkgName = intent.getDataString();
                        LauncherActivity.this.mAppNameIconList.clear();
                        appInfos = new AppNameAndIcon().getAppInfos(LauncherActivity.this);
                        //List<AppNameIcon> appInfos = new AppNameAndIcon().addApp(context,pkgName);
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
