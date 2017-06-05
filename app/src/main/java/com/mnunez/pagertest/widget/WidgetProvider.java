package com.mnunez.pagertest.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.mnunez.pagertest.BuildConfig;
import com.mnunez.pagertest.R;
import com.mnunez.pagertest.activities.SplashActivity;
import com.mnunez.pagertest.models.SocketEventEnum;
import com.mnunez.pagertest.models.User;
import com.mnunez.pagertest.network.ApiHandler;
import com.mnunez.pagertest.utils.ParseUtils;
import com.mnunez.pagertest.utils.SessionUtils;
import com.squareup.picasso.Picasso;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by mnunez on 10/4/16.
 */

public class WidgetProvider extends AppWidgetProvider {

    private Context mContext;
    private AppWidgetManager mAppWidgetManager;
    private int[] mAppWidgetsIds;
    private ApiHandler apihHandler;
    private WebSocket mWebSocket;

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        try {
            super.onUpdate(context, appWidgetManager, appWidgetIds);
            mContext = context;
            mAppWidgetManager = appWidgetManager;
            mAppWidgetsIds = appWidgetIds;
            this.updateWidgetContent(mContext.getString(R.string.user_details_no_status));
            if (apihHandler == null) {
                apihHandler = new ApiHandler();
            }
            if (mWebSocket == null) {
                mWebSocket = apihHandler.createWebSocket(BuildConfig.WEB_SOCKET_URL, new WebSocketListener() {

                    @Override
                    public void onMessage(WebSocket webSocket, final String text) {
                        SocketEventEnum socketEventEnum = ParseUtils.parseEventType(text);
                        if (socketEventEnum != null) {
                            switch (socketEventEnum) {
                                case STATUS:
                                    updateWidgetContent(ParseUtils.parseAndUpdateStatusChanged(text));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                        super.onFailure(webSocket, t, response);
                        Log.d(WidgetProvider.class.getSimpleName(), t.getMessage());
                    }
                });
            }
        }catch (Exception e){
            Log.d(WidgetProvider.class.getSimpleName(), e.getMessage());
        }
    }

    private void updateWidgetContent(String status) {
        User user = SessionUtils.getUserFollow(mContext);
        for (int appWidgetId : mAppWidgetsIds) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout);
            Intent configIntent = new Intent(mContext, SplashActivity.class);
            configIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent configPendingIntent = PendingIntent.getActivity(mContext, 0, configIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_container, configPendingIntent);
            if (user != null) {
                views.setTextViewText(R.id.name, user.getName());
                views.setTextViewText(R.id.role, user.getRole().getDescription());
                views.setTextViewText(R.id.status, status);
                Picasso.with(mContext).load(user.getAvatar()).resize(50, 50).into(views, R.id.avatar, new int[]{appWidgetId});
            } else {
                views.setTextViewText(R.id.name, mContext.getString(R.string.widget_not_following));
                views.setTextViewText(R.id.role, "");
                views.setTextViewText(R.id.status, "");
                Picasso.with(mContext).load(R.drawable.ic_user_placeholder).resize(50, 50).into(views, R.id.avatar, new int[]{appWidgetId});
            }
            mAppWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
