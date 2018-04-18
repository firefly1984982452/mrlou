package com.mrlou.mine;

import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class About_MrlouAct extends Activity implements HttpListener {

	private LinearLayout lay_about_mrlou_back;
	private String content,session_id;
	private TextView tv_mine_about;
	private ImageView imageView;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_about_mrlou);
		sharedPreferences = About_MrlouAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		imageView = (ImageView) findViewById(R.id.iv_mine_about_logo);
		imageView.setImageResource(R.drawable.logo);
		select_jobadd("2");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		tv_mine_about=(TextView) findViewById(R.id.tv_mine_about);
		lay_about_mrlou_back = (LinearLayout) findViewById(R.id.lay_about_mrlou_back);
		lay_about_mrlou_back.setOnClickListener(new OnClickListener() {

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
					About_MrlouAct.this.finish();
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
				tv_mine_about.setText(content);
//				findView();
				break;
			case 2:
				Toast.makeText(About_MrlouAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(About_MrlouAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_jobadd(String type) {
		Request request = Request.requestabout(session_id,type);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json, json2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				String msg = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("list");
					content = json2.getString("content");
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				} else {
					System.out.println("" + msg);
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
}
