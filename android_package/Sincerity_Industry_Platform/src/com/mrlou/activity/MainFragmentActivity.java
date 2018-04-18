package com.mrlou.activity;

import java.io.File;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.mrlou.dialog.AlertDialogs;
import com.mrlou.economic.Economic_mianAct;
import com.mrlou.favorites.Collection_Activity;
import com.mrlou.fragment.Fragment2;
import com.mrlou.fragment.Fragment4;
import com.mrlou.fragment.MyFragment;
import com.mrlou.h5.Map_Act;
import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_Main;
import com.mrlou.mine.About_MrlouAct;
import com.mrlou.mine.Mine_Activity;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.Constant;
import com.mrlou.util.CopyTask;
import com.mrlou.util.DownLoaderTask;
import com.mrlou.util.HttpGetThread;
import com.mrlou.util.HttpThread;
import com.mrlou.util.JieYaTask;
import com.mrlou.util.Request;
import com.mrlou.util.User_info;
import com.mrlou.view.KeyboardLayout;
import com.mrlou.view.KeyboardLayout.onKybdsChangeListener;
import com.mrlou.yi.Yi_Activity;
import com.umeng.socialize.controller.UMSocialService;

@TargetApi(Build.VERSION_CODES.KITKAT)
@SuppressLint("InlinedApi")
public class MainFragmentActivity extends FragmentActivity implements
		HttpListener {
	public String version = "414", errorString, userId = "001", orderId,
			commodity_name, commodity_describe, commodity_price, testString,
			tempVersion = "414", mrString, nameString = "mrlou.414.zip",
			uploadimg = "", resource = "ssss", isfirt = "";
	RelativeLayout mainLayout;
	private Context context = this;
	UMSocialService mController;
	private MyFragment fragment0, fragment1, fragment3;
	private Fragment2 fragment2;
	private Fragment4 fragment4;
	private ImageView ylb_index, fangyuan_index, lxs_index, kehu_index,
			mine_index;
	private FragmentManager fragmentManager;
	private LinearLayout bottomLayout;
	private int curfra = 2;
	private String source;
	KeyboardLayout keyboardLayout;
	FrameLayout layout;
	private FragmentTransaction transaction;
	private Mine_Activity mine_Activity;
	private Economic_mianAct economic_mianAct;
	private Yi_Activity yi_Activity;
	private Collection_Activity collection_Activity;
	private Map_Act map_Act;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private ArrayList<String> str_id, str_name, str_phone, str_company,
			str_work_age, str_avatar, str_region_total, str_rname, str_rid,
			str_company_status, str_all_toal;
	private boolean press_flag = false, press_message = false,
			press_perinfo = false;
	private SharedPreferences sharedPreferences;
	private String session_id;
	private ArrayList<String> str_content, str_time, str_createtime;
	private String phone_number, email, en_username, cn_username, company,
			company_id, building_a, building_b, avatar, avatar_id, type, intro,
			work_start, region_id, user_id, region_id_real, post_card_id,
			post_card, wechat_qr_id, wechat_qr, wechat, add_v, type_id,
			member_type, service_type,office_building,office_building_id,good_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainfra);
		com.umeng.socialize.utils.Log.LOG = true;
		baidugengxin();
		sharedPreferences = MainFragmentActivity.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		user_id = sharedPreferences.getString("user_id", "");
		mrString = context.getExternalFilesDir(null).getPath();
		fragmentManager = getSupportFragmentManager();
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, Constant.api_key);
		getDate();
		initviews();
		isfirstEnter();
		ToupdateNewest();
		testbj();
		setLineneser();
	}

	private void getDate() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
		str_name = new ArrayList<String>();
		str_phone = new ArrayList<String>();
		str_company = new ArrayList<String>();
		str_work_age = new ArrayList<String>();
		str_avatar = new ArrayList<String>();
		str_region_total = new ArrayList<String>();
		str_rname = new ArrayList<String>();
		str_rid = new ArrayList<String>();
		str_company_status = new ArrayList<String>();
		str_all_toal = new ArrayList<String>();
		str_createtime = new ArrayList<String>();
		str_time = new ArrayList<String>();
		str_content = new ArrayList<String>();
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	private void isfirstEnter() {
		SharedPreferences sharedPreferences = getSharedPreferences(
				"firstenter", Activity.MODE_PRIVATE);
		boolean loadfirst = sharedPreferences.getBoolean("loadfirst", true);
		if (loadfirst) {
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("loadfirst", false);
			editor.commit();
			isfirt = "true";
		} else {
			isfirt = "false";
		}
	}

	// display()
	private void display() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				bottomLayout.setVisibility(View.VISIBLE);
				keyboardLayout.setHsmLayout(false);
				Log.e("+++++++++++++执行显示+++++++++++", "显示中。。。。。。。");
			}
		}, 50);
	}

	// display()
	private void hide() {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				bottomLayout.setVisibility(View.GONE);
				keyboardLayout.setHsmLayout(true);
				Log.e("+++++++++++++执行隐藏+++++++++++", "隐藏中。。。。。。。");

			}
		});

	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("InlinedApi")
	private void testbj() {
		// if(!isTablet(context)){
		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		// }
	}

	private void setLineneser() {

		keyboardLayout.setOnkbdStateListener(new onKybdsChangeListener() {

			public void onKeyBoardStateChange(int state) {
				switch (state) {
				case KeyboardLayout.KEYBOARD_STATE_HIDE:
					Log.e("+++++++++++++++执行这里", "zhixingkeybrad11111");
					display();
					break;
				case KeyboardLayout.KEYBOARD_STATE_SHOW:
					Log.e("+++++++++++++++执行这里", "zhixingkeybrad2222");
					hide();
					break;
				}
			}
		});

	}

	private void baidugengxin() {
		BDAutoUpdateSDK.uiUpdateAction(this, new MyUICheckUpdateCallback());
	}

	private class MyUICheckUpdateCallback implements UICheckUpdateCallback {

		@Override
		public void onCheckComplete() {
		}

	}

	private void ToupdateNewest() {
		SharedPreferences sharedPreferences = getSharedPreferences("test",
				Activity.MODE_PRIVATE);
		if (isNetworkConnected()) {
			version = sharedPreferences.getString("version", "427");
			Request request = Request.requestNews(version);
			new HttpThread(request, this);
		} else {
			if (isfirt.equals("true")) {
				docopyfile("mrlou.414.zip", mrString);
			} else {
				setTabSelection(2);
			}
		}
	}

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
	// <----------最新
	private void initviews() {
		// 弹出请求对话框
		pDialog = new SweetAlertDialog(MainFragmentActivity.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(MainFragmentActivity.this, pDialog);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int phonewith = metrics.widthPixels;
		source = getIntent().getStringExtra("source");
		mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
		bottomLayout = (LinearLayout) findViewById(R.id.bottom);
		keyboardLayout = (KeyboardLayout) findViewById(R.id.keylayout);
		layout = (FrameLayout) findViewById(R.id.layout);
		ylb_index = (ImageView) findViewById(R.id.ylb_index);
		fangyuan_index = (ImageView) findViewById(R.id.fangyuan_index);
		lxs_index = (ImageView) findViewById(R.id.lxs_index);
		kehu_index = (ImageView) findViewById(R.id.kehu_index);
		mine_index = (ImageView) findViewById(R.id.mine_index);
		LayoutParams params = new LayoutParams(phonewith * 192 / 960,
				phonewith * 147 / 960);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
				phonewith, phonewith * 147 / 960);
		params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		bottomLayout.setLayoutParams(params2);
		bottomLayout.setBackgroundColor(Color.parseColor("#ffffff"));
		ylb_index.setLayoutParams(params);
		fangyuan_index.setLayoutParams(params);
		lxs_index.setLayoutParams(params);
		kehu_index.setLayoutParams(params);
		mine_index.setLayoutParams(params);
		ylb_index.setOnClickListener(new MyBottomImageViewClickListener());
		fangyuan_index.setOnClickListener(new MyBottomImageViewClickListener());
		lxs_index.setOnClickListener(new MyBottomImageViewClickListener());
		kehu_index.setOnClickListener(new MyBottomImageViewClickListener());
		mine_index.setOnClickListener(new MyBottomImageViewClickListener());
	}

	private void showDownLoadDialog() {
		doDownLoadWork();
	}

	private void doDownLoadWork() {
		DownLoaderTask task = new DownLoaderTask(testString, mrString,
				MainFragmentActivity.this);
		task.execute();
	}

	public void showUnzipDialog() {

		newdozip();

	}

	private void docopyfile(String name, String traget) {
		CopyTask copyTask = new CopyTask(MainFragmentActivity.this, name,
				traget);
		copyTask.execute();
	}

	public void newdozip() {
		JieYaTask task = new JieYaTask(mrString + "/" + nameString, mrString
				+ "/bulidersir", MainFragmentActivity.this);
		task.execute();
	}

	public void dozip(int result) {
		if (result == 7) {
			deleteFile(nameString, mrString);
			if (isfirt.equals("true")) {
				docopyfile("SourceHanSansCN-Normal.ttf", mrString
						+ "/bulidersir");
			}
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"test", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString("version", tempVersion);
			editor.commit();

		} else {
			Log.e("++++++jieya+++failed", "failed");
		}
	}

	public void docopy(String result, String filename) {
		if (result.equals("success")) {
			if (filename.equals("mrlou.414.zip")) {
				newdozip();
			}
			if (filename.equals("SourceHanSansCN-Normal.ttf")) {
				setTabSelection(2);
			}
		} else {
			if (filename.equals("SourceHanSansCN-Normal.ttf")) {
				setTabSelection(2);
			}
			Log.e("+++++++++++++copyfailed+++++++++", "copyfailed");
		}
	}

	public void deleteFile(String filename, String filepath) {
		File outFile = new File(filepath, filename);
		if (outFile.exists()) {
			outFile.delete();
		}
	}

	private class MyBottomImageViewClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int t = v.getId();
			switch (t) {
			case R.id.ylb_index:
//				if (!press_message) {
//					User_info.setFlag(false);
//					dialog.dialog();
//					select_area(User_info.getSession_id());
//				} else {
					setTabSelection(0);
//				}

				break;
			case R.id.fangyuan_index:
				setTabSelection(1);
				break;
			case R.id.lxs_index:
				setTabSelection(2);
				break;
			case R.id.kehu_index:
				if (!press_flag) {
					dialog.dialog();
					select_agent();
				} else {
					setTabSelection(3);
				}
				break;
			case R.id.mine_index:
				if (!press_perinfo) {
					dialog.dialog();
					select_perinfo(User_info.getSession_id(),
							User_info.getUser_id());
				} else {
					setTabSelection(4);
				}
				break;
			default:
				break;
			}
		}
	}

	private String Fanurl(String version, int index) {
		String urlString = "";
		switch (index) {
		case 0:
			urlString = "file://" + mrString + "/bulidersir"
					+ "/mine_index.html";
			break;
		case 1:
			urlString = "file://" + mrString + "/bulidersir"
					+ "/fangyuan_index.html";
			break;
		case 2:
			urlString = "file://" + mrString + "/bulidersir"
					+ "/lxs_index.html";
			break;
		case 3:
			urlString = "file://" + mrString + "/bulidersir"
					+ "/kehu_index.html";
			break;
		case 4:
			urlString = "file://" + mrString + "/bulidersir"
					+ "/mine_index.html";
			break;
		default:
			break;
		}
		return urlString;
	}

	public void select_agent() {
		Request request = Request.requesteconomicmain("310115");
		new HttpThread(request, this);
	}

	public void select_area(String session_id) {
		Request request = Request.requestmessage_main(session_id);
		new HttpThread(request, this);
	}

	public void select_perinfo(String session_id, String user_id) {
		Request request = Request.requestgetperinfo(session_id, user_id);
		new HttpGetThread(request, this);
	}

	private void setTabSelection(int index) {
		if (!isTablet(context)) {
			Rect rect = new Rect();
			getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);//取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
			int top = rect.top;//状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int phonewith = metrics.widthPixels;
			int phoneheight = metrics.heightPixels;
			RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
					phonewith, phoneheight - phonewith * 147 / 960 - top);
			layout.setLayoutParams(params3);
			Log.e("mainheight++++++++++++++++", "phoneheight" + phoneheight
					+ "状态栏高度" + top);
		}
		changeImage(index);
		transaction = fragmentManager.beginTransaction();
		// transaction.setCustomAnimations(R.anim.fragment_left_enter,
		// R.anim.fragment_right_out);
		hideFragments(transaction);
		switch (index) {
		case 0:
			if (curfra == index & yi_Activity != null) {
				return;
			}
			curfra = 0;
			if (yi_Activity == null) {
				yi_Activity = new Yi_Activity();
//				Bundle data = new Bundle();
//				data.putStringArrayList("content", str_content);
//				data.putStringArrayList("createtime", str_createtime);
//				data.putStringArrayList("time", str_time);
//				yi_Activity.setArguments(data);
				transaction.add(R.id.layout, yi_Activity);
			} else {
				transaction.show(yi_Activity);
			}
			transaction.commit();
			break;
		case 1:
			if (curfra == index & map_Act != null) {
				return;
			}
			curfra = 1;
			if (map_Act == null) {
				map_Act = new Map_Act();
				transaction.add(R.id.layout, map_Act);
			} else {
				transaction.show(map_Act);
			}
			transaction.commit();
			break;
		case 2:
			if (curfra == index & fragment2 != null) {
				return;
			}
			curfra = 2;
			if (fragment2 == null) {
				fragment2 = new Fragment2();
				Bundle data = new Bundle();
				String urlString;
				String frsource;
				if (source.equals("push2")
						&& !TextUtils
								.isEmpty(getIntent().getStringExtra("url"))) {
					urlString = "file://" + mrString + "/bulidersir/"
							+ getIntent().getStringExtra("url");
					frsource = "gg";
				} else {
					urlString = Fanurl(version, 2);
					frsource = "fragment2";
				}
				data.putString("frsource", frsource);
				data.putString("texturl", urlString);
				data.putString("baseurl", "file://" + mrString + "/bulidersir/");
				fragment2.setArguments(data);
				transaction.add(R.id.layout, fragment2);
			} else {
				String urlString;
				if (source.equals("push2")
						&& !TextUtils
								.isEmpty(getIntent().getStringExtra("url"))) {
					urlString = getIntent().getStringExtra("url");
					fragment2.showcurview(urlString);
				}
				transaction.show(fragment2);
			}
			System.out.println("===="+User_info.getAdd_v());
			if (User_info.getAdd_v()!=null) {
				if (User_info.getAdd_v().equals("0")) {
				AlertDialogs dialogs=new AlertDialogs();
				dialogs.alertDialogs(MainFragmentActivity.this,"您尚未认证会员,进入易楼精英汇认证","现在认证");
			}
			}

			transaction.commit();
			break;

		case 3:
			if (curfra == index & economic_mianAct != null) {
				return;
			}
			curfra = 3;
			if (economic_mianAct == null) {
				economic_mianAct = new Economic_mianAct();
				Bundle data = new Bundle();
				data.putStringArrayList("id", str_id);
				data.putStringArrayList("name", str_name);
				data.putStringArrayList("phone", str_phone);
				data.putStringArrayList("company", str_company);
				data.putStringArrayList("region_total", str_region_total);
				data.putStringArrayList("all_total", str_all_toal);
				data.putStringArrayList("rname", str_rname);
				data.putStringArrayList("avatar", str_avatar);
				data.putStringArrayList("company_status", str_company_status);
				data.putStringArrayList("rid", str_rid);
				data.putStringArrayList("work_age", str_work_age);
				economic_mianAct.setArguments(data);
				transaction.add(R.id.layout, economic_mianAct);
			} else {
				transaction.show(economic_mianAct);
			}
			transaction.commit();
			break;
		case 4:
			if (curfra == index & mine_Activity != null) {
				return;
			}
			curfra = 4;
			if (mine_Activity == null) {
				mine_Activity = new Mine_Activity();
				Bundle data = new Bundle();
				data.putString("phone_number", phone_number);
				data.putString("email", email);
				data.putString("en_username", en_username);
				data.putString("cn_username", cn_username);
				data.putString("company", company);
				data.putString("building_a", building_a);
				data.putString("building_b", building_b);
				data.putString("avatar", avatar);
				data.putString("avatar_id", avatar_id);
				data.putString("type", type);
				data.putString("intro", intro);
				data.putString("work_start", work_start);
				data.putString("region_id", region_id);
				data.putString("v", add_v);
				data.putString("service_type", service_type);
				data.putString("region_id_real", region_id_real);
				data.putString("post_card_id", post_card_id);
				data.putString("wechat_qr_id", wechat_qr_id);
				data.putString("post_card", post_card);
				data.putString("wechat_qr", wechat_qr);
				data.putString("company_id", company_id);
				data.putString("wechat", wechat);
				data.putString("member_type", member_type);
				data.putString("office_building", office_building);
				data.putString("office_building_id", office_building_id);
				data.putString("good_type", good_type);
				mine_Activity.setArguments(data);
				transaction.add(R.id.layout, mine_Activity);
			} else {
				transaction.show(mine_Activity);
			}
			transaction.commit();
			break;
		default:
			break;
		}
		// transaction.commitAllowingStateLoss();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (yi_Activity != null) {
			transaction.hide(yi_Activity);
		}
		if (fragment0 != null) {
			transaction.hide(fragment0);
		}
		if (fragment1 != null) {
			transaction.hide(fragment1);
		}
		if (fragment2 != null) {
			transaction.hide(fragment2);
		}
		if (economic_mianAct != null) {
			transaction.hide(economic_mianAct);
		}
		if (fragment3 != null) {
			transaction.hide(fragment3);
		}
		if (fragment4 != null) {
			transaction.hide(fragment4);
		}
		if (mine_Activity != null) {
			transaction.hide(mine_Activity);
		}
		if (map_Act != null) {
			transaction.hide(map_Act);
		}
	}

	private void changeImage(int t) {
		switch (t) {
		case 0:
			ylb_index.setImageResource(R.drawable.yi_select);
			fangyuan_index.setImageResource(R.drawable.collect_unselect);
			lxs_index.setImageResource(R.drawable.lxs_unselect);
			kehu_index.setImageResource(R.drawable.five_agent_unselect);
			mine_index.setImageResource(R.drawable.mine_unselect);
			break;
		case 1:
			ylb_index.setImageResource(R.drawable.yi_unselect);
			fangyuan_index.setImageResource(R.drawable.collect_select);
			lxs_index.setImageResource(R.drawable.lxs_unselect);
			kehu_index.setImageResource(R.drawable.five_agent_unselect);
			mine_index.setImageResource(R.drawable.mine_unselect);
			break;
		case 2:
			ylb_index.setImageResource(R.drawable.yi_unselect);
			fangyuan_index.setImageResource(R.drawable.collect_unselect);
			lxs_index.setImageResource(R.drawable.lxs_selected);
			kehu_index.setImageResource(R.drawable.five_agent_unselect);
			mine_index.setImageResource(R.drawable.mine_unselect);
			break;
		case 3:
			ylb_index.setImageResource(R.drawable.yi_unselect);
			fangyuan_index.setImageResource(R.drawable.collect_unselect);
			lxs_index.setImageResource(R.drawable.lxs_unselect);
			kehu_index.setImageResource(R.drawable.five_agent_select);
			mine_index.setImageResource(R.drawable.mine_unselect);
			break;
		case 4:
			ylb_index.setImageResource(R.drawable.yi_unselect);
			fangyuan_index.setImageResource(R.drawable.collect_unselect);
			lxs_index.setImageResource(R.drawable.lxs_unselect);
			kehu_index.setImageResource(R.drawable.five_agent_unselect);
			mine_index.setImageResource(R.drawable.mine_select);
			break;
		default:
			break;
		}
	}

	@Override
	public void doResponse(int reqID, String b) {

		if (reqID == Request.REQUEST_NEW) {
			try {
				JSONObject json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				String msg = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					String download = BaseHelper
							.getString(json, "download_url");
					tempVersion = BaseHelper.getString(json, "version");
					testString = download;
					String[] testname = testString.substring(7).split("/");
					int t = testname.length;
					if (t == 0) {

					} else {
						nameString = testname[t - 1];
					}
					newHandler.sendEmptyMessage(0);
				} else {
					testString = msg;
					newHandler.sendEmptyMessage(1);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json;
			JSONArray joArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					int lenth = joArray.length();
					for (int i = 0; i < lenth; i++) {
						json = joArray.getJSONObject(i);
						str_id.add(json.getString("id"));
						str_name.add(json.getString("cn_username")
								+ json.getString("en_username"));
						str_avatar.add(json.getString("avatar"));
						str_company.add(json.getString("company"));
						str_phone.add(json.getString("phone"));
						str_region_total.add(json.getString("region_total"));
						str_all_toal.add(json.getString("all_total"));
						str_rid.add(json.getString("rid"));
						str_rname.add(json.getString("rname"));
						str_work_age.add(json.getString("work_age"));
						str_company_status
								.add(json.getString("company_status"));
					}
					newHandler.sendEmptyMessage(2);
				} else {
					dialog.dismiss();
					// Toast.makeText(MainFragmentActivity.this, msg,
					// Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (reqID == Request.REQUEST_ECONOMIC_COMPANY) {
			JSONObject json;
			JSONArray joArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					int length = joArray.length();
					for (int i = 0; i < length; i++) {
						json = joArray.getJSONObject(i);
						str_content.add(json.getString("content"));
						str_createtime.add(json.getString("createtime"));
						str_time.add(json.getString("time"));
					}
					newHandler.sendEmptyMessage(3);
				} else {
					dialog.dismiss();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (reqID == Request.REQUEST_ECONOMIC_COLLECT) {
			JSONObject json, json2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("user");
					phone_number = json2.getString("phone_number");
					email = json2.getString("email");
					en_username = json2.getString("en_username");
					cn_username = json2.getString("cn_username");
					company = json2.getString("company");
					avatar = json2.getString("avatar");
					avatar_id = json2.getString("avatar_id");
					type = json2.getString("type");
					wechat_qr_id = json2.getString("wechat_qr_id");
					post_card_id = json2.getString("post_card_id");
					post_card = json2.getString("post_card");
					wechat_qr = json2.getString("wechat_qr");
					wechat = json2.getString("wechat");
					intro = json2.getString("intro");
					work_start = json2.getString("start_work");
					region_id = json2.getString("region_id");
					region_id_real = json2.getString("region_id_real");
					add_v = json2.getString("v");
					building_a = json2.getString("building_a");
					building_b = json2.getString("building_b");
					member_type = json2.getString("member_type");
					service_type = json2.getString("service_type");
					company_id = json2.getString("company_id");
					office_building_id=json2.getString("office_building_id");
					office_building=json2.getString("office_building");
					good_type=json2.getString("good_type");
					System.out.println("--good_type--"+good_type+"--office_building--"+office_building);
					newHandler.sendEmptyMessage(4);
				} else {
					dialog.dismiss();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showDownLoadDialog();
				break;
			case 1:
				if (isfirt.equals("true")) {
					docopyfile("mrlou.414.zip", mrString);
				} else {
					setTabSelection(2);
				}
				break;
			case 2:
				dialog.dismiss();
				setTabSelection(3);
				press_flag = true;
				break;
			case 3:
				dialog.dismiss();
				setTabSelection(0);
				press_message = true;
				break;
			case 4:
				dialog.dismiss();
				setTabSelection(4);
				press_perinfo = true;
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "网络连接失败，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};
	private long startTime = 0l;

	@Override
	public void doError(String s) {
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}

	public int getcurfra() {
		return curfra;
	}

	@Override
	protected void onResume() {
		System.out.println("--onResume--");
		super.onResume();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		System.out.println("--onRestart--");
		super.onRestart();
	}

	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		sharedPreferences = MainFragmentActivity.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		user_id = sharedPreferences.getString("user_id", "");
		type_id = sharedPreferences.getString("type_id", "");
		User_info.setSession_id(session_id);
		User_info.setUser_id(user_id);
		User_info.setType_id(type_id);
		super.onResumeFragments();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		long tempTime = System.currentTimeMillis();
		if (tempTime - startTime <= 2000) {
			finish();
			System.exit(0);
		} else {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			startTime = tempTime;
		}
	}
}
