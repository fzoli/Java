package org.dyndns.fzoli.radioinfo.android;

import java.io.File;

import org.dyndns.fzoli.radioinfo.RadioInfo;
import org.dyndns.fzoli.radioinfo.RadioInfoLoader;
import org.dyndns.fzoli.radioinfo.RadioInfoView;
import org.dyndns.fzoli.radioinfo.impl.ClassRadioInfoLoader;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements RadioInfoView {

	private final RadioInfoLoader LOADER = new ClassRadioInfoLoader(this,
			new File(Environment.getExternalStorageDirectory(), "music.txt"));

	private View progressBar, scrollView, linearLayoutMsg;
	private TextView textView1, textView2, textViewMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.airing);
		setContentView(R.layout.activity_main);
		
		progressBar = findViewById(R.id.progressBar);
		linearLayoutMsg = findViewById(R.id.linearLayoutMsg);
		scrollView = findViewById(R.id.horizontalScrollView);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textViewMsg = (TextView) findViewById(R.id.textViewMsg);
		
		refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				refresh();
				break;
			case R.id.menu_save:
				save();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setStatus(final Status status) {
		if (status == null) return;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				scrollView.setVisibility(status == Status.SUCCESS ? View.VISIBLE : View.GONE);
				progressBar.setVisibility(status == Status.LOADING ? View.VISIBLE : View.GONE);
				linearLayoutMsg.setVisibility(status.isError() ? View.VISIBLE : View.GONE);
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
					textViewMsg.setText(key);
				}
				else {
					textViewMsg.setText("");
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
					textView1.setText("-");
					textView2.setText("-");
				}
				else {
					textView1.setText(info.getMusic().getAddress());
					textView2.setText(info.getMusic().getArtist());
				}
			}
			
		});
	}

	private void refresh() {
		if (LOADER != null) LOADER.loadRadioInfo();
	}

    private void save() {
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
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
    }
}
