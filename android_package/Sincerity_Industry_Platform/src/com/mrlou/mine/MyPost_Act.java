package com.mrlou.mine;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.mapcore.util.r;
import com.mrlou.adapter.History_Adapter;
import com.mrlou.adapter.MyPost_Adapter;
import com.mrlou.h5.YlbMessage_Act;
import com.mrlou.h5.Ylb_act;
import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_SingleBuilderAct;
import com.mrlou.message.Message_ValuesHourseAct;
import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

import android.R.array;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MyPost_Act extends Activity implements HttpListener {

	private LinearLayout lay_min_history_add, lay_mypost_back, lay_mypost_main,
			lay_mime_mypost_none,lay_mime_mypost_none2, lay_min_mypost_add;
	private Button bt_check, bt_uncheck;
	private MyPost_Adapter adapter;
	private ArrayList<String> str_id, str_user_id, str_user_avatar,
			str_user_username, str_user_en_username, str_subject, str_title,
			str_reply_total, str_collect_total, str_status, str_create_time,
			str_expire_time, str_flag;
	private ArrayList<String> str_id1, str_user_id1, str_user_avatar1,
			str_user_username1, str_user_en_username1, str_subject1,
			str_title1, str_reply_total1, str_collect_total1, str_status1,
			str_create_time1, str_expire_time1, str_flag1;
	private ArrayList<String> str_id2, str_user_id2, str_user_avatar2,
			str_user_username2, str_user_en_username2, str_subject2,
			str_title2, str_reply_total2, str_collect_total2, str_status2,
			str_create_time2, str_expire_time2, str_flag2;
	private ListView lv_history;
	private String total_post, total_mypost, session_id;
	private SharedPreferences sharedPreferences;
	private String url, url2,mrString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_mypost);
		sharedPreferences = MyPost_Act.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		prapare();
		select_cjinfomain(session_id);
		// findView();
	}

	private void prapare() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
		str_user_id = new ArrayList<String>();
		str_user_avatar = new ArrayList<String>();
		str_user_username = new ArrayList<String>();
		str_user_en_username = new ArrayList<String>();
		str_subject = new ArrayList<String>();
		str_title = new ArrayList<String>();
		str_reply_total = new ArrayList<String>();
		str_collect_total = new ArrayList<String>();
		str_status = new ArrayList<String>();
		str_create_time = new ArrayList<String>();
		str_expire_time = new ArrayList<String>();
		str_flag = new ArrayList<String>();

		str_id1 = new ArrayList<String>();
		str_user_id1 = new ArrayList<String>();
		str_user_avatar1 = new ArrayList<String>();
		str_user_username1 = new ArrayList<String>();
		str_user_en_username1 = new ArrayList<String>();
		str_subject1 = new ArrayList<String>();
		str_title1 = new ArrayList<String>();
		str_reply_total1 = new ArrayList<String>();
		str_collect_total1 = new ArrayList<String>();
		str_status1 = new ArrayList<String>();
		str_create_time1 = new ArrayList<String>();
		str_expire_time1 = new ArrayList<String>();
		str_flag1 = new ArrayList<String>();

		str_id2 = new ArrayList<String>();
		str_user_id2 = new ArrayList<String>();
		str_user_avatar2 = new ArrayList<String>();
		str_user_username2 = new ArrayList<String>();
		str_user_en_username2 = new ArrayList<String>();
		str_subject2 = new ArrayList<String>();
		str_title2 = new ArrayList<String>();
		str_reply_total2 = new ArrayList<String>();
		str_collect_total2 = new ArrayList<String>();
		str_status2 = new ArrayList<String>();
		str_create_time2 = new ArrayList<String>();
		str_expire_time2 = new ArrayList<String>();
		str_flag2 = new ArrayList<String>();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_min_mypost_add = (LinearLayout) findViewById(R.id.lay_min_mypost_add);
		lay_min_mypost_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mrString = MyPost_Act.this.getExternalFilesDir(null).getPath();
				url = "file://"+mrString+"/bulidersir/ylb_add_thread.html";
				url2 = "file://"+mrString+"/bulidersir/";
				Intent intent = new Intent(MyPost_Act.this, YlbMessage_Act.class);
				intent.putExtra("url", url);
				intent.putExtra("url2", url2);
				startActivity(intent);
			}
		});
		lay_mime_mypost_none = (LinearLayout) findViewById(R.id.lay_mime_mypost_none);
		lay_mime_mypost_none2=(LinearLayout) findViewById(R.id.lay_mime_mypost_none2);
		str_collect_total.addAll(str_collect_total1);
		str_create_time.addAll(str_create_time1);
		str_expire_time.addAll(str_expire_time1);
		str_id.addAll(str_id1);
		str_reply_total.addAll(str_reply_total1);
		str_status.addAll(str_status1);
		str_subject.addAll(str_subject1);
		str_title.addAll(str_title1);
		str_user_avatar.addAll(str_user_avatar1);
		str_user_en_username.addAll(str_user_en_username1);
		str_user_id.addAll(str_user_id1);
		str_user_username.addAll(str_user_username1);
		str_flag.addAll(str_flag1);

		AnimationSet set = new AnimationSet(false);
		Animation animation = new AlphaAnimation(0, 1); // AlphaAnimation
														// 控制渐变透明的动画效果
		animation.setDuration(200); // 动画时间毫秒数
		set.addAnimation(animation); // 加入动画集合
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 1);

		lay_mypost_back = (LinearLayout) findViewById(R.id.lay_mypost_back);
		lay_mypost_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lv_history = (ListView) findViewById(R.id.lv_mime_mypost);
		adapter = new MyPost_Adapter(MyPost_Act.this, str_id, str_user_avatar,
				str_flag, str_create_time, str_status, str_title, str_subject,
				str_user_username, str_user_en_username, str_expire_time,
				session_id);
		if (str_id.size() == 0) {
			lay_mime_mypost_none.setVisibility(View.VISIBLE);
			lay_mime_mypost_none2.setVisibility(View.GONE);
			lv_history.setVisibility(View.GONE);
		} else {
			lay_mime_mypost_none.setVisibility(View.GONE);
			lay_mime_mypost_none2.setVisibility(View.GONE);
			lv_history.setVisibility(View.VISIBLE);
			lv_history.setAdapter(adapter);
		}
//		lay_mime_mypost_none.setVisibility(View.GONE);
//		lv_history.setVisibility(View.VISIBLE);
		lv_history.setAdapter(adapter);
		lv_history.setLayoutAnimation(controller);
		lv_history.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mrString = MyPost_Act.this.getExternalFilesDir(null).getPath();
				url = "file://"+mrString+"/bulidersir/ylb_thread.html";
				url2 = "file://"+mrString+"/bulidersir/";
				Intent intent = new Intent(MyPost_Act.this, Ylb_act.class);
				intent.putExtra("url", url);
				intent.putExtra("url2", url2);
				intent.putExtra("num", "1");
				intent.putExtra("id", str_id.get(position));
				startActivity(intent);
			}
		});
		// }

		bt_check = (Button) findViewById(R.id.bt_mine_mypost1);
		bt_uncheck = (Button) findViewById(R.id.bt_mine_mypost2);
		bt_check.setText("收藏帖子(" + total_post + ")");
		bt_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bt_uncheck.setBackgroundResource(R.drawable.corner_right_gray);
				bt_check.setBackgroundResource(R.drawable.corner_left_black);
				clear();
				str_flag.addAll(str_flag1);
				str_collect_total.addAll(str_collect_total1);
				str_create_time.addAll(str_create_time1);
				str_expire_time.addAll(str_expire_time1);
				str_id.addAll(str_id1);
				str_reply_total.addAll(str_reply_total1);
				str_status.addAll(str_status1);
				str_subject.addAll(str_subject1);
				str_title.addAll(str_title1);
				str_user_avatar.addAll(str_user_avatar1);
				str_user_en_username.addAll(str_user_en_username1);
				str_user_id.addAll(str_user_id1);
				str_user_username.addAll(str_user_username1);
				if (str_id.size() == 0) {
					lay_mime_mypost_none.setVisibility(View.VISIBLE);
					lay_mime_mypost_none2.setVisibility(View.GONE);
					lv_history.setVisibility(View.GONE);
				} else {
					lay_mime_mypost_none.setVisibility(View.GONE);
					lay_mime_mypost_none2.setVisibility(View.GONE);
					lv_history.setVisibility(View.VISIBLE);
					lv_history.setAdapter(adapter);
				}
				adapter.notifyDataSetChanged();
			}
		});
		bt_uncheck.setText("我的帖子(" + total_mypost + ")");
		bt_uncheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bt_check.setBackgroundResource(R.drawable.corner_left_gray);
				bt_uncheck.setBackgroundResource(R.drawable.corner_right_black);
				clear();
				str_flag.addAll(str_flag2);
				str_collect_total.addAll(str_collect_total2);
				str_create_time.addAll(str_create_time2);
				str_expire_time.addAll(str_expire_time2);
				str_id.addAll(str_id2);
				str_reply_total.addAll(str_reply_total2);
				str_status.addAll(str_status2);
				str_subject.addAll(str_subject2);
				str_title.addAll(str_title2);
				str_user_avatar.addAll(str_user_avatar2);
				str_user_en_username.addAll(str_user_en_username2);
				str_user_id.addAll(str_user_id2);
				str_user_username.addAll(str_user_username2);
				if (str_id.size() == 0) {
					lay_mime_mypost_none2.setVisibility(View.VISIBLE);
					lay_mime_mypost_none.setVisibility(View.GONE);
					lv_history.setVisibility(View.GONE);
				} else {
					lay_mime_mypost_none.setVisibility(View.GONE);
					lay_mime_mypost_none2.setVisibility(View.GONE);
					lv_history.setVisibility(View.VISIBLE);
					lv_history.setAdapter(adapter);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					MyPost_Act.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				findView();
				break;
			case 2:
				Toast.makeText(MyPost_Act.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(MyPost_Act.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void select_cjinfomain(String session_id) {
		Request request = Request.requestmypostmain(session_id);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json, json2, json3, json4;
			JSONArray joArray, joArray2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				String msg = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("list");
					json3 = json2.getJSONObject("data1");
					total_post = json3.getString("total");
					joArray = json3.getJSONArray("data");
					for (int i = 0; i < joArray.length(); i++) {
						json = joArray.getJSONObject(i);
						str_id1.add(json.getString("id"));
						str_user_id1.add(json.getString("user_id"));
						str_user_avatar1.add(json.getString("user_avatar"));
						str_user_username1.add(json.getString("user_username"));
						str_user_en_username1.add(json
								.getString("user_en_username"));
						str_subject1.add(json.getString("subject"));
						str_title1.add(json.getString("title"));
						str_reply_total1.add(json.getString("reply_total"));
						str_collect_total1.add(json.getString("collect_total"));
						str_status1.add(json.getString("status"));
						str_create_time1.add(json.getString("create_time"));
						str_expire_time1.add(json.getString("expire_time"));
						str_flag1.add("0");
					}
					json4 = json2.getJSONObject("data2");
					total_mypost = json4.getString("total");
					joArray2 = json4.getJSONArray("data");
					for (int i = 0; i < joArray2.length(); i++) {
						json = joArray2.getJSONObject(i);
						str_id2.add(json.getString("id"));
						str_user_id2.add(json.getString("user_id"));
						str_user_avatar2.add(json.getString("user_avatar"));
						str_user_username2.add(json.getString("user_username"));
						str_user_en_username2.add(json
								.getString("user_en_username"));
						str_subject2.add(json.getString("subject"));
						str_title2.add(json.getString("title"));
						str_reply_total2.add(json.getString("reply_total"));
						str_collect_total2.add(json.getString("collect_total"));
						str_status2.add(json.getString("status"));
						str_create_time2.add(json.getString("create_time"));
						str_expire_time2.add(json.getString("expire_time"));
						str_flag2.add("1");
					}
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				} else {
					Toast.makeText(MyPost_Act.this, "" + msg, Toast.LENGTH_SHORT).show();
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

	public void clear() {
		str_id.clear();
		str_user_id.clear();
		str_user_avatar.clear();
		str_user_username.clear();
		str_user_en_username.clear();
		str_subject.clear();
		str_title.clear();
		str_reply_total.clear();
		str_collect_total.clear();
		str_status.clear();
		str_create_time.clear();
		str_expire_time.clear();
		str_flag.clear();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MyPost_Act.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
