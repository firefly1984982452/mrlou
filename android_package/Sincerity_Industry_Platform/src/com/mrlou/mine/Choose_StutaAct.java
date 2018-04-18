package com.mrlou.mine;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;

public class Choose_StutaAct extends Activity {

	private LinearLayout lay_icon01, lay_icon02, lay_icon03, lay_icon04,
			lay_icon05, lay_icon06, lay_hourse_choose_main;
	private LinearLayout lay_back, lay_complete;
	private ImageView iv_icon01, iv_icon02, iv_icon03, iv_icon04, iv_icon05,
			iv_icon06;
	private String id, category, building_id, building_name, avatar, avatar_id,
			transaction_type, unit_no, session_id, verified, image_total,
			rent_price, rent_price_unit, sell_price, sell_price_unit, head_to,
			delivery_date, delivery_status, intro, square_meter, update_time,
			position, min_rent_period, unique, contact_phone, status,
			hourse_id, land, land_id,user_type,fee_rate;
	private ArrayList<String> str_images_url,str_images_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_hourse_choose_status);
		Intent intent = getIntent();
		session_id = intent.getStringExtra("session_id");
		building_name = intent.getStringExtra("building_name");
		id = intent.getStringExtra("id");
		category = intent.getStringExtra("category");
		transaction_type = intent.getStringExtra("transaction_type");
		position = intent.getStringExtra("position");
		square_meter = intent.getStringExtra("square_meter");
		rent_price = intent.getStringExtra("rent_price");
		sell_price = intent.getStringExtra("sell_price");
		rent_price_unit=intent.getStringExtra("rent_price_unit");
		sell_price_unit=intent.getStringExtra("sell_price_unit");
		head_to = intent.getStringExtra("head_to");
		fee_rate=intent.getStringExtra("fee_rate");
		delivery_status = intent.getStringExtra("delivery_status");
		delivery_date = intent.getStringExtra("delivery_date");
		intro = intent.getStringExtra("intro");
		contact_phone = intent.getStringExtra("contact_phone");
		unit_no = intent.getStringExtra("unit_no");
		position = intent.getStringExtra("position");
		status = intent.getStringExtra("status");
		min_rent_period=intent.getStringExtra("min_rent_period");
		hourse_id = intent.getStringExtra("hourse_id");
		str_images_url = intent.getStringArrayListExtra("images_url");
		str_images_id = intent.getStringArrayListExtra("images_id");
		land=intent.getStringExtra("land");
		land_id=intent.getStringExtra("land_id");
		avatar=intent.getStringExtra("avatar");
		avatar_id=intent.getStringExtra("avatar_id");
		user_type = intent.getStringExtra("user_type");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_back = (LinearLayout) findViewById(R.id.lay_choose_status_back);
		lay_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Choose_StutaAct.this,
						Hourse_InfoeditAct.class);
				intent.putExtra("session_id", session_id);
				intent.putExtra("building_name", building_name);
				intent.putExtra("id", id);
				intent.putExtra("transaction_type", transaction_type);
				intent.putExtra("position", position);
				intent.putExtra("position", position);
				intent.putExtra("category", category);
				intent.putExtra("square_meter", square_meter);
				intent.putExtra("rent_price", rent_price);
				intent.putExtra("sell_price", sell_price);
				intent.putExtra("rent_price_unit", rent_price_unit);
				intent.putExtra("sell_price_unit", sell_price_unit);
				intent.putExtra("head_to", head_to);
				intent.putExtra("delivery_status", delivery_status);
				intent.putExtra("delivery_date", delivery_date);
				intent.putExtra("intro", intro);
				intent.putExtra("contact_phone", contact_phone);
				intent.putExtra("unit_no", unit_no);
				intent.putExtra("position", position);
				intent.putExtra("status", status);
				intent.putExtra("fee_rate", fee_rate);
				intent.putExtra("min_rent_period", min_rent_period);
				intent.putExtra("hourse_id", hourse_id);
				intent.putExtra("land", land);
				intent.putExtra("land_id", land_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("avatar_id", avatar_id);
				intent.putStringArrayListExtra("images_url", str_images_url);
				intent.putStringArrayListExtra("images_id", str_images_id);
				intent.putExtra("user_type", user_type);
				startActivity(intent);
				Choose_StutaAct.this.finish();
			}
		});
		lay_complete = (LinearLayout) findViewById(R.id.lay_choose_status_complete);
		lay_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Choose_StutaAct.this,
						Hourse_InfoeditAct.class);
				intent.putExtra("session_id", session_id);
				intent.putExtra("building_name", building_name);
				intent.putExtra("id", id);
				intent.putExtra("transaction_type", transaction_type);
				intent.putExtra("position", position);
				intent.putExtra("position", position);
				intent.putExtra("category", category);
				intent.putExtra("square_meter", square_meter);
				intent.putExtra("rent_price", rent_price);
				intent.putExtra("sell_price", sell_price);
				intent.putExtra("rent_price_unit", rent_price_unit);
				intent.putExtra("sell_price_unit", sell_price_unit);
				intent.putExtra("head_to", head_to);
				intent.putExtra("delivery_status", delivery_status);
				intent.putExtra("delivery_date", delivery_date);
				intent.putExtra("intro", intro);
				intent.putExtra("contact_phone", contact_phone);
				intent.putExtra("min_rent_period", min_rent_period);
				intent.putExtra("fee_rate", fee_rate);
				intent.putExtra("unit_no", unit_no);
				intent.putExtra("position", position);
				intent.putExtra("status", status);
				intent.putExtra("hourse_id", hourse_id);
				intent.putExtra("land", land);
				intent.putExtra("land_id", land_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("avatar_id", avatar_id);
				intent.putStringArrayListExtra("images_url", str_images_url);
				intent.putStringArrayListExtra("images_id", str_images_id);
				intent.putExtra("user_type", user_type);
				startActivity(intent);
				Choose_StutaAct.this.finish();
			}
		});
		lay_icon01 = (LinearLayout) findViewById(R.id.lay_choose_stuta_icon01);
		lay_icon01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delivery_status = "1";
				selecticon(1);
			}
		});
		lay_icon02 = (LinearLayout) findViewById(R.id.lay_choose_stuta_icon02);
		lay_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delivery_status = "2";
				selecticon(2);
			}
		});
		lay_icon03 = (LinearLayout) findViewById(R.id.lay_choose_stuta_icon03);
		lay_icon03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delivery_status = "3";
				selecticon(3);
			}
		});
		lay_icon04 = (LinearLayout) findViewById(R.id.lay_choose_stuta_icon04);
		lay_icon04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delivery_status = "4";
				selecticon(4);
			}
		});
		lay_icon05 = (LinearLayout) findViewById(R.id.lay_choose_stuta_icon05);
		lay_icon05.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delivery_status = "5";
				selecticon(5);
			}
		});
		lay_icon06 = (LinearLayout) findViewById(R.id.lay_choose_stuta_icon06);
		lay_icon06.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delivery_status = "6";
				selecticon(6);
			}
		});
		iv_icon01 = (ImageView) findViewById(R.id.iv_choose_stuta_icon01);
		iv_icon02 = (ImageView) findViewById(R.id.iv_choose_stuta_icon02);
		iv_icon03 = (ImageView) findViewById(R.id.iv_choose_stuta_icon03);
		iv_icon04 = (ImageView) findViewById(R.id.iv_choose_stuta_icon04);
		iv_icon05 = (ImageView) findViewById(R.id.iv_choose_stuta_icon05);
		iv_icon06 = (ImageView) findViewById(R.id.iv_choose_stuta_icon06);
	}

	public void selecticon(int flag) {
		switch (flag) {
		case 1:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon05.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon06.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 2:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon05.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon06.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 3:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon05.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon06.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 4:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_icon05.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon06.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 5:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon05.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_icon06.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 6:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon05.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon06.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
			Intent intent = new Intent(Choose_StutaAct.this,
					Hourse_InfoeditAct.class);
			intent.putExtra("session_id", session_id);
			intent.putExtra("building_name", building_name);
			intent.putExtra("id", id);
			intent.putExtra("transaction_type", transaction_type);
			intent.putExtra("position", position);
			intent.putExtra("position", position);
			intent.putExtra("category", category);
			intent.putExtra("square_meter", square_meter);
			intent.putExtra("rent_price", rent_price);
			intent.putExtra("sell_price", sell_price);
			intent.putExtra("rent_price_unit", rent_price_unit);
			intent.putExtra("sell_price_unit", sell_price_unit);
			intent.putExtra("head_to", head_to);
			intent.putExtra("delivery_status", delivery_status);
			intent.putExtra("delivery_date", delivery_date);
			intent.putExtra("intro", intro);
			intent.putExtra("contact_phone", contact_phone);
			intent.putExtra("min_rent_period", min_rent_period);
			intent.putExtra("fee_rate", fee_rate);
			intent.putExtra("unit_no", unit_no);
			intent.putExtra("position", position);
			intent.putExtra("status", status);
			intent.putExtra("hourse_id", hourse_id);
			intent.putExtra("land", land);
			intent.putExtra("land_id", land_id);
			intent.putExtra("avatar", avatar);
			intent.putExtra("avatar_id", avatar_id);
			intent.putStringArrayListExtra("images_url", str_images_url);
			intent.putStringArrayListExtra("images_id", str_images_id);
			intent.putExtra("user_type", user_type);
			startActivity(intent);
			Choose_StutaAct.this.finish();
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
					Choose_StutaAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
}
