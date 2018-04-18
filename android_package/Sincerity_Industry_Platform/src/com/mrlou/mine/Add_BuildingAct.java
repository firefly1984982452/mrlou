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
import com.mrlou.h5.Building_Act;
import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_SingleBuilderAct;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class Add_BuildingAct extends Activity implements HttpListener {

	private EditText et_search;
	private LinearLayout lay_mine_addbuilding_confirm, lay_back,
			lay_addbuilding_complete;
	private String buliding_a, buliding_b;
	private ListView lv_addbuilding;
	private LinearLayout lay_01, lay_02, lay_03, lay_04, lay_05, lay_06,
			lay_07, lay_08, lay_09, lay_10, lay_11, lay_12, lay_13, lay_14,
			lay_15, lay_16, lay_17, lay_18, lay_19, lay_20;
	private TextView tv_01, tv_02, tv_03, tv_04, tv_05, tv_06, tv_07, tv_08,
			tv_09, tv_10, tv_11, tv_12, tv_13, tv_14, tv_15, tv_16, tv_17,
			tv_18, tv_19, tv_20;
	private TextView tv_addbuilding_type01, tv_addbuilding_type02,
			tv_addbuilding_type03, tv_addbuilding_type04,
			tv_addbuilding_type05, tv_addbuilding_type06,
			tv_addbuilding_type07, tv_addbuilding_type08,
			tv_addbuilding_type09, tv_addbuilding_type10,
			tv_addbuilding_type11, tv_addbuilding_type12,
			tv_addbuilding_type13, tv_addbuilding_type14,
			tv_addbuilding_type15, tv_addbuilding_type16,
			tv_addbuilding_type17, tv_addbuilding_type18,
			tv_addbuilding_type19, tv_addbuilding_type20;
	private LinearLayout lay_iv_01, lay_iv_02, lay_iv_03, lay_iv_04, lay_iv_05,
			lay_iv_06, lay_iv_07, lay_iv_08, lay_iv_09, lay_iv_10, lay_iv_11,
			lay_iv_12, lay_iv_13, lay_iv_14, lay_iv_15, lay_iv_16, lay_iv_17,
			lay_iv_18, lay_iv_19, lay_iv_20;
	private String phone_number, email, en_username, cn_username,
			company, avatar, status, type, user_id;
	private String avatars_id, wechat_qr_id, post_card_id,
			post_card, wechat, wechat_qr,company_id,add_v;
	private Add_BuildAdapter adapter;
	private ArrayList<String> str_content = new ArrayList<>();
	private ArrayList<String> str_name = new ArrayList<>();
	private ArrayList<String> str_flag = new ArrayList<>();
	private int m, n, num_len;
	private String[] str, str2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_addbuilding);
		getDate();
		init();
		findView();
	}

	private void getDate() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		phone_number = intent.getStringExtra("phone_number");
		email = intent.getStringExtra("email");
		en_username = intent.getStringExtra("en_username");
		cn_username = intent.getStringExtra("cn_username");
		company = intent.getStringExtra("company");
		buliding_a = intent.getStringExtra("building_a");
		buliding_b = intent.getStringExtra("building_b");
		avatar = intent.getStringExtra("avatar");
		avatars_id = intent.getStringExtra("avatar_id");
		status = intent.getStringExtra("status");
		type = intent.getStringExtra("type");
		user_id = intent.getStringExtra("user_id");
		post_card = intent.getStringExtra("post_card");
		post_card_id = intent.getStringExtra("post_card_id");
		wechat_qr = intent.getStringExtra("wechat_qr");
		wechat_qr_id = intent.getStringExtra("wechat_qr_id");
		wechat = intent.getStringExtra("wechat");
		company_id=intent.getStringExtra("company_id");
		add_v=intent.getStringExtra("v");
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_addbuilding_complete = (LinearLayout) findViewById(R.id.lay_addbuilding_complete);
		lay_addbuilding_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buliding_a = "";
				buliding_b = "";
				int lenth=str_name.size()-1;
				for (int i = lenth; i >=0; i--) {
					if (str_name.get(i).equals("0")) {
						str_name.remove(i);
						str_flag.remove(i);
					}else {
						if (str_name.get(i).equals("")) {
							str_name.remove(i);
							str_flag.remove(i);
						}
					}
				}
				if (str_name.size() == 1) {
					if (str_flag.get(0).equals("1")) {
						buliding_a = str_name.get(0);
					}else {
						buliding_b = str_name.get(0);
					}
				} else {
					for (int j = 0; j < str_name.size(); j++) {
						if (j == 0) {
							if (str_flag.get(0).equals("1")) {
								buliding_a = str_name.get(0);
							}else {
								buliding_b = str_name.get(0);
							}
						} else {
							if (str_flag.get(j).equals("1")) {
								buliding_a = buliding_a + "," + str_name.get(j);
							}else if(!buliding_b.equals("")){
								buliding_b = buliding_b + "," + str_name.get(j);
							}else {
								buliding_b=str_name.get(j);
							}
						}
					}
				}
				
				Intent intent;
				intent = new Intent(Add_BuildingAct.this,
						Setting_personal03.class);
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("phone_number", phone_number);
				intent.putExtra("company", company);
				intent.putExtra("company_id", company_id);
				intent.putExtra("type", type);
				intent.putExtra("user_id", user_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("status", status);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", buliding_a);
				intent.putExtra("building_b", buliding_b);
				intent.putExtra("wechat", wechat);
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("v", add_v);
				startActivity(intent);
				Add_BuildingAct.this.finish();
			}
		});
		if (!buliding_a.equals("")) {
			str = buliding_a.split(",");
			m = str.length;
			for (int i = 0; i < m; i++) {
				str_name.add(str[i]);
				str_flag.add("1");
			}
			num_len = str_flag.size();
			if (!buliding_b.equals("")) {
				str2 = buliding_b.split(",");
				n = str2.length;
				for (int i = 0; i < n; i++) {
					str_name.add(str2[i]);
					str_flag.add("2");
				}
				num_len = str_flag.size();
			}
			if (num_len == 1) {
				select(1, m);
				select_type(m);
			} else {
				select(num_len, m);
				select_type(m);
			}
		} else if (!buliding_b.equals("")){
			if (!buliding_b.equals("")) {
				str2 = buliding_b.split(",");
				n = str2.length;
				for (int i = 0; i < n; i++) {
					str_name.add(str2[i]);
					str_flag.add("2");
				}
				num_len = str_flag.size();
			}
			if (num_len == 1) {
				select(1, m);
				select_type(m);
			} else {
				select(num_len, m);
				select_type(m);
			}
		}else {
			num_len = 0;
		}

		lay_mine_addbuilding_confirm = (LinearLayout) findViewById(R.id.lay_mine_addbuilding_confirm);
		lay_mine_addbuilding_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_search.getText().toString().equals("")) {
					Toast.makeText(Add_BuildingAct.this, "请输入相对应楼盘", Toast.LENGTH_SHORT)
							.show();
				} else {
					str_name.add(et_search.getText().toString());
					str_flag.add("2");
					if (num_len <= 20) {
						int lengths=str_name.size()-1;
						for (int i = lengths; i >=0; i--) {
							if (str_name.get(i).equals("0")) {
								str_name.remove(i);
								str_flag.remove(i);
							}
						}
						select(num_len + 1, m);
						select_type(m);
						num_len = num_len + 1;
					} else {
						Toast.makeText(Add_BuildingAct.this,
								"您添加的已经超过20个了，最多只能添加20个", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		lv_addbuilding = (ListView) findViewById(R.id.lv_addbuilding);
		lv_addbuilding.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (m <= 20) {
					str_name.add(str_content.get(position));
					str_flag.add("2");
					int lengths=str_name.size()-1;
					for (int i = lengths; i >=0; i--) {
						if (str_name.get(i).equals("0")) {
							str_name.remove(i);
							str_flag.remove(i);
						}
					}
					select(num_len + 1, m);
					select_type(m);
					num_len = num_len + 1;
					// str_name.add(str_content.get(position));
					// select(m + 1);
					// m = m + 1;
				} else {
					Toast.makeText(Add_BuildingAct.this,
							"您添加的已经超过20个了，最多只能添加20个", Toast.LENGTH_SHORT).show();
				}
			}
		});
		adapter = new Add_BuildAdapter(Add_BuildingAct.this, str_content);
		lv_addbuilding.setAdapter(adapter);
		lay_back = (LinearLayout) findViewById(R.id.lay_addbuilding_back);
		lay_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				intent = new Intent(Add_BuildingAct.this,
						Setting_personal03.class);
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("phone_number", phone_number);
				intent.putExtra("company", company);
				intent.putExtra("company_id", company_id);
				intent.putExtra("type", type);
				intent.putExtra("user_id", user_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("status", status);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", buliding_a);
				intent.putExtra("building_b", buliding_b);
				intent.putExtra("wechat", wechat);
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("v", add_v);
				startActivity(intent);
				Add_BuildingAct.this.finish();
			}
		});
		et_search = (EditText) findViewById(R.id.et_addbuilding_search);
		et_search.addTextChangedListener(mTextWatcher);
	}

	public void init() {
		lay_01 = (LinearLayout) findViewById(R.id.lay_hide01);
		tv_01 = (TextView) findViewById(R.id.tv_addbuilding01);
		tv_addbuilding_type01=(TextView) findViewById(R.id.tv_addbuilding_type01);
		lay_iv_01 = (LinearLayout) findViewById(R.id.lay_addbuilding01);
		lay_iv_01.setOnClickListener(new bt_layout());
		lay_02 = (LinearLayout) findViewById(R.id.lay_hide02);
		tv_02 = (TextView) findViewById(R.id.tv_addbuilding02);
		tv_addbuilding_type02=(TextView) findViewById(R.id.tv_addbuilding_type02);
		lay_iv_02 = (LinearLayout) findViewById(R.id.lay_addbuilding02);
		lay_iv_02.setOnClickListener(new bt_layout());
		lay_03 = (LinearLayout) findViewById(R.id.lay_hide03);
		tv_03 = (TextView) findViewById(R.id.tv_addbuilding03);
		lay_iv_03 = (LinearLayout) findViewById(R.id.lay_addbuilding03);
		tv_addbuilding_type03=(TextView) findViewById(R.id.tv_addbuilding_type03);
		lay_iv_03.setOnClickListener(new bt_layout());
		lay_04 = (LinearLayout) findViewById(R.id.lay_hide04);
		tv_04 = (TextView) findViewById(R.id.tv_addbuilding04);
		lay_iv_04 = (LinearLayout) findViewById(R.id.lay_addbuilding04);
		tv_addbuilding_type04=(TextView) findViewById(R.id.tv_addbuilding_type04);
		lay_iv_04.setOnClickListener(new bt_layout());
		lay_05 = (LinearLayout) findViewById(R.id.lay_hide05);
		tv_05 = (TextView) findViewById(R.id.tv_addbuilding05);
		lay_iv_05 = (LinearLayout) findViewById(R.id.lay_addbuilding05);
		tv_addbuilding_type05=(TextView) findViewById(R.id.tv_addbuilding_type05);
		lay_iv_05.setOnClickListener(new bt_layout());
		lay_06 = (LinearLayout) findViewById(R.id.lay_hide06);
		tv_06 = (TextView) findViewById(R.id.tv_addbuilding06);
		lay_iv_06 = (LinearLayout) findViewById(R.id.lay_addbuilding06);
		tv_addbuilding_type06=(TextView) findViewById(R.id.tv_addbuilding_type06);
		lay_iv_06.setOnClickListener(new bt_layout());
		lay_07 = (LinearLayout) findViewById(R.id.lay_hide07);
		tv_07 = (TextView) findViewById(R.id.tv_addbuilding07);
		lay_iv_07 = (LinearLayout) findViewById(R.id.lay_addbuilding07);
		tv_addbuilding_type07=(TextView) findViewById(R.id.tv_addbuilding_type07);
		lay_iv_07.setOnClickListener(new bt_layout());
		lay_08 = (LinearLayout) findViewById(R.id.lay_hide08);
		tv_08 = (TextView) findViewById(R.id.tv_addbuilding08);
		lay_iv_08 = (LinearLayout) findViewById(R.id.lay_addbuilding08);
		tv_addbuilding_type08=(TextView) findViewById(R.id.tv_addbuilding_type08);
		lay_iv_08.setOnClickListener(new bt_layout());
		lay_09 = (LinearLayout) findViewById(R.id.lay_hide09);
		tv_09 = (TextView) findViewById(R.id.tv_addbuilding09);
		lay_iv_09 = (LinearLayout) findViewById(R.id.lay_addbuilding09);
		tv_addbuilding_type09=(TextView) findViewById(R.id.tv_addbuilding_type09);
		lay_iv_09.setOnClickListener(new bt_layout());
		lay_10 = (LinearLayout) findViewById(R.id.lay_hide10);
		tv_10 = (TextView) findViewById(R.id.tv_addbuilding10);
		lay_iv_10 = (LinearLayout) findViewById(R.id.lay_addbuilding10);
		tv_addbuilding_type10=(TextView) findViewById(R.id.tv_addbuilding_type10);
		lay_iv_10.setOnClickListener(new bt_layout());
		lay_11 = (LinearLayout) findViewById(R.id.lay_hide11);
		tv_11 = (TextView) findViewById(R.id.tv_addbuilding11);
		lay_iv_11 = (LinearLayout) findViewById(R.id.lay_addbuilding11);
		tv_addbuilding_type11=(TextView) findViewById(R.id.tv_addbuilding_type11);
		lay_iv_11.setOnClickListener(new bt_layout());
		lay_12 = (LinearLayout) findViewById(R.id.lay_hide12);
		tv_12 = (TextView) findViewById(R.id.tv_addbuilding12);
		lay_iv_12 = (LinearLayout) findViewById(R.id.lay_addbuilding12);
		tv_addbuilding_type12=(TextView) findViewById(R.id.tv_addbuilding_type12);
		lay_iv_12.setOnClickListener(new bt_layout());
		lay_13 = (LinearLayout) findViewById(R.id.lay_hide13);
		tv_13 = (TextView) findViewById(R.id.tv_addbuilding13);
		lay_iv_13 = (LinearLayout) findViewById(R.id.lay_addbuilding13);
		tv_addbuilding_type13=(TextView) findViewById(R.id.tv_addbuilding_type13);
		lay_iv_13.setOnClickListener(new bt_layout());
		lay_14 = (LinearLayout) findViewById(R.id.lay_hide14);
		tv_14 = (TextView) findViewById(R.id.tv_addbuilding14);
		lay_iv_14 = (LinearLayout) findViewById(R.id.lay_addbuilding14);
		tv_addbuilding_type14=(TextView) findViewById(R.id.tv_addbuilding_type14);
		lay_iv_14.setOnClickListener(new bt_layout());
		lay_15 = (LinearLayout) findViewById(R.id.lay_hide15);
		tv_15 = (TextView) findViewById(R.id.tv_addbuilding15);
		lay_iv_15 = (LinearLayout) findViewById(R.id.lay_addbuilding15);
		tv_addbuilding_type15=(TextView) findViewById(R.id.tv_addbuilding_type15);
		lay_iv_15.setOnClickListener(new bt_layout());
		lay_16 = (LinearLayout) findViewById(R.id.lay_hide16);
		tv_16 = (TextView) findViewById(R.id.tv_addbuilding16);
		lay_iv_16 = (LinearLayout) findViewById(R.id.lay_addbuilding16);
		tv_addbuilding_type16=(TextView) findViewById(R.id.tv_addbuilding_type16);
		lay_iv_16.setOnClickListener(new bt_layout());
		lay_17 = (LinearLayout) findViewById(R.id.lay_hide17);
		tv_17 = (TextView) findViewById(R.id.tv_addbuilding17);
		lay_iv_17 = (LinearLayout) findViewById(R.id.lay_addbuilding17);
		tv_addbuilding_type17=(TextView) findViewById(R.id.tv_addbuilding_type17);
		lay_iv_17.setOnClickListener(new bt_layout());
		lay_18 = (LinearLayout) findViewById(R.id.lay_hide18);
		tv_18 = (TextView) findViewById(R.id.tv_addbuilding18);
		lay_iv_18 = (LinearLayout) findViewById(R.id.lay_addbuilding18);
		tv_addbuilding_type18=(TextView) findViewById(R.id.tv_addbuilding_type18);
		lay_iv_18.setOnClickListener(new bt_layout());
		lay_19 = (LinearLayout) findViewById(R.id.lay_hide19);
		tv_19 = (TextView) findViewById(R.id.tv_addbuilding19);
		lay_iv_19 = (LinearLayout) findViewById(R.id.lay_addbuilding19);
		tv_addbuilding_type19=(TextView) findViewById(R.id.tv_addbuilding_type19);
		lay_iv_19.setOnClickListener(new bt_layout());
		lay_20 = (LinearLayout) findViewById(R.id.lay_hide20);
		tv_20 = (TextView) findViewById(R.id.tv_addbuilding20);
		lay_iv_20 = (LinearLayout) findViewById(R.id.lay_addbuilding20);
		tv_addbuilding_type20=(TextView) findViewById(R.id.tv_addbuilding_type20);
		lay_iv_20.setOnClickListener(new bt_layout());
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
			str_content.clear();
			adapter.notifyDataSetChanged();
			select_cjinfomain(et_search.getText().toString());
		}
	};

	class bt_layout implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			num_len--;
			switch (v.getId()) {
			case R.id.lay_addbuilding01:
				str_name.set(0, "0");
//				str_name.remove(0);
				lay_01.setVisibility(View.GONE);
//				str_flag.remove(0);
				break;
			case R.id.lay_addbuilding02:
				str_name.set(1, "0");
//				str_name.remove(1);
//				str_flag.remove(1);
				lay_02.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding03:
				str_name.set(2, "0");
//				str_name.remove(2);
//				str_flag.remove(2);
				lay_03.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding04:
				str_name.set(3, "0");
//				str_name.remove(3);
//				str_flag.remove(3);
				lay_04.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding05:
				str_name.set(4, "0");
//				str_name.remove(4);
//				str_flag.remove(4);
				lay_05.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding06:
				str_name.set(5, "0");
//				str_name.remove(5);
//				str_flag.remove(5);
				lay_06.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding07:
				str_name.set(6, "0");
//				str_name.remove(6);
//				str_flag.remove(6);
				lay_07.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding08:
				str_name.set(7, "0");
//				str_name.remove(7);
//				str_flag.remove(7);
				lay_08.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding09:
				str_name.set(8, "0");
//				str_name.remove(8);
//				str_flag.remove(8);
				lay_09.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding10:
				str_name.set(9, "0");
//				str_name.remove(9);
//				str_flag.remove(9);
				lay_10.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding11:
				str_name.set(10, "0");
//				str_name.remove(10);
//				str_flag.remove(10);
				lay_11.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding12:
				str_name.set(11, "0");
//				str_name.remove(11);
//				str_flag.remove(11);
				lay_12.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding13:
				str_name.set(12, "0");
//				str_name.remove(12);
//				str_flag.remove(12);
				lay_13.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding14:
				str_name.set(13, "0");
//				str_name.remove(13);
//				str_flag.remove(13);
				lay_14.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding15:
				str_name.set(14, "0");
//				str_name.remove(14);
//				str_flag.remove(14);
				lay_15.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding16:
				str_name.set(15, "0");
//				str_name.remove(15);
//				str_flag.remove(15);
				lay_16.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding17:
				str_name.set(16, "0");
//				str_name.remove(16);
//				str_flag.remove(16);
				lay_17.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding18:
				str_name.set(17, "0");
//				str_name.remove(17);
//				str_flag.remove(17);
				lay_18.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding19:
				str_name.set(18, "0");
//				str_name.remove(18);
//				str_flag.remove(18);
				lay_19.setVisibility(View.GONE);
				break;
			case R.id.lay_addbuilding20:
				str_name.set(19, "0");
//				str_name.remove(19);
//				str_flag.remove(19);
				lay_20.setVisibility(View.GONE);
				break;
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent;
			intent = new Intent(Add_BuildingAct.this, Setting_personal03.class);
			intent.putExtra("cn_username", cn_username);
			intent.putExtra("en_username", en_username);
			intent.putExtra("email", email);
			intent.putExtra("phone_number", phone_number);
			intent.putExtra("company", company);
			intent.putExtra("company_id", company_id);
			intent.putExtra("type", type);
			intent.putExtra("user_id", user_id);
			intent.putExtra("avatar", avatar);
			intent.putExtra("wechat_qr_id", wechat_qr_id);
			intent.putExtra("post_card_id", post_card_id);
			intent.putExtra("avatar_id", avatars_id);
			intent.putExtra("building_a", buliding_a);
			intent.putExtra("building_b", buliding_b);
			intent.putExtra("wechat", wechat);
			intent.putExtra("wechat_qr", wechat_qr);
			intent.putExtra("post_card", post_card);
			intent.putExtra("v", add_v);
			startActivity(intent);
			Add_BuildingAct.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Add_BuildingAct.this.finish();
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
				adapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(Add_BuildingAct.this, "系统无法找到该楼盘，可直接点击确认添加",
						Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Add_BuildingAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_cjinfomain(String key) {
		Request request = Request.requestaddbuild(key);
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
					if (joArray.length() == 0) {
						newHandler.sendEmptyMessage(2);
					} else {
						for (int i = 0; i < joArray.length(); i++) {
							json = joArray.getJSONObject(i);
							str_content.add(json.getString("name"));
						}
						newHandler.sendEmptyMessage(1);
					}
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

	public void select(int position, int num) {
		switch (position) {
		case 1:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			break;
		case 2:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			break;
		case 3:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			break;
		case 4:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			break;
		case 5:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			break;
		case 6:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			break;
		case 7:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			break;
		case 8:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			break;
		case 9:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			break;
		case 10:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			break;
		case 11:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			break;
		case 12:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			lay_12.setVisibility(View.VISIBLE);
			tv_12.setText(str_name.get(11));
			break;
		case 13:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			lay_12.setVisibility(View.VISIBLE);
			tv_12.setText(str_name.get(11));
			lay_13.setVisibility(View.VISIBLE);
			tv_13.setText(str_name.get(12));
			break;
		case 14:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			lay_12.setVisibility(View.VISIBLE);
			tv_12.setText(str_name.get(11));
			lay_13.setVisibility(View.VISIBLE);
			tv_13.setText(str_name.get(12));
			lay_14.setVisibility(View.VISIBLE);
			tv_14.setText(str_name.get(13));
			break;
		case 15:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			lay_12.setVisibility(View.VISIBLE);
			tv_12.setText(str_name.get(11));
			lay_13.setVisibility(View.VISIBLE);
			tv_13.setText(str_name.get(12));
			lay_14.setVisibility(View.VISIBLE);
			tv_14.setText(str_name.get(13));
			lay_15.setVisibility(View.VISIBLE);
			tv_15.setText(str_name.get(14));
			break;
		case 16:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			lay_12.setVisibility(View.VISIBLE);
			tv_12.setText(str_name.get(11));
			lay_13.setVisibility(View.VISIBLE);
			tv_13.setText(str_name.get(12));
			lay_14.setVisibility(View.VISIBLE);
			tv_14.setText(str_name.get(13));
			lay_15.setVisibility(View.VISIBLE);
			tv_15.setText(str_name.get(14));
			lay_16.setVisibility(View.VISIBLE);
			tv_16.setText(str_name.get(15));
			break;
		case 17:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			lay_12.setVisibility(View.VISIBLE);
			tv_12.setText(str_name.get(11));
			lay_13.setVisibility(View.VISIBLE);
			tv_13.setText(str_name.get(12));
			lay_14.setVisibility(View.VISIBLE);
			tv_14.setText(str_name.get(13));
			lay_15.setVisibility(View.VISIBLE);
			tv_15.setText(str_name.get(14));
			lay_16.setVisibility(View.VISIBLE);
			tv_16.setText(str_name.get(15));
			lay_17.setVisibility(View.VISIBLE);
			tv_17.setText(str_name.get(16));
			break;
		case 18:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			lay_12.setVisibility(View.VISIBLE);
			tv_12.setText(str_name.get(11));
			lay_13.setVisibility(View.VISIBLE);
			tv_13.setText(str_name.get(12));
			lay_14.setVisibility(View.VISIBLE);
			tv_14.setText(str_name.get(13));
			lay_15.setVisibility(View.VISIBLE);
			tv_15.setText(str_name.get(14));
			lay_16.setVisibility(View.VISIBLE);
			tv_16.setText(str_name.get(15));
			lay_17.setVisibility(View.VISIBLE);
			tv_17.setText(str_name.get(16));
			lay_18.setVisibility(View.VISIBLE);
			tv_18.setText(str_name.get(17));
			break;
		case 19:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			lay_12.setVisibility(View.VISIBLE);
			tv_12.setText(str_name.get(11));
			lay_13.setVisibility(View.VISIBLE);
			tv_13.setText(str_name.get(12));
			lay_14.setVisibility(View.VISIBLE);
			tv_14.setText(str_name.get(13));
			lay_15.setVisibility(View.VISIBLE);
			tv_15.setText(str_name.get(14));
			lay_16.setVisibility(View.VISIBLE);
			tv_16.setText(str_name.get(15));
			lay_17.setVisibility(View.VISIBLE);
			tv_17.setText(str_name.get(16));
			lay_18.setVisibility(View.VISIBLE);
			tv_18.setText(str_name.get(17));
			lay_19.setVisibility(View.VISIBLE);
			tv_19.setText(str_name.get(18));
			break;
		case 20:
			lay_01.setVisibility(View.VISIBLE);
			tv_01.setText(str_name.get(0));
			lay_02.setVisibility(View.VISIBLE);
			tv_02.setText(str_name.get(1));
			lay_03.setVisibility(View.VISIBLE);
			tv_03.setText(str_name.get(2));
			lay_04.setVisibility(View.VISIBLE);
			tv_04.setText(str_name.get(3));
			lay_05.setVisibility(View.VISIBLE);
			tv_05.setText(str_name.get(4));
			lay_06.setVisibility(View.VISIBLE);
			tv_06.setText(str_name.get(5));
			lay_07.setVisibility(View.VISIBLE);
			tv_07.setText(str_name.get(6));
			lay_08.setVisibility(View.VISIBLE);
			tv_08.setText(str_name.get(7));
			lay_09.setVisibility(View.VISIBLE);
			tv_09.setText(str_name.get(8));
			lay_10.setVisibility(View.VISIBLE);
			tv_10.setText(str_name.get(9));
			lay_11.setVisibility(View.VISIBLE);
			tv_11.setText(str_name.get(10));
			lay_12.setVisibility(View.VISIBLE);
			tv_12.setText(str_name.get(11));
			lay_13.setVisibility(View.VISIBLE);
			tv_13.setText(str_name.get(12));
			lay_14.setVisibility(View.VISIBLE);
			tv_14.setText(str_name.get(13));
			lay_15.setVisibility(View.VISIBLE);
			tv_15.setText(str_name.get(14));
			lay_16.setVisibility(View.VISIBLE);
			tv_16.setText(str_name.get(15));
			lay_17.setVisibility(View.VISIBLE);
			tv_17.setText(str_name.get(16));
			lay_18.setVisibility(View.VISIBLE);
			tv_18.setText(str_name.get(17));
			lay_19.setVisibility(View.VISIBLE);
			tv_19.setText(str_name.get(18));
			lay_20.setVisibility(View.VISIBLE);
			tv_20.setText(str_name.get(19));
			break;
		}
	}
	public void select_type( int num) {
		switch (num) {
		case 1:
			tv_addbuilding_type01.setText("已认证");
			break;
		case 2:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			break;
		case 3:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			break;
		case 4:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			break;
		case 5:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			break;
		case 6:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			break;
		case 7:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			break;
		case 8:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			break;
		case 9:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			break;
		case 10:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			break;
		case 11:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			break;
		case 12:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			tv_addbuilding_type12.setText("已认证");
			break;
		case 13:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			tv_addbuilding_type12.setText("已认证");
			tv_addbuilding_type13.setText("已认证");
			break;
		case 14:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			tv_addbuilding_type12.setText("已认证");
			tv_addbuilding_type13.setText("已认证");
			tv_addbuilding_type14.setText("已认证");
			break;
		case 15:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			tv_addbuilding_type12.setText("已认证");
			tv_addbuilding_type13.setText("已认证");
			tv_addbuilding_type14.setText("已认证");
			tv_addbuilding_type15.setText("已认证");
			break;
		case 16:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			tv_addbuilding_type12.setText("已认证");
			tv_addbuilding_type13.setText("已认证");
			tv_addbuilding_type14.setText("已认证");
			tv_addbuilding_type15.setText("已认证");
			tv_addbuilding_type16.setText("已认证");
			break;
		case 17:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			tv_addbuilding_type12.setText("已认证");
			tv_addbuilding_type13.setText("已认证");
			tv_addbuilding_type14.setText("已认证");
			tv_addbuilding_type15.setText("已认证");
			tv_addbuilding_type16.setText("已认证");
			tv_addbuilding_type17.setText("已认证");
			break;
		case 18:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			tv_addbuilding_type12.setText("已认证");
			tv_addbuilding_type13.setText("已认证");
			tv_addbuilding_type14.setText("已认证");
			tv_addbuilding_type15.setText("已认证");
			tv_addbuilding_type16.setText("已认证");
			tv_addbuilding_type17.setText("已认证");
			tv_addbuilding_type18.setText("已认证");
			break;
		case 19:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			tv_addbuilding_type12.setText("已认证");
			tv_addbuilding_type13.setText("已认证");
			tv_addbuilding_type14.setText("已认证");
			tv_addbuilding_type15.setText("已认证");
			tv_addbuilding_type16.setText("已认证");
			tv_addbuilding_type17.setText("已认证");
			tv_addbuilding_type18.setText("已认证");
			tv_addbuilding_type19.setText("已认证");
			break;
		case 20:
			tv_addbuilding_type01.setText("已认证");
			tv_addbuilding_type02.setText("已认证");
			tv_addbuilding_type03.setText("已认证");
			tv_addbuilding_type04.setText("已认证");
			tv_addbuilding_type05.setText("已认证");
			tv_addbuilding_type06.setText("已认证");
			tv_addbuilding_type07.setText("已认证");
			tv_addbuilding_type08.setText("已认证");
			tv_addbuilding_type09.setText("已认证");
			tv_addbuilding_type10.setText("已认证");
			tv_addbuilding_type11.setText("已认证");
			tv_addbuilding_type12.setText("已认证");
			tv_addbuilding_type13.setText("已认证");
			tv_addbuilding_type14.setText("已认证");
			tv_addbuilding_type15.setText("已认证");
			tv_addbuilding_type16.setText("已认证");
			tv_addbuilding_type17.setText("已认证");
			tv_addbuilding_type18.setText("已认证");
			tv_addbuilding_type19.setText("已认证");
			tv_addbuilding_type20.setText("已认证");
			break;
		}
	}
}
