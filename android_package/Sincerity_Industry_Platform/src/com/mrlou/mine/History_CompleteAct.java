package com.mrlou.mine;

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

import com.mrlou.adapter.History_Adapter;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class History_CompleteAct extends Activity implements HttpListener {

	private LinearLayout lay_min_history_add, lay_mine_history_back,
			lay_mine_history_main;
	private Button bt_check, bt_uncheck;
	private History_Adapter adapter;
	private ArrayList<String> str_id, str_user_id, str_user_type,
			str_building_id, str_building_name, str_region_code,
			str_transaction_type, str_square_meter, str_cj_time, str_industry,
			str_create_time, str_status, str_name, str_avatar, str_contact,
			str_proof, str_proof_id, str_flag;
	private ArrayList<String> str_id1, str_user_id1, str_user_type1,
			str_building_id1, str_building_name1, str_region_code1,
			str_transaction_type1, str_square_meter1, str_cj_time1,
			str_industry1, str_create_time1, str_status1, str_name1,
			str_avatar1, str_contact1, str_proof1, str_proof_id1, str_flag1;
	private ArrayList<String> str_id2, str_user_id2, str_user_type2,
			str_building_id2, str_building_name2, str_region_code2,
			str_transaction_type2, str_square_meter2, str_cj_time2,
			str_industry2, str_create_time2, str_status2, str_name2,
			str_avatar2, str_contact2, str_proof2, str_proof_id2, str_flag2;
	private ListView lv_history;
	private SharedPreferences sharedPreferences;
	private String session_id, total_examineed, total_unexamine,flag,add_v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_history);
		sharedPreferences = History_CompleteAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		Intent intent=getIntent();
		add_v=intent.getStringExtra("add_v");
		prapare();
		select_cjinfomain(session_id);
		// findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		flag="0";
		str_id.addAll(str_id1);
		str_user_id.addAll(str_user_id1);
		str_user_type.addAll(str_user_type1);
		str_building_id.addAll(str_building_id1);
		str_building_name.addAll(str_building_name1);
		str_region_code.addAll(str_region_code1);
		str_transaction_type.addAll(str_transaction_type1);
		str_square_meter.addAll(str_square_meter1);
		str_cj_time.addAll(str_cj_time1);
		str_industry.addAll(str_industry1);
		str_create_time.addAll(str_create_time1);
		str_status.addAll(str_status1);
		str_name.addAll(str_name1);
		str_contact.addAll(str_contact1);
		str_avatar.addAll(str_avatar1);
		str_proof.addAll(str_proof1);
		str_proof_id.addAll(str_proof_id1);
		str_flag.addAll(str_flag1);
		AnimationSet set = new AnimationSet(false);
		Animation animation = new AlphaAnimation(0, 1); // AlphaAnimation
														// 控制渐变透明的动画效果
		animation.setDuration(200); // 动画时间毫秒数
		set.addAnimation(animation); // 加入动画集合
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 1);

		lay_mine_history_back = (LinearLayout) findViewById(R.id.lay_mine_history_back);
		lay_mine_history_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lv_history = (ListView) findViewById(R.id.lv_mime_history);
		adapter = new History_Adapter(History_CompleteAct.this, str_id,str_name,
				str_transaction_type, str_square_meter, str_cj_time,
				str_industry, str_status, str_flag, str_avatar, str_proof,
				str_proof_id, str_contact);
		lv_history.setAdapter(adapter);
		lv_history.setLayoutAnimation(controller);
		lv_history.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (flag.equals("1")) {
					Intent intent = new Intent(History_CompleteAct.this, New_EditInfoAct.class);
					intent.putExtra("id", str_id.get(position));
					intent.putExtra("trans_type", str_transaction_type.get(position));
					intent.putExtra("building_name", str_name.get(position));
					intent.putExtra("squre_meter", str_square_meter.get(position));
					intent.putExtra("cj_time", str_cj_time.get(position));
					intent.putExtra("industry", str_industry.get(position));
					intent.putExtra("contact", str_contact.get(position));
					intent.putExtra("avatars", str_proof.get(position));
					intent.putExtra("avatars_id", str_proof_id.get(position));
					intent.putExtra("add_v", add_v);
					startActivity(intent);
					History_CompleteAct.this.finish();
				}
			}
		});

		bt_check = (Button) findViewById(R.id.bt_mine_history1);
		bt_uncheck = (Button) findViewById(R.id.bt_mine_history2);
		bt_check.setText("已审核(" + total_examineed + ")");
		bt_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag="0";
				bt_uncheck.setBackgroundResource(R.drawable.corner_right_gray);
				bt_check.setBackgroundResource(R.drawable.corner_left_black);
				clear();
				str_flag.addAll(str_flag1);
				str_id.addAll(str_id1);
				str_user_id.addAll(str_user_id1);
				str_user_type.addAll(str_user_type1);
				str_building_id.addAll(str_building_id1);
				str_building_name.addAll(str_building_name1);
				str_region_code.addAll(str_region_code1);
				str_transaction_type.addAll(str_transaction_type1);
				str_square_meter.addAll(str_square_meter1);
				str_cj_time.addAll(str_cj_time1);
				str_industry.addAll(str_industry1);
				str_create_time.addAll(str_create_time1);
				str_status.addAll(str_status1);
				str_name.addAll(str_name1);
				str_contact.addAll(str_contact1);
				str_avatar.addAll(str_avatar1);
				str_proof.addAll(str_proof1);
				str_proof_id.addAll(str_proof_id1);
				adapter.notifyDataSetChanged();
			}
		});
		bt_uncheck.setText("未审核(" + total_unexamine + ")");
		bt_uncheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag="1";
				bt_check.setBackgroundResource(R.drawable.corner_left_gray);
				bt_uncheck.setBackgroundResource(R.drawable.corner_right_black);
				clear();
				str_flag.addAll(str_flag2);
				str_id.addAll(str_id2);
				str_user_id.addAll(str_user_id2);
				str_user_type.addAll(str_user_type2);
				str_building_id.addAll(str_building_id2);
				str_building_name.addAll(str_building_name2);
				str_region_code.addAll(str_region_code2);
				str_transaction_type.addAll(str_transaction_type2);
				str_square_meter.addAll(str_square_meter2);
				str_cj_time.addAll(str_cj_time2);
				str_industry.addAll(str_industry2);
				str_create_time.addAll(str_create_time2);
				str_status.addAll(str_status2);
				str_name.addAll(str_name2);
				str_contact.addAll(str_contact2);
				str_avatar.addAll(str_avatar2);
				str_proof.addAll(str_proof2);
				str_proof_id.addAll(str_proof_id2);
				adapter.notifyDataSetChanged();
			}
		});

		lay_min_history_add = (LinearLayout) findViewById(R.id.lay_min_history_add);
		lay_min_history_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(History_CompleteAct.this,
						New_AddInfoAct.class);
				intent.putExtra("add_v", add_v);
				startActivity(intent);
				History_CompleteAct.this.finish();
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
					History_CompleteAct.this.finish();
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
				Toast.makeText(History_CompleteAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(History_CompleteAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_cjinfomain(String session_id) {
		Request request = Request.requestcjinfomain(session_id);
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
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("list");
					json3 = json2.getJSONObject("data1");
					total_examineed = json3.getString("total");
					joArray = json3.getJSONArray("list");
					for (int i = 0; i < joArray.length(); i++) {
						json = joArray.getJSONObject(i);
						str_id1.add(json.getString("id"));
						str_user_id1.add(json.getString("user_id"));
						str_user_type1.add(json.getString("user_type"));
						str_building_id1.add(json.getString("building_id"));
						str_building_name1.add(json.getString("building_name"));
						str_region_code1.add(json.getString("region_code"));
						str_transaction_type1.add(json
								.getString("transaction_type"));
						str_square_meter1.add(json.getString("square_meter"));
						str_cj_time1.add(json.getString("cj_time"));
						str_industry1.add(json.getString("industry"));
						str_create_time1.add(json.getString("create_time"));
						str_status1.add(json.getString("status"));
						str_name1.add(json.getString("name"));
						str_avatar1.add(json.getString("avatar"));
						str_contact1.add(json.getString("contact"));
						str_proof1.add(json.getString("proof"));
						str_proof_id1.add(json.getString("proof_id"));
						str_flag1.add("0");
					}
					json4 = json2.getJSONObject("data2");
					total_unexamine = json4.getString("total");
					joArray2 = json4.getJSONArray("list");
					for (int i = 0; i < joArray2.length(); i++) {
						json = joArray2.getJSONObject(i);
						str_id2.add(json.getString("id"));
						str_user_id2.add(json.getString("user_id"));
						str_user_type2.add(json.getString("user_type"));
						str_building_id2.add(json.getString("building_id"));
						str_building_name2.add(json.getString("building_name"));
						str_region_code2.add(json.getString("region_code"));
						str_transaction_type2.add(json
								.getString("transaction_type"));
						str_square_meter2.add(json.getString("square_meter"));
						str_cj_time2.add(json.getString("cj_time"));
						str_industry2.add(json.getString("industry"));
						str_create_time2.add(json.getString("create_time"));
						str_status2.add(json.getString("status"));
						str_name2.add(json.getString("name"));
						str_avatar2.add(json.getString("avatar"));
						str_contact2.add(json.getString("contact"));
						str_proof2.add(json.getString("proof"));
						str_proof_id2.add(json.getString("proof_id"));
						str_flag2.add("1");
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

	public void clear() {
		str_id.clear();
		str_user_id.clear();
		str_user_type.clear();
		str_building_id.clear();
		str_building_name.clear();
		str_region_code.clear();
		str_transaction_type.clear();
		str_square_meter.clear();
		str_cj_time.clear();
		str_industry.clear();
		str_create_time.clear();
		str_status.clear();
		str_name.clear();
		str_avatar.clear();
		str_contact.clear();
		str_proof.clear();
		str_flag.clear();
	}

	private void prapare() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
		str_user_id = new ArrayList<String>();
		str_user_type = new ArrayList<String>();
		str_building_id = new ArrayList<String>();
		str_building_name = new ArrayList<String>();
		str_region_code = new ArrayList<String>();
		str_transaction_type = new ArrayList<String>();
		str_square_meter = new ArrayList<String>();
		str_cj_time = new ArrayList<String>();
		str_industry = new ArrayList<String>();
		str_create_time = new ArrayList<String>();
		str_status = new ArrayList<String>();
		str_name = new ArrayList<String>();
		str_avatar = new ArrayList<String>();
		str_contact = new ArrayList<String>();
		str_proof = new ArrayList<String>();
		str_proof_id = new ArrayList<String>();
		str_flag=new ArrayList<String>();

		str_id1 = new ArrayList<String>();
		str_user_id1 = new ArrayList<String>();
		str_user_type1 = new ArrayList<String>();
		str_building_id1 = new ArrayList<String>();
		str_building_name1 = new ArrayList<String>();
		str_region_code1 = new ArrayList<String>();
		str_transaction_type1 = new ArrayList<String>();
		str_square_meter1 = new ArrayList<String>();
		str_cj_time1 = new ArrayList<String>();
		str_industry1 = new ArrayList<String>();
		str_create_time1 = new ArrayList<String>();
		str_status1 = new ArrayList<String>();
		str_name1 = new ArrayList<String>();
		str_avatar1 = new ArrayList<String>();
		str_contact1 = new ArrayList<String>();
		str_proof1 = new ArrayList<String>();
		str_proof_id1 = new ArrayList<String>();
		str_flag1=new ArrayList<String>();

		str_id2 = new ArrayList<String>();
		str_user_id2 = new ArrayList<String>();
		str_user_type2 = new ArrayList<String>();
		str_building_id2 = new ArrayList<String>();
		str_building_name2 = new ArrayList<String>();
		str_region_code2 = new ArrayList<String>();
		str_transaction_type2 = new ArrayList<String>();
		str_square_meter2 = new ArrayList<String>();
		str_cj_time2 = new ArrayList<String>();
		str_industry2 = new ArrayList<String>();
		str_create_time2 = new ArrayList<String>();
		str_status2 = new ArrayList<String>();
		str_name2 = new ArrayList<String>();
		str_avatar2 = new ArrayList<String>();
		str_contact2 = new ArrayList<String>();
		str_proof2 = new ArrayList<String>();
		str_proof_id2 = new ArrayList<String>();
		str_flag2=new ArrayList<String>();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
			simulateKey(KeyEvent.KEYCODE_BACK);
            return true;
        }
		return super.onKeyDown(keyCode, event);
	}
}
