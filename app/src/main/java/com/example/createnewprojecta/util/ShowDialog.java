package com.example.createnewprojecta.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * LogIn Name zhangyingyu
 * Create by 张瀛煜 on 2020-06-09 at 17:48 ：）
 */
public class ShowDialog {
    public interface ShowClick{
        void onclick1(DialogInterface dialogInterface);
        void onclick2(DialogInterface dialogInterface);
    }
    public static void ShowMyDialog(Context context, String msg, final ShowClick showClick){
        AlertDialog.Builder  builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showClick.onclick1(dialog);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showClick.onclick2(dialog);
            }
        });
        builder.create().show();
    }
}
