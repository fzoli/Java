package org.dyndns.fzoli.radioinfo.android;

import java.io.File;

import org.dyndns.fzoli.radioinfo.RadioInfo;
import org.dyndns.fzoli.radioinfo.RadioInfoLoader;
import org.dyndns.fzoli.radioinfo.RadioInfoView;
import org.dyndns.fzoli.radioinfo.impl.ClassRadioInfoLoader;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public abstract class ClassRadioInfoView implements RadioInfoView {

	private final RadioInfoLoader LOADER = new ClassRadioInfoLoader(this,
			new File(Environment.getExternalStorageDirectory(), "music.txt"));
	
	private final Context CTX;
	
	private Integer progressBar, scrollView, linearLayoutMsg;
	private int textView1, textView2, textViewMsg;
	
	public ClassRadioInfoView(Context ctx) {
		CTX = ctx;
	}

	protected Context getContext() {
		return CTX;
	}
	
	public void setComponents(Integer progressBar, Integer scrollView, Integer linearLayoutMsg,
			int textView1, int textView2, int textViewMsg) {
		this.progressBar = progressBar;
		this.scrollView = scrollView;
		this.linearLayoutMsg = linearLayoutMsg;
		this.textView1 = textView1;
		this.textView2 = textView2;
		this.textViewMsg = textViewMsg;
	}
	
	protected abstract void runOnUiThread(Runnable action);
	
	protected abstract void setTextViewText(int res, String txt);
	
	protected abstract void setViewVisibility(Integer viewId, int visibility);
	
	private void setViewVisible(Integer viewId, boolean visible) {
		if (viewId != null) setViewVisibility(viewId, visible ? View.VISIBLE : View.GONE);
	}
	
	private void setTextViewText(int res, int resTxt) {
		setTextViewText(res, CTX.getString(resTxt));
	}
	
	@Override
	public void setStatus(final Status status) {
		if (status == null) return;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				setViewVisible(scrollView, status == Status.SUCCESS);
				setViewVisible(progressBar, status == Status.LOADING);
				setViewVisible(linearLayoutMsg, status.isError());
				if (status.isError()) {
					int key;
					switch (status) {
						case CHANGED:
							key = R.string.msg_changed;
							break;
						case NO_DATA:
							key = R.string.msg_no_data;
							break;
						default:
							key = R.string.msg_unavailable;
					}
					setTextViewText(textViewMsg, key);
				}
				else {
					setTextViewText(textViewMsg, "");
				}
			}
			
		});
	}

	@Override
	public void setRadioInfo(final RadioInfo info) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (info == null || !info.isValid()) {
					setTextViewText(textView1, "-");
					setTextViewText(textView2, "-");
				}
				else {
					setTextViewText(textView1, info.getMusic().getAddress());
					setTextViewText(textView2, info.getMusic().getArtist());
				}
			}
			
		});
	}

	public void refresh() {
		if (LOADER != null) LOADER.loadRadioInfo();
	}

    public void save() {
    	if (LOADER == null) return;
    	int key = R.string.msg_no_save;
        if (LOADER.getRadioInfo() != null) {
        	switch (LOADER.getRadioInfo().save()) {
        		case SUCCESS:
        			key = R.string.msg_save_success;
        			break;
        		case ALREADY_SAVED:
        			key = R.string.msg_saved_already;
        			break;
        		case ERROR:
        			key = R.string.msg_save_error;
        		default:
        			break;
        	}
        }
        Toast.makeText(CTX, key, Toast.LENGTH_SHORT).show();
    }
	
}
