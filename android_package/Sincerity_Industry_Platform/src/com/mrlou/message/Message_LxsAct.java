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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mrlou.economic.Economic_SearchAct;
import com.mrlou.h5.YlbMessage_Act;
import com.mrlou.h5.Ylb_act;
import com.mrlou.listener.HttpListener;
import com.mrlou.mine.MyPost_Act;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class Message_LxsAct extends Activity implements HttpListener {

	private LinearLayout lay_message_lxs_back;
	private ListView lv_lxs;
	private Message_LxsAdapter adapter;
	private SharedPreferences sharedPreferences;
	private ArrayList<String> str_id, str_title, str_imgurl, str_content,
			str_news_type, str_link, str_createtime;
	private String url, url2,mrString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_lxs);
		sharedPreferences = Message_LxsAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		String session_id = sharedPreferences.getString("session_id", "");
		prepare();
		select_standoffice(session_id, "1");
		// findView();
	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
		str_title = new ArrayList<String>();
		str_imgurl = new ArrayList<String>();
		str_content = new ArrayList<String>();
		str_news_type = new ArrayList<String>();
		str_link = new ArrayList<String>();
		str_createtime = new ArrayList<String>();

		// for (int i = 0; i < 4; i++) {
		// str_id.add("1");
		// str_title.add("1111");
		// str_imgurl.add("11111");
		// str_content.add("111");
		// str_link.add("11");
		// str_createtime.add("111");
		// }

	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_message_lxs_back = (LinearLayout) findViewById(R.id.lay_message_lxs_back);
		lay_message_lxs_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lv_lxs = (ListView) findViewById(R.id.lv_message_lxs);
		adapter = new Message_LxsAdapter(Message_LxsAct.this, str_id,
				str_title, str_imgurl, str_content, str_news_type, str_link,
				str_createtime);
		lv_lxs.setAdapter(adapter);
		lv_lxs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (str_news_type.get(position).equals("2")) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(str_link.get(position));
					intent.setData(content_url);
					startActivity(intent);
				} else {
					mrString = Message_LxsAct.this.getExternalFilesDir(null).getPath();
					url = "file://"+mrString+"/bulidersir/ylb_thread.html";
					url2 = "file://"+mrString+"/bulidersir/";
					Intent intent = new Intent(Message_LxsAct.this,
							Ylb_act.class);
					intent.putExtra("url", url);
					intent.putExtra("url2", url2);
					intent.putExtra("num", "1");
					intent.putExtra("id", str_id.get(position));
					startActivity(intent);
				}
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
					Message_LxsAct.this.finish();
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
				Toast.makeText(Message_LxsAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Message_LxsAct.this, "网络异常，请尝试下拉刷新", Toast.LENGTH_SHORT)
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
							str_imgurl.add(json.getString("imgurl"));
							str_content.add(json.getString("content"));
							str_news_type.add(json.getString("news_type"));
							str_link.add(json.getString("link"));
							str_createtime.add(json.getString("time"));
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
			Message_LxsAct.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
