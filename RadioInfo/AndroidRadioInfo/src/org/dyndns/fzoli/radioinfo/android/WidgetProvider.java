package org.dyndns.fzoli.radioinfo.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

	private static final String SYNC_REFRESH_CLICKED = "automaticWidgetSyncRefreshButtonClick";
	private static final String SYNC_SAVE_CLICKED = "automaticWidgetSyncSaveButtonClick";

	private static ClassRadioInfoView criv;
	
	private static ClassRadioInfoView getRadioInfoView(Context context) {
		if (criv == null) {
			final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
	        final ComponentName watchWidget = new ComponentName(context, WidgetProvider.class);
			criv = new ClassRadioInfoView(context) {
				
				private void updateWidget() {
					appWidgetManager.updateAppWidget(watchWidget, remoteViews);
				}
				
				@Override
				protected void runOnUiThread(Runnable action) {
					action.run();
					updateWidget();
				}

				@Override
				protected void setTextViewText(int res, String txt) {
					remoteViews.setTextViewText(res, txt);
					refreshListeners();
				}

				@Override
				protected void setViewVisibility(Integer viewId, int visibility) {
					remoteViews.setViewVisibility(viewId, visibility);
					refreshListeners();
				}
				
				private void refreshListeners() {
					setListeners(getContext(), remoteViews);
				}
				
			};
			criv.setComponents(R.id.layout_progress, R.id.layout_song, R.id.layout_warn, R.id.tv_song_artist, R.id.tv_song_address, R.id.tv_msg);
		}
		return criv;
	}
	
	private static void setListeners(Context context, RemoteViews remoteViews) {
		remoteViews.setOnClickPendingIntent(R.id.btn_refresh, getPendingSelfIntent(context, SYNC_REFRESH_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.btn_save, getPendingSelfIntent(context, SYNC_SAVE_CLICKED));
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		ComponentName watchWidget = new ComponentName(context, WidgetProvider.class);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		setListeners(context, remoteViews);
		appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        getRadioInfoView(context).refresh();
	}
	
	@Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (SYNC_SAVE_CLICKED.equals(intent.getAction())) {
        	getRadioInfoView(context).save();
        }
        else if (SYNC_REFRESH_CLICKED.equals(intent.getAction())) {
        	getRadioInfoView(context).refresh();
        }
    }

    private static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
	
}
