package com.mrlou.mine;

import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Introduction_Act extends Activity {
	private String phone_number, email, en_username, cn_username,
			company, avatar, type, intro, work_start,
			region_id, region_id_real;
	private String avatars_id, wechat_qr_id, post_card_id, post_card,
			wechat, wechat_qr,add_v,company_id,office_building,office_building_id,good_type,user_id;
	private EditText et_mine_intro;
	private LinearLayout lay_mine_intro_back, lay_mine_intro_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_introduction);
		getDate();
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
		company_id = intent.getStringExtra("company_id");
		work_start = intent.getStringExtra("work_start");
		region_id = intent.getStringExtra("region_id");
		region_id_real = intent.getStringExtra("region_id_real");
		post_card = intent.getStringExtra("post_card");
		wechat_qr = intent.getStringExtra("wechat_qr");
		wechat = intent.getStringExtra("wechat");
		add_v=intent.getStringExtra("v");
		user_id = intent.getStringExtra("user_id");
		office_building=intent.getStringExtra("office_building");
		office_building_id=intent.getStringExtra("office_building_id");
		good_type=intent.getStringExtra("good_type");
	}

	private void findView() {
		// TODO Auto-generated method stub
		et_mine_intro = (EditText) findViewById(R.id.et_mine_intro);
		et_mine_intro.setText(intro);
		lay_mine_intro_back = (LinearLayout) findViewById(R.id.lay_mine_intro_back);
		lay_mine_intro_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Introduction_Act.this,
						Setting_personal02.class);
				intent.putExtra("company_id", company_id);
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("phone_number", phone_number);
				intent.putExtra("type", type);
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
				intent.putExtra("user_id", user_id);
				startActivity(intent);
				Introduction_Act.this.finish();
			}
		});
		lay_mine_intro_submit = (LinearLayout) findViewById(R.id.lay_mine_intro_submit);
		lay_mine_intro_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Introduction_Act.this,
						Setting_personal02.class);
				intent.putExtra("company_id", company_id);
				intent.putExtra("cn_username", cn_username);
				intent.putExtra("en_username", en_username);
				intent.putExtra("email", email);
				intent.putExtra("phone_number", phone_number);
				intent.putExtra("type", type);
				intent.putExtra("avatar", avatar);
				intent.putExtra("company", company);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("intro", et_mine_intro.getText().toString());
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
				intent.putExtra("user_id", user_id);
				startActivity(intent);
				Introduction_Act.this.finish();
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
					Introduction_Act.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
}
