package com.mrlou.h5;

import com.amap.api.maps.model.Text;
import com.mrlou.addservices.AddServices_CompanyAct;
import com.mrlou.addservices.AddServices_personalAct;
import com.mrlou.economic.Economic_CompanyAct;
import com.mrlou.economic.Economic_personalAct;
import com.mrlou.h5.Other_H5Act.DemoJavaScriptInterface;
import com.mrlou.message.Message_SingleBuilderAct;
import com.mrlou.mrlou.R;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author jx_chen
 * @date 2016-11-29---下午6:07:25
 * @problem
 */
public class Map_ClickAct extends Activity {
	private View view;
	private WebView ylbfrawebview;
	Handler handle = new Handler();
	private String url, url2,mrString,url_map,types;
	private LinearLayout lay_map_back;
	private TextView tv_act_mapname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_myfragment);
		ylbfrawebview = (WebView) findViewById(R.id.mapfrawebview);
		Intent intent=getIntent();
		url_map=intent.getStringExtra("url_map");
		types=intent.getStringExtra("type");
		initSetWebview(ylbfrawebview);
		ylbfrawebview.loadUrl(url_map);
	}

	private void initSetWebview(WebView webView) {
		// TODO Auto-generated method stub
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
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url_map);
				return true;
			}
		});
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			webView.getSettings().setDatabasePath(
					"/data/data/" + webView.getContext().getPackageName()
							+ "/databases/");
		}
		
		lay_map_back=(LinearLayout) findViewById(R.id.lay_map_back);
		lay_map_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		tv_act_mapname=(TextView) findViewById(R.id.tv_act_mapname);
		if (types.equals("1")) {
			tv_act_mapname.setText("企业服务地图");
		}else {
			tv_act_mapname.setText("经济服务地图");
		}
	}
	
	final class DemoJavaScriptInterface {
		DemoJavaScriptInterface() {
		}

		@JavascriptInterface
		public void back() {
			simulateKey(KeyEvent.KEYCODE_BACK);
		}

		@JavascriptInterface
		public void brokerPersonal(String id) {
			Intent intent = new Intent(Map_ClickAct.this,
					Economic_personalAct.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}

		@JavascriptInterface
		public void brokerCompany(String company) {
			Intent intent = new Intent(Map_ClickAct.this, Economic_CompanyAct.class);
			intent.putExtra("company", company);
			startActivity(intent);
		}

		@JavascriptInterface
		public void servicePersonal(String id) {
			Intent intent = new Intent(Map_ClickAct.this,
					AddServices_personalAct.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}

		@JavascriptInterface
		public void serviceCompany(String company) {
			Intent intent = new Intent(Map_ClickAct.this,
					AddServices_CompanyAct.class);
			intent.putExtra("company", company);
			startActivity(intent);
		}

		@JavascriptInterface
		public void building(String id) {
			mrString = Map_ClickAct.this.getExternalFilesDir(null)
					.getPath();
			url = "file://" + mrString + "/bulidersir/lxs_index.html";
			url2 = "file://" + mrString + "/bulidersir/";
			System.out.println("--楼盘--");
			Intent intent = new Intent(Map_ClickAct.this, Building_Act.class);
			intent.putExtra("url", url);
			intent.putExtra("url2", url2);
			intent.putExtra("frsource", "gg");
			intent.putExtra("id", id);
			startActivity(intent);
		}

		@JavascriptInterface
		public void room(String id) {
			System.out.print("万");
			mrString = Map_ClickAct.this.getExternalFilesDir(null)
					.getPath();
			url = "file://" + mrString + "/bulidersir/lxs_index.html";
			url2 = "file://" + mrString + "/bulidersir/";
			Intent intent = new Intent(Map_ClickAct.this, RoomInfo_Act.class);
			intent.putExtra("url", url);
			intent.putExtra("url2", url2);
			intent.putExtra("frsource", "gg");
			intent.putExtra("id", id);
			startActivity(intent);
		}

	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Map_ClickAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
}
