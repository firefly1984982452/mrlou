package com.mrlou.mine;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.adapter.MyHourse_Adapter;
import com.mrlou.h5.YlbMessage_Act;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class My_HourseAct extends Activity implements HttpListener {

	private LinearLayout lay_myhourse_back, lay_my_hourse_fabu;
	private ListView lv_hourse;
	private MyHourse_Adapter adapter;
	private SharedPreferences sharedPreferences;
	private String session_id, total, valtotal, donetotal, losetotal;
	private ArrayList<String> str_area_name, str_total, str_id, str_name,
			str_area_code, str_totals;
	private TextView tv_myhourse_total, tv_myhourse_valtotal,
			tv_myhourse_donetotal, tv_myhourse_losetotal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_my_hourse);
		sharedPreferences = My_HourseAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		init();
		select_area(session_id);
		// findView();
	}

	private void init() {
		// TODO Auto-generated method stub
		str_area_name = new ArrayList<String>();
		str_total = new ArrayList<String>();
		str_id = new ArrayList<String>();
		str_name = new ArrayList<String>();
		str_area_code = new ArrayList<String>();
		str_totals = new ArrayList<String>();

	}

	private void findView() {
		// TODO Auto-generated method stub
		tv_myhourse_total = (TextView) findViewById(R.id.tv_myhourse_total);
		tv_myhourse_total.setText(total);
		tv_myhourse_valtotal = (TextView) findViewById(R.id.tv_myhourse_valtotal);
		tv_myhourse_valtotal.setText(valtotal);
		tv_myhourse_donetotal = (TextView) findViewById(R.id.tv_myhourse_donetotal);
		tv_myhourse_donetotal.setText(donetotal);
		tv_myhourse_losetotal = (TextView) findViewById(R.id.tv_myhourse_losetotal);
		tv_myhourse_losetotal.setText(losetotal);
		lv_hourse = (ListView) findViewById(R.id.lv_myhourse);
		adapter = new MyHourse_Adapter(this, str_area_name, str_total, str_id,
				str_name, str_area_code, str_totals);
		lv_hourse.setAdapter(adapter);
		lv_hourse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(My_HourseAct.this,
						My_HourseDetailAct.class);
				if (str_id.get(position).equals("null")) {
					intent.putExtra("id", "0");
				}else {
					intent.putExtra("id", str_id.get(position));
				}
				intent.putExtra("buildingname", str_name.get(position));
				startActivity(intent);
			}
		});
		lay_myhourse_back = (LinearLayout) findViewById(R.id.lay_myhourse_back);
		lay_myhourse_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
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
					My_HourseAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void select_area(String session_id) {
		Request request = Request.requestmyhourse(session_id);
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
				Toast.makeText(My_HourseAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				lay_myhourse_back = (LinearLayout) findViewById(R.id.lay_myhourse_back);
				lay_myhourse_back.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						simulateKey(KeyEvent.KEYCODE_BACK);
					}
				});
				Toast.makeText(My_HourseAct.this, "暂无房源，请赶紧到楼先生首页去发布吧", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(My_HourseAct.this, "网络异常，请尝试下拉刷新", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json, json2;
			JSONArray joArray, joArray3,joArray4;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("list");
					total = json2.getString("total");
					valtotal = json2.getString("valtotal");
					donetotal = json2.getString("donetotal");
					losetotal = json2.getString("losetotal");
					joArray4=json2.getJSONArray("exce");
					if (joArray4.length()>0) {
						for (int i = 0; i < joArray4.length(); i++) {
							json = joArray4.getJSONObject(i);
							if (i==0) {
								str_area_name.add("异常房源");
								str_total.add(""+joArray4.length());
								str_id.add(json.getString("building_id"));
								str_name.add(json.getString("building_name"));
								str_area_code.add("1");
								str_totals.add("1");
							}else {
								str_total.add("");
								str_area_name.add("");
								str_id.add(json.getString("building_id"));
								str_name.add(json.getString("building_name"));
								str_area_code.add("1");
								str_totals.add("1");
							}
						}	
					}
					joArray = json2.getJSONArray("data");
					int length = joArray.length();
					for (int j = 0; j < length; j++) {
						json = joArray.getJSONObject(j);
						str_area_name.add(json.getString("area_name"));
						str_total.add(json.getString("total"));
						joArray3 = json.getJSONArray("cate");
						for (int m = 0; m < joArray3.length(); m++) {
							json2 = joArray3.getJSONObject(m);
							str_id.add(json2.getString("id"));
							str_name.add(json2.getString("name"));
							str_area_code.add(json2.getString("area_code"));
							str_totals.add(json2.getString("total"));
							if (m >= 1) {
								str_total.add("");
								str_area_name.add("");
							}

						}
					}
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				}else {
					newHandler.sendEmptyMessage(3);
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
