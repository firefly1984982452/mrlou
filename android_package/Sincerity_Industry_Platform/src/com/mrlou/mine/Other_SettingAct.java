package com.mrlou.mine;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrlou.h5.Other_H5Act;
import com.mrlou.mrlou.R;

/**
 * @author jx_chen
 * @date 2016-11-17---下午7:03:02
 * @problem
 */
public class Other_SettingAct extends Activity {

	private LinearLayout lay_help_center, lay_about_mrlou, lay_feedback,lay_business_cooperation,lay_other_setting_back;
	private TextView tv_company_phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_other_setting);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_feedback = (LinearLayout) findViewById(R.id.lay_feedback);
		lay_about_mrlou = (LinearLayout) findViewById(R.id.lay_about_mrlou);
		lay_help_center = (LinearLayout) findViewById(R.id.lay_help_center);
		lay_business_cooperation=(LinearLayout) findViewById(R.id.lay_business_cooperation);
		tv_company_phone = (TextView) findViewById(R.id.tv_company_phone);
		lay_other_setting_back=(LinearLayout) findViewById(R.id.lay_other_setting_back);
		lay_other_setting_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		tv_company_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ "400-968-7099"));
				Other_SettingAct.this.startActivity(intent);
			}
		});
		lay_business_cooperation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Other_SettingAct.this,Other_H5Act.class);
				intent.putExtra("url", "resourse_business_cooperation.html");
				startActivity(intent);
			}
		});
		lay_help_center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Other_SettingAct.this, Help_CenterAct.class);
				startActivity(intent);
			}
		});
		lay_about_mrlou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Other_SettingAct.this, About_MrlouAct.class);
				startActivity(intent);
			}
		});
		lay_feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Other_SettingAct.this, FeedBack_Act.class);
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
						Other_SettingAct.this.finish();
					} catch (Exception e) {
						Log.e("Exception when sendKeyDownUpSync", e.toString());
					}
				}
			}.start();
		}
}
