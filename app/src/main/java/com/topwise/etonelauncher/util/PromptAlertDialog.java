package com.topwise.etonelauncher.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.topwise.etonelauncher.R;

/**
 * Ａlertdialog提示方法
 */
public class PromptAlertDialog {
    private Context context;

    public PromptAlertDialog(Context context) {
        this.context = context;
    }

    public void  runAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.alertdialog_title)
                .setIcon(R.mipmap.system_setting)
        .setMessage(R.string.alertdialog_message)
        .setPositiveButton(R.string.alertdialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
