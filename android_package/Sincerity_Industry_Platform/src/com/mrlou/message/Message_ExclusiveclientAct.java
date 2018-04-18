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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class Message_ExclusiveclientAct extends Activity implements
		HttpListener {
	private LinearLayout lay_message_exclusiveclient_back,
			lay_message_exclusiveclient;
	private ListView lv_message_exclusiveclient;
	private Message_ExclusiveclientAdapter adapter;
	private SharedPreferences sharedPreferences;
	private ArrayList<String> str_id, str_title, str_imgurl, str_content,
			str_createtime, str_phone, str_work_age, str_company, str_name,str_company_status,str_v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_exclusiveclient);
		sharedPreferences = Message_ExclusiveclientAct.this
				.getSharedPreferences("user", Activity.MODE_PRIVATE);
		String session_id = sharedPreferences.getString("session_id", "");
		prepare();
		select_standoffice(session_id, "5");
	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
		str_title = new ArrayList<String>();
		str_imgurl = new ArrayList<String>();
		str_content = new ArrayList<String>();
		str_phone = new ArrayList<>();
		str_work_age = new ArrayList<>();
		str_company = new ArrayList<>();
		str_name = new ArrayList<>();
		str_createtime = new ArrayList<String>();
		str_company_status=new ArrayList<String>();
		str_v=new ArrayList<String>();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_message_exclusiveclient = (LinearLayout) findViewById(R.id.lay_message_exclusiveclient);
		lay_message_exclusiveclient_back = (LinearLayout) findViewById(R.id.lay_message_exclusiveclient_back);
		lay_message_exclusiveclient_back
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						simulateKey(KeyEvent.KEYCODE_BACK);
					}
				});
		lv_message_exclusiveclient = (ListView) findViewById(R.id.lv_message_exclusiveclient);
		adapter = new Message_ExclusiveclientAdapter(
				Message_ExclusiveclientAct.this, str_id, str_title, str_imgurl,
				str_content, str_name, str_company, str_createtime, str_phone,
				str_work_age,str_company_status,str_v);
		lv_message_exclusiveclient.setAdapter(adapter);
		if (str_createtime.get(0).equals("")) {
			lay_message_exclusiveclient.setVisibility(View.VISIBLE);
			lv_message_exclusiveclient.setVisibility(View.GONE);
		}
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Message_ExclusiveclientAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void select_standoffice(String session_id, String type) {
		Request request = Request.requestemessagetype(session_id, type);
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
				Toast.makeText(Message_ExclusiveclientAct.this, "暂无数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 99:
				Toast.makeText(Message_ExclusiveclientAct.this, "网络异常，请尝试下拉刷新",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json;
			JSONArray joArray, joArray2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					int len = joArray.length();
					for (int i = 0; i < len; i++) {
						json = joArray.getJSONObject(i);
						joArray2 = json.getJSONArray("data");
						for (int j = 0; j < joArray2.length(); j++) {
							json = joArray2.getJSONObject(j);
							str_id.add(json.getString("id"));
							str_title.add(json.getString("title"));
							str_imgurl.add(json.getString("avatar"));
							str_content.add(json.getString("content"));
							str_phone.add(json.getString("phone"));
							str_work_age.add(json.getString("work_age"));
							str_createtime.add(json.getString("time"));
							str_company.add(json.getString("company"));
							str_name.add(json.getString("cn_username")
									+ json.getString("en_username"));
							str_company_status.add(json.getString("company_status"));
							str_v.add(json.getString("v"));
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
			Message_ExclusiveclientAct.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
