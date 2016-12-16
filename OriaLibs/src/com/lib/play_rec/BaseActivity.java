package com.lib.play_rec;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.rec.JsonOperater;
import com.lib.play_rec.utils.ScreenManager;

public class BaseActivity extends AppCompatActivity {
	protected long endTime;// toast时间差
	protected Resources resources;
	protected GlobalInit globalInit;
	protected JsonOperater jsonOperater;
	public void clearRosource() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resources = getResources();
		globalInit = GlobalInit.getInstance(this);
		jsonOperater = JsonOperater.getInstance();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
		ScreenManager.getInstance().pushActivity(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ScreenManager.getInstance().popActivity(this);
	}

	@Override
	public void finish() {
		super.finish();
	}
	
	protected short byteToshort(byte[] by) {
		return (short) (((by[1] & 0xff) << 8) | (by[0] & 0xff));

	}

	protected String toHex(byte by) {
		return "" + "0123456789ABCDEF".charAt((by >> 4) & 0xf)
				+ "0123456789ABCDEF".charAt((by & 0xf));
	}

	public String getPreferenceValue(String key) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		String value = settings.getString(key, "");
		return value;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ScreenManager.getInstance().popActivity(this);
			return super.onKeyDown(keyCode, event);
		}
		return false;
	}

	/** 横竖屏切换 */
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// land
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// port
		}
	}

	/** 清楚缓存 */
	public void clearCookie() {
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}

	/** 去除前后空格 */
	protected String getTrimText(TextView text) {
		return text.getText().toString().trim();
	}

	/** Toast展现信息 */
	public void showMessage(final int msgId) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (System.currentTimeMillis() - endTime > 2500) {
					Toast.makeText(BaseActivity.this, msgId,
							Toast.LENGTH_SHORT).show();
					endTime = System.currentTimeMillis();
				}
			}
		});
	}

	public void showMessage(final String msgStr) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (System.currentTimeMillis() - endTime > 2500) {
					Toast.makeText(BaseActivity.this, msgStr,
							Toast.LENGTH_SHORT).show();
					endTime = System.currentTimeMillis();
				}
			}
		});
	}
}
