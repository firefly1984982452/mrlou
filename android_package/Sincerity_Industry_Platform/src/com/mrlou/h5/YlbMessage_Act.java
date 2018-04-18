package com.mrlou.h5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.mrlou.activity.BasicMapActivity;
import com.mrlou.activity.NewRegisterActivity;
import com.mrlou.data.FangWenData;
import com.mrlou.economic.Economic_CompanyAct;
import com.mrlou.economic.Economic_personalAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.mine.Elite_meetingAct;
import com.mrlou.mine.Setting_personal03;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.Constant;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.view.ActionSheetDialog;
import com.mrlou.view.ActionSheetDialog.OnSheetItemClickListener;
import com.mrlou.view.ActionSheetDialog.SheetItemColor;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

@SuppressLint("HandlerLeak")
public class YlbMessage_Act extends Activity implements HttpListener {
	private WebView firstwebView, secondWebView;
	private String curwebview = "1";
	private View myView;
	private static final int IMAGE = 2;
	private static final int CAMERA = 3;
	private static final int RESULT = 4;
	private static final int MAPCODE = 5;
	Handler handle = new Handler();
	private boolean flag = false;
	public File tempFile;
	UMSocialService mController;
	private String errorString, shareidString, baseurlString, fenxiangall,
			photo_stream = "sss", imgurl, imgid, lasturlString, bulididString,
			bdobject_type, bdobject_id, bdis_avatar, bdmark, bdcut,
			cur = "sss", prams = "sss", detail = "sss", mapdetail = "sss",
			firsttwo = "no", shareimg = "", sharecontent = "", shareurl = "",
			sharetitle = "", sharecopy = "", frsource = "", guanggao = "sss";
	ProgressDialog mDialog;
	private PopupWindow popupWindow;
	private int[] images = { R.drawable.wechat, R.drawable.wxcicle,
			R.drawable.qq, R.drawable.qzone, R.drawable.sms, R.drawable.copy,
			R.drawable.sina, R.drawable.gmail };
	private String[] names = { "微信好友", "朋友圈", "QQ", "QQ空间", "短信", "复制链接", "新浪",
			"邮件" };
	private ActionSheetDialog xjxcdialog;
	private ArrayList<FangWenData> fangWenDatas = new ArrayList<FangWenData>();
	private String yemiansource = "", testurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myfragment);
		firstwebView = (WebView) findViewById(R.id.myfrawebview);
		secondWebView = (WebView) findViewById(R.id.myfrawebview2);
		secondWebView.setVisibility(View.GONE);
		initSetWebview(firstwebView);
		initSetWebview(secondWebView);
		loadurl();
		initdialog();

	}

	private void initdialog() {
		mDialog = new ProgressDialog(YlbMessage_Act.this);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("上传中...");
		mDialog.setIndeterminate(false);
		mDialog.setCancelable(true);
	}

	private void loadurl() {
		Intent intent = getIntent();
		baseurlString = intent.getStringExtra("url2");
//		frsource = intent.getStringExtra("frsource");
		testurl = intent.getStringExtra("url");
		String id = intent.getStringExtra("id");
//		if (frsource.equals("gg")) {
//			guanggao = "gg";
//		}
		firstwebView.loadUrl(testurl);
		// firstwebView
		// .loadUrl("javascript:mrlou.href('ylb_thread.html','id="+id+"')");
	}

	public String getuuid() {
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) YlbMessage_Act.this
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled",
			"SdCardPath", "NewApi" })
	private void initSetWebview(WebView webView) {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.getSettings().setDatabaseEnabled(true);
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "mrlou");
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new ChromeClient());
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			webView.getSettings().setDatabasePath(
					"/data/data/" + webView.getContext().getPackageName()
							+ "/databases/");
		}
		if (Build.VERSION.SDK_INT >= 16) {
			webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		}

	}

	private void CreateDialog() {
		xjxcdialog = new ActionSheetDialog(YlbMessage_Act.this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.setTitle("从下途径选择图片上传")
				.addSheetItem("从相册中选择", SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								Intent intent2 = new Intent(Intent.ACTION_PICK,
										null);
								intent2.setDataAndType(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
										"image/*");
								startActivityForResult(intent2, IMAGE);
								xjxcdialog.dismiss();
							}
						})
				.addSheetItem("使用相机照相", SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								if (!Environment
										.getExternalStorageState()
										.equals(android.os.Environment.MEDIA_MOUNTED)) {
									Toast.makeText(YlbMessage_Act.this,
											"存储空间未就绪！", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								new Constant();
								tempFile = Constant.createFile(
										Constant.FILE_DIR,
										Constant.CACHE_STRING);
								Intent intent = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(tempFile));
								startActivityForResult(intent, CAMERA);
								xjxcdialog.dismiss();
							}
						});
		xjxcdialog.show();
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.e("++++++++++++onpagestarted++++", "onstarted:" + url);
		}

		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			if (errorCode == -2) {
				view.stopLoading();
				view.loadUrl("file:///android_asset/errorpage/loaderror.html");
			}
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();
		};

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			HitTestResult hit = view.getHitTestResult();
			Log.e("shouldorideurlloading.......",
					"hittestresult" + hit.getExtra() + "type" + hit.getType());
			if (url.contains("tel:")) {
				Uri uri = Uri.parse(url);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(it);
			} else {
				view.loadUrl(url);
			}
			return true;
		}

		public void onPageFinished(WebView view, String url) {
			Log.e("++++onpagefinished", "pagefinfished" + url);

			if (fangWenDatas.size() > 0 && yemiansource.equals("back")) {
				String testprams = "";
				Log.e("8888888888_url_888888888888", url);

				for (int i = fangWenDatas.size() - 1; i >= 0; i--) {
					Log.e("++=++++lishijilu+++++++", fangWenDatas.get(i)
							.getUrl());
				}
				for (int i = fangWenDatas.size() - 1; i >= 0; i--) {
					if (url.equals(fangWenDatas.get(i).getUrl())) {
						testprams = fangWenDatas.get(i).getPrams();
						Log.e("111111111+testpram+111111111111", testprams);
						break;
					}
				}
				if (!TextUtils.isEmpty(testprams)) {
					view.loadUrl("javascript:byParams('" + testprams + "')");
				}
				yemiansource = "";
			}
			if (cur.equals("href")) {
				cur = "sss";
				if (firsttwo.equals("yes")) {
					if (!TextUtils.isEmpty(prams)) {
						view.loadUrl("javascript:byParams('" + prams + "')");
						Log.e("cccccccccccccccccccccccc", "当前网址2" + url);

					}
					secondWebView.clearHistory();

					firsttwo = "no";
					firstwebView.setVisibility(View.GONE);
					secondWebView.setVisibility(View.VISIBLE);
				} else {
					if (!TextUtils.isEmpty(prams)) {
						view.loadUrl("javascript:byParams('" + prams + "')");
						Log.e("cccccccccccccccccccccccc", "当前网址2" + url);
					}
				}
			} else if (detail.equals("detail")) {
				if (!TextUtils.isEmpty(bulididString)) {
					String canshiString = "buildid=" + bulididString;
					view.loadUrl("javascript:byParams('" + canshiString + "')");
					Log.e("detailing......................", "当前网址3"
							+ "javascript:byParams('" + canshiString + "')");
					detail = "sss";
				}
			}

		}
	}

	class ChromeClient extends WebChromeClient {
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
		}

		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

	}

	final class DemoJavaScriptInterface {
		DemoJavaScriptInterface() {
		}
		@JavascriptInterface
		public void androidIs(String type, String data) {
			Log.e("androidIs", "类型为：" + type + "    数据为：" + data);
			// 1是进入个人品牌页，2是进入公司品牌页，3是地图页，4是进入易楼精英会
			if (type == "1" || "1".equals(type)) {
				Intent intent = new Intent(YlbMessage_Act.this,
						Economic_personalAct.class);
				intent.putExtra("id", data);
				startActivity(intent);
			} else if (type == "2" || "2".equals(type)) {
				Intent intent = new Intent(YlbMessage_Act.this,
						Economic_CompanyAct.class);
				intent.putExtra("company", data);
				startActivity(intent);
			} else if(type.equals("4")){
				startActivity(new Intent(YlbMessage_Act.this,Elite_meetingAct.class));
			}
		}
		@JavascriptInterface
		public void navmap(final String title, final String lat,
				final String lon, final String address, final String buliding_id) {
			handle.post(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(YlbMessage_Act.this,
							BasicMapActivity.class);
					Bundle data = new Bundle();
					try {
						data.putString("title",
								URLDecoder.decode(title, "utf-8"));
						data.putString("lat", URLDecoder.decode(lat, "utf-8"));
						data.putString("lon", URLDecoder.decode(lon, "utf-8"));
						data.putString("address",
								URLDecoder.decode(address, "utf-8"));
						data.putString("buliding_id",
								URLDecoder.decode(buliding_id, "utf-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					intent.putExtras(data);
					startActivityForResult(intent, MAPCODE);
				}
			});
		}

		@JavascriptInterface
		public void href(final String prurl, final String param,
				final String hide) {
			Log.e("fragment2++++href+++++", "地址" + prurl + "参数" + param
					+ "隐藏参数" + hide);
			System.out.println("id-----" + prurl);
			handle.post(new Runnable() {

				@Override
				public void run() {

					yemiansource = "href";
					fangWenDatas.add(new FangWenData(baseurlString + prurl,
							prams));

					if (curwebview.equals("1")) {
						// 获取firstview的高度
						int widthfirstview = firstwebView.getWidth();
						int heightfirstview = firstwebView.getHeight();
						Log.e("height+width", "height" + heightfirstview
								+ "width" + widthfirstview);
						secondWebView.setVisibility(View.INVISIBLE);
						LayoutParams praParams = new LinearLayout.LayoutParams(
								widthfirstview, heightfirstview);
						secondWebView.setLayoutParams(praParams);
						secondWebView.loadUrl(baseurlString + prurl);
						firsttwo = "yes";
						secondWebView.clearHistory();
						// curwebview="2";
					} else if (curwebview.equals("2")) {
						int widthfirstview = firstwebView.getWidth();
						int heightfirstview = firstwebView.getHeight();
						Log.e("height+width", "height" + heightfirstview
								+ "width" + widthfirstview);
						secondWebView.setVisibility(View.INVISIBLE);
						LayoutParams praParams = new LinearLayout.LayoutParams(
								widthfirstview, heightfirstview);
						secondWebView.setLayoutParams(praParams);
						Log.e("++++++++++++++++++++++++++++执行222222222222222222",
								baseurlString + prurl);
						secondWebView.loadUrl(baseurlString + prurl);
					}
					cur = "href";
					try {
						prams = URLDecoder.decode(param, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			});
		}

		@JavascriptInterface
		public void share(final String content, final String url,
				final String img, final String copy, final String shareid) {

			try {
				shareidString = URLDecoder.decode(shareid, "utf-8");
				fenxiangall = URLDecoder.decode(url, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			handle.post(new Runnable() {
				public void run() {
					try {
						Shareto(URLDecoder.decode(content, "utf-8"),
								URLDecoder.decode(url, "utf-8"),
								URLDecoder.decode(img, "utf-8"),
								URLDecoder.decode(copy, "utf-8"),
								URLDecoder.decode(shareid, "utf-8"));
					} catch (Exception e) {
						errorString = e.toString();
						Log.d("TTTTT", errorString);
					}
				}
			});
		}

		@JavascriptInterface
		public void openlogin() {
			handle.post(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(YlbMessage_Act.this,
							NewRegisterActivity.class);
					intent.putExtra("lasturl", "register_step1.html");
					intent.putExtra("baseurl", baseurlString);
					startActivity(intent);
					YlbMessage_Act.this.overridePendingTransition(
							R.anim.zoom_in, R.anim.zoom_out);
				}
			});
		}

		@JavascriptInterface
		public void loginbytoken(final String token) {
			handle.post(new Runnable() {
				@Override
				public void run() {
					try {
						loginrequest(token, getuuid(), "sss");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		@JavascriptInterface
		public void back() {
			handle.post(new Runnable() {

				@Override
				public void run() {
					simulateKey(KeyEvent.KEYCODE_BACK);
					if (curwebview.equals("2") && !secondWebView.canGoBack()) {
						firstwebView.setVisibility(View.VISIBLE);
						secondWebView.clearHistory();
						secondWebView.setVisibility(View.GONE);
						secondWebView.removeAllViews();
						curwebview = "1";
					} else if (curwebview.equals("2")
							&& secondWebView.canGoBack()) {
						if (secondWebView.copyBackForwardList().getSize() > 0
								&& secondWebView
										.copyBackForwardList()
										.getItemAtIndex(0)
										.getUrl()
										.equals(secondWebView
												.copyBackForwardList()
												.getItemAtIndex(
														secondWebView
																.copyBackForwardList()
																.getSize() - 1)
												.getUrl())) {
							firstwebView.setVisibility(View.VISIBLE);
							secondWebView.clearHistory();
							secondWebView.setVisibility(View.GONE);
							secondWebView.removeAllViews();
							curwebview = "1";
						} else {
							yemiansource = "back";
							secondWebView.goBack();
						}

					} else if (mapdetail.equals("mapdetail")) {
						mapdetail = "sss";
						firstwebView.clearHistory();
						firstwebView.loadUrl(baseurlString + "lxs_index.html");
					} else if (guanggao.equals("gg")) {
						guanggao = "sss";
						firstwebView.clearHistory();
						firstwebView.loadUrl(baseurlString + "lxs_index.html");
					} else {
						yemiansource = "back";
						firstwebView.goBack();
					}
				}
			});
		}

		@JavascriptInterface
		public void logout() {
			handle.post(new Runnable() {
				@Override
				public void run() {
					if (TextUtils
							.isEmpty(YlbMessage_Act.this.getSharedPreferences(
									"user", Activity.MODE_PRIVATE).toString())) {
						YlbMessage_Act.this
								.getSharedPreferences("user",
										Activity.MODE_PRIVATE).edit().clear()
								.commit();
					}
					if (curwebview.equals("1")) {
						firstwebView.loadUrl("javascript:logoutCallBack()");
					} else if (curwebview.equals("2")) {
						secondWebView.loadUrl("javascript:logoutCallBack()");
					}
				}
			});
		}

		@JavascriptInterface
		public void getuserinfo() {
			handle.post(new Runnable() {
				@Override
				public void run() {
					SharedPreferences userSharedPreferences = YlbMessage_Act.this
							.getSharedPreferences("user", Activity.MODE_PRIVATE);
					String sesseion_id = userSharedPreferences.getString(
							"session_id", null);
					String type_id = userSharedPreferences.getString("type_id",
							null);
					String user_id = userSharedPreferences.getString("user_id",
							null);

					if (curwebview.equals("1")) {
						firstwebView.loadUrl("javascript:UserInfoCallBack('"
								+ sesseion_id + "','" + user_id + "','"
								+ type_id + "')");
					} else if (curwebview.equals("2")) {
						secondWebView.loadUrl("javascript:UserInfoCallBack('"
								+ sesseion_id + "','" + user_id + "','"
								+ type_id + "')");
					}
				}
			});
		}

		@JavascriptInterface
		public void iphoto(final String object_type, final String object_id,
				final String is_avatar, final String mark, final String cut) {
			handle.post(new Runnable() {
				@Override
				public void run() {
					bdobject_type = object_type;
					bdobject_id = object_id;
					bdis_avatar = is_avatar;
					bdcut = cut;
					bdmark = mark;
					CreateDialog();
				}
			});
		}
	}

	public void uploadimgrequest(String photo, String object_type,
			String object_id, String is_avatar, String mark, String cut) {
		mDialog.show();
		Request request = Request.requestImg(photo, object_type, object_id,
				is_avatar, mark);
		new HttpThread(request, this);
	}

	public void loginrequest(String token, String device_id, String sys_agent) {
		SharedPreferences preferences = YlbMessage_Act.this
				.getSharedPreferences("channelid", Context.MODE_PRIVATE);
		String chaString = preferences.getString("channelid", " ");
		Request loginRequest = Request.requestloginbytoken(token, device_id,
				sys_agent, chaString);
		new HttpThread(loginRequest, this);
	}

	// <-------------分享相关popwindow+gridview的配置
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void initpopwindow() {
		View contentView = YlbMessage_Act.this.getLayoutInflater().inflate(
				R.layout.popwindow, null);
		GridView gridView = (GridView) contentView.findViewById(R.id.gridView);
		gridView.setAdapter(getAdapter());
		gridView.setOnItemClickListener(new ItemClickListener());
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.animation);
	}

	private final class ItemClickListener implements OnItemClickListener {
		@SuppressWarnings("deprecation")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:// 微信好友
				Fenxiangweixin(sharetitle, sharecopy, shareurl);
				mController.postShare(YlbMessage_Act.this, SHARE_MEDIA.WEIXIN,
						mShareListener);
				tosharelog(shareidString, SHARE_MEDIA.WEIXIN.toString());
				popupWindow.dismiss();
				break;
			case 1:// 微信朋友圈
				Fengwxcicle(sharetitle, sharecopy, shareurl);
				mController.postShare(YlbMessage_Act.this,
						SHARE_MEDIA.WEIXIN_CIRCLE, mShareListener);
				tosharelog(shareidString, SHARE_MEDIA.WEIXIN_CIRCLE.toString());
				popupWindow.dismiss();
				break;
			case 2:// qq
				Fenxiangqq(sharetitle, sharecopy, shareurl);
				mController.postShare(YlbMessage_Act.this, SHARE_MEDIA.QQ,
						mShareListener);
				tosharelog(shareidString, SHARE_MEDIA.QQ.toString());
				popupWindow.dismiss();
				break;
			case 3:// qq空间
				FenxiangqqQz(sharetitle, sharecopy, shareurl);
				mController.postShare(YlbMessage_Act.this, SHARE_MEDIA.QZONE,
						mShareListener);
				tosharelog(shareidString, SHARE_MEDIA.QZONE.toString());
				popupWindow.dismiss();
				break;
			case 4:// 短信
				FenxiangEmailSms(sharecontent, shareurl);
				mController.postShare(YlbMessage_Act.this, SHARE_MEDIA.SMS,
						mShareListener);
				tosharelog(shareidString, SHARE_MEDIA.SMS.toString());
				popupWindow.dismiss();
				break;
			case 5:// 复制
				ClipboardManager cmb = (ClipboardManager) YlbMessage_Act.this
						.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(fenxiangall.trim());
				Toast.makeText(YlbMessage_Act.this, "复制成功", Toast.LENGTH_SHORT)
						.show();
				popupWindow.dismiss();
				break;
			case 6:// 新浪
				FenxiangSina(sharetitle, sharecopy, shareurl);
				mController.postShare(YlbMessage_Act.this, SHARE_MEDIA.SINA,
						mShareListener);
				tosharelog(shareidString, SHARE_MEDIA.SINA.toString());
				popupWindow.dismiss();
				break;
			case 7:// 邮箱
				FenxiangEmailSms(sharecontent, shareurl);
				mController.postShare(YlbMessage_Act.this, SHARE_MEDIA.EMAIL,
						mShareListener);
				tosharelog(shareidString, SHARE_MEDIA.EMAIL.toString());
				popupWindow.dismiss();
				break;
			default:
				break;
			}
		}
	}

	SnsPostListener mShareListener = new SnsPostListener() {
		@Override
		public void onStart() {
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode,
				SocializeEntity entity) {
			if (stCode == 200) {
				sharecallback("success");
			} else {
				sharecallback("failed");
			}
		}
	};

	// 分享回调方法
	private void sharecallback(String result) {
		if (result.equals("success")) {

			if (curwebview.equals("1")) {
				firstwebView
						.loadUrl("javascript:shareSucccessCallBack('分享成功')");
			} else if (curwebview.equals("2")) {
				secondWebView
						.loadUrl("javascript:shareSucccessCallBack('分享成功')");
			}
		}
		if (result.equals("failed")) {
			if (curwebview.equals("1")) {
				firstwebView
						.loadUrl("javascript:shareSucccessCallBack('分享失败')");
			} else if (curwebview.equals("2")) {
				secondWebView
						.loadUrl("javascript:shareSucccessCallBack('分享失败')");
			}
		}
	}

	private void tosharelog(String id, String shareto) {
		Request request = Request.requestsharelog(id, shareto);
		new HttpThread(request, this);
	}

	private ListAdapter getAdapter() {
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("image", images[i]);
			item.put("name", names[i]);
			data.add(item);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(YlbMessage_Act.this,
				data, R.layout.grid_item, new String[] { "image", "name" },
				new int[] { R.id.imageView, R.id.textView });
		return simpleAdapter;
	}

	public void openPopWindow() {
		initpopwindow();
		popupWindow.showAtLocation(myView, Gravity.BOTTOM, 0, 0);
	}

	private void Shareto(String content, String url, String img, String copy,
			String shareid) {
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		shareimg = img;
		try {
			sharecontent = URLDecoder.decode(content, "utf-8") + url
					+ URLDecoder.decode(copy, "utf-8");
			sharetitle = URLDecoder.decode(content, "utf-8");
			sharecopy = URLDecoder.decode(copy, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		shareurl = url;

		openPopWindow();
	}

	private void FenxiangEmailSms(String content, String url) {
		mController.setShareContent(content);
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		smsHandler.setTargetUrl(url);
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
		emailHandler.setTargetUrl(url);
	}

	private void Fenxiangqq(String title, String content, String url) {
		QQShareContent qqShareContent = new QQShareContent(new UMImage(
				YlbMessage_Act.this, shareimg));
		qqShareContent.setTitle(title);
		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(url);
		mController.setShareMedia(qqShareContent);
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(YlbMessage_Act.this,
				"1104815437", "9sS47vI7nCTIhXvR");
		qqSsoHandler.setTargetUrl(url);
		qqSsoHandler.addToSocialSDK();

	}

	private void FenxiangqqQz(String title, String content, String url) {
		QZoneShareContent qZoneShareContent = new QZoneShareContent(
				new UMImage(YlbMessage_Act.this, shareimg));
		qZoneShareContent.setTitle(title);

		qZoneShareContent.setShareContent(content);
		mController.setShareMedia(qZoneShareContent);
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				YlbMessage_Act.this, "1104815437", "9sS47vI7nCTIhXvR");
		qZoneSsoHandler.setTargetUrl(url);
		qZoneSsoHandler.addToSocialSDK();

	}

	private void FenxiangSina(String title, String content, String url) {
		SinaShareContent sinaShareContent = new SinaShareContent(new UMImage(
				YlbMessage_Act.this, shareimg));
		sinaShareContent.setShareContent(content);
		sinaShareContent.setTitle(title);
		sinaShareContent.setTargetUrl(url);
		mController.setShareMedia(sinaShareContent);
		SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
		sinaSsoHandler.setTargetUrl(url);

	}

	private void Fenxiangweixin(String title, String content, String url) {
		WeiXinShareContent weiXinShareContent = new WeiXinShareContent(
				new UMImage(YlbMessage_Act.this, shareimg));
		weiXinShareContent.setShareContent(content);
		weiXinShareContent.setTargetUrl(url);
		weiXinShareContent.setTitle(title);
		mController.setShareMedia(weiXinShareContent);
		String appID = "wx5f8d6e9fa8ae4fa7";
		String appSecret = "55b897d2af6dd94bdf1ab29b5c02725c";
		UMWXHandler wxHandler = new UMWXHandler(YlbMessage_Act.this, appID,
				appSecret);
		wxHandler.addToSocialSDK();
	}

	private void Fengwxcicle(String title, String content, String url) {
		String appID = "wx5f8d6e9fa8ae4fa7";
		String appSecret = "55b897d2af6dd94bdf1ab29b5c02725c";
		UMWXHandler wxCircleHandler = new UMWXHandler(YlbMessage_Act.this,
				appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.setTitle(title);
		wxCircleHandler.setTargetUrl(url);
		wxCircleHandler.addToSocialSDK();
		CircleShareContent circleShareContent = new CircleShareContent(
				new UMImage(YlbMessage_Act.this, shareimg));
		circleShareContent.setTitle(title);
		circleShareContent.setShareContent(content);
		circleShareContent.setTargetUrl(url);
		mController.setShareMedia(circleShareContent);

	}

	Handler requestHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			SharedPreferences userSharedPreferences;
			switch (msg.what) {
			case 0:
				Toast.makeText(YlbMessage_Act.this, errorString,
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(YlbMessage_Act.this, errorString,
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				userSharedPreferences = YlbMessage_Act.this
						.getSharedPreferences("user", Activity.MODE_PRIVATE);
				String sesseion_id = userSharedPreferences.getString(
						"session_id", null);
				String type_id = userSharedPreferences.getString("type_id",
						null);
				String user_id = userSharedPreferences.getString("user_id",
						null);
				if (curwebview.equals("1")) {
					firstwebView.loadUrl("javascript:UserInfoCallBack('"
							+ sesseion_id + "','" + user_id + "','" + type_id
							+ "');");
				} else if (curwebview.equals("2")) {
					secondWebView.loadUrl("javascript:UserInfoCallBack('"
							+ sesseion_id + "','" + user_id + "','" + type_id
							+ "');");
				}
				break;
			case 3:
				mDialog.dismiss();
				if (curwebview.equals("1")) {
					firstwebView.loadUrl("javascript:uploadCallBack('" + imgid
							+ "','" + imgurl + "')");
				} else if (curwebview.equals("2")) {
					secondWebView.loadUrl("javascript:uploadCallBack('" + imgid
							+ "','" + imgurl + "')");
				}
				break;
			case -1:
				mDialog.dismiss();
				Toast.makeText(YlbMessage_Act.this, errorString,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 128);
		intent.putExtra("outputY", 128);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, RESULT);
	}

	@Override
	public void doResponse(int reqID, String b) {
		if (reqID == Request.REQUEST_SHARELOG) {
			try {
				JSONObject json = new JSONObject(b);
				String msg = BaseHelper.getString(json, "msg");
				errorString = msg;
				int errid = json.getInt("errorid");
				if (errid == 0) {
					requestHandler.sendEmptyMessage(1);
				} else {
					requestHandler.sendEmptyMessage(-1);
				}
			} catch (Exception e) {
				errorString = "sharelog error" + e.toString();
				e.printStackTrace();
				requestHandler.sendEmptyMessage(-1);
			}
		}
		if (reqID == Request.REQUEST_LOGINBYTOKEN) {
			try {
				JSONObject json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				errorString = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					String session_id = BaseHelper
							.getString(json, "session_id");
					String user_id = BaseHelper.getString(json, "user_id");
					String type_id = BaseHelper.getString(json, "type_id");
					SharedPreferences userSharedPreferences = YlbMessage_Act.this
							.getSharedPreferences("user", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = userSharedPreferences
							.edit();
					editor.putString("session_id", session_id);
					editor.putString("user_id", user_id);
					editor.putString("type_id", type_id);
					editor.commit();
					requestHandler.sendEmptyMessage(2);
				} else {
					requestHandler.sendEmptyMessage(-1);
				}
			} catch (JSONException e) {
				errorString = e.toString();
				e.printStackTrace();
				requestHandler.sendEmptyMessage(-1);
			}
		}
		if (reqID == Request.REQUEST_IMG_STREAM) {
			try {
				JSONObject json = new JSONObject(b);
				errorString = "uploadimg:" + BaseHelper.getString(json, "msg");
				int errid = json.getInt("errorid");
				if (errid == 0) {
					imgurl = BaseHelper.getString(json, "url");
					imgid = BaseHelper.getString(json, "id");
					requestHandler.sendEmptyMessage(3);
				} else {
					requestHandler.sendEmptyMessage(-1);
				}
			} catch (JSONException e) {
				errorString = e.toString();
				e.printStackTrace();
				requestHandler.sendEmptyMessage(-1);
			}
		}
	}

	@Override
	public void doError(String s) {
		Log.d("", "fdsfs");
	}

	public void uploadimgresult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA) {
			if (bdcut.equals("2")) {
				startPhotoZoom(Uri.fromFile(tempFile));
			} else if (TextUtils.isEmpty(bdcut)) {
				Bitmap b = BitmapFactory.decodeFile(tempFile.getPath());
				if (b != null) {
					Log.e("bitmapmapmmmmmmmmmmmmmmmmm",
							"bitmap+" + b.toString());
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					b.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
					try {
						stream.flush();
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Log.e("EEEEEEEEEEEEEEEEEEEEEEEEEEEE", "hhhhhh+stream"
							+ stream);
					photo_stream = new String(Base64.encode(
							stream.toByteArray(), Base64.DEFAULT));
					uploadimgrequest(photo_stream, bdobject_type, bdobject_id,
							bdis_avatar, bdmark, bdcut);
				}
			}
		}
		if (requestCode == IMAGE) {
			if (data == null) {
				Toast.makeText(YlbMessage_Act.this, "从相册获取返回错误",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (bdcut.equals("2")) {
				startPhotoZoom(data.getData());
			} else if (TextUtils.isEmpty(bdcut)) {
				Bitmap bitmap = null;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(
							YlbMessage_Act.this.getContentResolver(),
							data.getData());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				try {
					stream.flush();
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				photo_stream = new String(Base64.encode(stream.toByteArray(),
						Base64.DEFAULT));
				uploadimgrequest(photo_stream, bdobject_type, bdobject_id,
						bdis_avatar, bdmark, bdcut);
			}
		}

		if (requestCode == RESULT) {
			if (data == null) {
				Toast.makeText(YlbMessage_Act.this, "裁剪已取消", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Bitmap bitmap = data.getExtras().getParcelable("data");// 获取bitmap
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			photo_stream = new String(Base64.encode(out.toByteArray(),
					Base64.DEFAULT));
			uploadimgrequest(photo_stream, bdobject_type, bdobject_id,
					bdis_avatar, bdmark, bdcut);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != MAPCODE) {
			uploadimgresult(requestCode, resultCode, data);
		} else if (requestCode == MAPCODE) {
			if (resultCode == 7) {
				lasturlString = data.getStringExtra("lasturl");
				bulididString = data.getStringExtra("bulidid");
				detail = "detail";
				mapdetail = "mapdetail";
				firstwebView.loadUrl(baseurlString + lasturlString);
				Log.e("yyyyyyyyyyyyyyyyyyyy", "zhixing..................");
				curwebview = "1";
			} else if (resultCode == 9) {
				Log.e("pppppppppppppppp", "just finish");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					YlbMessage_Act.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void showcurview(String urlString) {
		firstwebView.loadUrl(baseurlString + urlString);
		// firstwebView.loadUrl("http://192.168.5.109:14023/lxs/lxs_index.html");
	}
}
