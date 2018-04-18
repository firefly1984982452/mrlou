package com.mrlou.message;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.h5.Ylb_act;
import com.mrlou.listener.HttpListener;
import com.mrlou.mine.MyPost_Act;
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

public class Message_PostcommentAct extends Activity implements HttpListener{

	private LinearLayout lay_message_postcomments_back;
	private ListView lv_message_postcomments;
	private Message_PostcommentsAdapter adapter;
	private SharedPreferences sharedPreferences;
	private ArrayList<String> str_id, str_target_id, str_event_date,
	str_week_no, str_name, str_title, str_content, str_status,
	str_is_read, str_notify_time,str_type;
	private String url, url2,mrString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_postcomments);
		prepare();
		sharedPreferences = Message_PostcommentAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		String session_id = sharedPreferences.getString("session_id", "");
		select_standoffice("4","1",session_id);
	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_id=new ArrayList<String>();
		str_target_id=new ArrayList<String>();
		str_event_date=new ArrayList<String>();
		str_week_no=new ArrayList<String>();
		str_name=new ArrayList<String>();
		str_title=new ArrayList<String>();
		str_content=new ArrayList<String>();
		str_status=new ArrayList<String>();
		str_is_read=new ArrayList<String>();
		str_notify_time=new ArrayList<String>();
		str_type=new ArrayList<>();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_message_postcomments_back = (LinearLayout) findViewById(R.id.lay_message_postcomments_back);
		lay_message_postcomments_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lv_message_postcomments = (ListView) findViewById(R.id.lv_message_postcomments);
		adapter = new Message_PostcommentsAdapter(Message_PostcommentAct.this,
				str_name, str_content, str_title, str_notify_time);
		lv_message_postcomments.setAdapter(adapter);
		lv_message_postcomments.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (str_type.get(position).equals("4")) {
					mrString = Message_PostcommentAct.this.getExternalFilesDir(null).getPath();
					url = "file://"+mrString+"/bulidersir/ylb_thread.html";
					url2 = "file://"+mrString+"/bulidersir/";
					Intent intent = new Intent(Message_PostcommentAct.this, Ylb_act.class);
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
					Message_PostcommentAct.this.finish();
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
				//lv_search.setVisibility(View.VISIBLE);
				findView();
				//adapter.notifyDataSetChanged();
				break;
			case 2:
				//Toast.makeText(Economic_SearchAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				//Toast.makeText(Economic_SearchAct.this, "网络异常，请尝试下拉刷新", Toast.LENGTH_SHORT)
				//		.show();
				break;
			}
		}
	};

	public void select_standoffice(String name,String page,String session_id) {
		Request request = Request.requesteconomicnoticemessage(name,page,session_id);
		new HttpThread(request, this);
	}
	
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
							str_type.add(json.getString("type"));
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
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
			Message_PostcommentAct.this.finish();
            return true;
        }
		return super.onKeyDown(keyCode, event);
	}
}
