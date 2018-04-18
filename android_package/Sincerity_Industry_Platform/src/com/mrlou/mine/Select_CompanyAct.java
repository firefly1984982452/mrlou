package com.mrlou.mine;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.adapter.Add_BuildAdapter;
import com.mrlou.identify.Idenfity_registerAct;
import com.mrlou.identify.Identify_AgentaAct;
import com.mrlou.identify.Identify_ProAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

/**
 * @author jx_chen
 * @date 2016-11-28---上午10:15:40
 * @problem
 */
public class Select_CompanyAct extends Activity implements HttpListener {

	private ListView lv_selectcompany;
	private Add_BuildAdapter adapter;
	private ArrayList<String> str_name = new ArrayList<>();
	private EditText et_search;
	private LinearLayout lay_selectcompany_complete, lay_selectcompany_back,lay_selectcompany_search;
	private String company_id, act, cn_username, en_username, email,
			phone_number, type, avatar, avatars_id, company, wechat, wechat_qr,
			post_card, post_card_id, wechat_qr_id, user_id, add_v, intro,
			region_id_real, work_start, region_id, building_a, building_b,
			status, service_type, services, services_id, type_id,office_building,office_building_id,good_type;
	private TextView tv_selectcompany_search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_company);
		getDate();
		findView();
	}

	private void getDate() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		act = intent.getStringExtra("act");
		cn_username = intent.getStringExtra("cn_username");
		en_username = intent.getStringExtra("en_username");
		email = intent.getStringExtra("email");
		phone_number = intent.getStringExtra("phone_number");
		user_id = intent.getStringExtra("user_id");
		type = intent.getStringExtra("type");
		avatar = intent.getStringExtra("avatar");
		avatars_id = intent.getStringExtra("avatar_id");
		company = intent.getStringExtra("company");
		wechat = intent.getStringExtra("wechat");
		post_card = intent.getStringExtra("post_card");
		post_card_id = intent.getStringExtra("post_card_id");
		wechat_qr = intent.getStringExtra("wechat_qr");
		wechat_qr_id = intent.getStringExtra("wechat_qr_id");
		company_id = intent.getStringExtra("company_id");
		if (act.equals("1")) {
			add_v = intent.getStringExtra("v");
		} else if (act.equals("2")) {
			intro = intent.getStringExtra("intro");
			work_start = intent.getStringExtra("work_start");
			region_id = intent.getStringExtra("region_id");
			region_id_real = intent.getStringExtra("region_id_real");
			add_v = intent.getStringExtra("v");
			office_building=intent.getStringExtra("office_building");
			office_building_id=intent.getStringExtra("office_building_id");
			good_type=intent.getStringExtra("good_type");
		} else if (act.equals("3")) {
			building_a = intent.getStringExtra("building_a");
			building_b = intent.getStringExtra("building_b");
			status = intent.getStringExtra("status");
			add_v = intent.getStringExtra("v");
		} else if (act.equals("4")) {
			work_start = intent.getStringExtra("work_start");
			region_id = intent.getStringExtra("region_id");
			region_id_real = intent.getStringExtra("region_id_real");
			service_type = intent.getStringExtra("service_type");
			intro = intent.getStringExtra("intro");
			add_v = intent.getStringExtra("v");
			office_building=intent.getStringExtra("office_building");
			office_building_id=intent.getStringExtra("office_building_id");
		} else if (act.equals("5")) {
			region_id = intent.getStringExtra("region_id");
			region_id_real = intent.getStringExtra("region_id_real");
			services = intent.getStringExtra("services");
			services_id = intent.getStringExtra("services_id");
		} else if (act.equals("6")) {
			type_id = intent.getStringExtra("type_id");
			building_a = intent.getStringExtra("building_a");
			building_b = intent.getStringExtra("building_b");
		} else if (act.equals("7")) {
			region_id = intent.getStringExtra("region_id");
			region_id_real = intent.getStringExtra("region_id_real");
			building_a = intent.getStringExtra("building_a");
			building_b = intent.getStringExtra("building_b");
		}
	}

	private void findView() {
		// TODO Auto-generated method stub
		lv_selectcompany = (ListView) findViewById(R.id.lv_selectcompany);
		adapter = new Add_BuildAdapter(Select_CompanyAct.this, str_name);
		lv_selectcompany.setAdapter(adapter);
		lv_selectcompany.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				lay_selectcompany_search.setVisibility(View.VISIBLE);
				tv_selectcompany_search.setText(str_name.get(position));
				company=str_name.get(position);
//				et_search.setText(str_name.get(position));
			}
		});
		et_search = (EditText) findViewById(R.id.et_selectcompany_search);
		et_search.addTextChangedListener(mTextWatcher);
		tv_selectcompany_search=(TextView) findViewById(R.id.tv_selectcompany_search);
		lay_selectcompany_search=(LinearLayout) findViewById(R.id.lay_selectcompany_search);
		lay_selectcompany_complete = (LinearLayout) findViewById(R.id.lay_selectcompany_complete);
		lay_selectcompany_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_search.getText().toString().equals("")) {
					Toast.makeText(Select_CompanyAct.this, "请输入相对应公司名称", Toast.LENGTH_SHORT)
							.show();
				} else  {
					Intent_Act();
				}
			}
		});
		lay_selectcompany_back = (LinearLayout) findViewById(R.id.lay_selectcompany_back);
		lay_selectcompany_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent_Act();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Intent_Act();
		return super.onKeyDown(keyCode, event);
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			temp = s;

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			str_name.clear();
			adapter.notifyDataSetChanged();
			select_selectcompany(et_search.getText().toString());
		}
	};

	public void select_selectcompany(String key) {
		Request request = Request.requestselectcompany(key);
		new HttpThread(request, this);
	}


	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(Select_CompanyAct.this, "系统无法找到该公司，可直接点击添加",
						Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Select_CompanyAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
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
					joArray = json.getJSONArray("data");
					if (joArray.length() == 0) {
						newHandler.sendEmptyMessage(2);
					} else {
						for (int i = 0; i < joArray.length(); i++) {
							json = joArray.getJSONObject(i);
							str_name.add(json.getString("name"));
						}
						newHandler.sendEmptyMessage(1);
					}
				} else {
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

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Select_CompanyAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void Intent_Act() {
		Intent intents = null;
		if (act.equals("1")) {
			intents = new Intent(Select_CompanyAct.this,
					Setting_personal01.class);
			intents.putExtra("v", add_v);
		} else if (act.equals("2")) {
			intents = new Intent(Select_CompanyAct.this,
					Setting_personal02.class);
			intents.putExtra("v", add_v);
			intents.putExtra("intro", intro);
			intents.putExtra("work_start", work_start);
			intents.putExtra("region_id", region_id);
			intents.putExtra("region_id_real", region_id_real);
			intents.putExtra("office_building", office_building);
			intents.putExtra("office_building_id", office_building_id);
			intents.putExtra("good_type", good_type);
		} else if (act.equals("3")) {
			intents = new Intent(Select_CompanyAct.this,
					Setting_personal03.class);
			intents.putExtra("v", add_v);
			intents.putExtra("building_a", building_a);
			intents.putExtra("building_b", building_b);
			intents.putExtra("status", status);
		} else if (act.equals("4")) {
			intents = new Intent(Select_CompanyAct.this,
					Setting_personal04.class);
			intents.putExtra("v", add_v);
			intents.putExtra("work_start", work_start);
			intents.putExtra("region_id", region_id);
			intents.putExtra("region_id_real", region_id_real);
			intents.putExtra("service_type", service_type);
			intents.putExtra("intro", intro);
			intents.putExtra("office_building", office_building);
			intents.putExtra("office_building_id", office_building_id);
		} else if (act.equals("5")) {
			intents = new Intent(Select_CompanyAct.this,
					Idenfity_registerAct.class);
			intents.putExtra("region_id", region_id);
			intents.putExtra("region_id_real", region_id_real);
			intents.putExtra("services", services);
			intents.putExtra("services_id", services_id);
		} else if (act.equals("6")) {
			intents = new Intent(Select_CompanyAct.this, Identify_ProAct.class);
			intents.putExtra("type_id", type_id);
			intents.putExtra("building_a", building_a);
			intents.putExtra("building_b", building_b);
		} else if (act.equals("7")) {
			intents = new Intent(Select_CompanyAct.this,
					Identify_AgentaAct.class);
			intents.putExtra("region_id", region_id);
			intents.putExtra("region_id_real", region_id_real);
			intents.putExtra("building_a", building_a);
			intents.putExtra("building_b", building_b);
		}
		intents.putExtra("user_id", user_id);
		intents.putExtra("cn_username", cn_username);
		intents.putExtra("en_username", en_username);
		intents.putExtra("email", email);
		intents.putExtra("phone_number", phone_number);
		intents.putExtra("type", type);
		intents.putExtra("avatar", avatar);
		intents.putExtra("avatar_id", avatars_id);
		intents.putExtra("company", et_search.getText().toString());
		intents.putExtra("wechat", wechat);
		intents.putExtra("wechat_qr", wechat_qr);
		intents.putExtra("post_card", post_card);
		intents.putExtra("wechat_qr_id", wechat_qr_id);
		intents.putExtra("post_card_id", post_card_id);
		intents.putExtra("company_id", company_id);
		startActivity(intents);
		Select_CompanyAct.this.finish();
	}
}
