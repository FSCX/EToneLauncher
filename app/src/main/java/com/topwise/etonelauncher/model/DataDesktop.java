package com.topwise.etonelauncher.model;

import android.content.Context;
import android.content.res.Resources;

import com.topwise.etonelauncher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 固定前七个的应用
 */
public class DataDesktop {

    private List<AppNameIcon> nameAndIcons = new ArrayList<>();
    public List<AppNameIcon> appInfos(Context context) {
        if (nameAndIcons.size() == 0) {
            Resources resources = context.getResources();

            AppNameIcon consume = new AppNameIcon("易通收银",resources.getDrawable(R.mipmap.etonpay, null), null, null,null);
            nameAndIcons.add(consume);

            AppNameIcon market = new AppNameIcon("应用市场", resources.getDrawable(R.mipmap.application_market, null), null, null,null);
            nameAndIcons.add(market);

            AppNameIcon system = new AppNameIcon("系统设置", resources.getDrawable(R.mipmap.system_setting, null), null, null,null);
            nameAndIcons.add(system);

            AppNameIcon shandongCard = new AppNameIcon("山东卡通", resources.getDrawable(R.mipmap.shandong_card, null), null, null,null);
            nameAndIcons.add(shandongCard);

            AppNameIcon kernel = new AppNameIcon("卡券核销", resources.getDrawable(R.mipmap.card_cancellation, null), null, null,null);
            nameAndIcons.add(kernel);

            AppNameIcon TaddO = new AppNameIcon("T+O", resources.getDrawable(R.mipmap.t_add_o, null), null, null,null);
            nameAndIcons.add(TaddO);

            AppNameIcon Settlement = new AppNameIcon("结算", resources.getDrawable(R.mipmap.settlement, null), null, null,null);
            nameAndIcons.add(Settlement);
        }

        return nameAndIcons;
    }
}
