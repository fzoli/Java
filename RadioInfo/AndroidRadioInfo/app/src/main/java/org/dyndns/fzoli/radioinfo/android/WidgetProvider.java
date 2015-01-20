package org.dyndns.fzoli.radioinfo.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

	private static final String REFRESH_CLICKED = "AndroidRadioInfo.RefreshButtonClick";
	private static final String SAVE_CLICKED = "AndroidRadioInfo.SaveButtonClick";

	private static ClassRadioInfoView criv;
	
	private static ClassRadioInfoView getRadioInfoView(Context context) {
		if (criv == null) {
			final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
	        final ComponentName watchWidget = new ComponentName(context, WidgetProvider.class);
			criv = new ClassRadioInfoView(context) {
				
				@Override
				protected void runOnUiThread(Runnable action) {
					action.run();
					updateWidget();
				}

				@Override
				protected void setTextViewText(int res, String txt) {
					remoteViews.setTextViewText(res, txt);
				}

				@Override
				protected void setViewVisibility(Integer viewId, int visibility) {
					remoteViews.setViewVisibility(viewId, visibility);
				}
				
				private void updateWidget() {
					refreshListeners();
					appWidgetManager.updateAppWidget(watchWidget, remoteViews);
				}
				
				private void refreshListeners() {
					setListeners(getContext(), remoteViews);
				}
				
			};
			criv.setComponents(R.id.layout_progress, R.id.layout_song, R.id.layout_warn, R.id.tv_song_addr, R.id.tv_song_artist, R.id.tv_msg);
		}
		return criv;
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
        getRadioInfoView(context).refresh();
	}
	
	@Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (SAVE_CLICKED.equals(intent.getAction())) {
        	getRadioInfoView(context).save();
        }
        else if (REFRESH_CLICKED.equals(intent.getAction())) {
        	getRadioInfoView(context).refresh();
        }
    }

	private static void setListeners(Context context, RemoteViews remoteViews) {
		remoteViews.setOnClickPendingIntent(R.id.btn_refresh, getPendingSelfIntent(context, REFRESH_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.btn_save, getPendingSelfIntent(context, SAVE_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.tv_song_addr, getPendingActivityIntent(context));
//        remoteViews.setOnClickPendingIntent(R.id.tv_song_artist, getPendingActivityIntent(context));
	}
	
    private static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
	
    private static PendingIntent getPendingActivityIntent(Context context) {
    	return PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
    }
    
}
