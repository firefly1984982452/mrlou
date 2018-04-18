package com.mrlou.mine;

import java.util.ArrayList;

import com.mrlou.adapter.ChooseArea_Adapter;
import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;
import com.umeng_social_sdk_res_lib.R.string;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Choose_AreaAct extends Activity {
	private LinearLayout lay_area_choose_back, lay_area_choose_complete;
	private ListView lv_choose_area;
	private ArrayList<String> str_name, str_flag, str_id;
	private ChooseArea_Adapter adapter;
	private String area = "", id;
	private String phone_number, email, en_username, cn_username, company,
			avatar, type, intro, work_start, region_id, region_id_real;
	private String avatars_id, wechat_qr_id, post_card_id, post_card, wechat,
			wechat_qr, add_v, leixing, service_type, company_id,
			office_building, office_building_id, good_type;
	private int num = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_area_choose);
		getDate();
		prapare();
		findView();
	}

	private void getDate() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		cn_username = intent.getStringExtra("cn_username");
		en_username = intent.getStringExtra("en_username");
		email = intent.getStringExtra("email");
		phone_number = intent.getStringExtra("phone_number");
		type = intent.getStringExtra("type");
		avatar = intent.getStringExtra("avatar");
		avatars_id = intent.getStringExtra("avatar_id");
		company = intent.getStringExtra("company");
		wechat_qr_id = intent.getStringExtra("wechat_qr_id");
		post_card_id = intent.getStringExtra("post_card_id");
		intro = intent.getStringExtra("intro");
		service_type = intent.getStringExtra("service_type");
		work_start = intent.getStringExtra("work_start");
		region_id = intent.getStringExtra("region_id");
		region_id_real = intent.getStringExtra("region_id_real");
		post_card = intent.getStringExtra("post_card");
		wechat_qr = intent.getStringExtra("wechat_qr");
		wechat = intent.getStringExtra("wechat");
		add_v = intent.getStringExtra("v");
		leixing = intent.getStringExtra("leixing");
		company_id = intent.getStringExtra("company_id");
		office_building = intent.getStringExtra("office_building");
		office_building_id = intent.getStringExtra("office_building_id");
		if (leixing.equals("1")) {
			good_type = intent.getStringExtra("good_type");
		}
	}

	private void prapare() {
		// TODO Auto-generated method stub
		str_name = new ArrayList<String>();
		str_flag = new ArrayList<String>();
		str_id = new ArrayList<>();
		str_name.add("浦东新区");
		str_flag.add("0");
		str_id.add("310115");
		str_name.add("黄浦区");
		str_flag.add("0");
		str_id.add("310101");
		str_name.add("静安区");
		str_flag.add("0");
		str_id.add("310106");
		str_name.add("徐汇区");
		str_flag.add("0");
		str_id.add("310104");
		str_name.add("长宁区");
		str_flag.add("0");
		str_id.add("310105");
		str_name.add("普陀区");
		str_flag.add("0");
		str_id.add("310107");
		str_name.add("闸北区");
		str_flag.add("0");
		str_id.add("310108");
		str_name.add("虹口区");
		str_flag.add("0");
		str_id.add("310109");
		str_name.add("杨浦区");
		str_flag.add("0");
		str_id.add("310110");
		str_name.add("宝山区");
		str_flag.add("0");
		str_id.add("310113");
		str_name.add("闵行区");
		str_flag.add("0");
		str_id.add("310112");
		str_name.add("嘉定区");
		str_flag.add("0");
		str_id.add("310114");
		str_name.add("青浦区");
		str_flag.add("0");
		str_id.add("310118");
		str_name.add("松江区");
		str_flag.add("0");
		str_id.add("310117");
		str_name.add("奉贤区");
		str_flag.add("0");
		str_id.add("310120");
		str_name.add("金山区");
		str_flag.add("0");
		str_id.add("310116");
		str_name.add("崇明区");
		str_flag.add("0");
		str_id.add("310230");
		if (region_id != "") {
			String[] str = region_id.split(" ");
			for (int i = 0; i < str_name.size(); i++) {
				for (int j = 0; j < str.length; j++) {
					if (str_name.get(i).equals(str[j])) {
						str_flag.set(i, "1");
					}
				}
			}
		}
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_area_choose_back = (LinearLayout) findViewById(R.id.lay_area_choose_back);
		lay_area_choose_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (leixing.equals("1")) {
					Intent intent = new Intent(Choose_AreaAct.this,
							Setting_personal02.class);
					intent.putExtra("cn_username", cn_username);
					intent.putExtra("en_username", en_username);
					intent.putExtra("email", email);
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("type", type);
					intent.putExtra("company_id", company_id);
					intent.putExtra("avatar", avatar);
					intent.putExtra("company", company);
					intent.putExtra("avatar_id", avatars_id);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("intro", intro);
					intent.putExtra("work_start", work_start);
					intent.putExtra("region_id", region_id);
					intent.putExtra("region_id_real", region_id_real);
					intent.putExtra("wechat", wechat);
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("post_card", post_card);
					intent.putExtra("v", add_v);
					intent.putExtra("office_building", office_building);
					intent.putExtra("office_building_id", office_building_id);
					intent.putExtra("good_type", good_type);
					startActivity(intent);
					Choose_AreaAct.this.finish();
				} else {
					Intent intent = new Intent(Choose_AreaAct.this,
							Setting_personal04.class);
					intent.putExtra("service_type", service_type);
					intent.putExtra("cn_username", cn_username);
					intent.putExtra("en_username", en_username);
					intent.putExtra("email", email);
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("type", type);
					intent.putExtra("avatar", avatar);
					intent.putExtra("company", company);
					intent.putExtra("company_id", company_id);
					intent.putExtra("avatar_id", avatars_id);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("intro", intro);
					intent.putExtra("work_start", work_start);
					intent.putExtra("region_id", region_id);
					intent.putExtra("region_id_real", region_id_real);
					intent.putExtra("wechat", wechat);
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("post_card", post_card);
					intent.putExtra("v", add_v);
					intent.putExtra("office_building", office_building);
					intent.putExtra("office_building_id", office_building_id);
					startActivity(intent);
					Choose_AreaAct.this.finish();
				}
			}
		});
		lay_area_choose_complete = (LinearLayout) findViewById(R.id.lay_area_choose_complete);
		lay_area_choose_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < str_name.size(); i++) {
					if (str_flag.get(i).equals("1")) {
						area = area + " " + str_name.get(i);
						if (num == 0) {
							id = str_id.get(i);
						} else {
							id = id + "," + str_id.get(i);
						}
						num++;
					}
				}
				if (area.equals("")) {
					area = "";
				}
				if (leixing.equals("1")) {
					Intent intent = new Intent(Choose_AreaAct.this,
							Setting_personal02.class);
					intent.putExtra("cn_username", cn_username);
					intent.putExtra("en_username", en_username);
					intent.putExtra("email", email);
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("type", type);
					intent.putExtra("avatar", avatar);
					intent.putExtra("company", company);
					intent.putExtra("company_id", company_id);
					intent.putExtra("avatar_id", avatars_id);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("intro", intro);
					intent.putExtra("work_start", work_start);
					intent.putExtra("region_id", area);
					intent.putExtra("region_id_real", id);
					intent.putExtra("wechat", wechat);
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("post_card", post_card);
					intent.putExtra("v", add_v);
					intent.putExtra("office_building", office_building);
					intent.putExtra("office_building_id", office_building_id);
					intent.putExtra("good_type", good_type);
					startActivity(intent);
					Choose_AreaAct.this.finish();
				} else {
					Intent intent = new Intent(Choose_AreaAct.this,
							Setting_personal04.class);
					intent.putExtra("service_type", service_type);
					intent.putExtra("cn_username", cn_username);
					intent.putExtra("en_username", en_username);
					intent.putExtra("email", email);
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("type", type);
					intent.putExtra("avatar", avatar);
					intent.putExtra("company", company);
					intent.putExtra("company_id", company_id);
					intent.putExtra("avatar_id", avatars_id);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("intro", intro);
					intent.putExtra("work_start", work_start);
					intent.putExtra("region_id", area);
					intent.putExtra("region_id_real", region_id_real);
					intent.putExtra("wechat", wechat);
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("post_card", post_card);
					intent.putExtra("v", add_v);
					intent.putExtra("office_building", office_building);
					intent.putExtra("office_building_id", office_building_id);
					startActivity(intent);
					Choose_AreaAct.this.finish();
				}

			}
		});
		lv_choose_area = (ListView) findViewById(R.id.lv_area_choose);
		adapter = new ChooseArea_Adapter(Choose_AreaAct.this, str_name,
				str_flag);
		lv_choose_area.setAdapter(adapter);
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Choose_AreaAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
}
