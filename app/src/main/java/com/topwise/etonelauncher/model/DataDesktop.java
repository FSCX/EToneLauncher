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

            AppNameIcon consume = new AppNameIcon(resources.getString(R.string.consume),resources.getDrawable(R.mipmap.etonpay, null), null, null,null);
            nameAndIcons.add(consume);

            AppNameIcon market = new AppNameIcon(resources.getString(R.string.market), resources.getDrawable(R.mipmap.application_market, null), null, null,null);
            nameAndIcons.add(market);

            AppNameIcon system = new AppNameIcon(resources.getString(R.string.system), resources.getDrawable(R.mipmap.system_setting, null), null, null,null);
            nameAndIcons.add(system);

            AppNameIcon shandongCard = new AppNameIcon(resources.getString(R.string.shandongCard), resources.getDrawable(R.mipmap.shandong_card, null), null, null,null);
            nameAndIcons.add(shandongCard);

            AppNameIcon kernel = new AppNameIcon(resources.getString(R.string.kernel), resources.getDrawable(R.mipmap.card_cancellation, null), null, null,null);
            nameAndIcons.add(kernel);

            AppNameIcon TaddO = new AppNameIcon(resources.getString(R.string.TaddO), resources.getDrawable(R.mipmap.t_add_o, null), null, null,null);
            nameAndIcons.add(TaddO);

            AppNameIcon Settlement = new AppNameIcon(resources.getString(R.string.Settlement), resources.getDrawable(R.mipmap.settlement, null), null, null,null);
            nameAndIcons.add(Settlement);
        }

        return nameAndIcons;
    }
}
