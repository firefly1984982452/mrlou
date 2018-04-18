package com.mrlou.activity;

import com.mrlou.application.MyApplication;
import com.mrlou.database.Database;
import com.mrlou.mrlou.R;
import com.mrlou.util.User_info;

import android.os.Bundle;
import android.view.KeyEvent;
import android.app.Activity;
import android.content.Intent;

public class InitActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		MyApplication.getInstance().addActivity(this);
		loadEnterStatus();
		Intent intent = new Intent(this, AdActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyApplication.getInstance().exit();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void loadEnterStatus() {
		Database db = new Database(InitActivity.this);
		db.loadEnterStatus();
		db.close();
		db = null;
	}

}
