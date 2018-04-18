package com.mrlou.activity;

import java.util.ArrayList;


import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.update.UpdateManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.util.User_info;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class AdActivity extends Activity implements HttpListener {

	private RelativeLayout mLayout;
	private TextView img_float;
	int num = 0;
	private final Timer timer = new Timer();
	private TimerTask task;
	private Handler handler;
	private UpdateManager updateManager;
	private ImageView img_back;
	private boolean flag = true;
	private ArrayList<String> str_link = new ArrayList<String>();
	private ArrayList<String> str_avatar = new ArrayList<String>();;
//	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
//	private ImageLoader imageLoader;
	private SharedPreferences sharedPreferences;
	private String session_id,user_id,type_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad);
		User_info.setAdd_v("1");
		System.out.println("~~~~~~"+User_info.getAdd_v());
		// 获取软件版本号，对应AndroidManifest.xml下android:versionName
		sharedPreferences = AdActivity.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		user_id = sharedPreferences.getString("user_id", "");
		type_id = sharedPreferences.getString("type_id", "");
		User_info.setSession_id(session_id);
		User_info.setUser_id(user_id);
		User_info.setType_id(type_id);
//		User_info.setFlag(false);
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(AdActivity.this));
//		options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
//				.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
//				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
//				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
//				.build();
		String versionCode = "0";
		try {
			versionCode = (AdActivity.this.getPackageManager().getPackageInfo(
					"com.mrlou.mrlou", 0).versionName);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		select_update(versionCode);
		// findView();
		// timer.schedule(task, 0, 1500);
	}

	private void findView() {
		// TODO Auto-generated method stub
		// updateManager = new UpdateManager(AdActivity.this,flag);
		// updateManager.showNoticeDialog();
		// if (!false) {
		// AdActivity.this.finish();
		// }
		img_back = (ImageView) findViewById(R.id.img_back);
		mLayout = (RelativeLayout) findViewById(R.id.rel_ad);
		mLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!str_link.get(num).equals("")) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(str_link.get(num));
					intent.setData(content_url);
					timer.cancel();
					task.cancel();
					startActivity(intent);
					
//					Intent intents = new Intent(AdActivity.this,
//							MainFragmentActivity.class);
//					intents.putExtra("source", "init");
//					startActivity(intents);
//					finish();
				}
			}
		});
		img_float = (TextView) findViewById(R.id.img_float);
		img_float.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timer.cancel();
				task.cancel();
				Intent intent = new Intent(AdActivity.this,
						MainFragmentActivity.class);
				intent.putExtra("source", "init");
				startActivity(intent);
				finish();
			}
		});
		// ImageLoader.getInstance().init(
		// ImageLoaderConfiguration.createDefault(AdActivity.this));
		handler = new Handler() {
			@SuppressLint("NewApi")
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 1) {
					// mLayout.setBackgroundResource(images[num++]);
					if (num < str_avatar.size()) {
						ImageLoader.getInstance().displayImage(
								str_avatar.get(num), img_back);
					}else{
						timer.cancel();
						task.cancel();
						Intent intent = new Intent(AdActivity.this,
								MainFragmentActivity.class);
						intent.putExtra("source", "init");
						startActivity(intent);
						finish();
					}
					num++;
				}
			}
		};
		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};

	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				updateManager = new UpdateManager(AdActivity.this, flag);
				updateManager.showNoticeDialog();
				//select_advertise("");
				break;
			case 2:
				select_advertise("");
				break;
			case 3:
				findView();
				timer.schedule(task, 0, 1500);
				break;
			case 4:
				Toast.makeText(AdActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Intent intent = new Intent(AdActivity.this,
						MainFragmentActivity.class);
				intent.putExtra("source", "init");
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	public void select_update(String version) {
		Request request = Request.requestgetversion(version);
		new HttpThread(request, this);
	}

	public void select_advertise(String ad) {
		Request request = Request.requestadvertise(ad);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(2);
				} else {
					newHandler.sendEmptyMessage(1);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (reqID == Request.REQUEST_ECONOMIC_COMPANY) {
			JSONObject json;
			JSONArray joArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					int lent = joArray.length();
					for (int i = 0; i < lent; i++) {
						json = joArray.getJSONObject(i);
						str_avatar.add(json.getString("avatar"));
						str_link.add(json.getString("link"));
					}
					newHandler.sendEmptyMessage(3);
				} else {
					newHandler.sendEmptyMessage(99);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}
}
