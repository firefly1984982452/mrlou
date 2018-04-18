package com.mrlou.message;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.listener.HttpListener;
import com.mrlou.mine.Customer_DetailAct;
import com.mrlou.mine.Customer_ManAct;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

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

public class Message_CustomerLogAct extends Activity implements HttpListener {

	private ListView lv_customer;
	private LinearLayout lay_noting, lay_back;
	private Message_CustomerAdapter adapter;
	private SharedPreferences sharedPreferences;
	private ArrayList<String> str_id, str_target_id, str_event_date, str_name,
			str_title, str_content, str_notify_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_customerlog);
		prepare();
		sharedPreferences = Message_CustomerLogAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		String session_id = sharedPreferences.getString("session_id", "");
		select_standoffice("5", "1", session_id);
		// findView();
	}

	private void select_standoffice(String type, String page, String session_id) {
		// TODO Auto-generated method stub
		Request request = Request.requesteconomicnoticemessage(type, page,
				session_id);
		new HttpThread(request, this);
	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
		str_target_id = new ArrayList<String>();
		str_event_date = new ArrayList<String>();
		str_name = new ArrayList<String>();
		str_title = new ArrayList<String>();
		str_content = new ArrayList<String>();
		str_notify_time = new ArrayList<String>();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_back = (LinearLayout) findViewById(R.id.lay_message_customer_back);
		lay_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_noting = (LinearLayout) findViewById(R.id.lay_message_customer_nothing);
		lv_customer = (ListView) findViewById(R.id.lv_message_customer);
		adapter = new Message_CustomerAdapter(Message_CustomerLogAct.this,
				str_name, str_content, str_notify_time);
		if (str_name.size() == 0) {
			lay_noting.setVisibility(View.VISIBLE);
			lv_customer.setVisibility(View.GONE);
		} else {
			lay_noting.setVisibility(View.GONE);
			lv_customer.setVisibility(View.VISIBLE);
			lv_customer.setAdapter(adapter);
			lv_customer.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Message_CustomerLogAct.this,
							Customer_DetailAct.class);
					intent.putExtra("id", str_target_id.get(position));
					startActivity(intent);
				}
			});
		}
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Message_CustomerLogAct.this.finish();
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
				// lv_search.setVisibility(View.VISIBLE);
				// adapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(Message_CustomerLogAct.this, "暂无数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 33:

				break;
			case 99:
				Toast.makeText(Message_CustomerLogAct.this, "网络异常，请重新尝试", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json;
			JSONArray joArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					int len = joArray.length();
					for (int i = 0; i < len; i++) {
						json = joArray.getJSONObject(i);
						str_id.add(json.getString("id"));
						str_target_id.add(json.getString("target_id"));
						str_event_date.add(json.getString("event_date"));
						str_name.add(json.getString("name"));
						str_title.add(json.getString("title"));
						str_content.add(json.getString("content"));
						str_notify_time.add(json.getString("notify_time"));
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
			Message_CustomerLogAct.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
