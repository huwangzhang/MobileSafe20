package com.example.huwang.mobilesafe20.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.huwang.mobilesafe20.service.WidgetService;

/**
 * Created by huwang on 2017/4/7.
 */

public class MyWidagetReceiver extends AppWidgetProvider {
    @Override
    public void onEnabled(Context context) {
        // TODO Auto-generated method stub
        super.onEnabled(context);
        context.startService(new Intent(context,WidgetService.class));
    }
    @Override
    public void onDisabled(Context context) {
        // TODO Auto-generated method stub

        super.onDisabled(context);
        context.stopService(new Intent(context,WidgetService.class));
    }
}
