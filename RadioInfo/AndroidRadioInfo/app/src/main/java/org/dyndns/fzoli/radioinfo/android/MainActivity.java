package org.dyndns.fzoli.radioinfo.android;

import java.util.WeakHashMap;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {

	private final ClassRadioInfoView CRIV = new ClassRadioInfoView(this) {
		
		private final WeakHashMap<Integer, View> VIEWS = new WeakHashMap<Integer, View>();
		
		private View findViewById(int resId) {
			View v = VIEWS.get(resId);
			if (v == null) {
				v = MainActivity.this.findViewById(resId);
				VIEWS.put(resId, v);
			}
			return v;
		}
		
		@Override
		protected void runOnUiThread(Runnable action) {
			MainActivity.this.runOnUiThread(action);
		}

		@Override
		protected void setTextViewText(int res, String txt) {
			((TextView)findViewById(res)).setText(txt);
		}

		@Override
		protected void setViewVisibility(Integer viewId, int visibility) {
			findViewById(viewId).setVisibility(visibility);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.airing);
		setContentView(R.layout.activity_main);
		CRIV.setComponents(R.id.progressBar, R.id.horizontalScrollView, R.id.linearLayoutMsg, R.id.textView1, R.id.textView2, R.id.textViewMsg);
		CRIV.refresh();
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
				CRIV.refresh();
				break;
			case R.id.menu_save:
				CRIV.save();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
