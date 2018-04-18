package com.mrlou.h5;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mrlou.mrlou.R;

/**
 * @author jx_chen
 * @date 2016-11-23---下午2:26:11
 * @problem
 */
public class Other_H5Act extends Activity {

	private WebView ylbfrawebview;
	private String url, mrString, url2;
	Handler handle = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myfragment);
		mrString = Other_H5Act.this.getExternalFilesDir(null).getPath();
		url2 = "file://" + mrString + "/bulidersir/";
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		ylbfrawebview = (WebView) findViewById(R.id.myfrawebview);
		initSetWebview(ylbfrawebview);
		ylbfrawebview.loadUrl(url2 + url);
	}

	private void initSetWebview(WebView webView) {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.getSettings().setDatabaseEnabled(true);
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "mrlou");
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			webView.getSettings().setDatabasePath(
					"/data/data/" + webView.getContext().getPackageName()
							+ "/databases/");
		}
	}

	final class DemoJavaScriptInterface {
		DemoJavaScriptInterface() {
		}

		@JavascriptInterface
		public void back() {
			simulateKey(KeyEvent.KEYCODE_BACK);
		}
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Other_H5Act.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
}
