package com.mnunez.pagertest.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

/**
 * Created by mnunez on 10/5/16.
 */

public class WidgetUtils {

    public static void updateWidgets(Activity activity) {
        int ids[] = AppWidgetManager.getInstance(activity.getApplication()).
                getAppWidgetIds(new ComponentName(activity.getApplication(), WidgetProvider.class));
        if (ids != null && ids.length > 0) {
            Intent intent = new Intent(activity, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            activity.sendBroadcast(intent);
        }
    }

}
