package com.mrlou.h5;

import com.mrlou.addservices.AddServices_CompanyAct;
import com.mrlou.addservices.AddServices_personalAct;
import com.mrlou.economic.Economic_CompanyAct;
import com.mrlou.economic.Economic_personalAct;
import com.mrlou.h5.Other_H5Act.DemoJavaScriptInterface;
import com.mrlou.message.Message_SingleBuilderAct;
import com.mrlou.mrlou.R;

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
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author jx_chen
 * @date 2016-11-29---下午6:07:25
 * @problem
 */
public class Map_Act extends Fragment {
	private View view;
	private WebView ylbfrawebview;
	Handler handle = new Handler();
	private String url, url2,mrString;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_myfragment, container, false);
		ylbfrawebview = (WebView) view.findViewById(R.id.myfrawebview);
		initSetWebview(ylbfrawebview);
		ylbfrawebview.loadUrl("https://appapi.imrlou.com/map/index.html?client=android");
		return view;
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
				view.loadUrl("http://www.imrlou.com/map/index.html?client=android");
				return true;
			}
		});
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

		@JavascriptInterface
		public void brokerPersonal(String id) {
			Intent intent = new Intent(getActivity(),
					Economic_personalAct.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}

		@JavascriptInterface
		public void brokerCompany(String company) {
			Intent intent = new Intent(getActivity(), Economic_CompanyAct.class);
			intent.putExtra("company", company);
			startActivity(intent);
		}

		@JavascriptInterface
		public void servicePersonal(String id) {
			Intent intent = new Intent(getActivity(),
					AddServices_personalAct.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}

		@JavascriptInterface
		public void serviceCompany(String company) {
			Intent intent = new Intent(getActivity(),
					AddServices_CompanyAct.class);
			intent.putExtra("company", company);
			startActivity(intent);
		}

		@JavascriptInterface
		public void building(String id) {
			mrString = getActivity().getExternalFilesDir(null)
					.getPath();
			url = "file://" + mrString + "/bulidersir/lxs_index.html";
			url2 = "file://" + mrString + "/bulidersir/";
			System.out.println("--楼盘--");
			Intent intent = new Intent(getActivity(), Building_Act.class);
			intent.putExtra("url", url);
			intent.putExtra("url2", url2);
			intent.putExtra("frsource", "gg");
			intent.putExtra("id", id);
			startActivity(intent);
		}

		@JavascriptInterface
		public void room(String id) {
			System.out.print("万");
			mrString = getActivity().getExternalFilesDir(null)
					.getPath();
			url = "file://" + mrString + "/bulidersir/lxs_index.html";
			url2 = "file://" + mrString + "/bulidersir/";
			Intent intent = new Intent(getActivity(), RoomInfo_Act.class);
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
					getActivity().finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
}
