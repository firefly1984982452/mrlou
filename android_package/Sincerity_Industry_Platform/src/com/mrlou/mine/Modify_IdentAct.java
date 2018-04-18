package com.mrlou.mine;

import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Modify_IdentAct extends Activity {

	private LinearLayout lay_mine_act_main, lay_mine_modify_search;
	private FontManager fm = new FontManager();
	private Button bt_type_1, bt_type_2, bt_type_3, bt_type_102, bt_type_103,
			bt_type_299;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_modity_ident);
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
		bt_type_2 = (Button) findViewById(R.id.bt_type_2);
		bt_type_3 = (Button) findViewById(R.id.bt_type_3);
		bt_type_102 = (Button) findViewById(R.id.bt_type_102);
		bt_type_103 = (Button) findViewById(R.id.bt_type_103);
		bt_type_299 = (Button) findViewById(R.id.bt_type_299);
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Modify_IdentAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
}
