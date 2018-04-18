package com.mrlou.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_Main;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.Constant;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.util.User_info;
import com.mrlou.view.ActionSheetDialog;
import com.mrlou.view.ActionSheetDialog.OnSheetItemClickListener;
import com.mrlou.view.ActionSheetDialog.SheetItemColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewRegisterActivity extends Activity implements HttpListener {
	private Context context;
	private WebView webView;
	Handler handle = new Handler();
	private String errorString;
	private String bdobject_type = "sss", bdobject_id = "sss",
			bdis_avatar = "sss", bdmark = "sss", bdcut = "sss", cur = "sss",
			prams = "sss", lasturl, baseurl, photo_stream = "sss", imgurl,
			imgid;
	private static final int CAMERA = 3;
	private static final int IMAGE = 2;
	private static final int RESULT = 4;
	ProgressDialog mDialog;
	public File tempFile;
	private ActionSheetDialog xjxcdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newopen);
		context = this;
		initdialog();
		getfromdata();
		initviews();
	}

	private void getfromdata() {
		lasturl = getIntent().getStringExtra("lasturl");
		baseurl = getIntent().getStringExtra("baseurl");
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "SdCardPath", "NewApi" })
	private void initviews() {
		webView = (WebView) findViewById(R.id.loginWebView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "mrlou");
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new ChromeClient());
		webView.loadUrl(baseurl + lasturl);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			webView.getSettings().setDatabasePath(
					"/data/data/" + webView.getContext().getPackageName()
							+ "/databases/");
		}
		if (Build.VERSION.SDK_INT >= 16) {
			webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		}
		// int screenDensity = getResources().getDisplayMetrics().densityDpi ;
		// WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM
		// ;
		// switch (screenDensity){
		// case DisplayMetrics.DENSITY_LOW :
		// zoomDensity = WebSettings.ZoomDensity.CLOSE;
		// break;
		// case DisplayMetrics.DENSITY_MEDIUM:
		// zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		// break;
		// case DisplayMetrics.DENSITY_HIGH:
		// zoomDensity = WebSettings.ZoomDensity.FAR;
		// break ;
		// }
		// webView.getSettings().setDefaultZoom(zoomDensity);

	}

	class MyWebViewClient extends WebViewClient {
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			view.loadUrl("file:///android_asset/errorpage/loaderror.html");
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("mailto:") || url.startsWith("geo:")
					|| url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
			} else {
				view.loadUrl(url);
			}
			return true;
		}

		public void onPageFinished(WebView view, String url) {
			if (cur.equals("href")) {
				if (!prams.equals(" ")) {
					webView.loadUrl("javascript:byParams('" + prams + "')");
					cur = "sss";
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
		public void href(final String prurl, final String param,
				final String hide) {
			handle.post(new Runnable() {
				@Override
				public void run() {
					webView.loadUrl(baseurl + prurl);
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
		public void back() {
			handle.post(new Runnable() {
				@Override
				public void run() {
					if (webView.canGoBack()) {
						webView.goBack();
					} else {
						finish();
					}
				}
			});
		}

		@JavascriptInterface
		public void closelogin() {
			handle.post(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			});
		}

		@JavascriptInterface
		public void loginbytoken(final String token) {
			System.out.println("====token===="+token);
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
		public void iphoto(final String object_type, final String object_id,
				final String is_avatar, final String mark, final String cut) {
			handle.post(new Runnable() {
				@Override
				public void run() {
					try {
						bdobject_type = URLDecoder.decode(object_type, "utf-8");
						bdobject_id = URLDecoder.decode(object_id, "utf-8");
						bdis_avatar = URLDecoder.decode(is_avatar, "utf-8");
						bdmark = URLDecoder.decode(mark, "utf-8");
						bdcut = URLDecoder.decode(cut, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					CreateDialog();
				}
			});
		}
	}

	// 注册
	public void loginrequest(String token, String device_id, String sys_agent) {
		SharedPreferences preferences = getSharedPreferences("channelid",
				Context.MODE_PRIVATE);
		String chaString = preferences.getString("channelid", " ");
		Request loginRequest = Request.requestloginbytoken(token, device_id,
				sys_agent, chaString);
		new HttpThread(loginRequest, this);
	}

	// 注册获取guuid
	public String getuuid() {
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	// <----拍照，相册选择图片解决方法
	public void uploadimgresult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAMERA) {// 拍照
			if (bdcut.equals("2")) {
				startPhotoZoom(Uri.fromFile(tempFile));
			} else if (TextUtils.isEmpty(bdcut)) {
				Bitmap b = BitmapFactory.decodeFile(tempFile.getPath());
				if (b != null) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					stream = yasuo(b);
					try {
						stream.flush();
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					photo_stream = new String(Base64.encode(
							stream.toByteArray(), Base64.DEFAULT)); // 进行Base64编码
					uploadimgrequest(photo_stream, bdobject_type, bdobject_id,
							bdis_avatar, bdmark, bdcut);
				}
			}

		}
		if (requestCode == IMAGE) {// 相册
			if (data == null) {
				Toast.makeText(NewRegisterActivity.this, "从相册获取返回错误",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (bdcut.equals("2")) {
				startPhotoZoom(data.getData());
			} else if (TextUtils.isEmpty(bdcut)) {
				Bitmap bitmap = null;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(
							NewRegisterActivity.this.getContentResolver(),
							data.getData());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				stream = yasuo(bitmap);
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
				Toast.makeText(NewRegisterActivity.this, "裁剪已取消",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Bitmap bitmap = data.getExtras().getParcelable("data");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out = yasuo(bitmap);
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
		uploadimgresult(requestCode, resultCode, data);
	}

	@Override
	public void doResponse(int reqID, String b) {
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
					String add_v=BaseHelper.getString(json, "v");
					SharedPreferences userSharedPreferences = getSharedPreferences(
							"user", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = userSharedPreferences
							.edit();
					editor.putString("session_id", session_id);
					editor.putString("user_id", user_id);
					editor.putString("type_id", type_id);
					editor.commit();
					User_info.setSession_id(session_id);
					User_info.setUser_id(user_id);
					User_info.setType_id(type_id);
					User_info.setAdd_v(add_v);
//					Message_Main.setFlag(true);
					requestHandler.sendEmptyMessage(1);
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

	@SuppressLint("HandlerLeak")
	Handler requestHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			SharedPreferences userSharedPreferences;
			switch (msg.what) {
			case 1:
				userSharedPreferences = getSharedPreferences("user",
						Activity.MODE_PRIVATE);
				String sesseion_id = userSharedPreferences.getString(
						"session_id", null);
				String type_id = userSharedPreferences.getString("type_id",
						null);
				String user_id = userSharedPreferences.getString("user_id",
						null);
				webView.loadUrl("javascript:UserInfoCallBack('" + sesseion_id
						+ "','" + user_id + "','" + type_id + "');");
				break;
			case -1:
				Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
				break;
			case 3:
				mDialog.dismiss();
				webView.loadUrl("javascript:uploadCallBack('" + imgid + "','"
						+ imgurl + "')");
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void doError(String s) {

	}

	private void CreateDialog() {
		xjxcdialog = new ActionSheetDialog(context)
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
									Toast.makeText(context, "存储空间未就绪！",
											Toast.LENGTH_SHORT).show();
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

	public ByteArrayOutputStream yasuo(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 90;
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 500) {
			baos.reset();
			options -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		return baos;
	}

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

	private void initdialog() {
		mDialog = new ProgressDialog(NewRegisterActivity.this);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("上传中...");
		mDialog.setIndeterminate(false);
		mDialog.setCancelable(false);
	}

	public void uploadimgrequest(String photo, String object_type,
			String object_id, String is_avatar, String mark, String cut) {
		mDialog.show();
		Request request = Request.requestImg(photo, object_type, object_id,
				is_avatar, mark);
		new HttpThread(request, this);
	}

	// 返回键[]
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
