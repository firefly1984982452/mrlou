package com.mrlou.yi;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_SelectAreaAdapter;
import com.mrlou.mine.Setting_personal01;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author jx_chen
 * @date 2016-12-1---上午9:44:16
 * @problem
 */
public class Yi_SelectAct extends Activity implements HttpListener {

	private ListView lv_area;
	private Message_SelectAreaAdapter mAdapter;
	private ArrayList<String> str_area, str_area_id, str_flag;
	private String area_id, session_id, type;
	private Button bt_select_yi_all, bt_select_yi_modify;
	private SharedPreferences sharedPreferences;
	private ArrayList<String> str_id, str_user_id, str_avatar, str_username,
			str_title, str_subject, str_region,str_reply_total,str_like_total;
	private LinearLayout lay_yi_select_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_select_arealist);
		sharedPreferences = Yi_SelectAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		prepare();
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_yi_select_back=(LinearLayout) findViewById(R.id.lay_yi_select_back);
		lay_yi_select_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lv_area = (ListView) findViewById(R.id.lv_select_area);
		mAdapter = new Message_SelectAreaAdapter(Yi_SelectAct.this, str_area,
				str_flag);
		lv_area.setAdapter(mAdapter);
		lv_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				area_id = str_area_id.get(position);
				for (int i = 0; i < str_flag.size(); i++) {
					if (position == i) {
						str_flag.set(i, "1");
					} else {
						str_flag.set(i, "0");
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		});
		bt_select_yi_all = (Button) findViewById(R.id.bt_select_yi_all);
		bt_select_yi_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_yi_hourse(session_id, "1", type);
				if (type.equals("1")) {
					YI_HourseAct.setType(type, "1");
				}else {
					Yi_CustomerAct.setType(type, "1");
				}
			}
		});
		bt_select_yi_modify = (Button) findViewById(R.id.bt_select_yi_modify);
		bt_select_yi_modify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_yi_hourse_search(session_id, "1", type, area_id);
				if (type.equals("1")) {
					YI_HourseAct.setType(type, area_id);
				}else {
					Yi_CustomerAct.setType(type, area_id);
				}
			}
		});
	}

	public void select_yi_hourse(String session_id, String page, String subject) {
		Request request = Request.request_yi(session_id, page, subject);
		new HttpThread(request, this);
	}

	public void select_yi_hourse_search(String session_id, String page,
			String subject, String area_code) {
		Request request = Request.request_searchthread(session_id, page,
				subject, area_code);
		new HttpThread(request, this);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (type.equals("1")) {
					YI_HourseAct.setSelect(str_id, str_user_id, str_avatar,
							str_username, str_title, str_subject, str_region,str_reply_total,str_like_total);
				} else {
					Yi_CustomerAct.setSelect(str_id, str_user_id, str_avatar,
							str_username, str_title, str_subject, str_region,str_reply_total,str_like_total);
				}
				simulateKey(KeyEvent.KEYCODE_BACK);
				break;
			case 2:
				Toast.makeText(Yi_SelectAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Yi_SelectAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	// 调用返回键的功能
		public void simulateKey(final int KeyCode) {
			new Thread() {
				public void run() {
					try {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(KeyCode);
						Yi_SelectAct.this.finish();
					} catch (Exception e) {
						Log.e("Exception when sendKeyDownUpSync", e.toString());
					}
				}
			}.start();
		}
	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC || reqID == Request.REQUEST_NEW) {
			JSONObject json;
			JSONArray jsonArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					jsonArray = json.getJSONArray("list");
					int len = jsonArray.length();
					for (int i = 0; i < len; i++) {
						json = jsonArray.getJSONObject(i);
						str_avatar.add(json.getString("user_avatar"));
						str_id.add(json.getString("id"));
						str_title.add(json.getString("title"));
						str_user_id.add(json.getString("user_id"));
						str_username.add(json.getString("user_username"));
						str_subject.add(json.getString("subject"));
						str_region.add(json.getString("region"));
						str_reply_total.add(json.getString("reply_total"));
						str_like_total.add(json.getString("like_total"));
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

	private void prepare() {
		str_id=new ArrayList<>();
		str_user_id=new ArrayList<>();
		str_avatar=new ArrayList<>();
		str_username=new ArrayList<>();
		str_title=new ArrayList<>();
		str_subject=new ArrayList<>();
		str_region=new ArrayList<>();
		str_reply_total=new ArrayList<>();
		str_like_total=new ArrayList<>();
		str_area = new ArrayList<String>();
		str_area.add("浦东新区");
		str_area.add("黄浦区");
		str_area.add("静安区");
		str_area.add("徐汇区");
		str_area.add("长宁区");
		str_area.add("普陀区");
		str_area.add("闸北区");
		str_area.add("虹口区");
		str_area.add("杨浦区");
		str_area.add("宝山区");
		str_area.add("闵行区");
		str_area.add("嘉定区");
		str_area.add("青浦区");
		str_area.add("松江区");
		str_area.add("奉贤区");
		str_area.add("金山区");
		str_area.add("崇明区");
		str_area_id = new ArrayList<String>();
		str_area_id.add("310115");
		str_area_id.add("310101");
		str_area_id.add("310106");
		str_area_id.add("310104");
		str_area_id.add("310105");
		str_area_id.add("310107");
		str_area_id.add("310108");
		str_area_id.add("310109");
		str_area_id.add("310110");
		str_area_id.add("310113");
		str_area_id.add("310112");
		str_area_id.add("310114");
		str_area_id.add("310118");
		str_area_id.add("310117");
		str_area_id.add("310120");
		str_area_id.add("310116");
		str_area_id.add("310230");
		str_flag = new ArrayList<>();
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
	}
}
