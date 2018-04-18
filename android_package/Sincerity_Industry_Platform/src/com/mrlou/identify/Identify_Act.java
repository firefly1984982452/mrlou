package com.mrlou.identify;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;

/**
 * @author jx_chen
 * @date 2016-10-31---上午10:58:57
 * @problem
 */
public class Identify_Act extends Activity {

	private LinearLayout  lay_mine_modify_search;
	private Button bt_type_1, bt_type_2, bt_type_3, bt_type_102, bt_type_103,
			bt_type_299;
	private String avatar, cn_username, en_username, email, avatars_id,
			company, wechat_qr_id, post_card_id, work_start, region_id,
			region_id_real, post_card, wechat_qr, wechat, building_a,
			building_b,company_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_modity_ident);
		Intent intent = getIntent();
		cn_username = intent.getStringExtra("cn_username");
		en_username = intent.getStringExtra("en_username");
		email = intent.getStringExtra("email");
		avatar = intent.getStringExtra("avatar");
		avatars_id = intent.getStringExtra("avatar_id");
		company = intent.getStringExtra("company");
		wechat_qr_id = intent.getStringExtra("wechat_qr_id");
		post_card_id = intent.getStringExtra("post_card_id");
		work_start = intent.getStringExtra("work_start");
		region_id = intent.getStringExtra("region_id");
		region_id_real = intent.getStringExtra("region_id_real");
		post_card = intent.getStringExtra("post_card");
		wechat_qr = intent.getStringExtra("wechat_qr");
		wechat = intent.getStringExtra("wechat");
		building_a = intent.getStringExtra("building_a");
		building_b = intent.getStringExtra("building_b");
		company_id=intent.getStringExtra("company_id");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_mine_modify_search = (LinearLayout) findViewById(R.id.lay_mine_modify_search);
		lay_mine_modify_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		bt_type_1 = (Button) findViewById(R.id.bt_type_1);
		bt_type_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Identify_Act.this,
						Identify_ProAct.class);
				intent.putExtra("type", "开发商");
				intent.putExtra("type_id", "1");
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("company", company);
				intent.putExtra("company_id", company_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("work_start", work_start);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", wechat);
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("region_id", region_id);
				intent.putExtra("region_id_real", region_id_real);
				startActivity(intent);
			}
		});
		bt_type_2 = (Button) findViewById(R.id.bt_type_2);
		bt_type_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Identify_Act.this,
						Identify_ProAct.class);
				intent.putExtra("type", "代理商");
				intent.putExtra("type_id", "2");
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("company", company);
				intent.putExtra("company_id", company_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("work_start", work_start);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", wechat);
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("region_id", region_id);
				intent.putExtra("region_id_real", region_id_real);
				startActivity(intent);
			}
		});
		bt_type_3 = (Button) findViewById(R.id.bt_type_3);
		bt_type_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Identify_Act.this,
						Identify_ProAct.class);
				intent.putExtra("type", "运营商");
				intent.putExtra("type_id", "3");
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("company", company);
				intent.putExtra("company_id", company_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("work_start", work_start);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", wechat);
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("region_id", region_id);
				intent.putExtra("region_id_real", region_id_real);
				startActivity(intent);
			}
		});
		bt_type_102 = (Button) findViewById(R.id.bt_type_102);
		bt_type_102.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Identify_Act.this,
						Identify_ProAct.class);
				intent.putExtra("type", "物业公司");
				intent.putExtra("type_id", "103");
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("company", company);
				intent.putExtra("company_id", company_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("work_start", work_start);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", wechat);
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("region_id", region_id);
				intent.putExtra("region_id_real", region_id_real);
				startActivity(intent);

			}
		});
		bt_type_103 = (Button) findViewById(R.id.bt_type_103);
		bt_type_103.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Identify_Act.this,
						Identify_AgentaAct.class);
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("company", company);
				intent.putExtra("company_id", company_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("work_start", work_start);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", wechat);
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("region_id", region_id);
				intent.putExtra("region_id_real", region_id_real);
				startActivity(intent);
			}
		});
		bt_type_299 = (Button) findViewById(R.id.bt_type_299);
		bt_type_299.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Identify_Act.this,
						Idenfity_registerAct.class);
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("company", company);
				intent.putExtra("company_id", company_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("work_start", work_start);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", wechat);
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("region_id", region_id);
				intent.putExtra("services", "");
				intent.putExtra("services_id", "");
				intent.putExtra("region_id_real", region_id_real);
				startActivity(intent);
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
					Identify_Act.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

}
