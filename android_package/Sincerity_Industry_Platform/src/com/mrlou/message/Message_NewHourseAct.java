package com.mrlou.message;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.array;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.ScrollView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mrlou.activity.InitActivity;
import com.mrlou.h5.Building_Act;
import com.mrlou.h5.RoomInfo_Act;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.mrlou.R.string;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class Message_NewHourseAct extends Activity implements HttpListener {

	private LinearLayout lay_message_newhourse_back,lay_message_newhourse_select;
	private ArrayList<String> str_id, str_category, str_building_id,
			str_building_name, str_avatar, str_transaction_type, str_unit_no,
			str_status, str_verified, str_image_total, str_rent_price,
			str_sell_price, str_release_time, str_user_type,
			str_fee_rate, str_unique;
	private ListView lv_newhourse;
	private Message_NewHourseAdapter adapter;
	private String url, url2, type_id, mrString,type;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_newhourse);
		sharedPreferences = Message_NewHourseAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		type_id = sharedPreferences.getString("type_id", "");
		prepare();
		Intent intent=getIntent();
		type=intent.getStringExtra("type");
		if (type.equals("0")) {
			select_area("");
		}else {
			init();
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		Intent intents=getIntent();
		str_id=intents.getStringArrayListExtra("id");
		str_category=intents.getStringArrayListExtra("category");
		str_building_id=intents.getStringArrayListExtra("building_id");
		str_building_name=intents.getStringArrayListExtra("building_name");
		str_avatar=intents.getStringArrayListExtra("avatar");
		str_transaction_type=intents.getStringArrayListExtra("transaction_type");
		str_unit_no=intents.getStringArrayListExtra("unit_no");
		str_status=intents.getStringArrayListExtra("status");
		str_verified=intents.getStringArrayListExtra("verified");
		str_image_total=intents.getStringArrayListExtra("image_total");
		str_rent_price=intents.getStringArrayListExtra("rent_price");
		str_sell_price=intents.getStringArrayListExtra("sell_price");
		str_user_type=intents.getStringArrayListExtra("user_type");
		str_unique=intents.getStringArrayListExtra("unique");
		str_fee_rate=intents.getStringArrayListExtra("fee_rate");
		str_release_time=intents.getStringArrayListExtra("release_time");
		findView();
	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
		str_category = new ArrayList<String>();
		str_building_id = new ArrayList<String>();
		str_building_name = new ArrayList<String>();
		str_avatar = new ArrayList<String>();
		str_transaction_type = new ArrayList<String>();
		str_unit_no = new ArrayList<String>();
		str_status = new ArrayList<String>();
		str_verified = new ArrayList<String>();
		str_image_total = new ArrayList<String>();
		str_rent_price = new ArrayList<String>();
		str_sell_price = new ArrayList<String>();
		str_user_type = new ArrayList<String>();
		str_unique = new ArrayList<String>();
		str_fee_rate = new ArrayList<String>();
		str_release_time = new ArrayList<String>();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_message_newhourse_back = (LinearLayout) findViewById(R.id.lay_message_newhourse_back);
		lay_message_newhourse_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_message_newhourse_select=(LinearLayout) findViewById(R.id.lay_message_newhourse_select);
		lay_message_newhourse_select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Message_NewHourseAct.this,Message_SelectAct.class);
				startActivity(intent);
				Message_NewHourseAct.this.finish();
			}
		});
		lv_newhourse = (ListView) findViewById(R.id.lv_message_newhourse);
		adapter = new Message_NewHourseAdapter(Message_NewHourseAct.this,
				str_id, str_category, str_building_id,
				str_building_name, str_avatar, str_transaction_type,
				str_unit_no, str_status, str_verified, str_image_total,
				str_rent_price, str_sell_price, str_release_time,
				str_user_type, str_fee_rate, str_unique, type_id);
		lv_newhourse.setAdapter(adapter);
		lv_newhourse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (!str_building_id.get(position).equals("")) {
					mrString = Message_NewHourseAct.this.getExternalFilesDir(
							null).getPath();
					url = "file://" + mrString + "/bulidersir/lxs_index.html";
					url2 = "file://" + mrString + "/bulidersir/";
					Intent intent = new Intent(Message_NewHourseAct.this,
							RoomInfo_Act.class);
					intent.putExtra("url", url);
					intent.putExtra("url2", url2);
					intent.putExtra("frsource", "gg");
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
					Message_NewHourseAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void select_area(String name) {
		Request request = Request.requestmessaagenewhourse(name);
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
				Toast.makeText(Message_NewHourseAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Message_NewHourseAct.this, "网络异常，请重新发起请求", Toast.LENGTH_SHORT)
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
			JSONArray joArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					int length = joArray.length();
					for (int i = 0; i < length; i++) {
						json2 = joArray.getJSONObject(i);
						str_id.add(json2.getString("id"));
						str_category.add(json2.getString("category"));
						str_building_id.add(json2.getString("building_id"));
						str_building_name.add(json2.getString("building_name"));
						str_avatar.add(json2.getString("avatar"));
						str_transaction_type.add(json2
								.getString("transaction_type"));
						str_unit_no.add(json2.getString("unit_no"));
						str_status.add(json2.getString("status"));
						str_verified.add(json2.getString("verified"));
						str_image_total.add(json2.getString("image_total"));
						str_rent_price.add(json2.getString("rent_price"));
						str_sell_price.add(json2.getString("sell_price"));
						str_release_time.add(json2.getString("release_time"));
						str_user_type.add(json2.getString("user_type"));
						str_fee_rate.add(json2.getString("fee_rate"));
						str_unique.add(json2.getString("unique"));
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
			Message_NewHourseAct.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
