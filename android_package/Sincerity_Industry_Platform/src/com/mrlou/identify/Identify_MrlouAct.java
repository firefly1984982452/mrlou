package com.mrlou.identify;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.mrlou.mrlou.R;

/**
 * @author jx_chen
 * @date 2016-10-31---上午11:24:29
 * @problem
 */
public class Identify_MrlouAct extends Activity {

	private LinearLayout lay_aboutregis_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.identify_mrlou);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_aboutregis_back = (LinearLayout) findViewById(R.id.lay_aboutregis_back);
		lay_aboutregis_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
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
					Identify_MrlouAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
}
