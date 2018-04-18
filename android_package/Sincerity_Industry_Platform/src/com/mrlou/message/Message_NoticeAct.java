package com.mrlou.message;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mrlou.economic.Economic_SearchAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.mine.Hourse_FollowAct;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class Message_NoticeAct extends Activity implements HttpListener {

	private LinearLayout lay_message_lxs_back, lay_message_notice;
	private ArrayList<String> str_id, str_content, str_createtime, str_title,str_data;
	private ListView lv_lxs;
	private Message_NoticeAdapter adapter;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_notice);
		sharedPreferences = Message_NoticeAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		String session_id = sharedPreferences.getString("session_id", "");
		prepare();
		select_notice(session_id);
		// findView();
	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
		str_content = new ArrayList<String>();
		str_createtime = new ArrayList<String>();
		str_title = new ArrayList<String>();
		str_data=new ArrayList<>();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_message_notice = (LinearLayout) findViewById(R.id.lay_message_notice);
		lay_message_lxs_back = (LinearLayout) findViewById(R.id.lay_message_notice_back);
		lay_message_lxs_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lv_lxs = (ListView) findViewById(R.id.lv_message_notice);
		if (str_id.size() > 0) {
			lay_message_notice.setVisibility(View.GONE);
			lv_lxs.setVisibility(View.VISIBLE);
			adapter = new Message_NoticeAdapter(Message_NoticeAct.this, str_data,str_id,
					str_title, str_content, str_createtime);
			lv_lxs.setAdapter(adapter);
			lv_lxs.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if (!str_id.get(position).equals("0")) {
						Intent intent = new Intent(Message_NoticeAct.this,
								Hourse_FollowAct.class);
						intent.putExtra("id", str_id.get(position));
						startActivity(intent);
					}
				}
			});
		} else {
			lay_message_notice.setVisibility(View.VISIBLE);
			lv_lxs.setVisibility(View.GONE);
		}
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Message_NoticeAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void select_notice(String session_id) {
		Request request = Request.requestnotice(session_id);
		new HttpThread(request, this);
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
				Toast.makeText(Message_NoticeAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Message_NoticeAct.this, "网络异常，请尝试刷新", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json, json2;
			JSONArray joArray, joArray2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					int len = joArray.length();
					for (int i = 0; i < len; i++) {
						json = joArray.getJSONObject(i);
						str_data.add(json.getString("date"));
						joArray2 = json.getJSONArray("list");
						for (int j = 0; j < joArray2.length(); j++) {
							json2 = joArray2.getJSONObject(j);
							str_id.add(json2.getString("room_id"));
							str_title.add(json2.getString("title"));
							str_content.add(json2.getString("content"));
							str_createtime.add(json2.getString("createtime"));
							if (j>0) {
								str_data.add("");
							}
						}
					}
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Message_NoticeAct.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
